package kaqt.supersonic.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class GradientList extends JList {
	private Color start;
	private Color end;

	public GradientList(Color start, Color end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	public GradientList(DefaultListModel model, Color start, Color end) {
		super(model);
		this.start = start;
		this.end = end;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, start, 0, h, end);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		super.paintComponent(g);
		this.setOpaque(false);
	}


}
