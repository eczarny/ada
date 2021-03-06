package com.divisiblebyzero.chess.pieces;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;

public class Knight extends Piece {
    private static final long serialVersionUID = -8631562924470037688L;

    public static long getAttackBitmap(long[][] bitboard, Piece piece) {
        long bitmap = Bitboard.getBitmapAtPosition(bitboard, piece.getPosition());
        long result = 0;
        
        /* The set of squares below the Knight. */
        result = result | ((bitmap <<  10) & ~(Bitboard.getFile(Bitboard.File.A) | Bitboard.getFile(Bitboard.File.B)));
        result = result | ((bitmap <<  17) & ~(Bitboard.getFile(Bitboard.File.A)));
        result = result | ((bitmap <<   6) & ~(Bitboard.getFile(Bitboard.File.G) | Bitboard.getFile(Bitboard.File.H)));
        result = result | ((bitmap <<  15) & ~(Bitboard.getFile(Bitboard.File.H)));
        
        /* The set of squares above the Knight. */
        result = result | ((bitmap >>> 10) & ~(Bitboard.getFile(Bitboard.File.G) | Bitboard.getFile(Bitboard.File.H)));
        result = result | ((bitmap >>> 17) & ~(Bitboard.getFile(Bitboard.File.H)));
        result = result | ((bitmap >>>  6) & ~(Bitboard.getFile(Bitboard.File.A) | Bitboard.getFile(Bitboard.File.B)));
        result = result | ((bitmap >>> 15) & ~(Bitboard.getFile(Bitboard.File.A)));
        
        return result;
    }
}
