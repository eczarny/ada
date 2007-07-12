package com.divisiblebyzero.chess.models.pieces;

//
//  chess.models.pieces.Rook.java
//  Ada Chess
//
//  Created by Eric Czarny on March 19, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.models.*;

public class Rook extends Piece {
	public static long getAttackBitmap(Piece piece, Bitboard bitboard) {
		long bitmap = bitboard.getBitmapAtPosition(piece.getPosition());
		long result = 0;

		int rank = piece.getPosition().getRank() + 1;
		int file = piece.getPosition().getFile();

		/* The set of squares, below the Rook, along its rank. */
		for (int i = 1; i < (8 - piece.getPosition().getRank()); i++) {
			result = result | (bitmap << (8 * i));

			if (bitboard.getBitmapAtPosition((new Position(rank, file))) > 0) {
				break;
			}

			rank++;
		}

		rank = piece.getPosition().getRank() - 1;
		file = piece.getPosition().getFile();

		/* The set of squares, above the Rook, along its rank. */
		for (int i = 1; i < (piece.getPosition().getRank() + 1); i++) {
			result = result | (bitmap >>> (8 * i));

			if (bitboard.getBitmapAtPosition((new Position(rank, file))) > 0) {
				break;
			}

			rank--;
		}

		rank = piece.getPosition().getRank();
		file = piece.getPosition().getFile() + 1;

		/* The set of squares, right of the Rook, along its file. */
		for (int i = 1; i < (8 - piece.getPosition().getFile()); i++) {
			result = result | (bitmap << (1 * i));

			if (bitboard.getBitmapAtPosition((new Position(rank, file))) > 0) {
				break;
			}

			file++;
		}

		rank = piece.getPosition().getRank();
		file = piece.getPosition().getFile() - 1;

		/* The set of squares, left of the Rook, along its file. */
		for (int i = 1; i < (piece.getPosition().getFile() + 1); i++) {
			result = result | (bitmap >>> (1 * i));

			if (bitboard.getBitmapAtPosition((new Position(rank, file))) > 0) {
				break;
			}

			file--;
		}

		return result;
	}
}