package kaqt.supersonic.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

public class GradientTextField extends JTextField {
	private Color start;
	private Color end;

	public GradientTextField(Color start, Color end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth();
		int h = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, start, w, 0, end);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		super.paintComponent(g);
	}
}
