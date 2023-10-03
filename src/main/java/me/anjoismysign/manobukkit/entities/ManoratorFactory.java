package me.anjoismysign.manobukkit.entities;

import me.anjoismysign.manobukkit.entities.decorators.ManoBlockState;
import me.anjoismysign.manobukkit.entities.decorators.ManoEntity;
import me.anjoismysign.manobukkit.entities.decorators.ManoWorld;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating ManoDecorators
 */
public class ManoratorFactory {
    private static ManoratorFactory instance;

    private ManoratorFactory() {
    }

    /**
     * Will get the instance of the ManoDecorators
     *
     * @return the instance of the ManoDecorators
     */
    public static ManoratorFactory getInstance() {
        if (instance == null) {
            instance = new ManoratorFactory();
        }
        return instance;
    }

    public ManoBlockState of(@NotNull BlockState blockState) {
        return ManoBlockState.of(blockState);
    }

    public ManoWorld of(@NotNull World world) {
        return ManoWorld.of(world);
    }

    public ManoEntity of(@NotNull Entity entity) {
        return ManoEntity.of(entity);
    }
}
