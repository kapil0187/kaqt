package kaqt.supersonic.ui;

import java.io.File;
import java.util.Set;

import javax.swing.DefaultListModel;

public class ChartsListModel extends DefaultListModel<String> {

	public ChartsListModel(Set<String> charts) {
		super();
		for (String chart : charts) {
			this.addElement(chart);
		}
	}

}