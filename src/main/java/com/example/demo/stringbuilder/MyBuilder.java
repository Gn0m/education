package com.example.demo.stringbuilder;

public class MyBuilder {

    private State state;

    public MyBuilder(String s) {
        state = new StartString(this, s);
    }


    public void concat(String s) {
        state.concat(s);
    }

    public void undo() {
        state.undo();
    }

    @Override
    public String toString() {
        return state.toString();
    }

    protected void setState(State state) {
        this.state = state;
    }
}
