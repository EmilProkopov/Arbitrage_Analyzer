package com.course_project.arbitrage_analyzer.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.course_project.arbitrage_analyzer.R;
import com.course_project.arbitrage_analyzer.arbitrage.ArbitragePresenterImpl;
import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.interfaces.ArbitrageView;
import com.course_project.arbitrage_analyzer.model.OrderBookGetter;
import com.github.mikephil.charting.charts.LineChart;

public class MainActivity extends AppCompatActivity implements ArbitrageView {

    FloatingActionButton fab;
    ProgressBar pb;

    ArbitragePresenter presenter;

    //onClick for settings button.
    public void onClick1(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClick2(View v) {

        presenter.onPauseResumeClick();

    }

    @Override
    public void updateResumePauseView(boolean paused) {
        Drawable d = fab.getDrawable();
        if (!paused) {
            d.setLevel(0);
        } else {
            d.setLevel(1);
        }
    }

    public void updateProgressBar(int progress) {
        pb.setProgress(progress);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "ON_CREATE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart chart = findViewById(R.id.diagram);
        chart.setNoDataText("Please wait. Data receiving may take up to 10 seconds");

        fab = findViewById(R.id.fab);
        pb = findViewById(R.id.progress_bar);

        presenter = new ArbitragePresenterImpl(this);

        this.updateProgressBar(0);
        this.updateResumePauseView(false);
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "ON_STOP");
        super.onStop();

        /*if (soloAsyncTask != null) {
            soloAsyncTask.cancel(true);
        }*/
        //paused = false;
    }

    @Override
    protected void onRestart() {
        //Cancel previous AsyncTask and start new.
        super.onRestart();
        Log.d("MainActivity", "ON_RESTART");

       // paused = false;

        /*if (soloAsyncTask != null) {
            soloAsyncTask.cancel(true);
        }

        startSoloAsyncTask();*/

        //updateFABandProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestroy();
        this.presenter = null;
    }

    @Override
    public void updateProgressBar(Integer progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pb.setProgress(progress, true);
        } else {
            pb.setProgress(progress);
        }
    }
}
