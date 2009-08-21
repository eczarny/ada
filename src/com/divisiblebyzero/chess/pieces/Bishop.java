package com.divisiblebyzero.chess.pieces;

//
// chess.pieces.Bishop.java
// Ada Chess
//
// Created by Eric Czarny on March 19, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class Bishop extends Piece {
    private static final long serialVersionUID = 1735367998231808538L;

	public static long getAttackBitmap(long[][] bitmaps, Piece piece) {
        long bitmap = Bitboard.getBitmapAtPosition(bitmaps, piece.getPosition());
        long result = 0;
        
        int rank = piece.getPosition().getRank() + 1;
        int file = piece.getPosition().getFile() + 1;
        
        /* Squares on the lower-right diagonal of the Bishop. */
        for (int i = 1; i < (8 - piece.getPosition().getFile()); i++) {
            result = result | (bitmap << (9 * i));
            
            if (Bitboard.getBitmapAtPosition(bitmaps, (new Position(rank, file))) > 0) {
                break;
            }
            
            rank++;
            file++;
        }
        
        rank = piece.getPosition().getRank() + 1;
        file = piece.getPosition().getFile() - 1;
        
        /* Squares on the lower-left diagonal of the Bishop. */
        for (int i = 1; i < (piece.getPosition().getFile() + 1); i++) {
            result = result | (bitmap << (7 * i));
            
            if (Bitboard.getBitmapAtPosition(bitmaps, (new Position(rank, file))) > 0) {
                break;
            }
            
            rank++;
            file--;
        }
        
        rank = piece.getPosition().getRank() - 1;
        file = piece.getPosition().getFile() + 1;
        
        /* Squares on the upper-right diagonal of the Bishop. */
        for (int i = 1; i < (8 - piece.getPosition().getFile()); i++) {
            result = result | (bitmap >>> (7 * i));
            
            if (Bitboard.getBitmapAtPosition(bitmaps, (new Position(rank, file))) > 0) {
                break;
            }
            
            rank--;
            file++;
        }
        
        rank = piece.getPosition().getRank() - 1;
        file = piece.getPosition().getFile() - 1;
        
        /* Squares on the upper-left diagonal of the Bishop. */
        for (int i = 1; i < (piece.getPosition().getFile() + 1); i++) {
            result = result | (bitmap >>> (9 * i));
            
            if (Bitboard.getBitmapAtPosition(bitmaps, (new Position(rank, file))) > 0) {
                break;
            }
            
            rank--;
            file--;
        }
        
        return result;
    }
}
