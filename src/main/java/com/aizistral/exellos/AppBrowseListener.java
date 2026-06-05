package com.aizistral.exellos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

import lombok.SneakyThrows;

public class AppBrowseListener implements ActionListener {
	private AppWindow parent;
	private Type type;
	private JFileChooser fileChooser;

	@SneakyThrows
	public AppBrowseListener(AppWindow parent, Type type) {
		this.parent = parent;
		this.type = type;
		this.fileChooser = new JFileChooser();

		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.fileChooser.setFileHidingEnabled(false);

		File currentDir = Paths.get("./").toFile().getCanonicalFile();

		while (!currentDir.isDirectory()) {
			currentDir = currentDir.getParentFile();
		}

		this.fileChooser.setCurrentDirectory(currentDir);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int returnState;

		if (this.type == Type.ENCRYPT_INPUT) {
			returnState = this.fileChooser.showOpenDialog(this.parent.frmExellos);
		} else if (this.type == Type.DECRYPT_OUTPUT) {
			returnState = this.fileChooser.showSaveDialog(this.parent.frmExellos);
		} else
			return;

		if (returnState == JFileChooser.APPROVE_OPTION) {
			File file = this.fileChooser.getSelectedFile();
			String path = null;

			try {
				path = file.getCanonicalPath();
			} catch (IOException ex) {
				path = file.getAbsolutePath();
			}

			if (this.type == Type.ENCRYPT_INPUT) {
				this.parent.encryptInputField.setText(path);
			} else if (this.type == Type.DECRYPT_OUTPUT) {
				//this.parent.decryptOutputField.setText(path);
			}
		}
	}

	public static enum Type {
		ENCRYPT_INPUT, DECRYPT_OUTPUT;
	}

}

