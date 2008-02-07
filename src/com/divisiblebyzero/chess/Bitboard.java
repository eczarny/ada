package com.divisiblebyzero.chess;

//
//  chess.Bitboard.java
//  Ada Chess
//
//  Created by Eric Czarny on February 26, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.pieces.*;

public class Bitboard {
	private long[][] bitmaps;
	private long[] masks;
	
	/* Letter representations of board's files. */
	public static class File {
		public static final int A = 0;
		public static final int B = 1;
		public static final int C = 2;
		public static final int D = 3;
		public static final int E = 4;
		public static final int F = 5;
		public static final int G = 6;
		public static final int H = 7;
	}
	
	/* All of the important bitmaps used by the Bitboard. */
	private static class Bitmaps {
		/* Bitmaps for white pieces */
		private static final long[] W_PIECES = {
				0x1000000000000000L, /* KING   */
				0x0800000000000000L, /* QUEEN  */
				0x8100000000000000L, /* ROOK   */
				0x2400000000000000L, /* BISHOP */
				0x4200000000000000L, /* KNIGHT */
				0x00FF000000000000L, /* PAWN   */
		};
		
		/* Bitmaps for black pieces */
		private static final long[] B_PIECES = {
				0x0000000000000010L, /* KING   */
				0x0000000000000008L, /* QUEEN  */
				0x0000000000000081L, /* ROOK   */
				0x0000000000000024L, /* BISHOP */
				0x0000000000000042L, /* KNIGHT */
				0x000000000000FF00L, /* PAWN   */
		};
		
		/* Bitmaps for board ranks */
		private static final long[] RANKS = {
				0xFF00000000000000L, /* 1 */
				0x00FF000000000000L, /* 2 */
				0x0000FF0000000000L, /* 3 */
				0x000000FF00000000L, /* 4 */
				0x00000000FF000000L, /* 5 */
				0x0000000000FF0000L, /* 6 */
				0x000000000000FF00L, /* 7 */
				0x00000000000000FFL, /* 8 */
		};
		
		/* Bitmaps for board files */
		private static final long[] FILES = {
				0x0101010101010101L, /* A */
				0x0202020202020202L, /* B */
				0x0404040404040404L, /* C */
				0x0808080808080808L, /* D */
				0x1010101010101010L, /* E */
				0x2020202020202020L, /* F */
				0x4040404040404040L, /* G */
				0x8080808080808080L, /* H */
		};
	}
	
	public Bitboard() {
		this.masks = new long[64];
		
		for (int i = 0; i < this.masks.length; i++) {
			this.masks[i] = 1L << i;
		}
		
		this.initialize();
	}
	
	private void initialize() {
		this.bitmaps = new long[6][2];
		
		for (int i = 0; i < this.bitmaps.length; i++) {
			this.bitmaps[i][Piece.WHITE] = Bitmaps.W_PIECES[i];
			this.bitmaps[i][Piece.BLACK] = Bitmaps.B_PIECES[i];
		}
	}
	
	public synchronized long getBitmap() {
		long result = 0;
		
		for (int i = 0; i < this.bitmaps.length; i++) {
			result = result | this.bitmaps[i][Piece.WHITE] | this.bitmaps[i][Piece.BLACK];
		}
		
		return result;
	}
	
	public synchronized long getBitmap(int color, int type) {
		return this.bitmaps[type][color];
	}
	
	public synchronized long getBitmap(Piece piece) {
		return this.getBitmap(piece.getColor(), piece.getType());
	}
	
	public synchronized long getBitmap(int color) {
		long result = 0;
		
		for (int i = 0; i < this.bitmaps.length; i++) {
			result = result | this.bitmaps[i][color];
		}
		
		return result;
	}
	
	public synchronized long getBitmapAtPosition(Position position) {
		long result = 0;
		
		if ((getIndexAtPosition(position) < 64) && (getIndexAtPosition(position) > -1)) {
			result = this.getBitmap() & this.masks[getIndexAtPosition(position)];
		}
		
		return result;
	}
	
	public synchronized long getBitmapAtPosition(int color, Position position) {
		long result = 0;
		
		if ((getIndexAtPosition(position) < 64) && (getIndexAtPosition(position) > -1)) {
			result = this.getBitmap(color) & this.masks[getIndexAtPosition(position)];
		}
		
		return result;
	}
	
	public synchronized static Position getPositionFromBitmap(long bitmap) {
		Position result = new Position(0, 0);
		
		for (int i = 1; i < 65; i++) {
			if ((bitmap & 1) == 1) {
				result.setFile((i - 1) % 8);
				
				break;
			}
			
			if ((i % 8) == 0) {
				result.setRank(result.getRank() + 1);
			}
			
			bitmap = bitmap >>> 1;
		}
		
		return result;
	}
	
	public synchronized long getAttackBitmap(Piece piece) {
		long result = 0;
		
		if (piece == null) {
			return result;
		}
		
		/* What piece are we looking for? */
		switch (piece.getType()) {
			case Piece.PAWN:
				result = Pawn.getAttackBitmap(piece, this);
				break;
			case Piece.KNIGHT:
				result = Knight.getAttackBitmap(piece, this);
				break;
			case Piece.BISHOP:
				result = Bishop.getAttackBitmap(piece, this);
				break;
			case Piece.ROOK:
				result = Rook.getAttackBitmap(piece, this);
				break;
			case Piece.QUEEN:
				result = Queen.getAttackBitmap(piece, this);
				break;
			case Piece.KING:
				result = King.getAttackBitmap(piece, this);
				break;
		}
		
		/* Blocks squares held by a piece of the same color. */
		result = result & ~this.getBitmap(piece.getColor());
		
		return result;
	}
	
	public static long getFile(int file) {
		return Bitmaps.FILES[file];
	}
	
	public static long getRank(int rank) {
		return Bitmaps.RANKS[rank];
	}
	
	public long getMaskAtPosition(Position position) {
		return this.masks[getIndexAtPosition(position)];
	}
	
	public synchronized void setPieceAtPosition(Piece piece, Position position) {
		this.bitmaps[piece.getType()][piece.getColor()] = this.getBitmap(piece) | this.getMaskAtPosition(position);
	}
	
	public synchronized void unsetPieceAtPosition(Piece piece, Position position) {
		this.bitmaps[piece.getType()][piece.getColor()] = this.getBitmap(piece) & ~this.getMaskAtPosition(position);
	}
	
	public static boolean isPositionAttacked(long bitmap, Position position) {
		return ((bitmap >>> (((position.getRank() * 8) + position.getFile())) & 1) == 1);
	}
	
	public static String getString(long bitmap) {
	    String result = "";
	    
	    for (int i = 1; i < 65; i++) {
	    	if ((bitmap & 1) == 1) {
	    		result = result + "1";
			} else {
	    		result = result + "0";
			}
	    	
			if ((i % 8) == 0) {
				result = result + "\n";
			}
			
			bitmap = bitmap >>> 1;
	    }
	    
	    return result;
	}
	
	public static int getIndexAtPosition(Position position) {
		return (position.getRank() << 3) + position.getFile();
	}
}
