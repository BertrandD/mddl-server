package com.middlewar.core.utils;

import java.util.Random;

/**
 * @author Leboc Philippe.
 */
public final class Rnd {

    private static final Random _random = new Random();

    /**
     * Gets a random integer number from min(inclusive) to max(inclusive)
     * @param min The minimum value
     * @param max The maximum value
     * @return A random integer number from min to max
     */
    public static int get(final int min, final int max) {
        return min + (int) (_random.nextDouble() * ((max - min) + 1));
    }

    /**
     * Gets a random long number from min(inclusive) to max(inclusive)
     * @param min The minimum value
     * @param max The maximum value
     * @return A random long number from min to max
     */
    public static long get(final long min, final long max) {
        return min + (long) (_random.nextDouble() * ((max - min) + 1));
    }

}
