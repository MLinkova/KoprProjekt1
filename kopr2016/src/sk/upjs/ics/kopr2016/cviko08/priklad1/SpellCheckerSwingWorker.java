package sk.upjs.ics.kopr2016.cviko08.priklad1;

import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class SpellCheckerSwingWorker extends SwingWorker<Void, Boolean>{

	private JTextArea textArea;
	private RedGreenPanel redGreenPanel;

	public SpellCheckerSwingWorker(JTextArea textArea, RedGreenPanel redGreenPanel) {
		this.textArea = textArea;
		this.redGreenPanel = redGreenPanel;
	}

	@Override
	protected Void doInBackground() throws Exception {
		while(true) {
			if (Thread.currentThread().isInterrupted())
				return null;
			final String[] textField = new String[1]; 
			SwingUtilities.invokeAndWait(new Runnable() {				
				@Override
				public void run() {
					textField[0] = textArea.getText();
				}
			});
			
			SpellChecker spellChecker = new SpellChecker();
			List<SpellChecker.SpellcheckBoundary> kontrola = spellChecker.check(textField[0]);
			if (kontrola.isEmpty()) {
				publish(true);// korektny text
			}
			else {
				publish(false);// zly text
			}
		}
	}
	
	@Override
	protected void done() {
		System.out.println("Hotovo!");
	}
	
	@Override
	protected void process(List<Boolean> chunks) {
		redGreenPanel.setGreenState(chunks.get(chunks.size()-1));  // korektny text
	}
}
