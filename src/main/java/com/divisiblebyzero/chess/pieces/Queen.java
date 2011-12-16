package com.divisiblebyzero.chess.pieces;

import com.divisiblebyzero.chess.Piece;

public class Queen extends Piece {
    private static final long serialVersionUID = 808262123282008413L;

    public static long getAttackBitmap(long[][] bitboard, Piece piece) {
        return Rook.getAttackBitmap(bitboard, piece) | Bishop.getAttackBitmap(bitboard, piece);
    }
}
