package kaqt.foundation.uielements;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton
{
	public ImageButton(Image image, int width, int height)
	{
		ImageIcon icon = new ImageIcon(image.getScaledInstance( width, height,  java.awt.Image.SCALE_SMOOTH ) );
		this.setIcon(icon);
		this.setPreferredSize(new Dimension(width, height));
	}
}
