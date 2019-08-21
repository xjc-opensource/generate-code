
package ext.opensource.generatecode;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import java.awt.Panel;
import java.awt.List;
import java.awt.Label;
import java.awt.TextField;

/**
 * @author ben
 * @Title: GenerateGuiApplication.java
 * @Description:
 **/

public class GenerateGuiApplication {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenerateGuiApplication window = new GenerateGuiApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GenerateGuiApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(123, 47, 143, 24);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4"}));
		
		JLabel lblNewLabel = new JLabel("TableName:");
		lblNewLabel.setBounds(25, 50, 80, 18);
		
		JButton btnNewButton = new JButton("exec");
		btnNewButton.setBounds(272, 10, 65, 27);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(btnNewButton);
		
		Label label = new Label("New label");
		label.setBounds(25, 77, 77, 25);
		frame.getContentPane().add(label);
		
		List list = new List();
		list.setBounds(123, 77, 140, 155);
		frame.getContentPane().add(list);
		
		TextField textField = new TextField();
		textField.setBounds(121, 10, 145, 25);
		frame.getContentPane().add(textField);
	}
}
