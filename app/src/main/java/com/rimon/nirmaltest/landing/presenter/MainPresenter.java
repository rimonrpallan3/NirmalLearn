package com.rimon.nirmaltest.landing.presenter;

import com.rimon.nirmaltest.landing.model.Animal;
import com.rimon.nirmaltest.landing.view.IMainView;

public class MainPresenter extends Animal implements IMainPresenter{

    IMainView IMainView;
    String setData ="Value Set ";

    public MainPresenter(IMainView IMainView) {
        this.IMainView = IMainView;
    }


    @Override
    public void fetchData() {
        IMainView.setData(setData);
    }

    @Override
    public void move() {
        System.out.println("Moves by MainPresenter.");
    }

    @Override
    public void eat() {
        System.out.println("Eats MainPresenter.");
    }
}
