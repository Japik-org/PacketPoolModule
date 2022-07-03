package com.japik.modules.packetpool;

import com.japik.module.AModuleConnection;
import com.japik.module.ModuleConnectionParams;
import com.japik.modules.packetpool.shared.IPacketPoolModuleConnection;
import com.japik.utils.datagram.packet.DatagramPacketRecyclable;
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
