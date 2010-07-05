package viz.render;
/**
 * @author kevin
 * A renderer that renders a node as a rectangle
 */

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import prefuse.*;
import prefuse.render.*;
import prefuse.visual.VisualItem;

public class NodeRenderer extends AbstractShapeRenderer {
	private Rectangle2D m_bounds=new Rectangle2D.Double();
	public NodeRenderer(){
		this.m_manageBounds=false;
	}
	
	public Shape getRawShape(VisualItem item){
		m_bounds.setRect(item.getBounds());
		return m_bounds;
	}
}
