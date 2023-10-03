package me.anjoismysign.manobukkit.entities.collider;

import org.bukkit.entity.Entity;

public interface Collider<T extends Entity> {
    /**
     * The entity that's meant to have a BoundingBox,
     * as the entity that's meant to collide with other entities.
     *
     * @return The entity
     */
    T getEntity();

    /**
     * Called when the entity collides with another entity
     *
     * @param other The entity it collided with
     * @return Whether to keep checking for collisions
     */
    boolean onCollide(Entity other);

    /**
     * The period at which it should check for collisions
     *
     * @return The period
     */
    long getPeriod();
}
