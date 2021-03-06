package com.course_project.arbitrage_analyzer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.course_project.arbitrage_analyzer.R;
import com.course_project.arbitrage_analyzer.arbitrage.ArbitragePresenterImpl;
import com.course_project.arbitrage_analyzer.interfaces.ArbitragePresenter;
import com.course_project.arbitrage_analyzer.interfaces.ArbitrageView;
import com.course_project.arbitrage_analyzer.model.DealListData;
import com.course_project.arbitrage_analyzer.model.OutputDataSet;
import com.course_project.arbitrage_analyzer.model.SettingsContainer;
import com.course_project.arbitrage_analyzer.model.disbalance_minimization.minimizers.MinimizerType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ArbitrageView {

    private final String LOGTAG = "MainActivity";
    private FloatingActionButton fab;
    private ImageButton chartTypeBtn;
    private ProgressBar pb;
    private SettingsContainer settings;
    private OutputDataSet lastOutputDataSet = null;
    private TextView profitStr;
    private TextView amountStr;
    private TextView currPairStr;
    private TextView timeStr;
    private RecyclerView dealList;
    private View bigStartPB;

    private boolean chartTypeProfit = true;

    ArbitragePresenter presenter;

    //onClick for settings button.
    public void onClick1(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClick2(View v) {

        presenter.onPauseResumeClick();
    }

    public void onChartTypeClick(View v) {

        chartTypeProfit = !chartTypeProfit;
        updateChart();

        Drawable d = chartTypeBtn.getDrawable();
        if (chartTypeProfit) {
            d.setLevel(0);
        } else {
            d.setLevel(1);
        }
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


    @Override
    public void updateProgressBar(Integer progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pb.setProgress(progress, true);
        } else {
            pb.setProgress(progress);
        }
    }


    private void updateSettings() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        settings = new SettingsContainer();

        settings.setUpdateRateSeconds(Integer.parseInt(sp.getString("update_rate", "10")));
        settings.setCurrencyPare(sp.getString("currency_pares", "BTC/USD"));
        settings.setDepthLimit(Integer.parseInt(sp.getString("depth_limit", "50")));
        settings.setBitfinex(sp.getBoolean("bitfinex", true));
        settings.setCex(sp.getBoolean("cex", true));
        settings.setExmo(sp.getBoolean("exmo", true));
        settings.setGdax(sp.getBoolean("gdax", true));

        settings.setHistorySize((short)Double.parseDouble(sp.getString("history_size", "10")));
        settings.setNumberOfLaunches((int)Double.parseDouble(sp.getString("launch_number", "1")));
        settings.setMaxIterations((int) Double.parseDouble(sp.getString("max_iterations", "1000")));

        double riskConst = Double.parseDouble(sp.getString("risk_const", "0.5"));
        if (riskConst > 1) {
            riskConst = 1;
        }
        if (riskConst < 0) {
            riskConst = 0;
        }
        settings.setRiskConst(riskConst);

        MinimizerType minType;
        String minimizerTypeString = sp.getString("minimizer_type", "Simple");
        switch (minimizerTypeString) {
            case ("Bayes-Laplace"):
                minType = MinimizerType.BayesLaplace;
                break;

            case ("Expected regret"):
                minType = MinimizerType.ExpectedRegret;
                break;

            case ("Cyclic coordinate descent"):
                minType = MinimizerType.CyclicCoordinateDescent;
                break;

            case ("Pattern search"):
                minType = MinimizerType.PatternSearch;
                break;

            case ("Nelder-Mead"):
                minType = MinimizerType.NelderMead;
                break;

                default:
                    minType = MinimizerType.Simple;
        }
        settings.setMinimizerType(minType);

        this.presenter.onSettingsChanged(settings);
    }


    @Override
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this,
                msg,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }


    private void updateChart() {

        if (lastOutputDataSet == null) {
            return;
        }
        if (chartTypeProfit) {
            displayProfitChart();
        } else {
            displayBidAskChart();
        }
    }


    private void displayProfitChart() {
        //Display profit points on the diagram.
        LineChart chart = findViewById(R.id.diagram);
        //Points of the plot.
        List<Entry> chartEntries = new ArrayList<>();
        //Zero point
        chartEntries.add(new Entry(0f, 0f));
        //Fill the dealList of points.
        for (int i = 0; i < lastOutputDataSet.getAmountPoints().size(); ++i) {

            chartEntries.add(new Entry(lastOutputDataSet.getAmountPoints().get(i).floatValue()
                    , lastOutputDataSet.getProfitPoints().get(i).floatValue()));
        }
        //Make a DataSet with ordinary points.
        LineDataSet ds = new LineDataSet(chartEntries, "Profit/Money Diagram");
        ds.setColor(R.color.colorPrimaryDark);
        ds.setCircleColors(getResources().getColor(R.color.diagramCircleOrdinary));

        //Make a DataSet with optimal point.
        float optimalAmount = (float) lastOutputDataSet.getMinimizerResult().getOptimalV();
        float optimalProfit = lastOutputDataSet.getOptimalProfit().floatValue();
        float realAmount = (float) lastOutputDataSet.getEstimate().getUsedSecondCurrencyAmount();
        float realProfit = lastOutputDataSet.getRealProfit().floatValue();

        LimitLine optimalLL = new LimitLine(optimalAmount, "");
        optimalLL.setLineWidth(1f);
        optimalLL.setLineColor(getApplicationContext().getResources().getColor(R.color.diagramCircleOptimal));
        optimalLL.disableDashedLine();

        List<Entry> optimalChartEntries = new ArrayList<>();
        optimalChartEntries.add(new Entry(optimalAmount, optimalProfit));
        LineDataSet ds2 = new LineDataSet(optimalChartEntries, "");

        ds2.setColor(R.color.colorPrimaryDark);
        ds2.setCircleColors(getResources().getColor(R.color.diagramCircleOptimal));


        //Make a DataSet with real point
        LimitLine realLL = new LimitLine(realAmount, "");
        realLL.setLineWidth(1f);
        realLL.setLineColor(getApplicationContext().getResources().getColor(R.color.diagramCircleReal));
        realLL.disableDashedLine();

        List<Entry> realChartEntries = new ArrayList<>();
        realChartEntries.add(new Entry(realAmount, realProfit));
        LineDataSet ds3 = new LineDataSet(realChartEntries, "");

        ds3.setColor(R.color.colorPrimaryDark);
        ds3.setCircleColors(getResources().getColor(R.color.diagramCircleReal));


        LineDataSet[] lineDataSets = new LineDataSet[3];
        lineDataSets[0] = ds;
        lineDataSets[1] = ds2;
        lineDataSets[2] = ds3;
        LineData ld = new LineData(lineDataSets);

        chart.setData(ld);
        chart.getDescription().setText("Horizontal: amount; Vertical: profit");
        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);

        chart.getXAxis().removeAllLimitLines();
        chart.getXAxis().addLimitLine(optimalLL);
        chart.getXAxis().addLimitLine(realLL);
        chart.invalidate();
    }

    private void displayBidAskChart() {

        LineChart chart = findViewById(R.id.diagram);

        List<Entry> askChartEntries = new ArrayList<>();
        List<Entry> bidChartEntries = new ArrayList<>();
        //Fill the dealList of points.
        for (int i = 0; i < lastOutputDataSet.getAskAmountPoints().size(); ++i) {
            askChartEntries.add(new Entry(lastOutputDataSet.getAskPricePoints().get(i).floatValue()
                    , lastOutputDataSet.getAskAmountPoints().get(i).floatValue()));
        }
        for (int i = 0; i < lastOutputDataSet.getBidAmountPoints().size(); ++i) {
            bidChartEntries.add(new Entry(lastOutputDataSet.getBidPricePoints().get(i).floatValue()
                    , lastOutputDataSet.getBidAmountPoints().get(i).floatValue()));
        }
        /*
        //////////////////////////////////////////////////////////
        ArrayList<Float> xesAsk = new ArrayList<>();
        ArrayList<Float> yesAsk = new ArrayList<>();
        ArrayList<Float> xesBid = new ArrayList<>();
        ArrayList<Float> yesBid = new ArrayList<>();
        for (int i = 0; i < lastOutputDataSet.getAskAmountPoints().size(); ++i) {
            xesAsk.add(lastOutputDataSet.getAskPricePoints().get(i).floatValue());
            yesAsk.add(lastOutputDataSet.getAskAmountPoints().get(i).floatValue());
        }
        for (int i = 0; i < lastOutputDataSet.getBidAmountPoints().size(); ++i) {
            yesBid.add(lastOutputDataSet.getBidAmountPoints().get(i).floatValue());
            xesBid.add(lastOutputDataSet.getBidPricePoints().get(i).floatValue());
        }
        /*
        Log.e("XAsk", xesAsk.toString());
        Log.e("YAsk", yesAsk.toString());
        Log.e("XBid", xesBid.toString());
        Log.e("YBid", yesBid.toString());
        //////////////////////////////////////////////////
        */
        Collections.sort(bidChartEntries, new EntryXComparator());
        Collections.sort(askChartEntries, new EntryXComparator());

        //Make a DataSet with ordinary points.
        LineDataSet askDs = new LineDataSet(askChartEntries, "Profit/Money Diagram");
        askDs.setColor(R.color.colorPrimaryDark);
        askDs.setCircleColors(getResources().getColor(R.color.diagramCircleAsk));

        LineDataSet bidDs = new LineDataSet(bidChartEntries, "");
        bidDs.setColor(R.color.colorPrimaryDark);
        bidDs.setCircleColors(getResources().getColor(R.color.diagramCircleBid));

        LineDataSet[] lineDataSets = new LineDataSet[2];
        lineDataSets[0] = askDs;
        lineDataSets[1] = bidDs;
        LineData ld = new LineData(lineDataSets);

        chart.setData(ld);
        chart.getDescription().setText("Horizontal: price; Vertical: amount");
        chart.getLegend().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.invalidate();
    }


    @Override
    public void updateData(OutputDataSet dataSet) {

        lastOutputDataSet = dataSet;
        updateChart();

        float optimalFCAmount = (float) lastOutputDataSet.getMinimizerResult().getOptimalAmount();
        float optimalSCAmount = (float) lastOutputDataSet.getMinimizerResult().getOptimalV();
        float optimalProfit = lastOutputDataSet.getOptimalProfit().floatValue();

        //Display optimal profit.
        profitStr.setText(getString(R.string.profit_string,
                        String.valueOf(Math.round(optimalProfit * 100) / 100.0),
                        dataSet.getSecondCurrency()));
        //Display optimal amount.
        amountStr.setText(getString(R.string.amount_string,
                        String.valueOf(Math.round(optimalFCAmount * 100) / 100.0),
                        dataSet.getFirstCurrency(),
                        String.valueOf(Math.round(optimalSCAmount * 100) / 100.0),
                        dataSet.getSecondCurrency()));

        //Display current currency pair.
        currPairStr.setText(settings.getCurrencyPare());

        //Prepare data about deals for displaying.
        DealListData dldata = new DealListData(lastOutputDataSet);
        //Display it
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dealList.setLayoutManager(llm);
        dealList.setAdapter(new DealListAdapter(dldata));

        //Display time
        long currentTime = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        timeStr.setText(getString(R.string.date_time_string,
                        format.format(currentTime)));

        //Hide big round progress bar
        bigStartPB.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(LOGTAG, "ON_CREATE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart chart = findViewById(R.id.diagram);
        chart.setNoDataText("Please wait. Data receiving may take up to 10 seconds");
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        fab = findViewById(R.id.fab);
        chartTypeBtn = findViewById(R.id.chart_type_btn);
        pb = findViewById(R.id.progress_bar);
        profitStr = findViewById(R.id.profit_string);
        amountStr = findViewById(R.id.amount_string);
        currPairStr = findViewById(R.id.currency_pair);
        timeStr = findViewById(R.id.time_line);
        dealList = findViewById(R.id.deal_list);
        bigStartPB = findViewById(R.id.big_round_progress_bar);

        this.updateProgressBar(0);
        this.updateResumePauseView(false);

        presenter = new ArbitragePresenterImpl(this);
        this.updateSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOGTAG, "ON_RESUME");
        this.updateSettings();
    }

    @Override
    protected void onStop() {

        Log.e(LOGTAG, "ON_STOP");
        super.onStop();

        presenter.onViewStop();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.e(LOGTAG, "ON_RESTART");

        this.updateSettings();
        presenter.onViewRestart();
    }

    @Override
    protected void onDestroy() {

        Log.e(LOGTAG, "onDestroy");
        super.onDestroy();
        this.presenter.onDestroy();
        this.presenter = null;
    }
}
