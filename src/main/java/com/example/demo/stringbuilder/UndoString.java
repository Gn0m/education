package com.example.demo.stringbuilder;

import java.util.Arrays;
import java.util.Objects;

public class UndoString implements State {

    private MyBuilder builder;
    private int charsLength;
    private char[] chars;
    private int charsItemCount;
    private String s;


    public UndoString(MyBuilder builder, String s, int charsItemCount, char[] chars, int charsLength) {
        this.builder = builder;
        this.s = s;
        this.charsItemCount = charsItemCount;
        this.chars = chars;
        this.charsLength = charsLength;
    }

    @Override
    public void concat(String s) {
        MyBuilder next = this.builder;
        addToChars(s);
        next.setState(new ConcatString(builder, s, charsItemCount, chars, charsLength));
    }

    @Override
    public void undo() {
        MyBuilder next = this.builder;
        if (Objects.nonNull(s)) {
            charsItemCount = charsItemCount - s.length();
        }
        char[] copy = new char[charsItemCount];
        System.arraycopy(chars, 0, copy, 0, charsItemCount);
        chars = copy;
        s = null;
        next.setState(new UndoString(builder, s, charsItemCount, chars, charsLength));
    }

    private void addToChars(String s) {
        char[] charArray = s.toCharArray();
        checkSize(charArray.length);
        int localLength = charsItemCount + charArray.length;
        for (char c : charArray) {
            for (int i = charsItemCount; i < localLength; ) {
                chars[i] = c;
                charsItemCount++;
                break;
            }
        }
    }


    private void checkSize(int arrayLength) {
        int size = charsItemCount;
        boolean fill = (double) (size + arrayLength) / charsLength >= 0.75;
        if (fill) {
            charsLength *= 2;
            chars = Arrays.copyOf(chars, charsLength);
        }
        boolean decrease = (double) (size + arrayLength) / charsLength <= 0.25;
        if (decrease) {
            charsLength /= 2;
            chars = Arrays.copyOf(chars, charsLength);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(chars, 0, charsItemCount);
    }
}
