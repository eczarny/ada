package com.divisiblebyzero.chess.model.pieces;

//
//  chess.model.pieces.Pawn.java
//  Ada Chess
//
//  Created by Eric Czarny on March 19, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.model.*;

public class Pawn extends Piece {
	public static long getAttackBitmap(Piece piece, Bitboard bitboard) {
		long bitmap = bitboard.getBitmapAtPosition(piece.getPosition());
		long result;
		
		/* For Pawns we need to know their color. */
		if (piece.getColor() == Piece.WHITE) {
			int rank = piece.getPosition().getRank();
			int file = piece.getPosition().getFile();
			
			/* If at a position of origin, allow the Pawn to advance two squares. */
			if (Bitboard.getPositionFromBitmap(bitmap).getRank() == 6) {
				if (bitboard.getBitmapAtPosition(new Position(rank - 1, file)) != 0) {
					result = (bitmap >>> 8);
				} else {
					if (bitboard.getBitmapAtPosition(new Position(rank - 2, file)) != 0) {
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
			opponents = opponents | bitboard.getBitmapAtPosition(Piece.BLACK,
					new Position(rank - 1, file + 1));
			opponents = opponents | bitboard.getBitmapAtPosition(Piece.BLACK,
					new Position(rank - 1, file - 1));
			
			/* Determine whether the Pawn can advance ahead, or attack... */
			if ((opponents > 0) && (bitboard.getBitmapAtPosition(Piece.BLACK,
					new Position(rank, file - 1)) > 0)) {
				result = 0;
			}
			
			/* Make sure we can't advance when we're being blocked... */
			if (bitboard.getBitmapAtPosition(Piece.BLACK, new Position(rank - 1, file)) > 0) {
				result = 0;
			}
			
			result = result | opponents;
		} else {
			int rank = piece.getPosition().getRank();
			int file = piece.getPosition().getFile();
			
			/* If at a position of origin, allow the Pawn to advance two squares. */
			if (Bitboard.getPositionFromBitmap(bitmap).getRank() == 1) {
				if (bitboard.getBitmapAtPosition(new Position(rank + 1, file)) != 0) {
					result = (bitmap << 8);
				} else {
					if (bitboard.getBitmapAtPosition(new Position(rank + 2, file)) != 0) {
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
			opponents = opponents | bitboard.getBitmapAtPosition(Piece.WHITE,
					new Position(rank + 1, file + 1));
			opponents = opponents | bitboard.getBitmapAtPosition(Piece.WHITE,
					new Position(rank + 1, file - 1));
			
			/* Determine whether the Pawn can advance ahead, or attack... */
			if ((opponents > 0) && (bitboard.getBitmapAtPosition(Piece.WHITE,
					new Position(rank, file + 1)) > 0)) {
				result = 0;
			}
			
			/* Make sure we can't advance when we're being blocked... */
			if (bitboard.getBitmapAtPosition(Piece.WHITE, new Position(rank + 1, file)) > 0) {
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
