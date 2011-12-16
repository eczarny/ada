package com.divisiblebyzero.chess.ai;

import java.util.LinkedList;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;

public class Search {
    private int depth;
    private long nodes;
    
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
        
        /* While there are still moves to evaluate... */
        for (Move move : availableMoves) {
            Piece current, capture;
            
            current = Bitboard.getPieceAtPosition(bitboard, move.getX());
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getX());
            
            /* Remember any possible captures... */
            capture = Bitboard.getPieceAtPosition(bitboard, move.getY());
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getY());
            
            long score = -this.search(bitboard, Search.invertColor(color), this.depth, -100000, 100000);
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getY());
            
            /* Ada tested a capture, make sure to put the piece back. */
            if (capture != null) {
                bitboard = Bitboard.setPieceAtPosition(bitboard, capture, move.getY());
            }
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getX());
            
            move.setScore(score);
        }
        
        long score = availableMoves.getFirst().getScore();
        
        result = availableMoves.getFirst();
        
        for (Move move : availableMoves) {
            System.out.print("    Potential Move: " + move + "\n");
            
            if (move.getScore() == -10000) {
                continue;
            }
            
            if (move.getScore() > score) {
                score = move.getScore();
                
                result = move;
            }
        }
        
        System.out.print("Move: " + result + ", Nodes: " + this.nodes + "\n");
        
        return result;
    }
    
    private long search(long[][] bitboard, int color, int depth, long alpha, long beta) {
        this.nodes++;
        
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
            
            long score = -search(bitboard, Search.invertColor(color), depth - 1, -beta, -alpha);
            
            if (score > alpha) {
                alpha = score;
            }
            
            bitboard = Bitboard.unsetPieceAtPosition(bitboard, current, move.getY());
            
            /* Ada tested a capture, make sure to put the piece back. */
            if (capture != null) {
                bitboard = Bitboard.setPieceAtPosition(bitboard, capture, move.getY());
            }
            
            bitboard = Bitboard.setPieceAtPosition(bitboard, current, move.getX());
            
            if (beta <= alpha) {
                break;
            }
        }
        
        return alpha;
    }
    
    private static int invertColor(int color) {
        if (color != Piece.Color.WHITE) {
            return Piece.Color.WHITE;
        } else {
            return Piece.Color.BLACK;
        }
    }
}
