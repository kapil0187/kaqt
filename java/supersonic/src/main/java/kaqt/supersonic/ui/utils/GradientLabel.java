package kaqt.supersonic.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class GradientLabel extends JLabel {
	private Color start;
	private Color end;

	public GradientLabel(String text) {
		super(text);

		start = Color.LIGHT_GRAY;
		end = getBackground();
	}

	public GradientLabel(String text, Color start, Color end) {
		super(text);
		this.start = start;
		this.end = end;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, start, w, 0, end);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		super.paintComponent(g);
	}

}
