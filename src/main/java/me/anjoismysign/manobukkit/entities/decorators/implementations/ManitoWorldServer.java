package me.anjoismysign.manobukkit.entities.decorators.implementations;

import me.anjoismysign.manobukkit.ManoBukkit;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Method;

public class ManitoWorldServer {
    private static final Class<?> WORLD_SERVER_CLASS = ManoBukkit.getInstance().getNMSClass("server.level.WorldServer");
    private static final Method TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS_METHOD;

    static {
        try {
            TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS_METHOD = WORLD_SERVER_CLASS.getMethod("tryAddFreshEntityWithPassengers",
                    ManoBukkit.getInstance().getNMSClass("world.entity.Entity"),
                    CreatureSpawnEvent.SpawnReason.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final Object worldServer;

    protected ManitoWorldServer(Object worldServer) {
        this.worldServer = worldServer;
    }

    /**
     * Will attempt to add the entity to the world.
     *
     * @param nmsEntity   the entity to add
     * @param spawnReason the spawn reason
     * @return true if the entity was added, false otherwise
     */
    public boolean tryAddFreshEntityWithPassengers(Object nmsEntity, CreatureSpawnEvent.SpawnReason spawnReason) {
        try {
            return (boolean) TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS_METHOD
                    .invoke(worldServer, nmsEntity, spawnReason);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
