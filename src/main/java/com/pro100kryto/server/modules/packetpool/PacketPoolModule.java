package com.pro100kryto.server.modules.packetpool;

import com.pro100kryto.server.livecycle.AShortLiveCycleImpl;
import com.pro100kryto.server.livecycle.ILiveCycleImpl;
import com.pro100kryto.server.module.AModule;
import com.pro100kryto.server.module.ModuleConnectionParams;
import com.pro100kryto.server.module.ModuleParams;
import com.pro100kryto.server.modules.packetpool.connection.IPacketPoolModuleConnection;
import com.pro100kryto.server.utils.datagram.pool.PacketPool;
import org.jetbrains.annotations.NotNull;

public class PacketPoolModule extends AModule<IPacketPoolModuleConnection> {
    private PacketPool packetPool;

    public PacketPoolModule(ModuleParams moduleParams) {
        super(moduleParams);
    }

    @Override
    public @NotNull IPacketPoolModuleConnection createModuleConnection(ModuleConnectionParams params) {
        return new PacketPoolModuleConnection(this, params, packetPool);
    }

    @Override
    protected @NotNull ILiveCycleImpl createDefaultLiveCycleImpl() {
        return new PacketPoolLiveCycleImpl();
    }

    private final class PacketPoolLiveCycleImpl extends AShortLiveCycleImpl {

        @Override
        public void init() {
            packetPool = new PacketPool(
                    settings.getIntOrDefault("pool-capacity", 256),
                    settings.getIntOrDefault("packet-size", 1024)
            );
            packetPool.refill();
        }

        @Override
        public void start() {
            if (packetPool.isEmpty()){
                packetPool.refill();
            }
        }

        @Override
        public void stopForce() {
            packetPool.clear();
        }

        @Override
        public void destroy() {
            packetPool.clear();
        }

        @Override
        public boolean canBeStoppedSafe() {
            return !packetPool.isInUse();
        }
    }
}
