package com.pro100kryto.server.modules.packetpool.connection;

import com.pro100kryto.server.module.IModuleConnection;
import com.pro100kryto.server.utils.datagram.packets.IPacketInProcess;
import com.sun.istack.Nullable;

public interface IPacketPoolModuleConnection extends IModuleConnection {
    @Nullable
    IPacketInProcess getNextPacket();
    int getMaxCapacity();
    int getRemainingCapacity();
}
