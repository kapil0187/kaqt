package kaqt.apps.supersonic;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.UIManager;

import kaqt.ui.supersonic.ControlWindow;

public class SuperSonic
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Properties properties = new Properties();
					InputStream input = null;
					
					try
					{
						input = new FileInputStream("config.properties");
					}
					catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					}

					if (input != null)
					{
						try
						{
							properties.load(input);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}

					
					UIManager
							.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
					ControlWindow frame = new ControlWindow(properties);
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
