package com.example.demo.stringbuilder;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class StartString implements State {

    private MyBuilder builder;
    private int charsLength = 10;
    private char[] chars = new char[charsLength];
    private int charsItemCount = 0;


    public StartString(MyBuilder builder, String s) {
        this.builder = builder;
        addToChars(s);
    }

    @Override
    public void concat(String s) {
        addToChars(s);
        MyBuilder next = this.builder;
        next.setState(new ConcatString(builder, s, charsItemCount, chars, charsLength));
    }

    @Override
    public void undo() {
        log.info("Empty");
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
    }

    @Override
    public String toString() {
        return String.valueOf(chars, 0, charsItemCount);
    }

}
