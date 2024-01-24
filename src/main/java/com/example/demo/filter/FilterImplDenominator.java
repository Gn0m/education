package com.example.demo.filter;

public class FilterImplDenominator implements Filter {

    @Override
    public Object apply(Object o) {
        if (o instanceof Integer integer) {
            return integer / 2d;
        }
        throw new UnsupportedOperationException();
    }
}
