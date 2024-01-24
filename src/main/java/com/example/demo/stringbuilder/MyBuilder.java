package com.example.demo.stringbuilder;

public class MyBuilder {

    private StringConstructor constructor = new StringConstructorImpl();

    public MyBuilder(String string) {
        constructor.concat(string, this);
    }

    public void concat(String s) {
        constructor.concat(s, this);
    }

    public void undo() {
        constructor.undo();
    }

    @Override
    public String toString() {
        return constructor.toString();
    }

    protected void setConstructor(StringConstructor constructor) {
        this.constructor = constructor;
    }
}
