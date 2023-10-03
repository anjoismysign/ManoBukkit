package me.anjoismysign.manobukkit.entities.collider;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ColliderFactory {
    private static ColliderFactory instance;

    public static ColliderFactory getInstance() {
        if (instance == null) {
            instance = new ColliderFactory();
        }
        return instance;
    }

    /**
     * Creates a new AsyncCollisioner.
     * Collision checks and collideFunction will be run asynchronously.
     *
     * @param entity          The entity that's meant to have a BoundingBox.
     * @param period          The period at which it should check for collisions.
     * @param collideFunction Called when the entity collides with another entity.
     *                        Return false to stop checking for collisions.
     * @param plugin          The plugin that's creating this AsyncCollisioner.
     * @param <T>             The type of entity.
     * @return The new AsyncCollisioner.
     */
    @NotNull
    public <T extends Entity> AsyncCollider<T> ASYNC(@NotNull T entity,
                                                     long period,
                                                     @NotNull Function<Entity, Boolean> collideFunction,
                                                     @NotNull JavaPlugin plugin) {
        return AsyncCollider.of(entity, period, collideFunction, plugin);
    }

    /**
     * Creates a new SyncCollisioner.
     * Collision checks and collideFunction will be run on main thread.
     *
     * @param entity          The entity that's meant to have a BoundingBox.
     * @param period          The period at which it should check for collisions.
     * @param collideFunction Called when the entity collides with another entity.
     *                        Return false to stop checking for collisions.
     * @param plugin          The plugin that's creating this SyncCollisioner.
     * @param <T>             The type of entity.
     * @return The new SyncCollisioner.
     */
    @NotNull
    public <T extends Entity> SyncCollider<T> SYNCHRONOUSLY(@NotNull T entity,
                                                            long period,
                                                            @NotNull Function<Entity, Boolean> collideFunction,
                                                            @NotNull JavaPlugin plugin) {
        return SyncCollider.of(entity, period, collideFunction, plugin);
    }
}
