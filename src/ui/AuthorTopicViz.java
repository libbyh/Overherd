package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.Layout;
import prefuse.action.layout.RandomLayout;
import prefuse.action.layout.graph.BalloonTreeLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import registry.ComponentRegistry;

public class AuthorTopicViz extends Display {
	public static final String GRAPH = "graph";
    public static final String NODES = "graph.nodes";
    public static final String EDGES = "graph.edges";
    public static final String AGGR = "aggregates";
    public static final String EDGE_DECORATORS = "edgeDeco";
    public static final String NODE_DECORATORS = "nodeDeco";
    public static final String AGGR_DECORATORS = "aggrDeco";
    
    private HashMap<String,Integer> authorCountMap;
    private HashMap<String,Node> authorNodeMap;	
    private Graph graph;
    

    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema(); 
    static { 
    	DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false); 
    	DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128)); 
    	DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma",12));
    }
    
    public AuthorTopicViz(){
    	super(new Visualization());
    	
    	initData();
    	
    	
    	//render
    	Renderer nodeR=new ShapeRenderer(20);
    	
    	DefaultRendererFactory drf=new DefaultRendererFactory();
    	drf.setDefaultRenderer(nodeR);
    	drf.add(new InGroupPredicate(EDGE_DECORATORS), new LabelRenderer(VisualItem.LABEL));
        drf.add(new InGroupPredicate(NODE_DECORATORS), new LabelRenderer(VisualItem.LABEL));
    	m_vis.setRendererFactory(drf);
    	
    	DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(100));
        m_vis.addDecorators(EDGE_DECORATORS, EDGES, DECORATOR_SCHEMA);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        m_vis.addDecorators(NODE_DECORATORS, NODES, DECORATOR_SCHEMA);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(255, 128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", Font.BOLD, 48));
    	
    	//color actions
    	ColorAction nStroke=new ColorAction(NODES, VisualItem.STROKECOLOR);
    	nStroke.setDefaultColor(ColorLib.gray(100));
    	nStroke.add("_hover", ColorLib.gray(50));
    	
    	ColorAction nEdges=new ColorAction(EDGES,VisualItem.STROKECOLOR);
    	nEdges.setDefaultColor(ColorLib.gray(100));
    	
    	//action lists
    	ActionList colors=new ActionList();
    	colors.add(nStroke);
    	colors.add(new NodeColorAction(NODES));
    	colors.add(nEdges);
    	
    	ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(NODES));
        animatePaint.add(new RepaintAction());
        m_vis.putAction("animatePaint", animatePaint);
        
