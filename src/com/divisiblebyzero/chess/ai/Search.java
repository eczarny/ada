package com.divisiblebyzero.chess.ai;

//
// chess.ai.Search.java
// Ada Chess
//
// Created by Eric Czarny on October 28, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.util.LinkedList;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;

public class Search {
    private int depth;
    private int nodes;
    
    public Search() {
        this.depth = 3;
        this.nodes = 0;
    }
    
    public Search(int depth) {
        this.depth = depth;
        this.nodes = 0;
    }
    
    public Move searchForMove(long[][] bitmaps, int color) {
        LinkedList<Move> availableMoves = Generator.generateMovesForColor(bitmaps, color);
        Move result;
        
        /* While there are still moves to evaluate... */
        for (Move move : availableMoves) {
            Piece current, capture;
            
            current = Bitboard.getPieceAtPosition(bitmaps, move.getX());
            
            bitmaps = Bitboard.unsetPieceAtPosition(bitmaps, current, move.getX());
            
            /* Remember any possible captures... */
            capture = Bitboard.getPieceAtPosition(bitmaps, move.getY());
            
            bitmaps = Bitboard.setPieceAtPosition(bitmaps, current, move.getY());
            
            int value = -this.search(bitmaps, Search.invertColor(color), this.depth, -10000, 10000);
            
            bitmaps = Bitboard.unsetPieceAtPosition(bitmaps, current, move.getY());
            
            /* Ada tested a capture situation, make sure to put the piece back. */
            if (capture != null) {
                bitmaps = Bitboard.setPieceAtPosition(bitmaps, capture, move.getY());
            }
            
            bitmaps = Bitboard.setPieceAtPosition(bitmaps, current, move.getX());
            
            move.setScore(value);
        }
        
        int score = availableMoves.getFirst().getScore();
        
        result = availableMoves.getFirst();
        
        for (Move move : availableMoves) {
            if (move.getScore() == -10000) {
                continue;
            }
            
            if (move.getScore() > score) {
                score = move.getScore();
                
                result = move;
            }
        }
        
        if (score == -10000) {
            result = null;
        }
        
        return result;
    }
    
    private int search(long[][] bitmaps, int color, int depth, int alpha, int beta) {
        this.nodes++;
        
        if (Evaluator.isCheck(bitmaps, color)) {
            return -10000;
        }
        
        /* We are at a leaf, evaluate position. */
        if (depth <= 0) {
            return Evaluator.evaluate(bitmaps, color);
        }
        
        LinkedList<Move> availableMoves = Generator.generateMovesForColor(bitmaps, color);
        
        for (Move move : availableMoves) {
            Piece current, capture;
            
            current = Bitboard.getPieceAtPosition(bitmaps, move.getX());
            
            bitmaps = Bitboard.unsetPieceAtPosition(bitmaps, current, move.getX());
            
            /* Remember any possible captures... */
            capture = Bitboard.getPieceAtPosition(bitmaps, move.getY());
            
            bitmaps = Bitboard.setPieceAtPosition(bitmaps, current, move.getY());
            
            int value = -search(bitmaps, Search.invertColor(color), depth - 1, -beta, -alpha);
            
            bitmaps = Bitboard.unsetPieceAtPosition(bitmaps, current, move.getY());
            
            /* Ada tested a capture situation, make sure to put the piece back. */
            if (capture != null) {
                bitmaps = Bitboard.setPieceAtPosition(bitmaps, capture, move.getY());
            }
            
            bitmaps = Bitboard.setPieceAtPosition(bitmaps, current, move.getX());
            
            if (value >= beta) {
                return beta;
            }
            
            if (value > alpha) {
                alpha = value;
            }
        }
        
        return alpha;
    }
    
    private static int invertColor(int color) {
        if (color != Piece.WHITE) {
            return Piece.WHITE;
        } else {
            return Piece.BLACK;
        }
    }
}
