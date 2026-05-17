package dataAccess;

import javax.swing.JOptionPane;

public class Pantalla {
	private Pantalla() {
        // Utility class — no instances
    }
	
	public static void enviarMensaje(String mensaje) {
		System.out.println(">> Pantalla: " + mensaje);
		JOptionPane.showMessageDialog(null, mensaje, "Pantalla", JOptionPane.INFORMATION_MESSAGE);
	}
}
