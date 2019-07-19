package com.rimon.nirmaltest.landing.model;

abstract  class Animal {

    // abstract methods
    abstract void move();
    abstract void eat();

    // concrete method
    void label() {
        System.out.println("Animal's data:");
    }
    //https://raygun.com/blog/oop-concepts-java/
}
