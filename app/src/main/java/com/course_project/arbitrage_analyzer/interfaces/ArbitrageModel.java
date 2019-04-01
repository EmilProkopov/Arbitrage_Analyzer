package com.course_project.arbitrage_analyzer.interfaces;

import com.course_project.arbitrage_analyzer.model.SettingsContainer;

public interface ArbitrageModel {

    void cancelBackgroundTask();

    void startBackgroundTask();

    void onDestroy();

    void updateSettings(SettingsContainer settings);
}
