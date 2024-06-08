package co.edu.uptc.view;

import javax.swing.JOptionPane;

public class IoManager {

	public int readInt(String message) {		
		return Integer.parseInt(JOptionPane.showInputDialog(message));	
	}

	public String readString(String message) {
		return JOptionPane.showInputDialog(message);
	}

	public long readLong(String message){
		return Long.parseLong(JOptionPane.showInputDialog(message));
	}

	public void printMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

}
