package com.pro100kryto.server.modules.packetpool;

import cn.danielw.fop.ObjectFactoryRaw;
import cn.danielw.fop.ObjectPool;
import cn.danielw.fop.Poolable;
import com.pro100kryto.server.utils.datagram.packet.DatagramPacketRecyclable;
import com.pro100kryto.server.utils.datagram.pool.RecycleStatus;
import lombok.Getter;

public class DatagramPacketFactory implements ObjectFactoryRaw<DatagramPacketFactory.DatagramPacketPooled> {
    @Getter
    private final int packetSize;

    public DatagramPacketFactory(int packetSize) {
        this.packetSize = packetSize;
    }

    @Override
    public Poolable<DatagramPacketPooled> create(ObjectPool<DatagramPacketPooled> pool, int partition) {
        final Poolable<DatagramPacketPooled> poolable = new Poolable<>(
                new DatagramPacketPooled(packetSize),
                pool, partition
        );
        poolable.getObject().thisPoolable = poolable;
        return poolable;
    }

    @Override
    public void recycle(Poolable<DatagramPacketPooled> poolable) {
        poolable.getObject().recycle();
    }

    @Override
    public void restore(Poolable<DatagramPacketPooled> poolable) {
        poolable.getObject().restoreWithoutBorrow();
    }

    @Override
    public void destroy(Poolable<DatagramPacketPooled> packetPooled) {
        packetPooled.getObject().destroyWithoutDecrease();
    }

    @Override
    public boolean validate(Poolable<DatagramPacketPooled> packetPooled) {
        return packetPooled.getObject().getStatus() == RecycleStatus.Recycled;
    }

    public static final class DatagramPacketPooled extends DatagramPacketRecyclable {
        private Poolable<DatagramPacketPooled> thisPoolable;

        public DatagramPacketPooled(int capacity) {
            super(capacity);
        }

        @Override
        public void recycle() {
            if (getStatus() == RecycleStatus.Recycled) return;
            super.recycle();
            thisPoolable.returnObject();
        }

        @Override
        public void restore() {
            thisPoolable.borrowObjectOrThrow();
            super.restore();
        }

        public void restoreWithoutBorrow() {
            super.restore();
        }

        @Override
        public void destroy() {
            thisPoolable.decreaseObjectIfExists();
            super.destroy();
        }

        public void destroyWithoutDecrease() {
            super.destroy();
        }
    }
}
