package com.example.geometrylibrary.figure;

import lombok.Data;

@Data
public class Rectangle {

    private Rectangle() {
    }

    public static double area(double sideA, double sideB) {
        return sideA * sideB;
    }

    public static double perimeter(double sideA, double sideB) {
        return (sideA + sideB) * 2;
    }
}
