package com.aizistral.exellos;

import static com.aizistral.exellos.Main.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import com.aizistral.exellos.AppBrowseListener.Type;
import com.aizistral.exellos.FileEncryptor.DecryptedFile;
import com.google.common.io.Files;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

class AppWindow {
	JFrame frmExellos;

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

		JTextField encryptInputField = new JTextField();
		encryptInputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		encryptInputField.setColumns(10);

		JButton inputBrowse = new JButton("Browse");
		inputBrowse.addActionListener(new AppBrowseListener(this.frmExellos, encryptInputField, Type.ENCRYPT_INPUT));
		inputBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel encryptionLabel = new JLabel(" Password: ");
		encryptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		encryptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JTextField encryptPasswordField = new JTextField();
		encryptPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		encryptPasswordField.setColumns(10);
		encryptPasswordField.setEnabled(true);

		JLabel lblNewLabel = new JLabel(" Output: ");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JTextArea encryptionOutputArea = new JTextArea();
		encryptionOutputArea.setEditable(false);
		encryptionOutputArea.setLineWrap(true);

		JButton ballButton = new JButton("Encrypt!");
		ballButton.addActionListener(event -> {
			try {
				Path input = Paths.get(encryptInputField.getText());
				String password = encryptPasswordField.getText();
				String encryptedBase64 = FileEncryptor.encrypt(input, password);

				encryptionOutputArea.setText(encryptedBase64);

				this.showSuccess("Encryption successful!");
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during encoding!", ex);
			}
		});
		ballButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		ImageIcon copyIconNormal = new ImageIcon(AppWindow.class.getResource("/assets/copy-icon-2.png"));
		ImageIcon copyIconSuccess = new ImageIcon(AppWindow.class.getResource("/assets/copy-icon-2-success.png"));

		JButton copyButton = new JButton("Copy Output");
		copyButton.setIcon(copyIconNormal);
		copyButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		copyButton.addActionListener(event -> {
			String output = encryptionOutputArea.getText();

			if (StringUtils.isEmpty(output)) {
				this.showError("Cannot copy - no output generated yet!");
				return;
			}

			StringSelection selection = new StringSelection(output);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, null);

			// Save original state
			String originalText = copyButton.getText();
			Icon originalIcon = copyButton.getIcon();

			// Show success state
			copyButton.setText("Copied!");
			copyButton.setIcon(copyIconSuccess);

			// Restore after 3 seconds
			Timer timer = new Timer(3000, restoreEvent -> {
				copyButton.setText(originalText);
				copyButton.setIcon(originalIcon);
			});

			timer.setRepeats(false);
			timer.start();
		});

		GroupLayout gl_encryptPanel = new GroupLayout(encryptPanel);
		gl_encryptPanel.setHorizontalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(encryptionOutputArea, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
								.addComponent(ballButton, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
								.addGroup(gl_encryptPanel.createSequentialGroup()
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.LEADING, false)
												.addComponent(encryptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(inputLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(8)
										.addGroup(gl_encryptPanel.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_encryptPanel.createSequentialGroup()
														.addComponent(encryptInputField, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
												.addComponent(encryptPasswordField, GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)))
								.addGroup(gl_encryptPanel.createSequentialGroup()
										.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(copyButton, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap())
				);
		gl_encryptPanel.setVerticalGroup(
				gl_encryptPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE, false)
								.addComponent(inputLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptInputField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(inputBrowse, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(encryptPasswordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(encryptionLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_encryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(copyButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(encryptionOutputArea, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
						.addContainerGap())
				);
		encryptPanel.setLayout(gl_encryptPanel);

		JPanel decryptPanel = new JPanel();
		tabbedPane.addTab("Decrypt", null, decryptPanel, null);

		JLabel decryptPasswordLabel = new JLabel(" Password :");
		decryptPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		decryptPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel decryptInputLabel = new JLabel(" Input (Base64 String): ");
		decryptInputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		decryptInputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JTextField decryptPasswordField = new JTextField();
		decryptPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		decryptPasswordField.setColumns(10);
		decryptPasswordField.setEnabled(true);

		JTextArea decryptionInputArea = new JTextArea();
		decryptionInputArea.setLineWrap(true);

		JButton ballButton_1 = new JButton("Decrypt!");
		ballButton_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		ballButton_1.addActionListener(event -> {
			try {
				String inputBase64 = decryptionInputArea.getText();
				String password = decryptPasswordField.getText();

				if (StringUtils.isEmpty(inputBase64)) {
					this.showError("Cannot decrypt - no input specified!");
					return;
				}

				DecryptedFile decryptedFile = FileEncryptor.decrypt(inputBase64, password);

				JFileChooser saveLocationChooser = new JFileChooser();

				saveLocationChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				saveLocationChooser.setFileHidingEnabled(false);

				File currentDir = Paths.get("./").toFile().getCanonicalFile();
				File defaultSaveLocation = new File(currentDir, decryptedFile.getFileName());
				saveLocationChooser.setSelectedFile(defaultSaveLocation);

				int returnState = saveLocationChooser.showSaveDialog(this.frmExellos);

				if (returnState == JFileChooser.APPROVE_OPTION) {
					File saveLocation = saveLocationChooser.getSelectedFile();
					String savePath = null;

					try {
						savePath = saveLocation.getCanonicalPath();
					} catch (IOException ex) {
						savePath = saveLocation.getAbsolutePath();
					}

					Files.write(decryptedFile.getBytes(), saveLocation);

					this.showSuccess("Successfully saved decrypted file to: " + savePath);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				this.showException("Error during decryption!", ex);
			}
		});

		GroupLayout gl_decryptPanel = new GroupLayout(decryptPanel);
		gl_decryptPanel.setHorizontalGroup(
				gl_decryptPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_decryptPanel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(decryptionInputArea, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(decryptInputLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
								.addComponent(ballButton_1, GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
								.addGroup(gl_decryptPanel.createSequentialGroup()
										.addComponent(decryptPasswordLabel)
										.addGap(8)
										.addComponent(decryptPasswordField, GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)))
						.addContainerGap())
				);
		gl_decryptPanel.setVerticalGroup(
				gl_decryptPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_decryptPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(decryptInputLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(decryptionInputArea, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_decryptPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(decryptPasswordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(decryptPasswordLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(ballButton_1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);
		decryptPanel.setLayout(gl_decryptPanel);
	}

	public void makeVisible() {
		this.frmExellos.setVisible(true);
	}

	void showSuccess(String message) {
		JOptionPane.showMessageDialog(this.frmExellos, message, "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
	}

	void showError(String error) {
		JOptionPane.showMessageDialog(this.frmExellos, error, "Error", JOptionPane.ERROR_MESSAGE);
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
