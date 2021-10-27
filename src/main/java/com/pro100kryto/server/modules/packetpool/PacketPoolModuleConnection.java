package com.pro100kryto.server.modules.packetpool;

import com.pro100kryto.server.logger.ILogger;
import com.pro100kryto.server.module.AModuleConnectionImpl;
import com.pro100kryto.server.modules.packetpool.connection.IPacketPoolModuleConnection;
import com.pro100kryto.server.utils.datagram.packets.IPacketInProcess;
import com.pro100kryto.server.utils.datagram.packets.PacketPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketPoolModuleConnection
        extends AModuleConnectionImpl<PacketPoolModule, IPacketPoolModuleConnection>
        implements IPacketPoolModuleConnection{

    private final PacketPool packetPool;

    public PacketPoolModuleConnection(@NotNull PacketPoolModule module, ILogger logger, PacketPool packetPool) {
        super(module, logger);
        this.packetPool = packetPool;
    }

    @Override
    @Nullable
    public IPacketInProcess getNextPacket() {
        return packetPool.getNextPacketOrNull();
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
