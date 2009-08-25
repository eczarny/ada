package com.divisiblebyzero.ada.view;

//
// ada.view.Table.java
// Ada Chess
//
// Created by Eric Czarny on February 26, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.divisiblebyzero.ada.common.Ada;
import com.divisiblebyzero.ada.common.i18n.LocalizedConstants;
import com.divisiblebyzero.ada.view.component.Board;
import com.divisiblebyzero.chess.Square;
import com.divisiblebyzero.utilities.LocalizationUtility;

public class Table extends JFrame {
    private static final long serialVersionUID = -4562513949419438657L;
    
    private Ada ada;
    private Board board;
    
    public Table(Ada ada) {
        super(LocalizationUtility.getInstance().getLocalizedString(
                LocalizedConstants.Table.WINDOW_TITLE));
        
        this.ada = ada;
        
        int x = Toolkit.getDefaultToolkit().getScreenSize().width;
        int y = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        if (System.getProperty("os.name").equals("Mac OS X")) {
            this.setBounds(((x - ((Square.SIZE * 8) + 20) + 1) / 2), ((y - (((Square.SIZE * 9) + 15) + 15)) / 2),
                (((Square.SIZE * 8) + 20) + 1), ((Square.SIZE * 9) - 7));
        } else {
            this.setBounds(((x - ((Square.SIZE * 8) + 20) + 7) / 2), ((y - (((Square.SIZE * 9) + 20) + 15)) / 2),
                (((Square.SIZE * 8) + 20) + 7), ((Square.SIZE * 9) - 7));
        }
        
        this.setResizable(false);
        
        this.initialize();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setVisible(true);
    }
    
    private void initialize() {
        Container container = this.getContentPane();
        
        container.setLayout(new BorderLayout(10, 10));
        
        this.board = new Board(this.ada);
        
        container.add(this.board, BorderLayout.CENTER);
    }
    
    public Ada getAda() {
        return this.ada;
    }
    
    public Board getBoard() {
        return this.board;
    }
}
