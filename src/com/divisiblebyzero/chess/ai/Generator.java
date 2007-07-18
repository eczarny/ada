package com.divisiblebyzero.chess.ai;

//
//  chess.ai.Generator.java
//  Ada Chess
//
//  Created by Eric Czarny on November 6, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.ui.*;
import com.divisiblebyzero.chess.model.*;

//
//  == Move Generation
//
//    define generateLegalMoves: Bitboard board, int color
//    	define Moves as result, int as piece
//			define int[] as quantities
//
//			quantities is { 1, 1, 2, 2, 2, 8 }
//
//			for each type of Piece(i), i is 0, i to 5
//        define long as current, current is Bitboard of Piece(i) and color
//
// 				for each Piece in current Board of quantities
// 					define Piece as origin, origin is the first Piece in current
//          define long as moves
//
//					set Position of origin at next Position in current
//
//					moves is all legal moves origin can make
//
//					while moves still exist (not equal to 0)
//            define Position as destination, attack is Position from Board(attacks)
//            define Move as move
//
//            destination is Position from Board(moves)
//
//						move.setX as origin, setY as destination
//						result.insert(move)
//
//            unset move in moves
//					end while
//
//          unset Piece in current
//        end for
//			end for
//
//			return result
//    end define
//

public class Generator {
	private static int quantityOf(Board board, int type, int color) {
		int result = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece current = board.getPiece(i, j);

				if (current == null) {
					continue;
				}

				if ((current.getType() == type) && (current.getColor() == color)) {
					result++;
				}
			}
		}

		return result;
	}

	public static Moves generateLegalMoves(Board board, int color) {
		Moves result = new Moves();

		for (int i = 0; i < 6; i++) {
			long current = board.getBitboard().getBitmap(color, i);

			for (int j = 0; j < Generator.quantityOf(board, i, color); j++) {
				Piece origin = new Piece(color, i);
				long moves;

				origin.setPosition(Bitboard.getPositionFromBitmap(current));

				moves = board.getBitboard().getAttackBitmap(origin);

				while (moves != 0) {
					Position destination = Bitboard.getPositionFromBitmap(moves);
					Move move = new Move();

					move.setX(origin.getPosition());
					move.setY(destination);

					if ((move.getX().getRank() > 7) || (move.getX().getFile() > 7)) {
						continue;
					}

					if (board.getPieceAtPosition(move.getX()) != null) {
						result.insert(move);
					}

					moves = moves & ~board.getBitboard().getMaskAtPosition(destination);
				}

				if ((origin.getPosition().getRank() > 7) || (origin.getPosition().getFile() > 7)) {
					continue;
				}

				current = current & ~board.getBitboard().getMaskAtPosition(origin.getPosition());
			}
		}

		return result;
	}

	public static Moves generateDecentCaptures(Board board, int color) {
		Moves result = Generator.generateLegalMoves(board, color);

		result.reset();

		while (result.getCurrentMove() != null) {
			Move move = result.getCurrentMove();

			if (board.getPieceAtPosition(move.getY()) == null) {
				result.remove();

				continue;
			}

			result.next();
		}

		return result;
	}
}
