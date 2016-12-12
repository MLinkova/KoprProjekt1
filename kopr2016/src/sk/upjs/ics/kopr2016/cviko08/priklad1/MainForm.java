package sk.upjs.ics.kopr2016.cviko08.priklad1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
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
		SpellCheckerSwingWorker swingWorker = new SpellCheckerSwingWorker(textArea, redGreenPanel);
		swingWorker.execute();
	}
	

	public static void main(String[] args) throws InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					MainForm form = new MainForm();
					form.setVisible(true);
					form.triggerSpellCheck();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
