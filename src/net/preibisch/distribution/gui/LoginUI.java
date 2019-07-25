package net.preibisch.distribution.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.jcraft.jsch.JSchException;

import net.preibisch.distribution.algorithm.clustering.jsch.SessionManager;
import net.preibisch.distribution.algorithm.controllers.items.server.Login;
import net.preibisch.distribution.tools.config.DEFAULT;
import net.preibisch.distribution.tools.config.server.Account;

public class LoginUI extends JFrame {
	public static void main(String[] args) {
		new LoginUI();
	}

	public LoginUI() {
		super("Login..");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(e.getComponent(), "You have to login first !", "Login first ..",
						JOptionPane.ERROR_MESSAGE);
//				throw new RuntimeException("LoginClosedException");
			}
		});

		JLabel lblUser = new JLabel("User Name:");
		JTextField tfUser = new JTextField(20);
		lblUser.setLabelFor(tfUser);
		tfUser.setText(DEFAULT.USER_PSEUDO);

		JLabel lblPassword = new JLabel("Password:");
		final JPasswordField pfPassword = new JPasswordField(20);
		lblPassword.setLabelFor(pfPassword);

		JButton btnLogin = new JButton("Login");

		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());

		panel.add(lblUser);
		panel.add(tfUser);
		panel.add(lblPassword);
		panel.add(pfPassword);

		panel.add(new JPanel());
		panel.add(btnLogin);
		getRootPane().setDefaultButton(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Login.setAccount(new Account(tfUser.getText(), pfPassword.getPassword()));
				setVisible(false);
				dispose();
				try {
					SessionManager.connect();
				} catch (JSchException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

//		SpringUtilities.makeCompactGrid(panel, 3, 2, // rows, cols
//				6, 6, // initX, initY
//				6, 6); // xPad, yPad

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 120);
		getContentPane().add(panel);
		setVisible(true);
	}
}
