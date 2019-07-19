package com.rimon.nirmaltest.landing.presenter;

import com.rimon.nirmaltest.landing.model.Animal;

public class Fish extends Animal {
    @Override
    public void move() {
        System.out.println("Moves by swimming.");
    }

    @Override
    public void eat() {
        System.out.println("Eats seafood.");
    }
}