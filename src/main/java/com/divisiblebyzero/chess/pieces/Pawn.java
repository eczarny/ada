package com.divisiblebyzero.chess.pieces;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class Pawn extends Piece {
    private static final long serialVersionUID = 6721305809438603880L;

    public static long getAttackBitmap(long[][] bitboard, Piece piece) {
        long bitmap = Bitboard.getBitmapAtPosition(bitboard, piece.getPosition());
        long result;
        
        /* For Pawns we need to know their color. */
        if (piece.getColor() == Piece.Color.WHITE) {
            int rank = piece.getPosition().getRank();
            int file = piece.getPosition().getFile();
            
            /* If at a position of origin, allow the Pawn to advance two squares. */
            if (Bitboard.getPositionFromBitmap(bitmap).getRank() == 6) {
                if (Bitboard.isPositionOccupied(bitboard, new Position(rank - 1, file))) {
                    result = (bitmap >>> 8);
                } else {
                    if (Bitboard.isPositionOccupied(bitboard, new Position(rank - 2, file))) {
                        result = (bitmap >>> 8);
                    } else {
                        result = (bitmap >>> 8) | (bitmap >>> 16);
                    }
                }
            } else {
                result = (bitmap >>> 8);
            }
            
            long opponents = 0;
            
            /* Gather enemies in attackable positions... */
            opponents = opponents | Bitboard.getBitmapAtPosition(bitboard, Piece.Color.BLACK, new Position(rank - 1, file + 1));
            opponents = opponents | Bitboard.getBitmapAtPosition(bitboard, Piece.Color.BLACK, new Position(rank - 1, file - 1));
            
            /* Determine whether the Pawn can advance ahead, or attack... */
            if ((opponents > 0) && (Bitboard.getBitmapAtPosition(bitboard, Piece.Color.BLACK,
                    new Position(rank, file - 1)) > 0)) {
                result = 0;
            }
            
            /* Make sure we can't advance when we're being blocked... */
            if (Bitboard.getBitmapAtPosition(bitboard, Piece.Color.BLACK, new Position(rank - 1, file)) > 0) {
                result = 0;
            }
            
            result = result | opponents;
        } else {
            int rank = piece.getPosition().getRank();
            int file = piece.getPosition().getFile();
            
            /* If at a position of origin, allow the Pawn to advance two squares. */
            if (Bitboard.getPositionFromBitmap(bitmap).getRank() == 1) {
                if (Bitboard.isPositionOccupied(bitboard, new Position(rank + 1, file))) {
                    result = (bitmap << 8);
                } else {
                    if (Bitboard.isPositionOccupied(bitboard, new Position(rank + 2, file))) {
                        result = (bitmap << 8);
                    } else {
                        result = (bitmap << 8) | (bitmap << 16);
                    }
                }
            } else {
                result = (bitmap << 8);
            }
            
            long opponents = 0;
            
            /* Gather enemies in attackable positions... */
            opponents = opponents | Bitboard.getBitmapAtPosition(bitboard, Piece.Color.WHITE, new Position(rank + 1, file + 1));
            opponents = opponents | Bitboard.getBitmapAtPosition(bitboard, Piece.Color.WHITE, new Position(rank + 1, file - 1));
            
            /* Determine whether the Pawn can advance ahead, or attack... */
            if ((opponents > 0) && (Bitboard.getBitmapAtPosition(bitboard, Piece.Color.WHITE, new Position(rank, file + 1)) > 0)) {
                result = 0;
            }
            
            /* Make sure we can't advance when we're being blocked... */
            if (Bitboard.getBitmapAtPosition(bitboard, Piece.Color.WHITE, new Position(rank + 1, file)) > 0) {
                result = 0;
            }
            
            result = result | opponents;
        }
        
        /* Some cleaning up... Make sure we can't wrap around the board. */
        if (piece.getPosition().getFile() == Bitboard.File.A) {
            result = result & ~Bitboard.getFile(Bitboard.File.H);
        } else if (piece.getPosition().getFile() == Bitboard.File.H) {
            result = result & ~Bitboard.getFile(Bitboard.File.A);
        }
        
        return result;
    }
}
