package viz.chart;

/**
 * 
 * @author kevin
 * Chart for the data
 */

import ui.*;

import java.awt.Color;
import java.awt.Dimension;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.swing.JPanel;

import prefuse.visual.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.time.*;
import org.jfree.data.xy.*;
import org.jfree.ui.*;

public class TimeChart extends ApplicationFrame {
	private TreeMap associatedTreeMap=null;
	private int step;
	private HashMap<String,Integer> dateHash=new HashMap<String,Integer>();
	private XYDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	
	public TimeChart(String title){
		super(title);
		dataset=createDataSet();
		chart=createChart(dataset);
		chartPanel=new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(700,50));
		
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}
	
	public TimeChart(String title, TreeMap treeMap){
		super(title);
		this.associatedTreeMap=treeMap;
		dataset=createDataSet();
		chart=createChart(dataset);
		chartPanel=new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(700,50));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}
	
	/**
	 * 
	 */
	public static JFreeChart createChart(XYDataset dataset){
		JFreeChart chart=ChartFactory.createTimeSeriesChart(
				"Number of posts",
				"Date",
				"Number",
				dataset,
				true,
				true,
				false);
		
		chart.setBackgroundPaint(Color.WHITE);
		
		XYPlot plot=(XYPlot)chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0,5.0,5.0,5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		XYItemRenderer r=plot.getRenderer();
		if(r instanceof XYLineAndShapeRenderer){
			XYLineAndShapeRenderer renderer=(XYLineAndShapeRenderer)r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			
		}
		
		DateAxis axis=(DateAxis)plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("DD-MM-yyyy"));
		
		return chart;
	}
	
	/**
	 * Build a dataset to be used to create the chart
	 * If associatedTreeMap is not set, create an empty dataset.
	 * If it is set, create a dataset based on the data in the tree  
	 */
	public XYDataset createDataSet(){
		TimeSeriesCollection dataset=new TimeSeriesCollection();
		if(associatedTreeMap==null){
			return dataset;
		}else {
			TimeSeries ts=new TimeSeries("Number of posts", Day.class);
			Iterator iter=associatedTreeMap.getVisualization().items("tree.nodes");
			DateFormat df=DateFormat.getDateInstance(DateFormat.MEDIUM);
		//	TimeZone myZone=TimeZone.getTimeZone("GMT-4");
			while(iter.hasNext()){
				VisualItem item=(VisualItem)iter.next();
				long time=item.getLong("epoch_seconds");
				time=time*1000;
				if(time>0){
				//	System.out.println("time:"+time);
				//	Date date=new Date(time);
					Calendar c=Calendar.getInstance();
					c.setTimeInMillis(time);
				//	System.out.println("Cal: "+c.toString());
					String key=c.get(Calendar.DAY_OF_MONTH) + " " + 
					(c.get(Calendar.MONTH)) + " " + c.get(Calendar.YEAR);
					System.out.println("converted key: "+ time + ", "+key);
				//	String ds=df.format(date);
				//	System.out.println("date:"+ds);
					if(dateHash.containsKey(key)){
						dateHash.put(key, dateHash.get(key)+1);
					}else{
						dateHash.put(key, 1);
					}
				}
			}
			
			for (String key : dateHash.keySet()){
				int num=dateHash.get(key);
				System.out.println(key + " : " + dateHash.get(key));
				Calendar c=Calendar.getInstance();
				String[] data=key.split("\\s");
				c.set(Integer.parseInt(data[2]), (Integer.parseInt(data[1])),
						Integer.parseInt(data[0]));
			//	System.out.println(c);
			//	try{
			//		c=df.parse(key);
			//	}catch(ParseException pe){
			//		pe.printStackTrace();
			//	}
				System.out.println("adding " + c.get(Calendar.DATE) + " "+
						(c.get(Calendar.MONTH)+1) +" " + c.get(Calendar.YEAR));
				ts.add(new Day(c.get(Calendar.DATE), (c.get(Calendar.MONTH)+1), c.get(Calendar.YEAR)),
						num);
			//
				
			}
			dataset.addSeries(ts);
			dataset.setDomainIsPointsInTime(true);
			return dataset;
		}
	}
	
	public JPanel getChartPanel(){
		ChartPanel panel=new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(1000,100));
		panel.setMinimumSize(new Dimension(600,90));
		panel.setMaximumSize(new Dimension(1000,100));
		return panel;
	}
	
	public JFreeChart getChart(){
		return chart;
	}
	
}
