package com.ytu;

import java.util.Arrays;
import java.util.Random;

/**
 * Hello world!
 */
public class Route88 {
    public static void main(String[] args) {

        System.out.println(Arrays.toString(decompress(20160401, 1000, 30)));
    }

    private static void compress(int[] array) {
        double bestDist = Math.pow(2, array.length) - 1;
        double bestSeed = 0L;
        double seed = 0;
        for(double i = 0; i < Math.pow(2, array.length); i++) {
            seed = i;
            //double testBits = randomBitString();
        }
    }



    private static int[] decompress(int seed, int module, int length) {
        int[] rebuildArray = new int[length];
        Random r = new Random(seed);
        for (int i = 0; i < length; i++) {
            rebuildArray[i] = r.nextInt() % module;
        }
        return rebuildArray;
    }
}
