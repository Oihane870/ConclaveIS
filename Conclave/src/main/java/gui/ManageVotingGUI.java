package gui;

import java.util.ResourceBundle;


import javax.swing.JFrame;

import businessLogic.BLFacade;
import domain.Conclave;
import domain.Votacion;
import exceptions.NoActiveVotingException;
import exceptions.VotingNotFinishedException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

public class ManageVotingGUI extends JFrame{

	
	
	private static BLFacade appFacadeInterface;
	private JFrame thisFrame;
	
	private static final long serialVersionUID = 1L;
	
    private String nombreGanador = null;
    
    private JLabel jLabelConclave = new JLabel("Cónclave: sin información");
    private JLabel jLabelVoting = new JLabel("Votación actual: ninguna");
    private JLabel jLabelVotes = new JLabel("Votos emitidos: —");
    private JLabel jLabelMsg = new JLabel();

    
    private JButton jButtonOpenVoting  = new JButton("Iniciar votación");
    private JButton jButtonCloseVoting = new JButton("Cerrar votación");
    private JButton jButtonRefresh = new JButton("Actualizar estado");
    private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

    
    private JLabel     jLabelWinner = new JLabel();
    private JLabel     jLabelPapalName = new JLabel("Nombre pontificio:");
    private JTextField jFieldPapalName = new JTextField();
    private JLabel     jLabelPapalNumber = new JLabel("Número de Papa:");
    private JTextField jFieldPapalNumber = new JTextField();
    private JLabel     jLabelBirthDate = new JLabel("Fecha nac. (dd/MM/yyyy):");
    private JTextField jFieldBirthDate = new JTextField();
    private JButton    jButtonAccept = new JButton("Ha aceptado — Registrar Papa");
    private JButton    jButtonReject = new JButton("No acepta — Fumata negra");
    

	public static BLFacade getBusinessLogic(){
    	return appFacadeInterface;
    }
 
    public static void setBussinessLogic (BLFacade facade){
    	appFacadeInterface=facade;
    }
    
