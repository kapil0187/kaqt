package kaqt.supersonictrader.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ControlWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	
	// WINDOWS
	private MarketDataView mdView;
	private boolean mdViewInit = false;
	
	// ICONS
	private static String mdGridIconPath = "/kaqt/supersonictrader/resources/mdgrid.gif"; 
	private static String mdMatricIconPath = "/kaqt/supersonictrader/resources/mdmatrix.png";
	
	public ControlWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 150);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnSymbology = new JMenu("SYMBOLOGY");
		menuBar.add(mnSymbology);
		
		JMenuItem mntmDatabase = new JMenuItem("DATABASE");
		mnSymbology.add(mntmDatabase);
		
		JMenu mnMarket = new JMenu("MARKET");
		menuBar.add(mnMarket);
		
		JMenuItem mntmMdview = new JMenuItem("MDView");
		mnMarket.add(mntmMdview);
		
		JMenuItem mntmMdgrid = new JMenuItem("MDGrid");
		mnMarket.add(mntmMdgrid);
		
		JMenuItem mntmMdmatrix = new JMenuItem("MDMatrix");
		mnMarket.add(mntmMdmatrix);
		
		JMenu mnExecution = new JMenu("EXECUTION");
		menuBar.add(mnExecution);
		
		JMenuItem mntmWorkingOrders = new JMenuItem("WORKING ORDERS");
		mnExecution.add(mntmWorkingOrders);
		
		JMenuItem mntmCompletedOrders = new JMenuItem("COMPLETED ORDERS");
		mnExecution.add(mntmCompletedOrders);
		
		JMenuItem mntmFills = new JMenuItem("FILLS");
		mnExecution.add(mntmFills);
		
		JMenu mnStrategy = new JMenu("STRATEGY");
		menuBar.add(mnStrategy);
		
		JMenuItem mntmStrategyRepo = new JMenuItem("STRATEGY REPO");
		mnStrategy.add(mntmStrategyRepo);
		
		JMenuItem mntmNew = new JMenuItem("NEW");
		mnStrategy.add(mntmNew);
		
		JMenu mnSettings = new JMenu("SETTINGS");
		menuBar.add(mnSettings);
		
		JMenuItem mntmConnections = new JMenuItem("CONNECTIONS");
		mnSettings.add(mntmConnections);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][]", "[20px,fill]"));
		
		//###########################################
		// TODO: CREATE class for this type of button
		//###########################################
		Image img = Toolkit.getDefaultToolkit().getImage(mdGridIconPath);// mdgridIcon.getImage();
		Image scaledImg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;
		ImageIcon mdgridIcon = new ImageIcon(scaledImg);
		JButton mdgridButton = new JButton(mdgridIcon);
		mdgridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initializeMarketDataWindow();
			}
		});
		mdgridButton.setPreferredSize(new Dimension(40, 40));
		contentPane.add(mdgridButton, "cell 0 0");
		
		img = Toolkit.getDefaultToolkit().getImage(mdMatricIconPath);
		scaledImg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;
		ImageIcon mdMatrixIcon = new ImageIcon(scaledImg);
		JButton mdmatrixButton = new JButton(mdMatrixIcon);
		mdmatrixButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mdmatrixButton.setPreferredSize(new Dimension(40, 40));
		contentPane.add(mdmatrixButton, "cell 1 0");
		//###########################################
		// TODO: CREATE class for this type of button
		//###########################################
	}

	private void initializeMarketDataWindow() {
		if (mdViewInit == false) {
			mdView = new MarketDataView();
			mdViewInit = true;
		}
		mdView.setVisible(true);
	}
	
}
