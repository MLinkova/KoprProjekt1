package sk.upjs.ics.kopr2016.cviko08.priklad1;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class RedGreenPanel extends JPanel {
	private static final long serialVersionUID = -4210967154633062013L;

	public RedGreenPanel() {
		setPreferredSize(new Dimension(640, 20));
	}
	
	public void setGreenState(boolean state) {
		Color color = state ? Color.GREEN : Color.RED;
		setBackground(color);		
	}
}
