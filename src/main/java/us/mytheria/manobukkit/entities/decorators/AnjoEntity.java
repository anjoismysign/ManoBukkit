package us.mytheria.manobukkit.entities.decorators;

import com.nesaak.noreflection.NoReflection;
import com.nesaak.noreflection.access.DynamicCaller;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import us.mytheria.manobukkit.ManoBukkit;

import java.lang.reflect.Method;
import java.util.Objects;

public class AnjoEntity {
    private static final Class<?> CRAFT_ENTITY_CLASS = ManoBukkit.getInstance().getCraftBukkitClass("entity.CraftEntity");

    private static final Method NMS_ENTITY;

    static {
        try {
            NMS_ENTITY = CRAFT_ENTITY_CLASS.getDeclaredMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Method TELEPORT_NO_CHECK;

    static {
        try {
            TELEPORT_NO_CHECK = NMS_ENTITY.getReturnType().getDeclaredMethod("b",
                    double.class,
                    double.class,
                    double.class,
                    float.class,
                    float.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private static final DynamicCaller NF_NMS_ENTITY = NoReflection.shared().get(NMS_ENTITY);
    private static final DynamicCaller NF_VEHICLE_TELEPORT = NoReflection.shared().get(TELEPORT_NO_CHECK);

    private final Entity entity;

    @NotNull
    public static AnjoEntity of(@NotNull Entity entity) {
        Objects.requireNonNull(entity);
        return new AnjoEntity(entity);
    }

    private AnjoEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Will teleport the entity and passengers to the specified location
     * if the entity is not a LivingEntity.
     *
     * @param location the location to teleport the entity to
     * @return true if the entity was teleported, false otherwise
     */
    public boolean vehicleTeleport(Location location) {
        NF_VEHICLE_TELEPORT.call(NF_NMS_ENTITY.call(entity),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch());
        return true;
    }
}
