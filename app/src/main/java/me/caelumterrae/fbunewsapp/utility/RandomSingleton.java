package me.caelumterrae.fbunewsapp.utility;

import java.util.Random;

public class RandomSingleton {
    private static RandomSingleton instance;
    private Random rnd;

    private RandomSingleton() {
        rnd = new Random();
    }

    public static RandomSingleton getInstance() {
        if(instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public int nextInt() {
        return rnd.nextInt();
    }
}
