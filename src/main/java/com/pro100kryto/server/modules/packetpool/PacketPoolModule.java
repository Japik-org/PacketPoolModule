package com.pro100kryto.server.modules.packetpool;

import com.pro100kryto.server.livecycle.AShortLiveCycleImpl;
import com.pro100kryto.server.livecycle.controller.ILiveCycleImplId;
import com.pro100kryto.server.livecycle.controller.LiveCycleController;
import com.pro100kryto.server.module.AModule;
import com.pro100kryto.server.module.ModuleConnectionParams;
import com.pro100kryto.server.module.ModuleParams;
import com.pro100kryto.server.modules.packetpool.connection.IPacketPoolModuleConnection;
import com.pro100kryto.server.utils.datagram.pool.PacketPool;
import lombok.Getter;
import lombok.Setter;
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
    protected void initLiveCycleController(LiveCycleController liveCycleController) {
        super.initLiveCycleController(liveCycleController);

        liveCycleController.putImplAll(new PacketPoolModuleLiveCycleImpl());
    }

    private final class PacketPoolModuleLiveCycleImpl extends AShortLiveCycleImpl implements ILiveCycleImplId {
        @Getter
        private final String name = "PacketPoolModuleLiveCycleImpl";
        @Getter @Setter
        private int priority = LiveCycleController.PRIORITY_NORMAL;

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
