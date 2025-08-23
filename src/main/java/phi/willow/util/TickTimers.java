package phi.willow.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TickTimers {

    private static final List<TickTimer> TIMERS = new ArrayList<>();
    private static final List<TickTimer> TO_ADD = new ArrayList<>();

    public static void schedule(Runnable task, int tickDelay) {
        TO_ADD.add(new TickTimer(task, tickDelay));
    }

    public static void tickAll() {
        List<TickTimer> toRemove = new ArrayList<>();
        for (TickTimer timer : TIMERS) {
            int ticksRemaining = timer.tick();
            if (ticksRemaining <= 0) {
                timer.getTask().run();
                toRemove.add(timer);
            }
        }
        TIMERS.removeAll(toRemove);
    }

    public static void onServerTick(ServerWorld world) {
        TIMERS.addAll(TO_ADD);
        TO_ADD.clear();
        tickAll();
    }

    // NOTE: could take a ClientWorld but that would involve client hooks. This works for now
    public static void onClientTick(World world) {
        TIMERS.addAll(TO_ADD);
        TO_ADD.clear();
        tickAll();
    }

    private static class TickTimer {

        private final Runnable task;
        private int ticksRemaining;

        public TickTimer(Runnable task, int tickDelay) {
            this.task = task;
            this.ticksRemaining = tickDelay;
        }

        public int tick() {
            this.ticksRemaining--;
            return this.ticksRemaining;
        }

        public Runnable getTask() {
            return this.task;
        }

    }
}
