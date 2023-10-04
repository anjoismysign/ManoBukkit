package me.anjoismysign.manobukkit.entities.decorators;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

public interface ManoBlockState extends ManoDecorator<BlockState> {
    /**
     * Allows updating a block state in a specific location
     * without changing the original block state's location.
     *
     * @param force        true to forcefully set the state
     * @param applyPhysics false to cancel updating physics on surrounding blocks
     * @param location     the location to update the block state at
     * @return true if the update was successful, false otherwise
     */
    boolean update(boolean force, boolean applyPhysics, @NotNull Location location);
}
