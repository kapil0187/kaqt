package kaqt.supersonic.ui.utils;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenu;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class GradientMenu extends JMenu {
	private static Color startColor = UiColors.BACKGROUND_COLOR_DARK;
	private static Color endColor = UiColors.BACKGROUND_COLOR_LIGHT;

	public GradientMenu() {
		super();
		this.setForeground(Color.WHITE);
		this.setOpaque(false);
	}
	
	public GradientMenu(String name) {
		super(name);
		this.setForeground(Color.WHITE);
		this.setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int h = getHeight();
		int w = getWidth();
		GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		
		super.paintComponent(g);
	}
	
}
