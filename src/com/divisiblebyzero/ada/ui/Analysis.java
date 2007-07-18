package com.divisiblebyzero.ada.ui;

//
//  ada.ui.Analysis.java
//  Ada Chess
//
//  Created by Eric Czarny on November 5, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import java.awt.*;
import javax.swing.*;

import com.divisiblebyzero.chess.ai.*;
import com.divisiblebyzero.chess.model.*;

public class Analysis extends JFrame {
	private JLabel depth;
	private JLabel analysis;
	private JLabel message;

	public Analysis() {
		super("Search Analysis");

		int x = Toolkit.getDefaultToolkit().getScreenSize().width;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height;

		if (System.getProperty("os.name").equals("Mac OS X")) {
			this.setBounds(((x - ((Square.SIZE * 8) + 20)) / 2) + 430, ((y - (((Square.SIZE * 9) + 15) + 15)) / 2),
				275, 100);
		} else {
			this.setBounds(((x - ((Square.SIZE * 8) + 20)) / 2) + 430, ((y - (((Square.SIZE * 9) + 15) + 15)) / 2),
				275, 100);
		}

		this.setResizable(false);

		this.initialize();

		this.setVisible(true);
	}

	private void initialize() {
		Container container = this.getContentPane();

		container.setLayout(new BorderLayout(0, 0));

		JPanel north = new JPanel();

		this.depth = new JLabel();

		this.depth.setFont(new Font("Monospaced", Font.PLAIN,  12));
		this.setDepth("Depth of Search: " + Search.DEPTH + " Ply");

		north.add(this.depth);

		north.setPreferredSize(new Dimension(275, 25));

		container.add(north, BorderLayout.NORTH);

		JPanel center = new JPanel();

		this.analysis = new JLabel();

		this.analysis.setFont(new Font("Monospaced", Font.PLAIN,  14));
		this.setAnalysis("Welcome to Ada!");

		center.add(this.analysis);

		center.setPreferredSize(new Dimension(275, 25));

		container.add(center, BorderLayout.CENTER);

		JPanel south = new JPanel();

		this.message = new JLabel();

		this.message.setFont(new Font("Monospaced", Font.PLAIN,  12));
		this.setMessage("Awaiting Move...");

		south.add(this.message);

		south.setPreferredSize(new Dimension(275, 25));

		container.add(south, BorderLayout.SOUTH);
	}

	public void setDepth(String depth) {
		this.depth.setText(depth);
	}

	public void setMessage(String message) {
		this.message.setText(message);
	}

	public void setAnalysis(String analysis) {
		this.analysis.setText(analysis);
	}
}
