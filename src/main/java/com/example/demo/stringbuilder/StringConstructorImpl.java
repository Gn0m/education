package com.example.demo.stringbuilder;

import lombok.Getter;

import java.util.Arrays;

public class StringConstructorImpl implements StringConstructor {

    private MyBuilder builder = null;
    private StringConstructor prev;
    private int length = 10;
    @Getter
    private char[] chars = new char[length];
    @Getter
    private int count = 0;

    public StringConstructorImpl(StringConstructor prev, String s, MyBuilder builder) {
        this.builder = builder;
        this.prev = prev;
        chars = prev.getChars();
        if (prev.getCount() != 0)
            this.count = prev.getCount();
        init(s);

    }

    public StringConstructorImpl() {
    }

    @Override
    public void concat(String s, MyBuilder builder) {

        StringConstructorImpl stringConstructor = new StringConstructorImpl(this, s, builder);

        builder.setConstructor(stringConstructor);
    }

    private void init(String s) {
        char[] charArray = s.toCharArray();
        checkSize(charArray.length);
        int localLength = count + charArray.length;
        for (char c : charArray) {
            for (int i = count; i < localLength; ) {
                chars[i] = c;
                count++;
                break;
            }
        }
    }

    private void checkSize(int arrayLength) {
        int size = count;
        boolean fill = (double) (size + arrayLength) / length >= 0.75;
        if (fill) {
            length *= 2;
            chars = Arrays.copyOf(chars, length);
        }
    }

    @Override
    public void undo() {
        builder.setConstructor(prev);
    }

    @Override
    public String toString() {
        return String.valueOf(chars, 0, count);
    }

}
