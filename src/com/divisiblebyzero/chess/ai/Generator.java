package com.divisiblebyzero.chess.ai;

//
//  chess.ai.Generator.java
//  Ada Chess
//
//  Created by Eric Czarny on November 6, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.component.Board;
import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Moves;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

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
