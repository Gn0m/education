package com.example.geometrylibrary.figure;

import lombok.Data;

@Data
public class Triangle {

    private Triangle() {
    }

    public static double areaSide(double side, double h) {
        return (side * h) / 2;
    }

    public static double areaRadius(double P, double r) {
        return (r * P) / 2;
    }

    public static double areaRadius(double sideA, double sideB, double sin) {
        return (sideA * sideB * sin) / 2;
    }

    public static double heronArea(double sideA, double sideB, double sideC) {
        double p = (sideA + sideB + sideC) / 2;
        return Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
    }

    public static double perimetr(double sideA, double sideB, double sideC) {
        return sideA + sideB + sideC;
    }
}
