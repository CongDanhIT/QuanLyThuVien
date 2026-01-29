package com.app.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class StatCard extends JPanel {
    private JLabel lblValue, lblSubText;

    public StatCard(String title, String value, String subText, Color accentColor) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#2D2A1E"));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(160, 160, 160));
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 12));

        lblValue = new JLabel(value);
        lblValue.setForeground(accentColor);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 36));

        lblSubText = new JLabel(subText);
        lblSubText.setForeground(Color.WHITE);
        lblSubText.setFont(new Font("SansSerif", Font.PLAIN, 13));

        add(lblTitle, BorderLayout.NORTH);
        add(lblValue, BorderLayout.CENTER);
        add(lblSubText, BorderLayout.SOUTH);
    }

    public void setData(String value, String subText) {
        lblValue.setText(value);
        lblSubText.setText(subText);
    }
}
