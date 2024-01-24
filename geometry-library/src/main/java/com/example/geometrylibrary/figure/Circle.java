package com.example.geometrylibrary.figure;

import lombok.Data;

@Data
public class Circle {

    private Circle() {
    }

    public static double area(double r) {
        return Math.PI * (r * r);
    }

    public static double perimeter(double r) {
        return (2 * Math.PI) * r;
    }
}
