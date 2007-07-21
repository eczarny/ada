package com.divisiblebyzero.ada.ui;

//
//  ada.ui.Setup.java
//  Ada Chess
//
//  Created by Eric Czarny on April 28, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.divisiblebyzero.ada.*;
import com.divisiblebyzero.chess.model.*;
import com.divisiblebyzero.chess.ai.*;

public class Setup extends JFrame {
	private Ada ada;
	
	public Setup(Ada ada) {
		super("Ada Setup");
		
		this.ada = ada;
		
		int x = Toolkit.getDefaultToolkit().getScreenSize().width;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		this.setBounds(((x - 400) / 2), ((y - 165) / 2), 400, 140);
		
		this.setResizable(false);
		
		this.initialize();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}
	
	private void initialize() {
		Container container = this.getContentPane();
		Controller controller = new Controller();
		
		JButton[] controls = new JButton[5];
		
		for (int i = 0; i < controls.length; i++) {
			controls[i] = new JButton();
		}
		
		container.setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		
		north.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		north.setPreferredSize(new Dimension(400, 75));
		
		north.add(new JLabel("What level should Ada play at?"));
		
		north.add(new JLabel("Hint: Level 1 is fast/weakest. Level 5 is slow/strongest."));
		
		north.add(new JLabel("Warning: Anything above level 3 may take some time..."));
		
		container.add(north, BorderLayout.NORTH);
		
		JPanel south = new JPanel();
		
		south.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		controls[0].addActionListener(controller);
		controls[0].setPreferredSize(new Dimension(50, 25));
		controls[0].setText("1");
		
		south.add(controls[0]);
		
		controls[1].addActionListener(controller);
		controls[1].setPreferredSize(new Dimension(50, 25));
		controls[1].setText("2");
		
		south.add(controls[1]);
		
		controls[2].addActionListener(controller);
		controls[2].setPreferredSize(new Dimension(50, 25));
		controls[2].setText("3");
		
		south.add(controls[2]);
		
		controls[3].addActionListener(controller);
		controls[3].setPreferredSize(new Dimension(50, 25));
		controls[3].setText("4");
		
		south.add(controls[3]);
		
		controls[4].addActionListener(controller);
		controls[4].setPreferredSize(new Dimension(50, 25));
		controls[4].setText("5");
		
		south.add(controls[4]);
		
		container.add(south, BorderLayout.SOUTH);
		
		this.ada.getTable().getBoard().isLocked(true);
	}
	
	private class Controller implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Search.DEPTH = Integer.parseInt(event.getActionCommand());
			
			Setup.this.ada.getAnalysis().setDepth("Depth of Search: " + Search.DEPTH + " Ply");
			
			Setup.this.ada.getTable().getBoard().isLocked(Piece.BLACK);
			
			Setup.this.setVisible(false);
		}
	}
}
