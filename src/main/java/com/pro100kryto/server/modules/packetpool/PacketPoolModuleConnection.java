package com.pro100kryto.server.modules.packetpool;

import com.pro100kryto.server.logger.ILogger;
import com.pro100kryto.server.module.AModuleConnection;
import com.pro100kryto.server.modules.packetpool.connection.IPacketPoolModuleConnection;
import com.pro100kryto.server.utils.datagram.packet.DatagramPacketWrapper;
import com.pro100kryto.server.utils.datagram.pool.PacketPool;
import com.pro100kryto.server.utils.datagram.pool.PoolEmptyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketPoolModuleConnection
        extends AModuleConnection<PacketPoolModule, IPacketPoolModuleConnection>
        implements IPacketPoolModuleConnection{

    private final PacketPool packetPool;

    public PacketPoolModuleConnection(@NotNull PacketPoolModule module, ILogger logger, PacketPool packetPool) {
        super(module, logger);
        this.packetPool = packetPool;
    }

    @Override
    @Nullable
    public DatagramPacketWrapper getNextPacket() throws PoolEmptyException {
        return packetPool.getNext();
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
