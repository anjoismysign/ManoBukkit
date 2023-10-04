package me.anjoismysign.manobukkit.entities.decorators.implementations;

import me.anjoismysign.manobukkit.ManoBukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ManitoBlockPosition {
    private static final Class<?> BLOCK_POSITION_CLASS = ManoBukkit.getInstance().getNMSClass("core.BlockPosition");
    private static final Constructor<?> BLOCK_POSITION_CONSTRUCTOR;

    static {
        try {
            BLOCK_POSITION_CONSTRUCTOR = BLOCK_POSITION_CLASS.getConstructor(int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private final Object blockPosition;

    @NotNull
    public static ManitoBlockPosition of(int x, int y, int z) {
        Object blockPosition;
        try {
            blockPosition = BLOCK_POSITION_CONSTRUCTOR.newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return new ManitoBlockPosition(blockPosition);
    }

    private ManitoBlockPosition(Object blockPosition) {
        this.blockPosition = blockPosition;
    }

    public Object get() {
        return blockPosition;
    }
}
