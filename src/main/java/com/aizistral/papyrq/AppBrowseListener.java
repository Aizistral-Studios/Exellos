package com.aizistral.papyrq;

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

		if (type == Type.ENCODE_OUTPUT) {
			this.fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return ".pdf file";
				}

				@Override
				public boolean accept(File file) {
					return Objects.equals(FilenameUtils.getExtension(file.getPath()).toLowerCase(), "pdf");
				}
			});
		} else if (type == Type.DECODE_INPUT) {
			this.fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return ".json file";
				}

				@Override
				public boolean accept(File file) {
					return Objects.equals(FilenameUtils.getExtension(file.getPath()).toLowerCase(), "json");
				}
			});
		}

		File currentDir = Paths.get("./").toFile().getCanonicalFile();

		while (!currentDir.isDirectory()) {
			currentDir = currentDir.getParentFile();
		}

		this.fileChooser.setCurrentDirectory(currentDir);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int returnState;

		if (this.type == Type.ENCODE_INPUT || this.type == Type.DECODE_INPUT) {
			returnState = this.fileChooser.showOpenDialog(this.parent.frmPapyrQ);
		} else if (this.type == Type.ENCODE_OUTPUT || this.type == Type.DECODE_OUTPUT) {
			returnState = this.fileChooser.showSaveDialog(this.parent.frmPapyrQ);
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

			if (this.type == Type.ENCODE_INPUT) {
				this.parent.encodeInputField.setText(path);

				String output = FilenameUtils.removeExtension(path) + "_encoded.pdf";
				this.parent.encodeOutputField.setText(output);
			} else if (this.type == Type.ENCODE_OUTPUT) {
				this.parent.encodeOutputField.setText(path);
			} else if (this.type == Type.DECODE_INPUT) {
				this.parent.decodeInputField.setText(path);
			} else if (this.type == Type.DECODE_OUTPUT) {
				this.parent.decodeOutputField.setText(path);
			}
		}
	}

	public static enum Type {
		ENCODE_INPUT, ENCODE_OUTPUT, DECODE_INPUT, DECODE_OUTPUT;
	}

}

