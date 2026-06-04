package com.aizistral.papyrq;

import static com.aizistral.papyrq.Main.*;

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

import com.aizistral.papyrq.AppBrowseListener.Type;

public class AppWindow {
	JFrame frmPapyrQ;
	JTextField encodeInputField;
	JTextField encodeOutputField;
	JTextField encodeEncryptionKeyField;
	JTextField decodeInputField;
	JTextField decodeOutputField;
	JTextField decodeEncryptionKeyField;

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
		this.frmPapyrQ = new JFrame();
		this.frmPapyrQ.setTitle("PapyrQ");
		this.frmPapyrQ.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.frmPapyrQ.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.frmPapyrQ.setResizable(true);
		this.frmPapyrQ.setLocation(100, 100);
		this.frmPapyrQ.setMinimumSize(new Dimension(500, 265));
		this.frmPapyrQ.setSize(630, 265);
		this.frmPapyrQ.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmPapyrQ.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel mainPanel = new JPanel();
		this.frmPapyrQ.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		mainPanel.add(tabbedPane);

		JPanel encryptPanel = new JPanel();
		tabbedPane.addTab("Encode", null, encryptPanel, null);

		JLabel inputLabel = new JLabel("Input File:");
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.encodeInputField = new JTextField();
		this.encodeInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.encodeInputField.setColumns(10);

		JButton inputBrowse = new JButton("Browse");
		inputBrowse.addActionListener(new AppBrowseListener(this, Type.ENCODE_INPUT));
		inputBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel outputLabel = new JLabel("Output File:");
		outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		outputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.encodeOutputField = new JTextField();
		this.encodeOutputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.encodeOutputField.setColumns(10);

		JButton outputBrowse = new JButton("Browse");
		outputBrowse.addActionListener(new AppBrowseListener(this, Type.ENCODE_OUTPUT));
		outputBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel encryptionLabel = new JLabel("Encryption Key:");
		encryptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		encryptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		this.encodeEncryptionKeyField = new JTextField();
		this.encodeEncryptionKeyField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.encodeEncryptionKeyField.setColumns(10);
		this.encodeEncryptionKeyField.setEnabled(true);

		JButton ballButton = new JButton("Encode!");
		ballButton.addActionListener(event -> {
			try {
				Path input = Paths.get(this.encodeInputField.getText());
				Path output = Paths.get(this.encodeOutputField.getText());
				String encryptionKey = this.encodeEncryptionKeyField.getText();

				//QREncoder.encode(input, output, encryptionKey);
				this.showSuccess("Successfully saved QR codes to: " + output);
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during encoding!", ex);
			}
		});
		ballButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		GroupLayout gl_encryptPanel = new GroupLayout(encryptPanel);
		gl_encryptPanel.setHorizontalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(ballButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
								.addGroup(gl_encryptPanel.createSequentialGroup()
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.LEADING, false)
												.addComponent(encryptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(inputLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(outputLabel, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
										.addGap(8)
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_encryptPanel.createSequentialGroup()
														.addComponent(this.encodeInputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_encryptPanel.createSequentialGroup()
														.addComponent(this.encodeOutputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(outputBrowse, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addComponent(this.encodeEncryptionKeyField, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))))
						.addContainerGap())
				);
		gl_encryptPanel.setVerticalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(inputLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.encodeInputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(outputLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
										.addComponent(this.encodeOutputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addComponent(outputBrowse, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.encodeEncryptionKeyField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptionLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addGap(119))
				);
		encryptPanel.setLayout(gl_encryptPanel);

		JPanel decryptPanel = new JPanel();
		tabbedPane.addTab("Decode", null, decryptPanel, null);

		JButton ballButton_1 = new JButton("Decode!");
		ballButton_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ballButton_1.addActionListener(event -> {
			try {
				Path input = Paths.get(this.decodeInputField.getText());
				Path output = Paths.get(this.decodeOutputField.getText());
				String encryptionKey = this.decodeEncryptionKeyField.getText();

				//QRDecoder.decode(input, output, encryptionKey);
				this.showSuccess("Successfully saved decoded file to: " + output);
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during encoding!", ex);
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

		this.decodeInputField = new JTextField();
		this.decodeInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decodeInputField.setColumns(10);

		JButton decodeInputBrowseButton = new JButton("Browse");
		decodeInputBrowseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		decodeInputBrowseButton.addActionListener(new AppBrowseListener(this, Type.DECODE_INPUT));

		this.decodeOutputField = new JTextField();
		this.decodeOutputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decodeOutputField.setColumns(10);

		JButton decodeOutputBrowseButton = new JButton("Browse");
		decodeOutputBrowseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		decodeOutputBrowseButton.addActionListener(new AppBrowseListener(this, Type.DECODE_OUTPUT));

		this.decodeEncryptionKeyField = new JTextField();
		this.decodeEncryptionKeyField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		this.decodeEncryptionKeyField.setColumns(10);
		this.decodeEncryptionKeyField.setEnabled(true);

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
														.addComponent(this.decodeInputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(decodeInputBrowseButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_decryptPanel.createSequentialGroup()
														.addComponent(this.decodeOutputField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(decodeOutputBrowseButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addComponent(this.decodeEncryptionKeyField, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))))
						.addContainerGap())
				);
		gl_decryptPanel.setVerticalGroup(
				gl_decryptPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 280, Short.MAX_VALUE)
				.addGroup(gl_decryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(inputLabel_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(this.decodeInputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(decodeInputBrowseButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(outputLabel_1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
										.addComponent(this.decodeOutputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addComponent(decodeOutputBrowseButton, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.decodeEncryptionKeyField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptionLabel_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton_1, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(119, Short.MAX_VALUE))
				);
		decryptPanel.setLayout(gl_decryptPanel);
	}

	public void makeVisible() {
		this.frmPapyrQ.setVisible(true);
	}

	void showSuccess(String message) {
		JOptionPane.showMessageDialog(this.frmPapyrQ, message, "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
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

		JOptionPane.showMessageDialog(this.frmPapyrQ, message, "Encountered Exception", JOptionPane.ERROR_MESSAGE);
	}

}
