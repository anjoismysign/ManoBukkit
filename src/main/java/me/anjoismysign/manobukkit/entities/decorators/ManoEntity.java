package me.anjoismysign.manobukkit.entities.decorators;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface ManoEntity extends ManoDecorator<Entity> {
    /**
     * Will teleport the entity and passengers to the specified location
     * if the entity is not a LivingEntity.
     *
     * @param location the location to teleport the entity to
     * @return true if the entity was teleported, false otherwise
     */
    boolean vehicleTeleport(Location location);
}
