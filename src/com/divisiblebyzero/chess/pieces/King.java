package com.divisiblebyzero.chess.pieces;

//
// chess.pieces.King.java
// Ada Chess
//
// Created by Eric Czarny on March 19, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;

public class King extends Piece {
    private static final long serialVersionUID = 8277991081370155149L;

	public static long getAttackBitmap(long[][] bitmaps, Piece piece) {
        long bitmap = Bitboard.getBitmapAtPosition(bitmaps, piece.getPosition());
        long result = 0;
        
        /* The squares directly above, and below, the King. */
        result = result | (bitmap << 8) | (bitmap >>> 8);
        
        /* The three squares to the right of the King. */
        result = result | ((bitmap <<  1) & ~Bitboard.getFile(Bitboard.File.A));
        result = result | ((bitmap <<  9) & ~Bitboard.getFile(Bitboard.File.A));
        result = result | ((bitmap <<  7) & ~Bitboard.getFile(Bitboard.File.H));
        
        /* The three squares to the left of the King. */
        result = result | ((bitmap >>> 1) & ~Bitboard.getFile(Bitboard.File.H));
        result = result | ((bitmap >>> 9) & ~Bitboard.getFile(Bitboard.File.H));
        result = result | ((bitmap >>> 7) & ~Bitboard.getFile(Bitboard.File.A));
        
        return result;
    }
}
