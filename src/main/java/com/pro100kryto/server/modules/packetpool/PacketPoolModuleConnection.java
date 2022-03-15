package com.pro100kryto.server.modules.packetpool;

import com.pro100kryto.server.module.AModuleConnection;
import com.pro100kryto.server.module.ModuleConnectionParams;
import com.pro100kryto.server.modules.packetpool.shared.IPacketPoolModuleConnection;
import com.pro100kryto.server.utils.datagram.packet.DatagramPacketRecyclable;
import org.jetbrains.annotations.Nullable;

public final class PacketPoolModuleConnection
        extends AModuleConnection<PacketPoolModule, IPacketPoolModuleConnection>
        implements IPacketPoolModuleConnection{

    public PacketPoolModuleConnection(PacketPoolModule module, ModuleConnectionParams params) {
        super(module, params);
    }

    @Override
    @Nullable
    public DatagramPacketRecyclable getNextPacket() {
        return module.getPool().borrowObject(false).getObject();
    }

    @Override
    public int getMaxCapacity() {
        return module.getPool().getMaxSize();
    }

    @Override
    public int getRemainingCapacity() {
        return module.getPool().getSize();
    }
}
