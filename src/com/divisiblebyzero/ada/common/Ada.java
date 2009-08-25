package com.divisiblebyzero.ada.common;

//
// ada.common.Ada.java
// Ada Chess
//
// Created by Eric Czarny on April 29, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.io.Serializable;

import com.divisiblebyzero.ada.player.ComputerPlayer;
import com.divisiblebyzero.ada.player.HumanPlayer;
import com.divisiblebyzero.ada.player.Player;
import com.divisiblebyzero.ada.view.Table;
import com.divisiblebyzero.chess.Piece;

public class Ada implements Serializable {
    private static final long serialVersionUID = -4131304847689842562L;
    
    private Table table;
    private Player[] players;
    
    public Ada() {
        this.table = new Table(this);
        this.players = new Player[2];
        
        this.initialize();
    }
    
    private void initialize() {
        this.players[Piece.WHITE] = new HumanPlayer();
        this.players[Piece.BLACK] = new ComputerPlayer();
        
        this.table.setVisible(true);
    }
    
    public Table getTable() {
        return this.table;
    }
    
    public Player getWhitePlayer() {
        return this.getPlayerForColor(Piece.WHITE);
    }
    
    public Player getBlackPlayer() {
        return this.getPlayerForColor(Piece.BLACK);
    }
    
    private Player getPlayerForColor(int color) {
        return this.players[color];
    }
}
