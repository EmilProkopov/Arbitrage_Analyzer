package com.course_project.arbitrage_analyzer.arbitrage;

import com.course_project.arbitrage_analyzer.interfaces.ArbitrageModel;
import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.interfaces.ArbitrageView;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;

public class ArbitragePresenterImpl implements ArbitragePresenter,
        OrderBookGetter.OrderBookGetterProgressListener {

    private ArbitrageView view;
    private ArbitrageModel model;
    private boolean paused;

    public ArbitragePresenterImpl(ArbitrageView view) {
        this.view = view;
        this.paused = false;
        //this.model = new
        this.model.startBackgroundTask();
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
        this.model = null;
    }

    @Override
    public void onViewStop() {
        this.model.canselBackgroundTask();
        this.paused = false;
    }

    @Override
    public void onPauseResumeClick() {
        this.paused = !this.paused;
        if (this.paused) {
            this.model.canselBackgroundTask();
        } else {
            this.model.startBackgroundTask();
        }
        this.view.updateResumePauseView(this.paused);
    }

    @Override
    public void onViewRestart() {
        this.model.restartBackgroundTask();
        this.paused = false;
        this.view.updateResumePauseView(this.paused);
    }

    @Override
    public void onUpdateOrderBookGetterProgress(Integer progress) {
        this.view.updateProgressBar((progress));
    }
}
