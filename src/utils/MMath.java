package utils;

import static java.lang.StrictMath.sqrt;

public class MMath {
    public static int abs(int a) {
        return a > 0 ? a : -a;
    }

    public static int triangle(int a, int b) {
        return (int) sqrt(a * a + b * b);
    }
}
