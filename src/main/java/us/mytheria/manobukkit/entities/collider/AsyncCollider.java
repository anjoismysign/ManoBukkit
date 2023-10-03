package us.mytheria.manobukkit.entities.collider;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import us.mytheria.manobukkit.entities.LogicController;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * An asynchronous Collider.
 * Will perform collision checks and collision logic off main thread.
 *
 * @param <T> The type of entity.
 */
public class AsyncCollider<T extends Entity> implements Collider<T>, LogicController {
    private final T entity;
    private final long period;
    private final Function<Entity, Boolean> collideFunction;

    protected BukkitTask tick;
    protected final JavaPlugin plugin;

    @NotNull
    protected static <T extends Entity> AsyncCollider<T> of(@NotNull T entity,
                                                            long period,
                                                            @NotNull Function<Entity, Boolean> collideFunction,
                                                            @NotNull JavaPlugin plugin) {
        Objects.requireNonNull(entity, "entity cannot be null");
        Objects.requireNonNull(collideFunction, "collideFunction cannot be null");
        Objects.requireNonNull(plugin, "plugin cannot be null");
        return new AsyncCollider<>(entity, period, collideFunction, plugin);
    }

    protected AsyncCollider(T entity,
                            long period,
                            Function<Entity, Boolean> collideFunction,
                            JavaPlugin plugin) {
        this.entity = entity;
        this.period = period;
        this.collideFunction = collideFunction;
        this.plugin = plugin;

    }

    public T getEntity() {
        return entity;
    }

    public boolean onCollide(Entity other) {
        return collideFunction.apply(other);
    }

    public long getPeriod() {
        return period;
    }

    /**
     * Will pause the collision checking.
     */
    public void pauseLogic() {
        tick.cancel();
    }

    /**
     * Will resume the collision checking.
     */
    public void resumeLogic() {
        if (tick != null && !tick.isCancelled())
            return;
        this.tick =
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!entity.isValid()) {
                            tick.cancel();
                            return;
                        }
                        Iterator<Entity> iterator = entity.getWorld().getEntities().stream()
                                .filter(e -> !e.equals(entity))
                                .filter(e -> e.getBoundingBox().overlaps(entity.getBoundingBox()))
                                .iterator();
                        while (iterator.hasNext()) {
                            Entity other = iterator.next();
                            if (!onCollide(other))
                                break;
                        }
                    }
                }.runTaskTimerAsynchronously(plugin, 0, period);
    }
}
