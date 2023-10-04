package me.anjoismysign.manobukkit.entities.decorators.implementations;

import me.anjoismysign.manobukkit.ManoBukkit;
import me.anjoismysign.manobukkit.entities.decorators.ManoBlockState;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ManitoBlockState implements ManoBlockState {
    private final static Class<?> CRAFT_BLOCK_STATE_CLASS =
            ManoBukkit.getInstance().getCraftBukkitClass("block.CraftBlockState");

    private static final Method SET_WORLD_HANDLE_METHOD;
    private static final Method IS_PLACED_METHOD;
    private static final Field POSITION_FIELD;
    private static final Field WORLD_FIELD;

    static {
        try {
            SET_WORLD_HANDLE_METHOD = CRAFT_BLOCK_STATE_CLASS.getMethod("setWorldHandle",
                    ManoBukkit.getInstance().getNMSClass("world.level.GeneratorAccess"));
            IS_PLACED_METHOD = CRAFT_BLOCK_STATE_CLASS.getMethod("isPlaced");
            POSITION_FIELD = CRAFT_BLOCK_STATE_CLASS.getDeclaredField("position");
            WORLD_FIELD = CRAFT_BLOCK_STATE_CLASS.getDeclaredField("world");
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final BlockState blockState;

    /**
     * Converts a BlockState to a BlobBlockState.
     *
     * @param blockState the BlockState to convert
     * @return the BlobBlockState
     */
    @NotNull
    protected static ManitoBlockState of(@NotNull BlockState blockState) {
        Objects.requireNonNull(blockState);
        return new ManitoBlockState(blockState);
    }

    private ManitoBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockState get() {
        return blockState;
    }

    private boolean isPlaced() {
        try {
            return (boolean) IS_PLACED_METHOD.invoke(blockState);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void setWorldHandle(Object nmsWorld) {
        try {
            SET_WORLD_HANDLE_METHOD.invoke(blockState, nmsWorld);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Object getPosition() {
        Object result;
        try {
            result = POSITION_FIELD.get(blockState);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Nullable
    private Object getWorld() {
        Object result;
        try {
            result = WORLD_FIELD.get(blockState);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private void setPosition(Object position) {
        try {
            POSITION_FIELD.set(blockState, position);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setWorld(Object world) {
        try {
            WORLD_FIELD.set(blockState, world);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
        ManitoWorld manoWorld = ManitoWorld.of(craftWorld);
        Object nmsWorld = manoWorld.getNMSWorld();
        Object oldAccess = isPlaced() ? ManitoWorld.of(blockState.getWorld()).getNMSWorld() : nmsWorld;
        ManitoBlockPosition blockPosition = ManitoBlockPosition.of(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        setWorldHandle(nmsWorld);
        boolean update;
        POSITION_FIELD.setAccessible(true);
        WORLD_FIELD.setAccessible(true);
        try {
            setWorldHandle(nmsWorld);
            Object oldPosition = getPosition();
            Object oldWorld = getWorld();
            setPosition(blockPosition.get());
            setWorld(craftWorld);
            update = blockState.update(force, applyPhysics);
            setWorld(oldWorld);
            setPosition(oldPosition);
        } catch (Throwable e) {
            e.printStackTrace();
            update = false;
        }
        POSITION_FIELD.setAccessible(false);
        WORLD_FIELD.setAccessible(false);
        setWorldHandle(oldAccess);
        return update;
    }
}
