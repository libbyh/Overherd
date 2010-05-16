package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.Layout;
import prefuse.action.layout.RandomLayout;
import prefuse.action.layout.graph.BalloonTreeLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.Activity;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.DragControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.FocusControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;
import registry.ComponentRegistry;
import viz.control.MyAuthorTopicVizNodeControl;
import nlp.*;

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
  //  protected final GraphDistanceFilter dFilter;
    protected int MaxDepth=99;
    private VisualGraph g;
    private static Node currentRootNode;

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
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(50));
        m_vis.addDecorators(NODE_DECORATORS, NODES, DECORATOR_SCHEMA);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(255, 128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", Font.BOLD, 48));
    	
    	//color actions
    	ColorAction nStroke=new ColorAction(NODES, VisualItem.STROKECOLOR);
    	nStroke.setDefaultColor(ColorLib.gray(100));
    	nStroke.add("_hover", ColorLib.gray(50));
    	
    
    	ColorAction nEdges=new ColorAction(EDGES,VisualItem.STROKECOLOR);
    	nEdges.setDefaultColor(ColorLib.gray(180));
    	
    	//action lists
    //	dFilter=new GraphDistanceFilter(GRAPH,MaxDepth);
    	ActionList recolor=new ActionList();
    	recolor.add(nStroke);
    //	colors.add(nFill);
    	
    	NodeColorAction nodeColor=new NodeColorAction(NODES);
   // 	recolor.add(subLayout);
    	recolor.add(nodeColor);
    	recolor.add(nEdges);
    	
    	m_vis.putAction("recolor", recolor);
    	
    	ActionList animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator(NODES));
        animatePaint.add(new RepaintAction());
        m_vis.putAction("animatePaint", animatePaint);
        
        //repaint
        ActionList repaint=new ActionList();
        repaint.add(recolor);
        repaint.add(new RepaintAction());
      //  repaint.add(dFilter);
        m_vis.putAction("repaint", repaint);
        
        
   /*     ActionList filterRedraw=new ActionList();
        
        repaint.add(dFilter);
        repaint.add(recolor);
        m_vis.putAction("filterRedraw", filterRedraw);
     */   
