package com.divisiblebyzero.ada.view;

//
//  ada.view.Table.java
//  Ada Chess
//
//  Created by Eric Czarny on February 26, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.divisiblebyzero.ada.Ada;
import com.divisiblebyzero.chess.model.Square;

public class Table extends JFrame {
	private Ada ada;
	private Board board;
	private JLabel status;
	
	public Table(Ada ada) {
		super("Welcome to Ada!");
		
		this.ada = ada;
		
		int x = Toolkit.getDefaultToolkit().getScreenSize().width;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		if (System.getProperty("os.name").equals("Mac OS X")) {
			this.setBounds(((x - ((Square.SIZE * 8) + 20) + 1) / 2), ((y - (((Square.SIZE * 9) + 15) + 15)) / 2),
				(((Square.SIZE * 8) + 20) + 1), (((Square.SIZE * 9) + 15) + 15));
		} else {
			this.setBounds(((x - ((Square.SIZE * 8) + 20) + 7) / 2), ((y - (((Square.SIZE * 9) + 20) + 15)) / 2),
				(((Square.SIZE * 8) + 20) + 7), (((Square.SIZE * 9) + 20) + 15));
		}
		
		this.setResizable(false);
		
		this.initialize();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}

	private void initialize() {
		Container container = this.getContentPane();
		
		container.setLayout(new BorderLayout(10, 10));
		
		this.board = new Board(this);
		
		container.add(this.board, BorderLayout.CENTER);
		
		JPanel south = new JPanel();
		
		this.status = new JLabel();
		
		this.status.setFont(new Font("Monospaced", Font.PLAIN,  14));
		this.setStatus("Welcome to Ada!");
		
		south.add(this.status);
		
		south.setPreferredSize(new Dimension(Square.SIZE * 8, 35));
		
		container.add(south, BorderLayout.SOUTH);
	}
	
	public Ada getAda() {
		return this.ada;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public void setStatus(String status) {
		this.status.setText(status);
	}
	
	public void networkUpdateNotification() {
		this.board.networkUpdateNotification();
	}
}
