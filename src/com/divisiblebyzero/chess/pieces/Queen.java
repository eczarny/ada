package com.divisiblebyzero.chess.pieces;

//
// Queen.java
// Ada Chess
//
// Created by Eric Czarny on March 19, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Piece;

public class Queen extends Piece {
    private static final long serialVersionUID = 808262123282008413L;

	public static long getAttackBitmap(long[][] bitboard, Piece piece) {
        return Rook.getAttackBitmap(bitboard, piece) | Bishop.getAttackBitmap(bitboard, piece);
    }
}
