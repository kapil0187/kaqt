package kaqt.supersonic.ui;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ImageLabel extends JLabel {
	
	private Icon imageIcon;
	
    public ImageLabel(String filepath) { 
    	super();
    	imageIcon = new ImageIcon(filepath);
    	this.setIcon(imageIcon);
    }
    
    @Override
    public void setIcon(Icon icon) {
    	imageIcon = icon;
    	super.setIcon(icon);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
		g.drawImage(((ImageIcon)(imageIcon)).getImage(), 0, 0,
				this.getWidth(), this.getHeight(), this);
    }

}
