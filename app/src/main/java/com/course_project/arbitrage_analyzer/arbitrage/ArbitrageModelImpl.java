package com.course_project.arbitrage_analyzer.arbitrage;

import com.course_project.arbitrage_analyzer.asynctasks.SoloAsyncTask;
import com.course_project.arbitrage_analyzer.interfaces.ArbitrageModel;
import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;

public class ArbitrageModelImpl implements ArbitrageModel {

    SoloAsyncTask worker = null;
    ArbitragePresenter presenter;

    public ArbitrageModelImpl(ArbitragePresenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void cancelBackgroundTask() {
        if (worker != null) {
            worker.cancel(true);
        }
    }

    @Override
    public void startBackgroundTask() {
        this.cancelBackgroundTask();
        worker = new SoloAsyncTask((OrderBookGetter.OrderBookGetterProgressListener) presenter, presenter);
        worker.execute();
    }

    @Override
    public void onDestroy() {
        worker.cancel(true);
        worker = null;
    }

    @Override
    public void updateSettings(SettingsContainer settings) {
        worker.updateSettings(settings);
    }
}
