package me.anjoismysign.manobukkit.entities.decorators;

import me.anjoismysign.manobukkit.entities.decorators.ManoDecorator;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface ManoWorld extends ManoDecorator<World> {
    /**
     * Will attempt to add the entity to the world.
     *
     * @param entity the entity to add
     * @return true if the entity was added, false otherwise
     */
    boolean tryAddFreshEntityWithPassengers(Entity entity);
}