//      full paint
        ActionList fullPaint = new ActionList();
        fullPaint.add(new NodeColorAction(NODES));
        m_vis.putAction("fullPaint", fullPaint);
       
        ForceDirectedLayout forceLayout=new ForceDirectedLayout(GRAPH, true);
        RadialTreeLayout radialLayout=new RadialTreeLayout(GRAPH,300);
        
        NodeLinkTreeLayout nodeLinkLayout=new NodeLinkTreeLayout(GRAPH);
        BalloonTreeLayout balloonTreeLayout=new BalloonTreeLayout(GRAPH);
        RandomLayout randomLayout=new RandomLayout(GRAPH);
        
        ActionList initialLayout=new ActionList(1000);	
        initialLayout.add(colors);
        initialLayout.add(radialLayout);
        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(GRAPH);
        initialLayout.add(subLayout);
        
        LabelLayout2 edgeDecoLayout=new LabelLayout2(EDGE_DECORATORS);
        LabelLayout2 nodeDecoLayout=new LabelLayout2(NODE_DECORATORS);
        initialLayout.add(edgeDecoLayout);
        initialLayout.add(nodeDecoLayout);
        
        AuthorSizeAction authorSizeAction=new AuthorSizeAction();
        initialLayout.add(authorSizeAction);
        initialLayout.add(new RepaintAction());
        m_vis.putAction("initialLayout", initialLayout);
        
        setSize(600,500);
        pan(300, 250);
        setHighQuality(true);
   //     addControlListener(new AggregateDragControl2());
   //     addControlListener(popup);
        addControlListener(new ZoomControl());
        addControlListener(new DragControl());
        addControlListener(new PanControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new NeighborHighlightControl());
        
        
        m_vis.run("initialLayout");
    }
    
    public void initData(){
    	graph=new Graph();
    	findUniqueAuthors();	
    	
    	//set up data columns for nodes
    	graph.addColumn("numPosts", int.class);
    	graph.addColumn(VisualItem.LABEL, String.class);
    	
    	//for each student, create a node
    	Iterator<String> iter=authorCountMap.keySet().iterator();
    	while(iter.hasNext()){
    		Node node=graph.addNode();
    		String name=iter.next();
    		int count=(Integer)authorCountMap.get(name).intValue();
    		node.setString(VisualItem.LABEL, name);
    		node.setInt("numPosts", count);      	
    		
    		//add to authorMap    		
    		authorNodeMap.put(name, node);
    		
    	}
    	
    	//map parent
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	Tree tree=(Tree)treeMap.getVisualization().getSourceData("tree");
    	Iterator NodeIter=tree.nodes();
    	while (NodeIter.hasNext()){
    		Node originalNode=(Node)NodeIter.next();
    		String authorName=originalNode.getString("message_author");
    		Node nodeOne=authorNodeMap.get(authorName);
    		Node originalParent=originalNode.getParent();
    		if(originalParent!=null){
    			String parentName=originalParent.getString("message_author");
    			Node nodeTwo=authorNodeMap.get(parentName);
    			if(nodeTwo !=null){
    				System.out.println("Edge created");
    				Edge edge=graph.addEdge(nodeTwo, nodeOne);
    				edge.setString(VisualItem.LABEL, "");
    			}
    		}
    	}
    	
    
    
    	
    	VisualGraph g=m_vis.addGraph(GRAPH, graph);
    	m_vis.setInteractive(EDGES, null, false);
    	m_vis.setValue(NODES, null, VisualItem.SHAPE, new Integer(Constants.SHAPE_ELLIPSE));
    	
    }
    
    /**
     * Find unique student names and count how many posts they created
     */
    public void findUniqueAuthors(){
    	authorCountMap=new HashMap<String,Integer>();
    	authorNodeMap=new HashMap<String,Node>();
    	
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	Tree tree=(Tree)treeMap.getVisualization().getSourceData("tree");
    	Iterator iter=tree.nodes();
    	while(iter.hasNext()){
    		Node node=(Node)iter.next();
   // 		node.get
    		String authorName=node.getString("message_author");
    	//	System.out.println("author found:" + authorName);
    		if(authorCountMap.containsKey(authorName)){//update author count
    			int count=(Integer)authorCountMap.get(authorName).intValue();
    			count+=1;
    			authorCountMap.put(authorName, Integer.valueOf(count));
    		}else{ //first encounter
    			authorCountMap.put(authorName, Integer.valueOf(1));
    		}
    		
    	
    	}
    }
    
    public static class NodeColorAction extends ColorAction {
        
        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR);
        }
        
        public int getColor(VisualItem item) {
            if ( m_vis.isInGroup(item, Visualization.SEARCH_ITEMS) )
                return ColorLib.rgb(255,190,190);
            else if ( m_vis.isInGroup(item, Visualization.FOCUS_ITEMS) )
                return ColorLib.rgb(198,229,229);
            else if ( item.isHover() ){
            	m_vis.run("updateList");
            	return ColorLib.rgb(255,200,125);
            }
            else
                return ColorLib.rgb(255,255,255);
        }
        
    }
    
    public class AuthorSizeAction extends SizeAction {
        public double getSize(VisualItem item) {
        	double y=1.0;
        	if(item instanceof Node){
        	//	System.out.println("num posts: "+item.get("numPosts"));
        		int num=Integer.parseInt(item.get("numPosts")+"");
        		y = num * 0.2;
        		
        	}else if(item instanceof Edge){ 
        		y= 0.5;
        	}else {
        		y=1.0;
        	}
        
        	return y;
        }
    }
    
    /**
     * Set label positions. Labels are assumed to be DecoratorItem instances,
     * decorating their respective nodes. The layout simply gets the bounds
     * of the decorated node and assigns the label coordinates to the center
     * of those bounds.
     */
    class LabelLayout2 extends Layout {
        public LabelLayout2(String group) {
            super(group);
        }
        public void run(double frac) {
            Iterator iter = m_vis.items(m_group);
            while ( iter.hasNext() ) {
                DecoratorItem item = (DecoratorItem)iter.next();
                VisualItem node = item.getDecoratedItem();
                Rectangle2D bounds = node.getBounds();
                setX(item, null, bounds.getCenterX());
                setY(item, null, bounds.getCenterY());
            }
        //    m_vis.repaint();
        }
    } // end of inner class LabelLayout
    
    public static void main(String ... args){
    	JFrame frame=new JFrame();
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	AuthorTopicViz av=new AuthorTopicViz();
    	frame.getContentPane().setLayout(new BorderLayout());
    	frame.getContentPane().add(av, BorderLayout.CENTER);
    	
    	frame.setSize(900, 800);
    	frame.setVisible(true);
    }
}
