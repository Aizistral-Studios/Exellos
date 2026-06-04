package com.aizistral.exellos;

import java.awt.EventQueue;

import com.alee.laf.WebLookAndFeel;

public class Main {
	public static final StandardLogger LOGGER = new StandardLogger("PapyrQ");

	public static void main(String[] args) throws Exception {
		LOGGER.log("Installing WebLaF...");
		WebLookAndFeel.install();
		LOGGER.log("WebLaF installed.");

		EventQueue.invokeLater(() -> new AppWindow().makeVisible());
	}

}
