package com.example.demo.stringbuilder;

public interface StringConstructor {

    void concat(String s, MyBuilder builder);

    void undo();

    int getCount();

    char[] getChars();

}
