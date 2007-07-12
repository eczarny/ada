package com.divisiblebyzero.chess.models.pieces;

//
//  chess.models.pieces.Queen.java
//  Ada Chess
//
//  Created by Eric Czarny on March 19, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.models.*;

public class Queen extends Piece {
	public static long getAttackBitmap(Piece piece, Bitboard bitboard) {
		return Rook.getAttackBitmap(piece, bitboard) | Bishop.getAttackBitmap(piece, bitboard);
	}
}