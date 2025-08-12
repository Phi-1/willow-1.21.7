package phi.willow.util;

import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Server-only for now
 */
public class TickTimers {

    private static final List<TickTimer> TIMERS = new ArrayList<>();

    public static void schedule(Runnable task, int tickDelay) {
        TIMERS.add(new TickTimer(task, tickDelay));
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
        for (TickTimer timer : toRemove) {
            TIMERS.remove(timer);
        }
    }

    public static void onServerTick(ServerWorld world) {
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
