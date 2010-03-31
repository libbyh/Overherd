

package ui;

import viz.action.*;
import viz.control.*;
import viz.layout.*;
import viz.render.*;
import prefuse.*;
import prefuse.action.*;
import prefuse.action.animate.*;
import prefuse.action.assignment.*;
import prefuse.action.filter.*;
import prefuse.action.layout.*;
import prefuse.action.layout.graph.*;
import prefuse.controls.*;
import prefuse.data.*;
import prefuse.data.expression.*;
import prefuse.data.expression.parser.*;
import prefuse.data.query.*;
import prefuse.data.tuple.*;
import prefuse.render.*;
import prefuse.util.*;
import prefuse.util.ui.*;
import prefuse.visual.*;
import prefuse.visual.expression.*;
import prefuse.visual.sort.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;



/**
 * The main TreeMap display class for the forum data, based on the TreeMap demo.
 * 
 * @author <a href="http://kevinnam.com">kevin nam</a> 
 * 
 */

public class TreeMap extends Display {
	//initial input data to display.  To do: Add a function to dynamically change the input later
//	private String inputFile="E:/Users/kevin/My Documents/Aptana RadRails Workspace/Sakai/forumTree.xml";
	
	private static final String tree="tree";
	private static final String nodes="tree.nodes";
	private static final String edges="tree.edges";
	private static final String labels="name";
//	private static final String label="name";	//what will be displayed when mouseovered
	private SearchQueryBinding searchQuery;
	private int width=800;
	private int height=700;
	private JRangeSlider slider;
	private Display thisInstance;
	private MyOrPredicate depthFilter=null;
	public MainUI associatedMainUI;
	
	private int maxDepth=0;
	
