package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;


import businessLogic.BLFacade;
import domain.Cardenal;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainGUI extends JFrame {
	
    private String sellerMail;
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade facade){
		appFacadeInterface=facade;
	}
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	protected JLabel jLabelSelectOption;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI( ) {
		super();

		
		
		this.setSize(495, 290);
		this.setTitle("CONCLAVE");

		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jContentPane = new JPanel();
		jContentPane.setLayout(new GridLayout(5, 1, 0, 0));
		
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelSelectOption);
		
		btnNewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.OpenConclave")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cardenal maestro = elegirMaestro();
				if (maestro != null) {
					JFrame a = new OpenConclaveGUI(maestro);
					a.setVisible(true);
				}
			}
		});
		jContentPane.add(btnNewButton);
		
		btnNewButton_1 = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ManageVoting")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ManageVotingGUI();
				a.setVisible(true);
			}
		});
		jContentPane.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CastVote")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new VoteGUI();
				a.setVisible(true);
			}
		});
		jContentPane.add(btnNewButton_2);
		jContentPane.add(panel);
		
		
		setContentPane(jContentPane);
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") +": "+sellerMail);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		btnNewButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.OpenConclave")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_1.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ManageVoting"));
		btnNewButton_2.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CastVote"));

		
	}

	
	private Cardenal elegirMaestro() {
		try {
			java.util.List<Cardenal> cardenales = appFacadeInterface.getAllCardenales();
			if (cardenales.isEmpty()) {
				JOptionPane.showMessageDialog(this,
					"No hay cardenales en el sistema.",
					"Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		String[] nombres = new String[cardenales.size()];
		for (int i = 0; i< cardenales.size(); i++) {
			nombres[i] = cardenales.get(i).getNombre()+ " - " + cardenales.get(i).getCargo();
		}
		String elegido = (String) JOptionPane.showInputDialog(
				this,
				"Selecciona el Maestro de Ceremonias:",
				"Maestro de Ceremonias",
				JOptionPane.QUESTION_MESSAGE,
				null, nombres, nombres[0]);

			if (elegido == null) return null;
			for (int i = 0; i < nombres.length; i++) {
				if (nombres[i].equals(elegido)) return cardenales.get(i);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
				"Error al cargar cardenales: " + ex.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	

	
	
} // @jve:decl-index=0:visual-constraint="0,0"

