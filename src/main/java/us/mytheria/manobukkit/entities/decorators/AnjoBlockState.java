package us.mytheria.manobukkit.entities.decorators;

import com.nesaak.noreflection.NoReflection;
import com.nesaak.noreflection.access.DynamicCaller;
import com.nesaak.noreflection.access.FieldAccess;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import us.mytheria.manobukkit.ManoBukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class AnjoBlockState {
    private static final Class<?> BLOCK_POSITION_CLASS = ManoBukkit.getInstance().getNMSClass("core.BlockPosition");
    private static final Class<?>[] BLOCK_POSITION_CONSTRUCTOR_TYPE = {int.class, int.class, int.class};
    private static final Constructor<?> BLOCK_POSITION_CONSTRUCTOR;

    static {
        try {
            BLOCK_POSITION_CONSTRUCTOR = BLOCK_POSITION_CLASS.getConstructor(BLOCK_POSITION_CONSTRUCTOR_TYPE);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Class<?> CRAFT_BLOCK_STATE_CLASS =
            ManoBukkit.getInstance().getCraftBukkitClass("block.CraftBlockState");
    private final static Class<?> CRAFT_WORLD_CLASS =
            ManoBukkit.getInstance().getCraftBukkitClass("CraftWorld");
    private final static Field POSITION_FIELD;

    static {
        try {
            POSITION_FIELD = CRAFT_BLOCK_STATE_CLASS.getDeclaredField("position");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Field WORLD_FIELD;

    static {
        try {
            WORLD_FIELD = CRAFT_BLOCK_STATE_CLASS.getDeclaredField("world");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Method NMS_WORLD;

    static {
        try {
            NMS_WORLD = CRAFT_WORLD_CLASS.getDeclaredMethod("getHandle");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final static Method SET_WORLD_HANDLE;

    static {
        try {
            SET_WORLD_HANDLE = CRAFT_WORLD_CLASS.getDeclaredMethod("setWorldHandle", NMS_WORLD.getReturnType());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Method UPDATE;

    static {
        try {
            UPDATE = CRAFT_BLOCK_STATE_CLASS.getDeclaredMethod("update", boolean.class, boolean.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final DynamicCaller NF_NMS_WORLD = NoReflection.shared().get(NMS_WORLD);
    private static final DynamicCaller NF_SET_WORLD_HANDLE = NoReflection.shared().get(SET_WORLD_HANDLE);
    private static final DynamicCaller NF_BLOCK_POSITION_CONSTRUCTOR = NoReflection.shared().get(BLOCK_POSITION_CONSTRUCTOR);
    private static final DynamicCaller NF_UPDATE = NoReflection.shared().get(UPDATE);
    private static final FieldAccess NF_WORLD = NoReflection.shared().get(WORLD_FIELD);
    private static final FieldAccess NF_POSITION = NoReflection.shared().get(POSITION_FIELD);

    private final BlockState blockState;

    /**
     * Converts a BlockState to a BlobBlockState.
     *
     * @param blockState the BlockState to convert
     * @return the BlobBlockState
     */
    @NotNull
    public static AnjoBlockState of(@NotNull BlockState blockState) {
        Objects.requireNonNull(blockState);
        return new AnjoBlockState(blockState);
    }

    private AnjoBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    /**
     * Allows updating a block state in a specific location
     * without changing the original block state's location.
     *
     * @param force        true to forcefully set the state
     * @param applyPhysics false to cancel updating physics on surrounding blocks
     * @param location     the location to update the block state at
     * @return true if the update was successful, false otherwise
     */
    public boolean update(boolean force, boolean applyPhysics, @NotNull Location location) {
        var craftWorld = Objects.requireNonNull(location.getWorld());
        Object nmsWorld = NF_NMS_WORLD.call(craftWorld);
        NF_SET_WORLD_HANDLE.call(blockState, NF_NMS_WORLD.call(craftWorld));
        Object blockPosition = NF_BLOCK_POSITION_CONSTRUCTOR.call(location.getBlockX(),
                location.getBlockY(), location.getBlockZ());
        Object oldPosition = NF_POSITION.get(blockState);
        Object oldWorld = NF_WORLD.get(blockState);
        NF_POSITION.set(blockState, blockPosition);
        NF_WORLD.set(blockState, craftWorld);
        boolean update = (boolean) NF_UPDATE.call(blockState, force, applyPhysics);
        NF_WORLD.set(blockState, oldWorld);
        NF_POSITION.set(blockState, oldPosition);
        NF_SET_WORLD_HANDLE.call(blockState, nmsWorld);
        return update;
    }
}