//      full paint
        ActionList fullPaint = new ActionList();
        fullPaint.add(new NodeColorAction(NODES));
        m_vis.putAction("fullPaint", fullPaint);
       
        RadialTreeLayout treeLayout=new RadialTreeLayout(GRAPH);
        m_vis.putAction("treeLayout", treeLayout);
        
        CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(GRAPH);
        m_vis.putAction("subLayout", subLayout);
        
        LabelLayout2 edgeDecoLayout=new LabelLayout2(EDGE_DECORATORS);
        LabelLayout2 nodeDecoLayout=new LabelLayout2(NODE_DECORATORS);
        AuthorSizeAction authorSizeAction=new AuthorSizeAction();
      
        
        
        //filter
        ActionList filter=new ActionList();
        filter.add(new TreeRootAction(GRAPH));
        filter.add(authorSizeAction);
   //     filter.add(dFilter);
        filter.add(treeLayout);
        filter.add(subLayout);
        filter.add(edgeDecoLayout);
        filter.add(nodeDecoLayout);
        filter.add(nodeColor);
        filter.add(nEdges);
        filter.add(nStroke);
        filter.add(recolor);
    //    filter.add(new RepaintAction());
        m_vis.putAction("filter", filter);
        
        //animate
        ActionList animate=new ActionList(1250);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator(GRAPH));
        animate.add(new VisibilityAnimator(NODE_DECORATORS));
        animate.add(new PolarLocationAnimator(GRAPH, "linear"));
        animate.add(new PolarLocationAnimator(NODE_DECORATORS, "linear"));
        animate.add(new ColorAnimator(GRAPH));
    //    animate.add(new ColorAnimator(NODE_DECORATORS));
        animate.add(recolor);
        animate.add(new RepaintAction());
        m_vis.putAction("animate", animate);
        m_vis.alwaysRunAfter("filter", "animate");
        
        
        setSize(600,500);
        pan(300, 250);
        setHighQuality(true);
   //     addControlListener(new AggregateDragControl2());
   //     addControlListener(popup);
        setItemSorter(new TreeDepthItemSorter());
        addControlListener(new ZoomControl());
        addControlListener(new DragControl());
        addControlListener(new PanControl());
        addControlListener(new WheelZoomControl());
       
        addControlListener(new FocusControl(2,"filter"));
        addControlListener(new HoverActionControl("repaint"));
        addControlListener(new MyAuthorTopicVizNodeControl());
        addControlListener(new NeighborHighlightControl());
        
        
        this.setToolTipText("Double click a node to center it.\n Click a neighbor node to show the posts between the students.");
      
        JPanel helpPanel=new JPanel(){
        	protected void paintComponent(Graphics g){
        		g.setColor(getBackground());
        		g.fillRect(0, 0, getWidth(), getHeight());
        		super.paintComponent(g);
        	}
        };
        helpPanel.setOpaque(false);
        helpPanel.setBackground(new Color(40,40,40,60));
        helpPanel.add(new JLabel("Double click a node to center it."));
        helpPanel.add(new JLabel("And click a neighbor node to show conversation."));
        this.add(helpPanel);
        helpPanel.setBounds(3, 4, 300, 50);
        
        m_vis.addFocusGroup("linear", new DefaultTupleSet());
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS); 
        m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
            new TupleSetListener() {
                public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                    TupleSet linearInterp = m_vis.getGroup("linear");
                    if ( add.length < 1 ) return; linearInterp.clear();
                    for ( Node n = (Node)add[0]; n!=null; n=n.getParent() )
                        linearInterp.addTuple(n);
                }
            }
        );
        
     // position and fix the default focus node
        NodeItem focus = (NodeItem)g.getNode(0);
      //  PrefuseLib.setX(focus, null, 400);
      //  PrefuseLib.setY(focus, null, 250);
        focusGroup.setTuple(focus);
        m_vis.run("filter");
        
    //    setupTFIDF();
        
        setupContentAnalysis();
        
    }
    
    
    /**
     * Sets up author-author matrix with conversation contents.  
     * Also calculate TFIDF values for words
     * 
     */
    public void setupContentAnalysis(){
    	
    	//Check if findUniqueAuthors has been called
    	if(authorCountMap.isEmpty()){
    		this.findUniqueAuthors();
    	}
    	
    	//for each author, create a UserConversationMap object and put it in the map
    	if(ComponentRegistry.registeredUserMatrix==null){
    		ComponentRegistry.registeredUserMatrix=new HashMap<String,UserConversationMap>();
    	}
    	
    	Set<String> authors=authorCountMap.keySet();
    	for(String author:authors){
    		ComponentRegistry.registeredUserMatrix.put(author, new UserConversationMap(author));
    	}
    	    	
    	//add each node documents 
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	
    	if(ComponentRegistry.registeredTFIDFHandler==null){
    		ComponentRegistry.registeredTFIDFHandler=
    			new TFIDFHandler();
    	}
    	TFIDFHandler handler=ComponentRegistry.registeredTFIDFHandler;
    	
    	//for each edge, find two authors, update user matrix
    	Tree tree=treeMap.getTree();
    	int numEdges=tree.getEdgeCount();
    	
    	int countDone=0;
    	Iterator iter=tree.edges();
    	while(iter.hasNext()){
    		Edge edge=(Edge)iter.next();
    		VisualItem edgeItem=treeMap.getVisualization().getVisualItem("tree.edges", edge);
    		
    		Node sourceNode=edge.getSourceNode();
    		Node targetNode=edge.getTargetNode();
    		String sourceAuthor=sourceNode.getString("author");
    		String targetAuthor=targetNode.getString("author");

    		
    		String sourceMessage="";
    		String targetMessage="";
    		if(sourceNode.canGetString("message_body")){
    			sourceMessage=sourceNode.getString("message_body");
    		}
    		if(targetNode.canGetString("message_body")){
    			targetMessage=targetNode.getString("message_body");
    		}
    		
    		//if A wrote message Ma and B replied to it with Mb, map A<->B:Ma, A<->B:Mb, and save this information for each A and B
    		UserConversationMap sourceMap=ComponentRegistry.registeredUserMatrix.get(sourceAuthor);
    		UserConversationMap targetMap=ComponentRegistry.registeredUserMatrix.get(targetAuthor);
    		if(sourceMessage!=null && !sourceMessage.equals("")){
    			//if map already has the other author
    			if(sourceMap.getConversationMap().containsKey(targetAuthor)){
    				HashSet<String> mList=sourceMap.getConversationMap().get(targetAuthor);
    				mList.add(sourceMessage);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				sourceMap.getEntireConversationBuffer().append(sourceMessage);
    				sourceMap.getEntireConversationBuffer().append(" ");
    			}else{//create an entry for the other author
    				HashSet<String> mList=new HashSet<String>();
    				mList.add(sourceMessage);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				sourceMap.getEntireConversationBuffer().append(sourceMessage);
    				sourceMap.getEntireConversationBuffer().append(" ");
    			}
    			//do the same for target author
    			//if map already has the other author
    			if(targetMap.getConversationMap().containsKey(sourceAuthor)){
    				HashSet<String> mList=targetMap.getConversationMap().get(sourceAuthor);
    				mList.add(sourceMessage);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				targetMap.getEntireConversationBuffer().append(targetMessage);
    				targetMap.getEntireConversationBuffer().append(" ");
    			}else{//create an entry for the other author
    				HashSet<String> mList=new HashSet<String>();
    				mList.add(sourceMessage);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				targetMap.getEntireConversationBuffer().append(targetMessage);
    				targetMap.getEntireConversationBuffer().append(" ");
    			}
    		}
    		
    		if(targetMessage!=null && !targetMessage.equals("")){
    			//if map already has the other author
    			if(sourceMap.getConversationMap().containsKey(targetAuthor)){
    				HashSet<String> mList=sourceMap.getConversationMap().get(targetAuthor);
    				mList.add(targetMessage);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    				
    			}else{//create an entry for the other author
    				HashSet<String> mList=new HashSet<String>();
    				mList.add(targetMessage);
    				sourceMap.getConversationMap().put(targetAuthor, mList);
    			}
    			//do the same for target author
    			//if map already has the other author
    			if(targetMap.getConversationMap().containsKey(sourceAuthor)){
    				HashSet<String> mList=targetMap.getConversationMap().get(sourceAuthor);
    				mList.add(targetMessage);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    				
    			}else{//create an entry for the other author
    				HashSet<String> mList=new HashSet<String>();
    				mList.add(targetMessage);
    				targetMap.getConversationMap().put(sourceAuthor, mList);
    			}
    		}
    //		sourceMap.addConversationForUser(targetAuthor, conversation)
    		countDone+=1;
    	}
    	
    	
    	
    	/*Iterator iter=treeMap.getTree().nodes();
    	while(iter.hasNext()){
    		Node node=(Node)iter.next();
    		VisualItem nodeItem=treeMap.getVisualization().getVisualItem("tree.nodes", node);
    		
    		if(nodeItem.canGetString("message_body")){
    			System.out.println("node item:"+nodeItem);
    			String text=nodeItem.getString("message_body");
    			
    			System.out.println("body text:"+text);
    			if(text!=null){
    				handler.addDoc(text);
    			}
    		}
    	}
    	*/
    	
    	handler.calculateTFIDFForAllWords();
    }
    
    
    /**
     * Set up for TFIDF for finding keywords
     */
  /*  public void setupTFIDF(){
    	if(ComponentRegistry.registeredTFIDFHandler==null){
    		ComponentRegistry.registeredTFIDFHandler=
    			new TFIDFHandler();
    	}
    	
    	//add each node documents 
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	TFIDFHandler handler=ComponentRegistry.registeredTFIDFHandler;
    	
    	Iterator iter=treeMap.getTree().nodes();
    	while(iter.hasNext()){
    		Node node=(Node)iter.next();
    		VisualItem nodeItem=treeMap.getVisualization().getVisualItem("tree.nodes", node);
    		
    		if(nodeItem.canGetString("message_body")){
    			System.out.println("node item:"+nodeItem);
    			String text=nodeItem.getString("message_body");
    			
    			System.out.println("body text:"+text);
    			if(text!=null){
    				handler.addDoc(text);
    			}
    		}
    	}
    	
    	handler.calculateTFIDFForAllWords();
    	
    	
    	
    }
    */
    /**
     * Creates a matrix of conversation, so each student is mapped to a list of other students with conversatoin content
     */
    public void createConversationMatrix(){
    	
    }
    
    public JPanel setupKeywordView(){
    	JPanel viewPanel=new JPanel();
    	
    	
    	return viewPanel;
    }
    
    public void initData(){
    	graph=new Graph();
    	findUniqueAuthors();	
    	
    	//set up data columns for nodes
    	graph.addColumn("numPosts", int.class);
    	graph.addColumn(VisualItem.LABEL, String.class);
    	graph.addColumn("fill_color", int.class);
    	
    	//for each student, create a node
    	Iterator<String> iter=authorCountMap.keySet().iterator();
    	while(iter.hasNext()){
    		Node node=graph.addNode();
    		String name=iter.next();
    		int count=(Integer)authorCountMap.get(name).intValue();
    		node.setString(VisualItem.LABEL, name);
    		node.setInt("numPosts", count);      	
    		node.setInt("fill_color", ColorLib.rgba((int)(Math.random()*255),
    				(int)(Math.random()*255), (int)(Math.random()*255), 100));
    		//add to authorMap    		
    		authorNodeMap.put(name, node);
    		
    	}
    	
    	//map parent
    	TreeMap treeMap=ComponentRegistry.registeredTreeMap;
    	Tree tree=(Tree)treeMap.getVisualization().getSourceData("tree");
    	Iterator NodeIter=tree.nodes();
    	while (NodeIter.hasNext()){
    		Node originalNode=(Node)NodeIter.next();
    		String authorName=originalNode.getString("author");
    		Node nodeOne=authorNodeMap.get(authorName);
    		Node originalParent=originalNode.getParent();
    		if(originalParent!=null ){
    			String parentName=originalParent.getString("author");
    			Node nodeTwo=authorNodeMap.get(parentName);
    			if(nodeTwo !=null){
    				System.out.println("Edge created");
    				Edge edge=graph.addEdge(nodeTwo, nodeOne);
    				edge.setString(VisualItem.LABEL, "");
    			}
    		}
    	}
    	
    
    
    	
    	g=m_vis.addGraph(GRAPH, graph);
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
    		String authorName=node.getString("author");
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
    
    
    public Graph getAuthorGraph(){
    	return this.graph;
    }
    
    public Node getCurrentRootNode(){
    	return currentRootNode;
    }
    
    public static class NodeColorAction extends ColorAction {
        
        public NodeColorAction(String group) {
            super(group, VisualItem.FILLCOLOR);
        }
        
        public int getColor(VisualItem item) {
            if ( m_vis.isInGroup(item, Visualization.SEARCH_ITEMS) ){
                return ColorLib.rgb(255,190,190);
            }
            else if ( m_vis.isInGroup(item, Visualization.FOCUS_ITEMS) ){
                return ColorLib.rgb(198,229,229);
            } else if ( item.isHover() ){
            	return ColorLib.rgba(255,200,125,200);
            } else if(item.isHighlighted()){
            	return ColorLib.rgba(200, 5, 100,190);
            }
            else
            	return item.getInt("fill_color");
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
    
    
    /**
     * Switch the root of the tree by requesting a new spanning tree
     * at the desired root
     */
    public static class TreeRootAction extends GroupAction {
        public TreeRootAction(String graphGroup) {
            super(graphGroup);
        }
        public void run(double frac) {
            TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
            if ( focus==null || focus.getTupleCount() == 0 ) return;
            
            Graph g = (Graph)m_vis.getGroup(m_group);
            Node f = null;
            Iterator tuples = focus.tuples();
            while (tuples.hasNext() && !g.containsTuple(f=(Node)tuples.next()))
            {
                f = null;
            }
            if ( f == null ) return;
            currentRootNode=f;
            g.getSpanningTree(f);
        }
    }
}


