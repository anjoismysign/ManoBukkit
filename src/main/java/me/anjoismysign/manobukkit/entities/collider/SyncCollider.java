package me.anjoismysign.manobukkit.entities.collider;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * An asynchronous Collider.
 * Will perform collision checks and collision logic on main thread.
 *
 * @param <T> The type of entity.
 */
public class SyncCollider<T extends Entity> extends AsyncCollider<T> {

    @NotNull
    protected static <T extends Entity> SyncCollider<T> of(@NotNull T entity,
                                                           long period,
                                                           @NotNull Function<Entity, Boolean> collideFunction,
                                                           @NotNull JavaPlugin plugin) {
        Objects.requireNonNull(entity, "entity cannot be null");
        Objects.requireNonNull(collideFunction, "collideFunction cannot be null");
        Objects.requireNonNull(plugin, "plugin cannot be null");
        return new SyncCollider<>(entity, period, collideFunction, plugin);
    }

    private SyncCollider(T entity,
                         long period,
                         Function<Entity, Boolean> collideFunction,
                         JavaPlugin plugin) {
        super(entity, period, collideFunction, plugin);
    }

    /**
     * Will resume the collision checking.
     */
    public void resumeLogic() {
        if (tick != null && !tick.isCancelled())
            return;
        T entity = getEntity();
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
                }.runTaskTimer(plugin, 0, getPeriod());
    }
}
