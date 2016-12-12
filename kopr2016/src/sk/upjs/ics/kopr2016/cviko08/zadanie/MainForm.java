package sk.upjs.ics.kopr2016.cviko08.zadanie;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 9126407517129194435L;

	private JTextArea textArea;
	private RedGreenPanel redGreenPanel;

	public MainForm() {
		textArea = new JTextArea();
		add(textArea);
		
		redGreenPanel = new RedGreenPanel();
		add(redGreenPanel, BorderLayout.PAGE_END);
		
		setSize(new Dimension(640, 480));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void triggerSpellCheck() throws InterruptedException {
		while(true) {
			String textField = textArea.getText();
			SpellChecker spellChecker = new SpellChecker();
			List<SpellChecker.SpellcheckBoundary> kontrola = spellChecker.check(textField);
			if (kontrola.isEmpty()) {
				redGreenPanel.setGreenState(true);  // korektny text
			}
			else {
				redGreenPanel.setGreenState(false); // zly text
			}
		}
	}
	

	public static void main(String[] args) throws InterruptedException {
		MainForm form = new MainForm();
		form.setVisible(true);
		form.triggerSpellCheck();
	}
}