	private static final Schema LABEL_SCHEMA=PrefuseLib.getVisualItemSchema();
	static{
		LABEL_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
		LABEL_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(200));
		LABEL_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 12));
	}
	
	
	public TreeMap(Tree t, String label){
		super(new Visualization());
		setup(t, label);
	}
	
	public TreeMap(Tree t,String label, int width, int height){
		super(new Visualization());
		this.width=width;
		this.height=height;
		setup(t, label);
	}
	
	public void setup(Tree t, String label){
		//m_vis the the initalized Visualization
		VisualTree vt=m_vis.addTree(tree, t);
		thisInstance=this;
		m_vis.setVisible(edges, null, false);
		
		//only leaf nodes are interactive
		m_vis.setInteractive(nodes, (Predicate)ExpressionParser.parse("childcount()>0"), false);
		m_vis.setInteractive(labels, null, true);
		m_vis.addDecorators(labels,nodes,(Predicate)ExpressionParser.parse("treedepth()==2"), LABEL_SCHEMA);
		
		//set up default render factory for nodes and edges
		DefaultRendererFactory rf=new DefaultRendererFactory();
		//use a customized NodeRenderer to render a node as a rectangle
		rf.add(new InGroupPredicate(nodes), new NodeRenderer());
		rf.add(new InGroupPredicate(labels), new TopicRenderer(label));
		rf.add(new InGroupPredicate(edges), new EdgeRenderer());
		m_vis.setRendererFactory(rf);
		
		//set colors
		ActionList colorActions=new ActionList();
		final ColorAction borderColor=new BorderColorAction(nodes);
		colorActions.add(borderColor);
		final FillColorAction fillcolor=new FillColorAction(nodes);
		colorActions.add(fillcolor);
		
		m_vis.putAction("colors", colorActions);
		
		//animate
		ActionList animatePaint=new ActionList(400);
		animatePaint.add(new ColorAnimator(nodes));
		animatePaint.add(new RepaintAction());
		m_vis.putAction("animatePaint", animatePaint);
		
		//layout
		ActionList layoutActions=new ActionList();
		layoutActions.add(new SquarifiedTreeMapLayout(tree));
		layoutActions.add(new LabelLayout(labels));
		layoutActions.add(colorActions);
		layoutActions.add(new RepaintAction());
		m_vis.putAction("layout", layoutActions);
		
		setSize(width,height);
		setItemSorter(new TreeDepthItemSorter());
		addControlListener(new ControlAdapter(){
			public void itemEntered(VisualItem item, MouseEvent e){
				item.setStrokeColor(borderColor.getColor(item));
				item.setHighlighted(true);
				item.setFillColor(fillcolor.getColor(item));
				item.getVisualization().repaint();
				NodeItem nitem=(NodeItem)item;
				Iterator iter=nitem.edges();
			//	while(iter.hasNext()){
			//		EdgeItem eitem=(EdgeItem)iter.next();
					//System.out.println(eitem);
			//		NodeItem sibling=eitem.getAdjacentItem(nitem);
			//		System.out.println(sibling);
			//		sibling.setHighlighted(true);
			//		sibling.getVisualization().repaint();
			//	}
			}
			
			public void itemExited(VisualItem item, MouseEvent e){
				item.setStrokeColor(item.getEndStrokeColor());
				item.setHighlighted(false);
				item.setFillColor(fillcolor.getColor(item));
				item.getVisualization().repaint();
			}
		});
		
		this.addControlListener(new NeighborHighlightControl());
		this.addControlListener(new MyWheelNaviControl(this.associatedMainUI));
	//	this.addControlListener(new WheelZoomControl());
		
		
		searchQuery=new SearchQueryBinding(vt.getNodeTable(), label);
		m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, searchQuery.getSearchSet());
		
		RangeQueryBinding rQ=new RangeQueryBinding(vt.getNodeTable(), "epoch_seconds");

		AndPredicate filter=new AndPredicate(rQ.getPredicate());
		System.out.println("max num:"+rQ.getNumberModel().getMaximum());
		System.out.println("min num:"+rQ.getNumberModel().getMinimum());
		
		//set max depth and min and max epoch
		Iterator iter =m_vis.items(nodes);
		
		long max=0;
		long min=Long.MAX_VALUE;
		VisualItem item=null;
		while(iter.hasNext()){
			item=(VisualItem)iter.next();
			long num=item.getLong("epoch_seconds");
		//	System.out.println("epoch: " + num);
			if(num>max){
				max=num;
			}
			if(num<min && num!=0){
				min=num;
			}
			int tempDepth=((NodeItem)item).getDepth();
			if( tempDepth > maxDepth){
				maxDepth=tempDepth;
			}
		}
		System.out.println("min: " + min);
		System.out.println("max: " + max);
		min-=1000;
		max+=1000;
		rQ.getNumberModel().setValueRange(min, max, min, max);
	//	filter.add(rQ.getPredicate());
		
		ActionList update=new ActionList();
		
		depthFilter=new MyOrPredicate();
		
		AndPredicate rangeDepthFilter=new AndPredicate();
		rangeDepthFilter.add(depthFilter);
		rangeDepthFilter.add(rQ.getPredicate());
		
		update.add(new VisibilityFilter(nodes, rangeDepthFilter));//rQ.getPredicate()));
		
		//depth predicates for selecting nodes at certain depth
		//initially all depths selected, so need to be added to the filter
		maxDepth+=1;
		for(int i=0; i<maxDepth; i++){
			addDepthPredicate(i);
		}
		
	//	System.out.println("predicate num: "+depthPredicates.size());
		
		m_vis.putAction("update", update);
		
		UpdateListener lstnr=new UpdateListener(){
			public void update(Object src){
				m_vis.run("update");
				m_vis.run("layout");
			
			}
		};
		depthFilter.addExpressionListener(lstnr);
		rangeDepthFilter.addExpressionListener(lstnr);
		searchQuery.getPredicate().addExpressionListener(new UpdateListener(){
			public void update(Object src){
				m_vis.cancel("animatePaint");
				m_vis.run("colors");
				m_vis.run("animatePaint");
			}
		});
		
		slider=rQ.createHorizontalRangeSlider();
		slider.setThumbColor(ColorLib.getColor(100, 100, 34));
		slider.setMinExtent(0);
		slider.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				thisInstance.setHighQuality(false);
			}
			
			public void mouseReleased(MouseEvent e){
				thisInstance.setHighQuality(true);
				thisInstance.repaint();
			}
		});
		
		m_vis.run("update");
		m_vis.run("layout");
	}
	
	
	public SearchQueryBinding getSearchQuery(){
		return searchQuery;
	}
	
	public JRangeSlider getSlider(){
		return slider;
	}
	
	public void temp(){
		
	}
	
	/**
	 * Returns max depth of the nodes
	 */
	public int getMaxDepth(){
		return maxDepth;
	}
	
	/**
	 * Returns depth predicates
	 */
	public OrPredicate getDepthPredicates(){
		return depthFilter;
	}
	
	/**
	 * 
	 * @param depth The depth for which to add a predicate
	 */
	public void addDepthPredicate(int depth){
		Predicate p=(Predicate)ExpressionParser.parse("treedepth()="+depth);	
		depthFilter.add(p);
	}
	
	/**
	 * 
	 * @param depth The depth for which to remove the predicate 
	 * @return whether the removal was successful.
	 */
	public boolean removeDepthPredicate(int depth){
		Predicate p=(Predicate)ExpressionParser.parse("treedepth()="+depth);
		ArrayList list=depthFilter.getPredicateList();
		
		int size=list.size();
	//	System.out.println("predicate list size: "+size);
		for (int i=0; i<size; i++){
			Predicate o=(Predicate)list.get(i);
		//	System.out.println(o.toString());
			if(o.toString().equals(p.toString())){
		//		System.out.println("found equals!");
				return list.remove(o);
			}
		}
		return false;
	}
}
