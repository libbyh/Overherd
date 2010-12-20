package util;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter;

/**
 * Keywords highlighter used in the AuthorTopicVizUI 
 * @author <a href="http://kevinnam.com">kevin nam</a>
 *
 */
public class MyKeywordsHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
	public MyKeywordsHighlighter(Color color){
		super(color);
	}
}
