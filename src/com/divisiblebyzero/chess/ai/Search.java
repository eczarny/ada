package com.divisiblebyzero.chess.ai;

//
// Search.java
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
    
    public Move searchForMove(long[][] bitboard, int color) {
        LinkedList<Move> availableMoves = MoveGenerator.generateMovesForColor(bitboard, color);
        Move result = null;
        
        if (this.depth == 0) {
        	return result;
        }
        
        /* While there are still moves to evaluate... */
        for (Move move : availableMoves) {
            Piece current, capture;
            
            current = Bitboard.getPieceAtPosition(bitboard, move.getX());
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getX());
            
            /* Remember any possible captures... */
            capture = Bitboard.getPieceAtPosition(bitboard, move.getY());
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getY());
            
            int score = -this.search(bitboard, Search.invertColor(color), this.depth, -10000, 10000);
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getY());
            
            /* Ada tested a capture, make sure to put the piece back. */
            if (capture != null) {
            	bitboard = Bitboard.setPieceAtPosition(bitboard, capture, move.getY());
            }
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getX());
            
            move.setScore(score);
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
    
    private int search(long[][] bitboard, int color, int depth, int alpha, int beta) {
        this.nodes++;
        
        if (Evaluator.isCheck(bitboard, color)) {
            return -10000;
        }
        
        /* We are at a leaf, evaluate position. */
        if (depth <= 0) {
            return Evaluator.evaluate(bitboard, color);
        }
        
        LinkedList<Move> availableMoves = MoveGenerator.generateMovesForColor(bitboard, color);
        
        for (Move move : availableMoves) {
            Piece current, capture;
            
            current = Bitboard.getPieceAtPosition(bitboard, move.getX());
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getX());
            
            /* Remember any possible captures... */
            capture = Bitboard.getPieceAtPosition(bitboard, move.getY());
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getY());
            
            int score = -search(bitboard, Search.invertColor(color), depth - 1, -beta, -alpha);
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getY());
            
            /* Ada tested a capture, make sure to put the piece back. */
            if (capture != null) {
            	bitboard = Bitboard.setPieceAtPosition(bitboard, capture, move.getY());
            }
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getX());
            
            if (score >= beta) {
                return beta;
            }
            
            if (score > alpha) {
                alpha = score;
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
