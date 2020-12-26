package com.pro100kryto.server.modules;

import com.pro100kryto.server.StartStopStatus;
import com.pro100kryto.server.logger.ILogger;
import com.pro100kryto.server.module.AModuleConnection;
import com.pro100kryto.server.module.Module;
import com.pro100kryto.server.modules.packetpool.connection.IPacketPoolModuleConnection;
import com.pro100kryto.server.service.IServiceControl;
import com.pro100kryto.server.utils.datagram.exceptions.PoolEmptyException;
import com.pro100kryto.server.utils.datagram.packets.IPacketInProcess;
import com.pro100kryto.server.utils.datagram.packets.PacketPool;
import com.sun.istack.Nullable;

public class PacketPoolModule extends Module {
    private static final String ID = "PacketPoolModule";
    private PacketPool packetPool;

    public PacketPoolModule(IServiceControl service, String name) {
        super(service, name);
    }

    @Override
    protected void startAction() throws Throwable {
        final int packetSize = Integer.parseInt(settings.getOrDefault("packet-size", "1024"));
        final int poolCapacity = Integer.parseInt(settings.getOrDefault("pool-capacity", "1024"));
        if (packetPool==null) {
            packetPool = new PacketPool(poolCapacity, packetSize);
        }
        packetPool.refill();

        if (moduleConnection==null){
            moduleConnection = new PacketPoolModuleConnection(logger, name, type);
        }
    }

    @Override
    protected void stopAction(boolean force) throws Throwable {
        packetPool.clear();
        if (packetPool.isInUse()) logger.writeWarn("Pool is in use");
        if (force) packetPool = null;
    }

    @Override
    public void tick() throws Throwable {
        Thread.yield();
    }

    private final class PacketPoolModuleConnection extends AModuleConnection
            implements IPacketPoolModuleConnection {

        public PacketPoolModuleConnection(ILogger logger, String moduleName, String moduleType) {
            super(logger, moduleName, moduleType);
        }

        @Override
        public boolean isAliveModule() {
            return getStatus() == StartStopStatus.STARTED;
        }

        // -----------------

        @Override @Nullable
        public IPacketInProcess getNextPacket() {
            try {
                return packetPool.nextAndGet();
            } catch (PoolEmptyException ignored) {
            }
            return null;
        }

        @Override
        public int getMaxCapacity() {
            return packetPool.getMaxCapacity();
        }

        @Override
        public int getRemainingCapacity() {
            return packetPool.getRemainingCapacity();
        }
    }
}
