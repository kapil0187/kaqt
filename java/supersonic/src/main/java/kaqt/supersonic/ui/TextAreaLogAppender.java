package kaqt.supersonic.ui;

import javax.swing.JTextArea;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaLogAppender extends AppenderSkeleton {
	private JTextArea destination;

	public TextAreaLogAppender(JTextArea destination) {
		super();
		this.destination = destination;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		destination.append(event.getMessage().toString() + "\n");
	}
}
