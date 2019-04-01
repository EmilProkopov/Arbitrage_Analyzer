package com.course_project.arbitrage_analyzer.interfaces;

import com.course_project.arbitrage_analyzer.model.OutputDataSet;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;

public interface ArbitragePresenter {

    void onPauseResumeClick();

    void onDestroy();

    void onViewStop();

    void onViewRestart();

    void onSettingsChanged(SettingsContainer settings);

    void showToast(String msg);

    void onWorkerResult(OutputDataSet dataSet);
}
