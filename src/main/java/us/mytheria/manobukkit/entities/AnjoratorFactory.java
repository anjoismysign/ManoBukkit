package us.mytheria.manobukkit.entities;

import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import us.mytheria.manobukkit.entities.decorators.AnjoBlockState;
import us.mytheria.manobukkit.entities.decorators.AnjoEntity;
import us.mytheria.manobukkit.entities.decorators.AnjoWorld;

/**
 * A factory for creating AnjoDecorators
 */
public class AnjoratorFactory {
    private static AnjoratorFactory instance;

    private AnjoratorFactory() {
    }

    /**
     * Will get the instance of the AnjoratorFactory
     *
     * @return the instance of the AnjoratorFactory
     */
    public static AnjoratorFactory getInstance() {
        if (instance == null) {
            instance = new AnjoratorFactory();
        }
        return instance;
    }

    public AnjoBlockState of(@NotNull BlockState blockState) {
        return AnjoBlockState.of(blockState);
    }

    public AnjoWorld of(@NotNull World world) {
        return AnjoWorld.of(world);
    }

    public AnjoEntity of(@NotNull Entity entity) {
        return AnjoEntity.of(entity);
    }
}
