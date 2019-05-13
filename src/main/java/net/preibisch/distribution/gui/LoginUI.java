package main.java.net.preibisch.distribution.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import loci.plugins.config.SpringUtilities;
import main.java.net.preibisch.distribution.algorithm.controllers.items.server.Login;
import main.java.net.preibisch.distribution.tools.config.Config;
import main.java.net.preibisch.distribution.tools.config.DEFAULT;

public class LoginUI {
	public static void main(String[] args) {
		show();
	}
 
    public static void show() {
        final JFrame frame = new JFrame("JPasswordField Demo");
 
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
        frame.getRootPane().setDefaultButton(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DEFAULT.USER_PASSWORD = pfPassword.getPassword();
				DEFAULT.USER_PSEUDO = tfUser.getText();
				frame.setVisible(false);
				frame.dispose();
			}
		});

        SpringUtilities.makeCompactGrid(panel,
                3, 2, //rows, cols
                6, 6, //initX, initY
                6, 6); //xPad, yPad
 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 120);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
