package com.japik.modules.packetpool;

import cn.danielw.fop.DisruptorObjectPool;
import cn.danielw.fop.PoolConfig;
import com.japik.livecycle.AShortLiveCycleImpl;
import com.japik.livecycle.controller.ILiveCycleImplId;
import com.japik.livecycle.controller.LiveCycleController;
import com.japik.module.AModule;
import com.japik.module.ModuleConnectionParams;
import com.japik.module.ModuleParams;
import com.japik.modules.packetpool.shared.IPacketPoolModuleConnection;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
public class PacketPoolModule extends AModule<IPacketPoolModuleConnection> {
    private DisruptorObjectPool<DatagramPacketFactory.DatagramPacketPooled> pool;

    public PacketPoolModule(ModuleParams moduleParams) {
        super(moduleParams);
    }

    @Override
    public @NotNull IPacketPoolModuleConnection createModuleConnection(ModuleConnectionParams params) {
        return new PacketPoolModuleConnection(this, params);
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
            final PoolConfig config = new PoolConfig();
            // 128 * 8 = 1024 packets in pool
            config.setPartitionsCount(settings.getIntOrDefault("partitions-count", 8));
            config.setMaxPartitionSize(settings.getIntOrDefault("partitions-max-size", 128));
            config.setMinPartitionSize(settings.getIntOrDefault("partitions-min-size", 128));
            config.setMaxIdleMilliseconds(settings.getIntOrDefault("idle-mills", config.getMaxIdleMilliseconds()));
            config.setMaxWaitMilliseconds(settings.getIntOrDefault("wait-mills", config.getMaxWaitMilliseconds()));

            pool = new DisruptorObjectPool<>(
                    config,
                    new DatagramPacketFactory(
                            settings.getIntOrDefault("packet-size", 1024)
                    )
            );
        }

        @Override
        public void start() {
        }

        @Override
        public void stopForce() {
        }

        @Override
        public void destroy() {
            try {
                pool.shutdown();
            } catch (InterruptedException e) {
                logger.warn("Failed shutdown pool", e);
            }
            pool = null;
        }
    }
}
