package gui;

import javax.swing.*; 


import businessLogic.BLFacade;
import dataAccess.Pantalla;
import domain.Cardenal;
import domain.Conclave;
import exceptions.ConclaveAlreadyOpenException;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;





public class OpenConclaveGUI extends JFrame {
	private static final long serialVersionUID = 1L;

    private static BLFacade appFacadeInterface;
    private JFrame thisFrame;
    private JPanel panelCardenales;
    private JLabel jLabelMsg; 
    private List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
    private List<Cardenal> cardenales = new ArrayList<Cardenal>();
    private Cardenal maestro;
    
    
    
    
    public static BLFacade getBusinessLogic(){
    	return appFacadeInterface;
    }
 
    public static void setBussinessLogic (BLFacade facade){
    	appFacadeInterface=facade;
    }


	
	public OpenConclaveGUI(Cardenal maestro) {
		super();
		thisFrame = this;
		this.maestro = maestro;
		this.setSize(495, 290);
		this.setTitle("CONCLAVE" + " - "+ ResourceBundle.getBundle("Etiquetas").getString("MainGUI.OpenConclave"));
        this.getContentPane().setLayout(null);
        
        JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("OpenConclaveGUI.Title"));
        lblNewLabel.setBounds(10, 10, 128, 13);
        getContentPane().add(lblNewLabel);
        
        
        //pasar lista
        panelCardenales = new JPanel();
        panelCardenales.setLayout(new BoxLayout(panelCardenales, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelCardenales);
        scroll.setBounds(20, 40, 440, 150);
        getContentPane().add(scroll);
        
        //errores
        jLabelMsg = new JLabel("");
        jLabelMsg.setForeground(Color.RED);
        jLabelMsg.setBounds(20, 230, 440, 20);
        getContentPane().add(jLabelMsg);
        
        JButton btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("OpenConclaveGUI.ButtonOpen")); //$NON-NLS-1$ //$NON-NLS-2$
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		abrirConclave();
        	}
        });
        btnNewButton.setBounds(20, 200, 139, 20);
        getContentPane().add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Atras")); //$NON-NLS-1$ //$NON-NLS-2$
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		thisFrame.setVisible(false);
        		
        	}
        });
        btnNewButton_1.setBounds(356, 200, 85, 21);
        getContentPane().add(btnNewButton_1);
        
        
       
        
     // Cargar cardenales al abrir la ventana
        cargarCardenales();
        
        
        
       
        
	}
	private void cargarCardenales() {
        cardenales = MainGUI.getBusinessLogic().getAllCardenales();
        for (Cardenal c : cardenales) {
            String texto = c.getNombre() + " - " + c.getCargo();
            if (!c.esElegible()) {
                texto += " (menor de 80, no elector)";
            }
            JCheckBox checkbox = new JCheckBox(texto);
            checkbox.setEnabled(c.esElegible());
            checkboxes.add(checkbox);
            panelCardenales.add(checkbox);
        }
        panelCardenales.revalidate();
        panelCardenales.repaint();
    }
	
	private void abrirConclave() {
        List<Cardenal> electores = new ArrayList<Cardenal>();
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                electores.add(cardenales.get(i));
            }
        }
        if (electores.isEmpty()) {
            jLabelMsg.setText("Selecciona al menos un cardenal presente");
            return;
        }
        try {
        	BLFacade facade = MainGUI.getBusinessLogic();
            Conclave conclave = facade.abrirConclave(maestro, electores);
            Pantalla.enviarMensaje("Extra Omnes");

            JOptionPane.showMessageDialog(thisFrame, "Conclave abierto correctamente");
            thisFrame.setVisible(false);
        } catch (ConclaveAlreadyOpenException ex) {
            jLabelMsg.setText(ex.getMessage());
        }
    }
}
