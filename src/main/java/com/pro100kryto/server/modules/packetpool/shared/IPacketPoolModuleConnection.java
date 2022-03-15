package com.pro100kryto.server.modules.packetpool.shared;

import com.pro100kryto.server.module.IModuleConnection;
import com.pro100kryto.server.utils.datagram.packet.DatagramPacketRecyclable;

import java.rmi.RemoteException;

public interface IPacketPoolModuleConnection extends IModuleConnection {
    DatagramPacketRecyclable getNextPacket() throws RemoteException;
    int getMaxCapacity() throws RemoteException;
    int getRemainingCapacity() throws RemoteException;
}
