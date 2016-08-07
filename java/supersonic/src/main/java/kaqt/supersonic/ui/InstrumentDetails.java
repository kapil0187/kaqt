package kaqt.supersonic.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import kaqt.supersonic.model.Instrument;
import kaqt.supersonic.ui.utils.VertGradientLabel;
import net.miginfocom.swing.MigLayout;

public class InstrumentDetails extends JFrame {

	private JPanel contentPane;
	private Instrument instrument;
	private JTextArea instrumentDetails;

	public InstrumentDetails(Instrument instrument) {
		setResizable(false);
		this.instrument = instrument;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 425, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow,fill]", "[30px,fill][grow]"));
		
		JLabel instrumentNameLabel = new VertGradientLabel(instrument.getDescription());
		instrumentNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instrumentNameLabel.setForeground(new Color(255, 215, 0));
		instrumentNameLabel.setFont(new Font("Cambria Math", Font.BOLD, 12));
		contentPane.add(instrumentNameLabel, "cell 0 0");
		
		instrumentDetails = new JTextArea();
		instrumentDetails.setForeground(new Color(255, 215, 0));
		instrumentDetails.setFont(new Font("Cambria Math", Font.PLAIN, 11));
		instrumentDetails.setEditable(false);
		contentPane.add(instrumentDetails, "cell 0 1,grow");
		
		this.addInstrumentDetails();
	}

	
	private void addInstrumentDetails() {
		instrumentDetails.append("ID: " + instrument.get_id() + "\n");
		instrumentDetails.append("INSTRUMENT: " + instrument.getInstrument() + "\n");
		instrumentDetails.append("EXCHANGE: " + instrument.getExchange() + "\n");
		instrumentDetails.append("DECRIPTION: " + instrument.getDescription() + "\n");
		instrumentDetails.append("MMY: " + instrument.getMmy() + "\n");
		instrumentDetails.append("MATURITY DATE: " + instrument.getMaturity_date() + "\n");
		instrumentDetails.append("MULTIPLIER: " + instrument.getMultiplier() + "\n");
		instrumentDetails.append("MIN PRICE TICK: " + instrument.getMin_px_incr() + "\n");
		instrumentDetails.append("DENOMINATOR: " + instrument.getDenominator() + "\n");
		instrumentDetails.append("NUMERATOR: " + instrument.getNumerator() + "\n");
		instrumentDetails.append("TICK INCR: " + instrument.getTick_incr() + "\n");
		instrumentDetails.append("INITIAL MARGIN: " + instrument.getInit_margin() + "\n");
		instrumentDetails.append("MAINT MARGIN: " + instrument.getMaint_margin());
	}
}
