package cmm.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatePanel extends JPanel{
	private static JLabel stateLabel=new JLabel("ª∂”≠ π”√ CMMΩ‚ Õ∆˜v1.2             ");
	private JLabel positionInfo = new JLabel("Position:   ");
	private static JLabel position = new JLabel();;
	
	public StatePanel(){
		this.add(stateLabel);
		this.add(positionInfo);
		this.add(position);
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	public static String getStateString() {
		return stateLabel.getText();
	}

	public static void setStateString(String msg) {
		StatePanel.stateLabel .setText(msg);
	}
	
	public static void setPositionString(int x, int y) {
		StatePanel.position.setText(x+":"+y);
	}
	
	
	
}
