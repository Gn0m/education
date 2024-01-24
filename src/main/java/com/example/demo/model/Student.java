package com.example.demo.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Student {
    private final Map<String, Integer> grades;

    public Student(Map<String, Integer> grades) {
        this.grades = grades;
    }

}
