package util;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter;

public class MyKeywordsHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
	public MyKeywordsHighlighter(Color color){
		super(color);
	}
}
