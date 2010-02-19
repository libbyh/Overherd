

package ui;

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
 * @version 0.1
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
		m_vis.addDecorators(labels,nodes,(Predicate)ExpressionParser.parse("treedepth()==1"), LABEL_SCHEMA);
		
		//set up default render factory for nodes and edges
		DefaultRendererFactory rf=new DefaultRendererFactory();
		//use a customized NodeRenderer to render a node as a rectangle
		rf.add(new InGroupPredicate(nodes), new NodeRenderer());
	//	rf.add(new InGroupPredicate(labels), new LabelRenderer(label));
		rf.add(new InGroupPredicate(labels), new TopicRenderer(label));
		m_vis.setRendererFactory(rf);
		
		//set colors
		ActionList colorActions=new ActionList();
		final ColorAction borderColor=new BorderColorAction(nodes);
		colorActions.add(borderColor);
		colorActions.add(new FillColorAction(nodes));
		
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
				item.getVisualization().repaint();
			}
			
			public void itemExited(VisualItem item, MouseEvent e){
				item.setStrokeColor(item.getEndStrokeColor());
				item.getVisualization().repaint();
			}
		});
		
		searchQuery=new SearchQueryBinding(vt.getNodeTable(), label);
		m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, searchQuery.getSearchSet());
		
		RangeQueryBinding rQ=new RangeQueryBinding(vt.getNodeTable(), "epoch_seconds");

		AndPredicate filter=new AndPredicate(rQ.getPredicate());
		
		rQ.getNumberModel().setValueRange(0, 65000000, 0, 65000000);
		
		ActionList update=new ActionList();
		update.add(new VisibilityFilter(nodes, filter));
		
		m_vis.putAction("update", update);
		
		UpdateListener lstnr=new UpdateListener(){
			public void update(Object src){
				m_vis.run("update");
			}
		};
		filter.addExpressionListener(lstnr);
		
		searchQuery.getPredicate().addExpressionListener(new UpdateListener(){
			public void update(Object src){
				m_vis.cancel("animatePaint");
				m_vis.run("colors");
				m_vis.run("animatePaint");
			}
		});
		
		slider=rQ.createHorizontalRangeSlider();
		slider.setThumbColor(null);
		slider.setMinExtent(150000);
		slider.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				thisInstance.setHighQuality(false);
			}
			
			public void mouseReleased(MouseEvent e){
				thisInstance.setHighQuality(true);
				thisInstance.repaint();
			}
		});
		
		m_vis.run("layout");
	}
	
	
	/**
	 * A renderer that renders a node as a rectangle
	 * @author kevin
	 *
	 */
	public static class NodeRenderer extends AbstractShapeRenderer{
		private Rectangle2D m_bounds=new Rectangle2D.Double();
		public NodeRenderer(){
			this.m_manageBounds=false;
		}
		
		public Shape getRawShape(VisualItem item){
			m_bounds.setRect(item.getBounds());
			return m_bounds;
		}
	}
	
	/**
	 * A label decorator layout
	 */
	
	public static class LabelLayout extends Layout {
		
		public LabelLayout(String group){
			super(group);
		}
		public void run(double num){
			Iterator iter=m_vis.items(m_group);
			
			while(iter.hasNext()){
				//for each label
				DecoratorItem label=(DecoratorItem)iter.next();
			/*	StringBuffer tempBuff=new StringBuffer();
				tempBuff.append(label.getString("name"));
				int spaceCount=0;
				int buffLen=tempBuff.length();
				for(int i=0; i<buffLen; i++){
					if(tempBuff.charAt(i)==' '){
						spaceCount++;
					}
					if (spaceCount==8){
						tempBuff.insert(i, '\n');
						break;
					}
				}
				*/
			//	System.out.println("Label:" + label.getString("name"));
				//set its location to the center of the node it belongs to
				VisualItem n=label.getDecoratedItem();
				
			//	System.out.println("Width: "+n.getBounds().getWidth());
				this.setX(label, null, n.getBounds().getCenterX());
				this.setY(label, null, n.getBounds().getCenterY());
			}
		}
	}
	
	/**
	 * A customized fill color action.  
	 */
	public static class FillColorAction extends ColorAction {
		public FillColorAction(String group){
			super(group, VisualItem.FILLCOLOR);
		}
		private ColorMap cmap=new ColorMap(ColorLib.getInterpolatedPalette(10,
				ColorLib.rgb(85, 85, 85), ColorLib.rgb(0, 0, 0)), 0, 9);
		
		public int getColor(VisualItem item){
			if(item instanceof NodeItem){
				NodeItem ni=(NodeItem)item;
				if(ni.getChildCount()>0){
					return 0;
				}else{
					if(m_vis.isInGroup(ni, Visualization.SEARCH_ITEMS)){
						return ColorLib.rgb(191,99,130);
				//	}else if(m_vis.isInGroup(ni, Visualization.SELECTED_ITEMS)){
				//		return ColorLib.rgb(142, 88, 23);
					}else{
						return cmap.getColor(ni.getDepth());
					}
				}
			}else{
				return cmap.getColor(0);
			}
		}
	}
	
	/**
	 * A customized border color action
	 */
	
	public static class BorderColorAction extends ColorAction{
		public BorderColorAction(String group){
			super(group, VisualItem.STROKECOLOR);
		}
		
		public int getColor(VisualItem item){
			NodeItem ni=(NodeItem)item;
			if(ni.isHover()){
				return ColorLib.rgb(99, 130, 191);
			}
			int depth=ni.getDepth();
			if(depth<2){
				return ColorLib.gray(100);
			}else if(depth<4){
				return ColorLib.gray(75);
				
			}else {
				return ColorLib.gray(65);
			}
		}
	}
	
	public SearchQueryBinding getSearchQuery(){
		return searchQuery;
	}
	
	public JRangeSlider getSlider(){
		return slider;
	}
}
