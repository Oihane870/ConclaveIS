package gui;

import java.util.List;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


import businessLogic.BLFacade;
import domain.Cardenal;
import exceptions.AlreadyVotedException;
import exceptions.NoActiveVotingException;

import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class VoteGUI extends JFrame {
	private static final long serialVersionUID = 1L;

    private static BLFacade appFacadeInterface;
    private JFrame thisFrame;
    
    private List<Cardenal> electores;
    private DefaultComboBoxModel<String> electorModel = new DefaultComboBoxModel<>();
    private JButton jButtonVote  = new JButton();

    private JComboBox<String> jComboElectors = new JComboBox<>(electorModel);
    private JLabel jLabelMsg = new JLabel();
    private JTextField jFieldCandidate  = new JTextField();

    
    public static BLFacade getBusinessLogic(){
    	return appFacadeInterface;
    }
 
    public static void setBussinessLogic (BLFacade facade){
    	appFacadeInterface=facade;
    }
    
    
    
    public VoteGUI() {
    	super();
    	thisFrame = this;
    	this.setSize(495, 290);
		this.setTitle("CONCLAVE" + " - "+ ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CastVote"));
        this.getContentPane().setLayout(null);
        
        JLabel lblNewLabel_2 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("VoteGUI.Title")); //$NON-NLS-1$ //$NON-NLS-2$
        lblNewLabel_2.setBounds(10, 10, 172, 13);
        getContentPane().add(lblNewLabel_2);
        
        //elector
        JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("VoteGUI.Elector"));
        lblNewLabel.setBounds(10, 45, 116, 30);
        getContentPane().add(lblNewLabel);
        
        jComboElectors.setBounds(new Rectangle(167, 47, 193, 26));
        this.getContentPane().add(jComboElectors);
        
        
        //candidato
        JLabel lblNewLabel_1 = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("VoteGUI.Candidate"));
        lblNewLabel_1.setBounds(10, 102, 116, 30);
        getContentPane().add(lblNewLabel_1);
        
        jFieldCandidate = new JTextField();
        jFieldCandidate.setBounds(167, 101, 193, 33);
        getContentPane().add(jFieldCandidate);
        jFieldCandidate.setColumns(10);
        
        JButton jButtonVote = new JButton(ResourceBundle.getBundle("Etiquetas").getString("VoteGUI.Vote"));
        jButtonVote.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		emitirVoto();
        		
        	}
        });
        jButtonVote.setBounds(21, 189, 161, 33);
        getContentPane().add(jButtonVote);
        
        JButton jButtonBack = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Atras"));
        jButtonBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		thisFrame.setVisible(false);
        	}
        	
        });
        jButtonBack.setBounds(311, 189, 102, 33);
        getContentPane().add(jButtonBack);
        
        
        //mensajes
        jLabelMsg.setBounds(new Rectangle(20, 215, 430, 20));
        jLabelMsg.setForeground(Color.RED);
        this.getContentPane().add(jLabelMsg);
        
        cargarElectores();
        
        
        
    	
    }
    
    private void cargarElectores() {
        try {
            BLFacade facade = MainGUI.getBusinessLogic();
            electores = facade.getConclaveActivo().getElectores();
            electorModel.removeAllElements();
            for (Cardenal c : electores) {
                electorModel.addElement(c.getNombre() + " — " + c.getCargo());
            }
            if (electores.isEmpty()) {
                jLabelMsg.setText("No hay electores registrados en el cónclave activo.");
                jButtonVote.setEnabled(false);
            }
        } catch (NullPointerException ex) {
            jLabelMsg.setText("No hay cónclave activo.");
            jButtonVote.setEnabled(false);
        } catch (Exception ex) {
            jLabelMsg.setText("Error al cargar electores: " + ex.getMessage());
            jButtonVote.setEnabled(false);
        }
    }
    
    private void emitirVoto() {
        jLabelMsg.setText("");

        int selectedIndex = jComboElectors.getSelectedIndex();
        if (selectedIndex < 0) {
            jLabelMsg.setText("Selecciona tu nombre de la lista.");
            return;
        }

        String candidate = jFieldCandidate.getText().trim();
        if (candidate.isEmpty()) {
            jLabelMsg.setText("Escribe el nombre del candidato.");
            return;
        }

        try {
            Cardenal elector = electores.get(selectedIndex);
            BLFacade facade  = MainGUI.getBusinessLogic();
            facade.emitirVoto(elector.getId(), candidate);

            JOptionPane.showMessageDialog(this,
                "Voto emitido correctamente.",
                "Voto registrado", JOptionPane.INFORMATION_MESSAGE);

            // limpiar despues de votar
            jFieldCandidate.setText("");

        } catch (AlreadyVotedException ex) {
            jLabelMsg.setText(ex.getMessage());
        } catch (NoActiveVotingException ex) {
            jLabelMsg.setText(ex.getMessage());
        } catch (Exception ex) {
            jLabelMsg.setText("Error: " + ex.getMessage());
        }
    }
    
    
}
