package au.id.teda.broadband.usage.ui;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;

public class ChartActivity extends Activity {

	private GraphicalView mChart;
	 
    private XYSeries visitsSeries ;
    private XYMultipleSeriesDataset dataset;
 
    private XYSeriesRenderer visitsRenderer;
    private XYMultipleSeriesRenderer multiRenderer;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
 
        // Setting up chart
        setupChart();
 
        // Start plotting chart
        new ChartTask().execute();
 
    }
 
    private void setupChart(){
 
        // Creating an  XYSeries for Visits
        visitsSeries = new XYSeries("Unique Visitors");
 
        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset();
        // Adding Visits Series to the dataset
        dataset.addSeries(visitsSeries);
 
        // Creating XYSeriesRenderer to customize visitsSeries
        visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.WHITE);
        visitsRenderer.setPointStyle(PointStyle.CIRCLE);
        visitsRenderer.setFillPoints(true);
        visitsRenderer.setLineWidth(2);
        visitsRenderer.setDisplayChartValues(true);
 
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
 
        multiRenderer.setChartTitle("Visits Chart");
        multiRenderer.setXTitle("Seconds");
        multiRenderer.setYTitle("Count");
        multiRenderer.setZoomButtonsVisible(true);
 
        multiRenderer.setXAxisMin(0);
        multiRenderer.setXAxisMax(10);
 
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(10);
 
        multiRenderer.setBarSpacing(2);
 
        // Adding visitsRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(visitsRenderer);
 
        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
 
        mChart = (GraphicalView) ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
 
        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(mChart);
    }
 
    private class ChartTask extends AsyncTask<Void, String, Void>{
 
        // Generates dummy data in a non-ui thread
        @Override
        protected Void doInBackground(Void... params) {
            int i = 0;
            try{
                do{
                    String [] values = new String[2];
                    Random r = new Random();
                    int visits = r.nextInt(10);
 
                    values[0] = Integer.toString(i);
                    values[1] = Integer.toString(visits);
 
                    publishProgress(values);
                    Thread.sleep(1000);
                    i++;
                }while(i<=10);
                    }catch(Exception e){ }
                return null;
            }
 
            // Plotting generated data in the graph
            @Override
            protected void onProgressUpdate(String... values) {
                visitsSeries.add(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                mChart.repaint();
            }
        }
	
}
