package me.anjoismysign.manobukkit.entities.decorators.implementations;

import me.anjoismysign.manobukkit.ManoBukkit;
import me.anjoismysign.manobukkit.entities.decorators.ManoWorld;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ManitoWorld implements ManoWorld {
    private static final Class<?> CRAFT_WORLD_CLASS = ManoBukkit.getInstance().getCraftBukkitClass("CraftWorld");


    private static final Method GET_HANDLE_METHOD;

    static {
        try {
            GET_HANDLE_METHOD = CRAFT_WORLD_CLASS.getMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final World world;

    @NotNull
    protected static ManitoWorld of(@NotNull World world) {
        Objects.requireNonNull(world);
        return new ManitoWorld(world);
    }

    private ManitoWorld(World world) {
        this.world = world;
    }

    protected Object getNMSWorld() {
        try {
            return GET_HANDLE_METHOD.invoke(world);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private ManitoWorldServer getManoWorldServer() {
        Object nmsWorld = Objects.requireNonNull(getNMSWorld());
        return new ManitoWorldServer(nmsWorld);
    }

    public World get() {
        return world;
    }

    /**
     * Will attempt to add the entity to the world.
     *
     * @param entity the entity to add
     * @return true if the entity was added, false otherwise
     */
    public boolean tryAddFreshEntityWithPassengers(Entity entity) {
        ManitoEntity manoEntity = ManitoEntity.of(entity);
        var nmsEntity = manoEntity.getNMSEntity();
        boolean result;
        try {
            result = getManoWorldServer().tryAddFreshEntityWithPassengers(nmsEntity,
                    CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }
}
