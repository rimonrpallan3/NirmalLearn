package com.rimon.nirmaltest.landing.presenter;

import com.rimon.nirmaltest.landing.view.IMainView;

public class MainPresenter implements IMainPresenter{

    IMainView IMainView;
    String setData ="Value Set ";

    public MainPresenter(IMainView IMainView) {
        this.IMainView = IMainView;
    }


    @Override
    public void fetchData() {
        IMainView.setData(setData);
    }
}
