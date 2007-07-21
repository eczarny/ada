package com.divisiblebyzero.chess.model.pieces;

//
//  chess.model.pieces.Knight.java
//  Ada Chess
//
//  Created by Eric Czarny on March 19, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.model.*;

public class Knight extends Piece {
	public static long getAttackBitmap(Piece piece, Bitboard bitboard) {
		long bitmap = bitboard.getBitmapAtPosition(piece.getPosition());
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
