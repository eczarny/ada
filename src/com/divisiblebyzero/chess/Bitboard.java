package com.divisiblebyzero.chess;

//
// Bitboard.java
// Ada Chess
//
// Created by Eric Czarny on February 26, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.io.Serializable;

import com.divisiblebyzero.chess.pieces.*;

public class Bitboard implements Serializable {
    private static final long serialVersionUID = -7010213843321142548L;
    
    private static long[] masks = null;
    
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
        private static final long[] WHITE_PIECES = {
                0x1000000000000000L, /* KING   */
                0x0800000000000000L, /* QUEEN  */
                0x8100000000000000L, /* ROOK   */
                0x2400000000000000L, /* BISHOP */
                0x4200000000000000L, /* KNIGHT */
                0x00FF000000000000L, /* PAWN   */
        };
        
        /* Bitmaps for black pieces */
        private static final long[] BLACK_PIECES = {
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
        masks = new long[64];
        
        for (int i = 0; i < masks.length; i++) {
            masks[i] = 1L << i;
        }
    }
    
    public static long[][] generateBitboard() {
        long[][] bitboard = new long[6][2];
        
        for (int i = 0; i < bitboard.length; i++) {
        	bitboard[i][Piece.Color.WHITE] = Bitmaps.WHITE_PIECES[i];
        	bitboard[i][Piece.Color.BLACK] = Bitmaps.BLACK_PIECES[i];
        }
        
        if (Bitboard.masks == null) {
            Bitboard.masks = new long[64];
            
            for (int i = 0; i < Bitboard.masks.length; i++) {
                Bitboard.masks[i] = 1L << i;
            }
        }
        
        return bitboard;
    }
    
    public static long getBitmap(long[][] bitboard) {
        long result = 0;
        
        for (int i = 0; i < bitboard.length; i++) {
            result = result | bitboard[i][Piece.Color.WHITE] | bitboard[i][Piece.Color.BLACK];
        }
        
        return result;
    }
    
    public static long getBitmap(long[][] bitboard, Piece piece) {
        return getBitmap(bitboard, piece.getColor(), piece.getType());
    }
    
    public static long getBitmap(long[][] bitboard, int color, int type) {
        return bitboard[type][color];
    }
    
    public static long getBitmap(long[][] bitboard, int color) {
        long result = 0;
        
        for (int i = 0; i < bitboard.length; i++) {
            result = result | bitboard[i][color];
        }
        
        return result;
    }
    
    public static long getBitmapAtPosition(long[][] bitboard, Position position) {
        return Bitboard.getBitmap(bitboard) & Bitboard.getMaskAtPosition(position);
    }
    
    public static long getBitmapAtPosition(long[][] bitboard, int color, Position position) {
        long result = Bitboard.getBitmap(bitboard, color) & Bitboard.getMaskAtPosition(position);
        
        return result;
    }
    
    public static Position getPositionFromBitmap(long bitmap) {
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
    
    public static int getColorFromBitmap(long[][] bitboard, long bitmap) {
        int result = -1;
        
        for (int i = 0; i < bitboard.length; i++) {
            if ((bitboard[i][Piece.Color.WHITE] & bitmap) != 0) {
                result = Piece.Color.WHITE;
                
                break;
            } else if ((bitboard[i][Piece.Color.BLACK] & bitmap) != 0) {
                result = Piece.Color.BLACK;
                
                break;
            }
        }
        
        return result;
    }
    
    public static int getTypeFromBitmap(long[][] bitboard, long bitmap) {
        int result = -1;
        
        for (int i = 0; i < bitboard.length; i++) {
            if ((bitboard[i][Piece.Color.WHITE] & bitmap) != 0) {
                result = i;
                
                break;
            } else if ((bitboard[i][Piece.Color.BLACK] & bitmap) != 0) {
                result = i;
                
                break;
            }
        }
        
        return result;
    }
    
    public static Piece getPieceAtPosition(long[][] bitboard, Position position) {
        Piece result = null;
        
        if (Bitboard.isPositionOccupied(bitboard, position)) {
        	long bitmap = Bitboard.getBitmapAtPosition(bitboard, position);
        	
            result = new Piece(Bitboard.getColorFromBitmap(bitboard, bitmap), Bitboard.getTypeFromBitmap(bitboard, bitmap));
            
            result.setPosition(position);
        }
        
        return result;
    }
    
    public static long getAttackBitmap(long[][] bitboard, Piece piece) {
        long result = 0;
        
        if (piece == null) {
            return result;
        }
        
        /* What piece are we looking for? */
        switch (piece.getType()) {
            case Piece.Type.PAWN:
                result = Pawn.getAttackBitmap(bitboard, piece);
                
                break;
            case Piece.Type.KNIGHT:
                result = Knight.getAttackBitmap(bitboard, piece);
                
                break;
            case Piece.Type.BISHOP:
                result = Bishop.getAttackBitmap(bitboard, piece);
                
                break;
            case Piece.Type.ROOK:
                result = Rook.getAttackBitmap(bitboard, piece);
                
                break;
            case Piece.Type.QUEEN:
                result = Queen.getAttackBitmap(bitboard, piece);
                
                break;
            case Piece.Type.KING:
                result = King.getAttackBitmap(bitboard, piece);
                
                break;
            default:
                break;
        }
        
        /* Blocks squares held by a piece of the same color. */
        result = result & ~Bitboard.getBitmap(bitboard, piece.getColor());
        
        return result;
    }
    
    public static long getFile(int file) {
        return Bitmaps.FILES[file];
    }
    
    public static long getRank(int rank) {
        return Bitmaps.RANKS[rank];
    }
    
    public static long getMaskAtPosition(Position position) {
    	long result = 0;
    	
    	if ((getIndexAtPosition(position) < 64) && (getIndexAtPosition(position) > -1)) {
            result = Bitboard.masks[getIndexAtPosition(position)];
        }
    	
        return result;
    }
    
    public static long[][] setPieceAtPosition(long[][] bitboard, Piece piece, Position position) {
    	bitboard[piece.getType()][piece.getColor()] = Bitboard.getBitmap(bitboard, piece) | Bitboard.getMaskAtPosition(position);
        
        return bitboard;
    }
    
    public static long[][] unsetPieceAtPosition(long[][] bitboard, Piece piece, Position position) {
    	bitboard[piece.getType()][piece.getColor()] = Bitboard.getBitmap(bitboard, piece) & ~Bitboard.getMaskAtPosition(position);
        
        return bitboard;
    }
    
    public static boolean isPositionOccupied(long[][] bitboard, Position position) {
        return (Bitboard.getBitmap(bitboard) & Bitboard.getMaskAtPosition(position)) != 0;
    }
    
    public static boolean isPositionAttacked(long bitmap, Position position) {
        return (bitmap >>> (((position.getRank() * 8) + position.getFile())) & 1) == 1;
    }
    
    public static int getIndexAtPosition(Position position) {
    	return (position.getRank() << 3) + position.getFile();
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
}
