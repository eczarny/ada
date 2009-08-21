package com.divisiblebyzero.chess.pieces;

//
// chess.pieces.Queen.java
// Ada Chess
//
// Created by Eric Czarny on March 19, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Piece;

public class Queen extends Piece {
    private static final long serialVersionUID = 808262123282008413L;

	public static long getAttackBitmap(long[][] bitmaps, Piece piece) {
        return Rook.getAttackBitmap(bitmaps, piece) | Bishop.getAttackBitmap(bitmaps, piece);
    }
}
