package com.divisiblebyzero.chess.model;

//
//  chess.models.Pieces.java
//  Ada Chess
//
//  Created by Eric Czarny on March 23, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import java.awt.*;

public class Pieces {
	private Piece[][] pieces;

	/* Material values, used in evaluation. */
	public static class Values {
		public static final int KING   = 999;
		public static final int QUEEN  = 900;
		public static final int ROOK   = 500;
		public static final int BISHOP = 350;
		public static final int KNIGHT = 300;
		public static final int PAWN   = 100;
	}

	public Pieces() {
		this.pieces = new Piece[16][2];
	}

	public void load(MediaTracker tracker) {
		int[] order = {
				2, 4, 3, 1, 0, 3, 4, 2,
				5, 5, 5, 5, 5, 5, 5, 5
		};

		for (int i = 0; i < this.pieces.length; i++) {
			for (int j = 0; j < this.pieces[i].length; j++) {
				this.pieces[i][j] = new Piece(j, order[i]);

				tracker.addImage(this.pieces[i][j].getImage(), 0);

				try {
					tracker.waitForAll();
				} catch (Exception e) {
					
				}
			}
		}
	}

	public Piece getPiece(int color, int type) {
		return this.pieces[type][color];
	}
}
