package us.mytheria.manobukkit.entities.decorators;

import com.nesaak.noreflection.NoReflection;
import com.nesaak.noreflection.access.DynamicCaller;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import us.mytheria.manobukkit.ManoBukkit;

import java.lang.reflect.Method;
import java.util.Objects;

public class AnjoWorld {
    private static final Class<?> CRAFT_WORLD_CLASS = ManoBukkit.getInstance().getCraftBukkitClass("CraftWorld");
    private static final Class<?> CRAFT_ENTITY_CLASS = ManoBukkit.getInstance().getCraftBukkitClass("entity.CraftEntity");

    private final static Method NMS_WORLD;

    static {
        try {
            NMS_WORLD = CRAFT_WORLD_CLASS.getDeclaredMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Method NMS_ENTITY;

    static {
        try {
            NMS_ENTITY = CRAFT_ENTITY_CLASS.getDeclaredMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Method TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS;

    static {
        try {
            TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS = NMS_WORLD.getReturnType().getDeclaredMethod("tryAddFreshEntityWithPassengers", NMS_ENTITY.getReturnType(), CreatureSpawnEvent.SpawnReason.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final DynamicCaller NF_NMS_WORLD = NoReflection.shared().get(NMS_WORLD);
    private static final DynamicCaller NF_NMS_ENTITY = NoReflection.shared().get(NMS_ENTITY);
    private static final DynamicCaller NF_TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS = NoReflection.shared().get(TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS);

    private final World world;

    @NotNull
    public static AnjoWorld of(@NotNull World world) {
        Objects.requireNonNull(world);
        return new AnjoWorld(world);
    }

    private AnjoWorld(World world) {
        this.world = world;
    }

    /**
     * Will attempt to add the entity to the world.
     *
     * @param entity the entity to add
     * @return true if the entity was added, false otherwise
     */
    public boolean tryAddFreshEntityWithPassengers(Entity entity) {
        var nmsEntity = NF_NMS_ENTITY.call(entity);
        var nmsWorld = NF_NMS_WORLD.call(world);
        return (boolean) NF_TRY_ADD_FRESH_ENTITY_WITH_PASSENGERS.call(nmsWorld, nmsEntity,
                CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
}
