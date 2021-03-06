package pje;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * @author Arthur Assima & Nordine El Ammari
 * Pie Chart for the results
 */
public class PieChart extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * @param applicationTitle Title of the app
	 * @param chartTitle Title of the chart
	 * @param negative Number of negative
	 * @param neutral Number of neutral
	 * @param positive Number of positive
	 */
	public PieChart(String applicationTitle, String chartTitle, int negative, int neutral, int positive) {
        super(applicationTitle);
        // This will create the dataset 
        PieDataset dataset = createDataset(negative, neutral, positive);
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a sample dataset 
     */
    /**
	 * @param negative Number of negative
	 * @param neutral Number of neutral
	 * @param positive Number of positive
     * @return the pie dataset
     */
    private  PieDataset createDataset(int negative, int neutral, int positive) {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Négatif", negative);
        result.setValue("Neutre", neutral);
        result.setValue("Positif", positive);
        return result;
        
    }
    
    /**
     * Creates a chart
     */
    /**
     * @param dataset the dataset to create the chart
     * @param title the title of the chart
     * @return the chart
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,  				// chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
        
    }
    
   
   

}
