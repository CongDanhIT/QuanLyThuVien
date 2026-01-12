package com.app.main;

import com.app.view.LoginFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class AppLauncher {
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(() -> {
	        com.formdev.flatlaf.FlatDarkLaf.setup();
	        // Bo tròn cực đại và chỉnh độ tương phản Focus
	        UIManager.put("Button.arc", 999);
	        UIManager.put("Component.arc", 50);
	        UIManager.put("TextComponent.arc", 50);
	        UIManager.put("Component.focusColor", "#FFC845");
	        new LoginFrame().setVisible(true);
	    });
	}
}