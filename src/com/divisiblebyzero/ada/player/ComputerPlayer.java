package com.divisiblebyzero.ada.player;

//
// ada.player.ComputerPlayer.java
// Ada Chess
//
// Created by Eric Czarny on August 21, 2009.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.component.Board;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.ai.Search;

public class ComputerPlayer implements Player {
    
    public void makeMove(Board board, int color) {
        SearchThread searchThread = new SearchThread(board, color);
        
        searchThread.start();
    }
    
    private class SearchThread extends Thread {
        private Board board;
        private int color;
        
        public SearchThread(Board board, int color) {
            this.board = board;
            this.color = color;
        }
        
        public void run() {
            Search search = new Search(3);
            Move move = search.searchForMove(this.board.getBitmaps(), this.color);
            
            this.board.makeMove(move);
        }
    }
}
