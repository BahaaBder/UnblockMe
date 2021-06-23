package com.unblockme.unblockme.core;


import com.unblockme.unblockme.utils.Dimension;
import com.unblockme.unblockme.utils.Position;

/**
 * Class that manages (facilitates) block creation
 */
public class BlockFactory {
    /**
     * Create a horizontal block of width w
     *
     * @param x position following X
     * @param y position following Y
     * @param w block width (along X)
     * @return Block
     */
    public static Block createHorizontalBlock(int x, int y, int w) {
        return new Block(new Dimension(1, w), new Position(x, y));
    }

    /**
     * Creates a vertical block of length l
     *
     * @param x position following X
     * @param y position following Y
     * @param l block width (along X)
     * @return
     */
    public static Block createVerticalBlock(int x, int y, int l) {
        return new Block(new Dimension(l, 1), new Position(x, y));
    }
}
