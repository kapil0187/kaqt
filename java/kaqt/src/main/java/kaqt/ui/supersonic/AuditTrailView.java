package kaqt.ui.supersonic;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import kaqt.foundation.uielements.JTableAppender;

import org.apache.log4j.Logger;

public class AuditTrailView extends JFrame
{
	final static Logger logger = Logger.getLogger(AuditTrailView.class);
	
	private JPanel contentPane;
	private JTable auditTrailTable;
	private DefaultTableModel auditTrailTableModel;
	private JTableAppender logAppender;

	public AuditTrailView()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane auditTrailSP = new JScrollPane();
		contentPane.add(auditTrailSP, "cell 0 0,grow");
		
		auditTrailTableModel = new DefaultTableModel();
		auditTrailTable = new JTable(auditTrailTableModel);
		auditTrailSP.setViewportView(auditTrailTable);
		
		logAppender = new JTableAppender(auditTrailTable);
		logger.addAppender(logAppender);
	}

}
