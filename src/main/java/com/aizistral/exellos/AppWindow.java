package com.aizistral.exellos;

import static com.aizistral.exellos.Main.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.aizistral.exellos.AppBrowseListener.Type;
import javax.swing.JTextArea;

public class AppWindow {
	JFrame frmExellos;
	JTextField encryptInputField;
	JTextField encryptPasswordField;
	JTextField decryptInputField;
	JTextField decryptOutputField;
	JTextField decryptPasswordField;

	/**
	 * Create the application.
	 */
	public AppWindow() {
		LOGGER.log("Initializing app window...");
		this.initialize();
		LOGGER.log("Window initialized.");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmExellos = new JFrame();
		this.frmExellos.setTitle("Exellos");
		this.frmExellos.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.frmExellos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.frmExellos.setResizable(true);
		this.frmExellos.setLocation(100, 100);
		this.frmExellos.setMinimumSize(new Dimension(500, 300));
		this.frmExellos.setSize(630, 400);
		this.frmExellos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmExellos.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel mainPanel = new JPanel();
		this.frmExellos.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		mainPanel.add(tabbedPane);

		JPanel encryptPanel = new JPanel();
		tabbedPane.addTab("Encrypt", null, encryptPanel, null);

		JLabel inputLabel = new JLabel(" Input File: ");
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.encryptInputField = new JTextField();
		this.encryptInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.encryptInputField.setColumns(10);

		JButton inputBrowse = new JButton("Browse");
		inputBrowse.addActionListener(new AppBrowseListener(this, Type.ENCRYPT_INPUT));
		inputBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel encryptionLabel = new JLabel(" Password: ");
		encryptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		encryptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.encryptPasswordField = new JTextField();
		this.encryptPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.encryptPasswordField.setColumns(10);
		this.encryptPasswordField.setEnabled(true);

		JLabel lblNewLabel = new JLabel(" Output: ");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);

		JButton ballButton = new JButton("Encrypt!");
		ballButton.addActionListener(event -> {
			try {
				Path input = Paths.get(this.encryptInputField.getText());
				String password = this.encryptPasswordField.getText();
				String encryptedBase64 = FileEncryptor.encrypt(input, password);

				textArea.setText(encryptedBase64);

				this.showSuccess("Encryption successful!");
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during encoding!", ex);
			}
		});
		ballButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		GroupLayout gl_encryptPanel = new GroupLayout(encryptPanel);
		gl_encryptPanel.setHorizontalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(textArea, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
								.addComponent(ballButton, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING, gl_encryptPanel.createSequentialGroup()
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.LEADING, false)
												.addComponent(encryptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(inputLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(8)
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_encryptPanel.createSequentialGroup()
														.addComponent(this.encryptInputField, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addComponent(this.encryptPasswordField, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE))))
						.addContainerGap())
				);
		gl_encryptPanel.setVerticalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(inputLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.encryptInputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.encryptPasswordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptionLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
						.addContainerGap())
				);
		encryptPanel.setLayout(gl_encryptPanel);

		JPanel decryptPanel = new JPanel();
		tabbedPane.addTab("Decrypt", null, decryptPanel, null);

		JButton ballButton_1 = new JButton("Decrypt!");
		ballButton_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ballButton_1.addActionListener(event -> {
			try {
				Path input = Paths.get(this.decryptInputField.getText());
				Path output = Paths.get(this.decryptOutputField.getText());
				String encryptionKey = this.decryptPasswordField.getText();

				//QRDecoder.decode(input, output, encryptionKey);
				this.showSuccess("Successfully saved decrypted file to: " + output);
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during decryption!", ex);
			}
		});

		JLabel encryptionLabel_1 = new JLabel("Encryption Key:");
		encryptionLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		encryptionLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel inputLabel_1 = new JLabel("Input File:");
		inputLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel outputLabel_1 = new JLabel("Output File:");
		outputLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		outputLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.decryptInputField = new JTextField();
		this.decryptInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decryptInputField.setColumns(10);

		JButton decryptInputBrowseButton = new JButton("Browse");
		decryptInputBrowseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		decryptInputBrowseButton.addActionListener(new AppBrowseListener(this, Type.DECRYPT_INPUT));

		this.decryptOutputField = new JTextField();
		this.decryptOutputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decryptOutputField.setColumns(10);

		JButton decodeOutputBrowseButton = new JButton("Browse");
		decodeOutputBrowseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		decodeOutputBrowseButton.addActionListener(new AppBrowseListener(this, Type.DECRYPT_OUTPUT));

		this.decryptPasswordField = new JTextField();
		this.decryptPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decryptPasswordField.setColumns(10);
		this.decryptPasswordField.setEnabled(true);

		GroupLayout gl_decryptPanel = new GroupLayout(decryptPanel);
		gl_decryptPanel.setHorizontalGroup(
				gl_decryptPanel.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 601, Short.MAX_VALUE)
				.addGroup(gl_decryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(ballButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
								.addGroup(gl_decryptPanel.createSequentialGroup()
										.addGroup(gl_decryptPanel.createParallelGroup(Alignment.LEADING, false)
												.addComponent(encryptionLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(inputLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(outputLabel_1, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
										.addGap(8)
										.addGroup(gl_decryptPanel.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_decryptPanel.createSequentialGroup()
														.addComponent(this.decryptInputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(decryptInputBrowseButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_decryptPanel.createSequentialGroup()
														.addComponent(this.decryptOutputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(decodeOutputBrowseButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addComponent(this.decryptPasswordField, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))))
						.addContainerGap())
				);
		gl_decryptPanel.setVerticalGroup(
				gl_decryptPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 280, Short.MAX_VALUE)
				.addGroup(gl_decryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(inputLabel_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.decryptInputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(decryptInputBrowseButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(outputLabel_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
										.addComponent(this.decryptOutputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addComponent(decodeOutputBrowseButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.decryptPasswordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptionLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(119, Short.MAX_VALUE))
				);
		decryptPanel.setLayout(gl_decryptPanel);
	}

	public void makeVisible() {
		this.frmExellos.setVisible(true);
	}

	void showSuccess(String message) {
		JOptionPane.showMessageDialog(this.frmExellos, message, "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
	}

	void showException(String message, Exception exception) {
		message += System.lineSeparator() + System.lineSeparator() + exception;
		int lines = 0;

		for (StackTraceElement element : exception.getStackTrace()) {
			message += System.lineSeparator() + "    " + element.toString();

			if (++lines > 10) {
				message += System.lineSeparator() + "    " +  "...";
				break;
			}
		}

		JOptionPane.showMessageDialog(this.frmExellos, message, "Encountered Exception", JOptionPane.ERROR_MESSAGE);
	}
}
