package com.divisiblebyzero.chess.ai;

import java.util.LinkedList;

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class MoveGenerator {
    
    public static LinkedList<Move> generateMovesForPiece(long[][] bitboard, Piece piece) {
        LinkedList<Move> result = new LinkedList<Move>();
        long bitmap;
        
        bitmap = Bitboard.getAttackBitmap(bitboard, piece);
        
        while (bitmap != 0) {
            Position destination = Bitboard.getPositionFromBitmap(bitmap);
            Move move = new Move();
            
            move.setX(piece.getPosition());
            move.setY(destination);
            
            result.add(move);
            
            bitmap = bitmap & ~Bitboard.getMaskAtPosition(destination);
        }
        
        return result;
    }
    
    public static LinkedList<Move> generateMovesForColor(long[][] bitboard, int color) {
        LinkedList<Move> result = new LinkedList<Move>();
        
        for (int i = 0; i < 6; i++) {
            long bitmap = Bitboard.getBitmap(bitboard, color, i);
            
            while (bitmap != 0) {
                Piece origin = new Piece(color, i);
                
                origin.setPosition(Bitboard.getPositionFromBitmap(bitmap));
                
                result.addAll(MoveGenerator.generateMovesForPiece(bitboard, origin));
                
                bitmap = bitmap & ~Bitboard.getMaskAtPosition(origin.getPosition());
            }
        }
        
        return result;
    }
}