    public ManageVotingGUI() {
    	super();
    	thisFrame = this;
    	this.setSize(560, 540);
		this.setTitle("CONCLAVE" + " - "+ ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ManageVoting"));
        this.getContentPane().setLayout(null);
        
        
        
        jLabelConclave.setBounds(new Rectangle(10, 33, 444, 20));
        this.getContentPane().add(jLabelConclave);
        
        jLabelVoting.setText(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.NumVoting"));
        jLabelVoting.setBounds(10, 63, 288, 29);
        getContentPane().add(jLabelVoting);

        jLabelVotes.setText(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.VoteCount"));
        jLabelVotes.setBounds(10, 138, 200, 26);
        getContentPane().add(jLabelVotes);
        
        jButtonOpenVoting = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.Open"));
        jButtonOpenVoting.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		abrirVotacion();
        	}
        });
        jButtonOpenVoting.setBounds(308, 92, 146, 33);
        getContentPane().add(jButtonOpenVoting);
        
        jButtonRefresh = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.Update"));
        jButtonRefresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		refreshStatus();
        	}
        });
        jButtonRefresh.setBounds(308, 135, 146, 33);
        getContentPane().add(jButtonRefresh);
        
        jButtonCloseVoting = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.Close"));
        jButtonCloseVoting.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		cerrarVotacion();
        	}
        });
        jButtonCloseVoting.setBounds(308, 177, 146, 33);
        getContentPane().add(jButtonCloseVoting);
        
        
        
        JLabel lblNewLabel_2 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ManageVotingGUI.Title"));
        lblNewLabel_2.setBounds(10, 10, 125, 13);
        getContentPane().add(lblNewLabel_2);
        
        
        //mensajes
		jLabelMsg.setBounds(new Rectangle(20, 190, 510, 20));
        jLabelMsg.setForeground(Color.RED);
        this.getContentPane().add(jLabelMsg);
        
        
        
        // aceptar papa (oculto hasta que haya ganador)
        
        jLabelWinner.setForeground(new Color(0, 100, 0));
        jLabelWinner.setBounds(new Rectangle(20, 225, 510, 25));
        this.getContentPane().add(jLabelWinner);

        jLabelPapalName.setBounds(new Rectangle(20, 260, 180, 20));
        this.getContentPane().add(jLabelPapalName);
        jFieldPapalName.setBounds(new Rectangle(210, 258, 200, 24));
        this.getContentPane().add(jFieldPapalName);

        jLabelPapalNumber.setBounds(new Rectangle(20, 292, 180, 20));
        this.getContentPane().add(jLabelPapalNumber);
        jFieldPapalNumber.setBounds(new Rectangle(210, 290, 80, 24));
        this.getContentPane().add(jFieldPapalNumber);

        jLabelBirthDate.setBounds(new Rectangle(20, 324, 180, 20));
        this.getContentPane().add(jLabelBirthDate);
        jFieldBirthDate.setBounds(new Rectangle(210, 322, 120, 24));
        this.getContentPane().add(jFieldBirthDate);

        
        jButtonAccept.setForeground(new Color(0, 100, 0));
        jButtonAccept.setBounds(new Rectangle(20, 365, 280, 35));
        jButtonAccept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	aceptarPapa(); 
            }
        });
        this.getContentPane().add(jButtonAccept);

        
        jButtonReject.setForeground(Color.RED);
        jButtonReject.setBounds(new Rectangle(320, 365, 210, 35));
        jButtonReject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
            	rechazarPapa(); 
            }
        });
        this.getContentPane().add(jButtonReject);

        jButtonClose.setBounds(new Rectangle(380, 460, 150, 35));
        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { thisFrame.setVisible(false); }
        });
        this.getContentPane().add(jButtonClose);

        // ocultar seccion 
        setAcceptanceSectionVisible(false);

        
        refreshStatus();
        
        
    }
    
    
    private void refreshStatus() {
        jLabelMsg.setText("");
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            Conclave conclave = facade.getConclaveActivo();

            if (conclave == null) {
                jLabelConclave.setText("No hay cónclave activo.");
                jLabelVoting.setText("Votación actual: ninguna");
                
                jButtonOpenVoting.setEnabled(false);
                jButtonCloseVoting.setEnabled(false);
                return;
            }

            jLabelConclave.setText("Cónclave abierto el: " + conclave.getFechaInicio()
                + "  |  Electores: " + conclave.getElectores().size());

            Votacion votacion = conclave.getVotacionActual();
            if (votacion == null) {
                jLabelVoting.setText("Votación actual: ninguna abierta");
                jLabelVotes.setText("Votaciones celebradas: " + conclave.getVotaciones().size());
                jButtonOpenVoting.setEnabled(true);
                jButtonCloseVoting.setEnabled(false);
            } else {
                jLabelVoting.setText("Votación #" + votacion.getNumVotacion()
                    + "  iniciada: " + votacion.getHoraComienzo());
                jLabelVotes.setText("Han votado: " + votacion.getElectoresQueYaVotaron().size());
                jButtonOpenVoting.setEnabled(false);
                jButtonCloseVoting.setEnabled(votacion.puedeSerCerrada());
            }

        } catch (Exception ex) {
            jLabelMsg.setText("Error al actualizar: " + ex.getMessage());
        }
    }

    
    
    
    
    private void abrirVotacion() {
        jLabelMsg.setText("");
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            Votacion v = facade.abrirVotacion();
            JOptionPane.showMessageDialog(this,
                "Votación #" + v.getNumVotacion() + " iniciada.\n" +
                "Los electores disponen de 1 hora para votar.",
                "Votación abierta", JOptionPane.INFORMATION_MESSAGE);
            refreshStatus();
        } catch (NoActiveVotingException ex) {
            jLabelMsg.setText(ex.getMessage());
        } catch (Exception ex) {
            jLabelMsg.setText("Error: " + ex.getMessage());
        }
    }
    
    
    private void cerrarVotacion() {
        jLabelMsg.setText("");
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            nombreGanador = facade.cerrarVotacion();

            if (nombreGanador == null) {
                JOptionPane.showMessageDialog(this,
                    "No se han alcanzado los 2/3. Fumata negra.",
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
                setAcceptanceSectionVisible(false);
                refreshStatus();
            } else {
                // mostrar seccion papa
                jLabelWinner.setText("Candidato con 2/3: " + nombreGanador + " — ¿acepta el cargo?");
                jFieldBirthDate.setText("");
                jFieldPapalName.setText("");
                jFieldPapalNumber.setText("");
                setAcceptanceSectionVisible(true);
                jButtonOpenVoting.setEnabled(false);
                jButtonCloseVoting.setEnabled(false);
            }

        } catch (VotingNotFinishedException ex) {
            jLabelMsg.setText(ex.getMessage());
        } catch (NoActiveVotingException ex) {
            jLabelMsg.setText(ex.getMessage());
        } catch (Exception ex) {
            jLabelMsg.setText("Error: " + ex.getMessage());
        }
    }
    
    private void aceptarPapa() {
        jLabelMsg.setText("");
        String papalName = jFieldPapalName.getText().trim();
        String numberStr = jFieldPapalNumber.getText().trim();
        String birthStr = jFieldBirthDate.getText().trim();

        if (papalName.isEmpty() || numberStr.isEmpty() || birthStr.isEmpty()) {
            jLabelMsg.setText("Completa todos los campos antes de registrar al Papa.");
            return;
        }

        try {
            int numPapa = Integer.parseInt(numberStr);

            // Parse birth date dd/MM/yyyy
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaNac = (java.util.Date) sdf.parse(birthStr);

            BLFacade facade = MainGUI.getBusinessLogic();
            facade.registrarPapa(nombreGanador, fechaNac, papalName, numPapa);

            JOptionPane.showMessageDialog(this,
                "¡Habemus Papam!\n" + papalName + " (" + nombreGanador + ")\nPapa número " + numPapa,
                "Fumata blanca", JOptionPane.INFORMATION_MESSAGE);

            setAcceptanceSectionVisible(false);
            thisFrame.setVisible(false);

        } catch (NumberFormatException ex) {
            jLabelMsg.setText("El número de Papa debe ser un número entero.");
        } catch (java.text.ParseException ex) {
            jLabelMsg.setText("Formato de fecha incorrecto. Usa dd/MM/yyyy.");
        } catch (Exception ex) {
            jLabelMsg.setText("Error: " + ex.getMessage());
        }
    }

    private void rechazarPapa() {
        jLabelMsg.setText("");
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            facade.rechazarPapa();

            JOptionPane.showMessageDialog(this,
            		nombreGanador + " no ha aceptado el cargo.\nFumata negra. El proceso continúa.",
                "Resultado", JOptionPane.INFORMATION_MESSAGE);

            nombreGanador = null;
            setAcceptanceSectionVisible(false);
            refreshStatus();

        } catch (Exception ex) {
            jLabelMsg.setText("Error: " + ex.getMessage());
        }
    }

    private void setAcceptanceSectionVisible(boolean visible) {
        jLabelWinner.setVisible(visible);
        jLabelPapalName.setVisible(visible);
        jFieldPapalName.setVisible(visible);
        jLabelPapalNumber.setVisible(visible);
        jFieldPapalNumber.setVisible(visible);
        jLabelBirthDate.setVisible(visible);
        jFieldBirthDate.setVisible(visible);
        jButtonAccept.setVisible(visible);
        jButtonReject.setVisible(visible);
    }
}
