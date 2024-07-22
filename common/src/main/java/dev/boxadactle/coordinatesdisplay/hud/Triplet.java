package dev.boxadactle.coordinatesdisplay.hud;

public class Triplet<F, S, T> {
    F first;
    S second;
    T third;

    public Triplet(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getA() {
        return first;
    }

    public S getB() {
        return second;
    }

    public T getC() {
        return third;
    }
}
