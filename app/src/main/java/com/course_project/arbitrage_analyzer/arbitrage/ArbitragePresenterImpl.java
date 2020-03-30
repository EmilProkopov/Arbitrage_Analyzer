package com.course_project.arbitrage_analyzer.arbitrage;

import com.course_project.arbitrage_analyzer.interfaces.ArbitrageModel;
import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.interfaces.ArbitrageView;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;
import com.course_project.arbitrage_analyzer.model.OutputDataSet;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;

public class ArbitragePresenterImpl implements ArbitragePresenter,
        OrderBookGetter.OrderBookGetterProgressListener {

    private final String LOGTAG = "LOGTAG";
    private ArbitrageView view;
    private ArbitrageModel model;
    private boolean paused;
    private SettingsContainer lastSettings = null;

    public ArbitragePresenterImpl(ArbitrageView view) {
        this.view = view;
        this.paused = false;
        this.model = new ArbitrageModelImpl(this);
        this.model.startBackgroundTask();
    }

    @Override
    public void onDestroy() {
        this.model.onDestroy();
        this.model = null;
    }

    @Override
    public void onViewStop() {
        this.model.cancelBackgroundTask();
        this.paused = false;
    }

    @Override
    public void onPauseResumeClick() {
        this.paused = !this.paused;
        if (this.paused) {
            model.cancelBackgroundTask();
        } else {
            model.startBackgroundTask();
            if (lastSettings != null) {
                model.updateSettings(lastSettings);
            }
        }
        view.updateResumePauseView(this.paused);
        view.updateProgressBar(0);
    }

    @Override
    public void onViewRestart() {
        this.model.startBackgroundTask();
        this.paused = false;
        this.view.updateResumePauseView(this.paused);
    }

    @Override
    public void onUpdateOrderBookGetterProgress(Integer progress) {
        this.view.updateProgressBar((progress));
    }

    @Override
    public void onSettingsChanged(SettingsContainer settings) {
        lastSettings = settings;
        model.updateSettings(settings);
    }


    @Override
    public void showToast(String msg) {
        view.showToast(msg);
    }


    @Override
    public void onWorkerResult(OutputDataSet dataSet) {
        view.updateData(dataSet);
    }

}
