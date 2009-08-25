package com.divisiblebyzero.chess.pieces;

//
// chess.pieces.Rook.java
// Ada Chess
//
// Created by Eric Czarny on March 19, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class Rook extends Piece {
    private static final long serialVersionUID = -5523276415522998967L;

	public static long getAttackBitmap(long[][] bitmaps, Piece piece) {
        long bitmap = Bitboard.getBitmapAtPosition(bitmaps, piece.getPosition());
        long result = 0;
        
        int rank = piece.getPosition().getRank() + 1;
        int file = piece.getPosition().getFile();
        
        /* The set of squares, below the Rook, along its rank. */
        for (int i = 1; i < (8 - piece.getPosition().getRank()); i++) {
            result = result | (bitmap << (8 * i));
            
            if (Bitboard.isPositionOccupied(bitmaps, new Position(rank, file))) {
                break;
            }
            
            rank++;
        }
        
        rank = piece.getPosition().getRank() - 1;
        file = piece.getPosition().getFile();
        
        /* The set of squares, above the Rook, along its rank. */
        for (int i = 1; i < (piece.getPosition().getRank() + 1); i++) {
            result = result | (bitmap >>> (8 * i));
            
            if (Bitboard.isPositionOccupied(bitmaps, new Position(rank, file))) {
                break;
            }
            
            rank--;
        }
        
        rank = piece.getPosition().getRank();
        file = piece.getPosition().getFile() + 1;
        
        /* The set of squares, right of the Rook, along its file. */
        for (int i = 1; i < (8 - piece.getPosition().getFile()); i++) {
            result = result | (bitmap << (1 * i));
            
            if (Bitboard.isPositionOccupied(bitmaps, new Position(rank, file))) {
                break;
            }
            
            file++;
        }
        
        rank = piece.getPosition().getRank();
        file = piece.getPosition().getFile() - 1;
        
        /* The set of squares, left of the Rook, along its file. */
        for (int i = 1; i < (piece.getPosition().getFile() + 1); i++) {
            result = result | (bitmap >>> (1 * i));
            
            if (Bitboard.isPositionOccupied(bitmaps, new Position(rank, file))) {
                break;
            }
            
            file--;
        }
        
        return result;
    }
}
