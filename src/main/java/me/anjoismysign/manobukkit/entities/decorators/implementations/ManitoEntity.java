package me.anjoismysign.manobukkit.entities.decorators.implementations;

import me.anjoismysign.manobukkit.ManoBukkit;
import me.anjoismysign.manobukkit.entities.decorators.ManoEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ManitoEntity implements ManoEntity {
    private static final Class<?> CRAFT_ENTITY_CLASS = ManoBukkit.getInstance().getCraftBukkitClass("entity.CraftEntity");
    private static final Class<?> NMS_ENTITY = ManoBukkit.getInstance().getNMSClass("world.entity.Entity");

    private static final Method GET_HANDLE_METHOD;
    private static final Method VEHICLE_TELEPORT_METHOD;

    static {
        try {
            GET_HANDLE_METHOD = CRAFT_ENTITY_CLASS.getMethod("getHandle");
            VEHICLE_TELEPORT_METHOD = NMS_ENTITY.getMethod("b", double.class, double.class, double.class, float.class, float.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final Entity entity;

    @NotNull
    protected static ManitoEntity of(@NotNull Entity entity) {
        Objects.requireNonNull(entity);
        return new ManitoEntity(entity);
    }

    private ManitoEntity(Entity entity) {
        this.entity = entity;
    }

    protected Object getNMSEntity() {
        try {
            return GET_HANDLE_METHOD.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Entity get() {
        return entity;
    }

    /**
     * Will teleport the entity and passengers to the specified location
     * if the entity is not a LivingEntity.
     *
     * @param location the location to teleport the entity to
     * @return true if the entity was teleported, false otherwise
     */
    public boolean vehicleTeleport(Location location) {
        var nmsEntity = getNMSEntity();
        try {
            VEHICLE_TELEPORT_METHOD.invoke(nmsEntity,
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch());
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
