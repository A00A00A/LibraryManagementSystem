package org.example;
public abstract class AbstractAction {
    public abstract String getActionName();
    public abstract void run();
    public String getDesc() {
        return "no description.";
    }
    public void println(Object s) {
        System.out.println(this.getActionName().toUpperCase() + "> " + s);
    }
    public void print(Object s) {
        System.out.print(this.getActionName().toUpperCase() + "> " + s);
    }
}