package com.japik.modules.packetpool.shared;

import com.japik.module.IModuleConnection;
import com.japik.utils.datagram.packet.DatagramPacketRecyclable;

import java.rmi.RemoteException;

public interface IPacketPoolModuleConnection extends IModuleConnection {
    DatagramPacketRecyclable getNextPacket() throws RemoteException;
    int getMaxCapacity() throws RemoteException;
    int getRemainingCapacity() throws RemoteException;
}
