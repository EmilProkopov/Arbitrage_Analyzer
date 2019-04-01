package com.course_project.arbitrage_analyzer.interfaces;

import com.course_project.arbitrage_analyzer.model.OutputDataSet;

public interface ArbitrageView {

    void updateResumePauseView(boolean paused);

    void updateProgressBar(Integer progress);

    void showToast(String msg);

    void updateData(OutputDataSet dataSet);
}
