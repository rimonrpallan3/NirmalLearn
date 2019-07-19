package com.rimon.nirmaltest.landing.model;

 abstract public class Animal {

    // abstract methods
    abstract public void move();
    abstract public void eat();

    // concrete method
    public void label() {
        System.out.println("Animal's data:");
    }
    //https://raygun.com/blog/oop-concepts-java/
}
