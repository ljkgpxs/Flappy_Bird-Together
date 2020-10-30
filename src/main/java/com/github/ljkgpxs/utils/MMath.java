package com.github.ljkgpxs.utils;

import static java.lang.StrictMath.sqrt;

public class MMath {
    public static int abs(int a) {
        return a > 0 ? a : -a;
    }

    public static int triangle(double a, double b) {
        return (int) sqrt(a * a + b * b);
    }

    public static double max(double a, double b) { return a > b ? a : b; }

    public static double min(double a, double b) { return a < b ? a : b; }

    public static double max(double ... as) {
        double m = as[0];
        for (double a : as) {
            if (a > m)
                m = a;
        }
        return m;
    }

    public static double min(double ... as) {
        double m = as[0];
        for (double a : as) {
            if (a < m)
                m = a;
        }
        return m;
    }

}
