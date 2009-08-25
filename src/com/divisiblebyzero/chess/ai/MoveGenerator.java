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

public class MoveGenerator {
    
    public static LinkedList<Move> generateMovesForPiece(long[][] bitmaps, Piece piece) {
        LinkedList<Move> result = new LinkedList<Move>();
        long bitmap;
        
        bitmap = Bitboard.getAttackBitmap(bitmaps, piece);
        
        while (bitmap != 0) {
            Position destination = Bitboard.getPositionFromBitmap(bitmap);
            Move move = new Move();
            
            move.setX(piece.getPosition());
            move.setY(destination);
            
            result.add(move);
            
            bitmap = bitmap & ~Bitboard.getMaskAtPosition(destination);
        }
        
        return result;
    }
    
    public static LinkedList<Move> generateMovesForColor(long[][] bitmaps, int color) {
        LinkedList<Move> result = new LinkedList<Move>();
        
        for (int i = 0; i < 6; i++) {
            long bitmap = Bitboard.getBitmap(bitmaps, color, i);
            
            while (bitmap != 0) {
                Piece origin = new Piece(color, i);
                
                origin.setPosition(Bitboard.getPositionFromBitmap(bitmap));
                
                result.addAll(MoveGenerator.generateMovesForPiece(bitmaps, origin));
                
                bitmap = bitmap & ~Bitboard.getMaskAtPosition(origin.getPosition());
            }
        }
        
        return result;
    }
}
