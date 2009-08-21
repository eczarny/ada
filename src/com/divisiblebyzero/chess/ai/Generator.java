package com.divisiblebyzero.chess.ai;

//
// chess.ai.Generator.java
// Ada Chess
//
// Created by Eric Czarny on November 6, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.util.LinkedList;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class Generator {
    
    public static LinkedList<Move> generateMovesForPiece(long[][] bitmaps, Piece piece) {
        LinkedList<Move> result = new LinkedList<Move>();
        long moves;
        
        piece.setPosition(Bitboard.getPositionFromBitmap(Bitboard.getBitmap(bitmaps, piece)));
        
        moves = Bitboard.getAttackBitmap(bitmaps, piece);
        
        while (moves != 0) {
            Position destination = Bitboard.getPositionFromBitmap(moves);
            Move move = new Move();
            
            move.setX(piece.getPosition());
            move.setY(destination);
            
            if ((move.getX().getRank() > 7) || (move.getX().getFile() > 7)) {
                continue;
            }
            
            if (Bitboard.getBitmapAtPosition(bitmaps, move.getX()) > 0) {
                result.add(move);
            }
            
            moves = moves & ~Bitboard.getMaskAtPosition(destination);
        }
        
        return result;
    }
    
    public static LinkedList<Move> generateMovesForColor(long[][] bitmaps, int color) {
        LinkedList<Move> result = new LinkedList<Move>();
        
        for (int i = 0; i < 6; i++) {
            long current = Bitboard.getBitmap(bitmaps, color, i);
            
            for (int j = 0; j < Generator.quantityOf(bitmaps, i, color); j++) {
                Piece origin = new Piece(color, i);
                
                result.addAll(Generator.generateMovesForPiece(bitmaps, origin));
                
                if ((origin.getPosition().getRank() > 7) || (origin.getPosition().getFile() > 7)) {
                    continue;
                }
                
                current = current & ~Bitboard.getMaskAtPosition(origin.getPosition());
            }
        }
        
        return result;
    }
    
    public static LinkedList<Move> generateDecentCaptures(long[][] bitmaps, int color) {
        LinkedList<Move> moves = Generator.generateMovesForColor(bitmaps, color);
        LinkedList<Move> result = new LinkedList<Move>();
        
        for (Move move : moves) {
            if (Bitboard.getBitmapAtPosition(bitmaps, move.getY()) > 0) {
                result.add(move);
            }
        }
        
        return result;
    }
    
    private static int quantityOf(long[][] bitmaps, int type, int color) {
        int result = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = Bitboard.getPieceAtPosition(bitmaps, new Position(i, j));
                
                if (current == null) {
                    continue;
                }
                
                if ((current.getType() == type) && (current.getColor() == color)) {
                    result++;
                }
            }
        }
        
        return result;
    }
}
