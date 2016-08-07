package kaqt.supersonic.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class VertGradientLabel extends JLabel {
	private static Color startColor = UiColors.BACKGROUND_COLOR_DARK;
	private static Color endColor = UiColors.BACKGROUND_COLOR_LIGHT;

	public VertGradientLabel(String text) {
		super(text);
		setForeground(Color.WHITE);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth();
		int h = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		super.paintComponent(g);
	}
}
