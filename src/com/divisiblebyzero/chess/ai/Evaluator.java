package com.divisiblebyzero.chess.ai;

//
// Evaluator.java
// Ada Chess
//
// Created by Eric Czarny on October 28, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Position;

public class Evaluator {
    
    private static class Regions {
        public static long CENTER = 0x0000001818000000L;
    }
    
    private static class Strengths {
        
        private static final int[][][] Pawn = {
            {
                { /* Black */
                     0,  0,  0,  0,  0,  0,  0,  0
                }, {
                    10, 16, 24, 32, 32, 24, 16, 10
                }, {
                    10, 16, 24, 32, 32, 24, 16, 10
                }, {
                     6, 12, 18, 28, 28, 16, 12,  6
                }, {
                     4,  8, 18, 26, 26, 16,  8,  4
                }, {
                     4,  6, 12, 12, 12,  4,  6,  4
                }, {
                     2,  4,  4,  0,  0,  4,  4,  2
                }, {
                     0,  0,  0,  0,  0,  0,  0,  0
                }
            }, {
                { /* White */
                     0,  0,  0,  0,  0,  0,  0,  0
                }, {
                     2,  4,  4,  0,  0,  4,  4,  2
                }, {
                     4,  6, 12, 12, 12,  4,  6,  4
                }, {
                     4,  8, 18, 26, 26, 16,  8,  4
                }, {
                     6, 12, 18, 28, 28, 16, 12,  6
                }, {
                    10, 16, 24, 32, 32, 24, 16, 10
                }, {
                    10, 16, 24, 32, 32, 24, 16, 10
                }, {
                     0,  0,  0,  0,  0,  0,  0,  0
                }
            }
        };
        
        private static final int[][][] Knight = {
            {
                { /* Black */
                    -8, -4,  2,  4,  4,  2, -4, -8
                }, {
                     2,  6, 14, 20, 20, 14,  6,  2
                }, {
                     6, 14, 28, 32, 32, 28, 14,  6
                }, {
                     8, 18, 30, 32, 32, 30, 18,  8
                }, {
                     8, 18, 26, 30, 30, 26, 18,  8
                }, {
                     6, 14, 24, 22, 22, 24, 14,  6
                }, {
                     2,  6, 14, 20, 20, 14,  6,  2
                }, {
                    -8, -4,  2,  4,  4,  2, -4, -8
                }
            }, {
              { /* White */
                    -8, -4,  2,  4,  4,  2, -4, -8
                }, {
                     2,  6, 14, 20, 20, 14,  6,  2
                }, {
                     6, 14, 24, 22, 22, 24, 14,  6
                }, {
                     8, 18, 26, 30, 30, 26, 18,  8
                }, {
                     8, 18, 30, 32, 32, 30, 18,  8
                }, {
                     6, 14, 28, 32, 32, 28, 14,  6
                }, {
                     2,  6, 14, 20, 20, 14,  6,  2
                }, {
                    -8, -4,  2,  4,  4,  2, -4, -8
                }
            }
        };
        
        private static final int[][][] Bishop = {
            {
                { /* Black */
                    16, 16, 16, 16, 16, 16, 16, 16
                }, {
                    16, 30, 32, 32, 32, 32, 30, 16
                }, {
                    16, 28, 32, 32, 32, 32, 28, 16
                }, {
                    16, 26, 32, 32, 32, 32, 26, 16
                }, {
                    16, 26, 32, 32, 32, 32, 26, 16
                }, {
                    26, 28, 32, 32, 32, 32, 28, 26
                }, {
                    26, 30, 32, 32, 32, 30, 29, 26
                }, {
                    16, 16, 16, 16, 16, 16, 16, 16
                }
            }, {
                { /* White */
                    16, 16, 16, 16, 16, 16, 16, 16
                }, {
                    26, 30, 32, 32, 32, 30, 29, 26
                }, {
                    26, 28, 32, 32, 32, 32, 28, 26
                }, {
                    16, 26, 32, 32, 32, 32, 26, 16
                }, {
                    16, 26, 32, 32, 32, 32, 26, 16
                }, {
                    16, 28, 32, 32, 32, 32, 28, 16
                }, {
                    16, 30, 32, 32, 32, 32, 30, 16
                }, {
                    16, 16, 16, 16, 16, 16, 16, 16
                }
            }
        };
        
        private static final int[][][] Rook = {
            {
                { /* Black */
                     0,  0,  0,  0,  0,  0,  0,  0
                }, {
                    10, 10, 10, 10, 10, 10, 10, 10
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                     0,  0,  0,  3,  3,  0,  0,  0
                }
            }, {
                { /* White */
                     0,  0,  0,  3,  3,  0,  0,  0
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    -2,  0,  0,  0,  0,  0,  0, -2
                }, {
                    10, 10, 10, 10, 10, 10, 10, 10
                }, {
                     0,  0,  0,  0,  0,  0,  0,  0
                }
            }
        };
        
        private static final int[][][] Queen = {
            {
                { /* Black */
                    -2, -2,  0,  0,  0,  0,  0,  0
                }, {
                    -2, -2,  0,  0,  0,  0,  0,  0
                }, {
                    -2, -2,  0,  0,  0,  0,  0,  0
                }, {
                     0,  0,  0,  4,  4,  0,  0,  0
                }, {
                     0,  0,  0,  4,  4,  0,  0,  0
                }, {
                     0,  2,  2,  2,  2,  0,  0,  0
                }, {
                     0,  0,  2,  2,  2,  0,  0,  0
                }, {
                    -2, -2, -2,  0,  0, -2, -2, -2
                }
            }, {
                { /* White */
                    -2, -2, -2,  0,  0, -2, -2, -2
                }, {
                     0,  0,  2,  2,  2,  0,  0,  0
                }, {
                     0,  2,  2,  2,  2,  0,  0,  0
                }, {
                     0,  0,  0,  4,  4,  0,  0,  0
                }, {
                     0,  0,  0,  4,  4,  0,  0,  0
                }, {
                    -2, -2,  0,  0,  0,  0,  0,  0
                }, {
                    -2, -2,  0,  0,  0,  0,  0,  0
                }, {
                    -2, -2,  0,  0,  0,  0,  0,  0
                }
            }
        };
        
        private static final int[][][] King = {
            {
                { /* Black */
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                    -8, -16, -16, -16, -16, -16, -16,  -8
                }, {
                    -6,  -6, -12, -12, -12, -12,  -6,  -6
                }, {
                     4,   4,   8, -12,  -8, -12,  10,   6
                }
            }, {
                { /* White */
                     4,   4,   8, -12,  -8, -12,  10,   6
                }, {
                    -6,  -6, -12, -12, -12, -12,  -6,  -6
                }, {
                    -8, -16, -16, -16, -16, -16, -16,  -8
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }, {
                   -20, -20, -20, -20, -20, -20, -20, -20
                }
            }
        };
    }
    
    public static boolean isCheck(long[][] bitboard, int color) {
        long king = Bitboard.getBitmap(bitboard, color, Piece.Type.KING);
        long result = 0;
        
        if (color == Piece.Color.WHITE) {
            color = Piece.Color.BLACK;
        } else {
            color = Piece.Color.WHITE;
        }
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
            	Position position = new Position(i, j);
            	Piece current = null;
            	
            	if (!Bitboard.isPositionOccupied(bitboard, position)) {
            		continue;
            	}
            	
            	current = Bitboard.getPieceAtPosition(bitboard, position);
                
                if (current.getColor() == color) {
                    result = king & Bitboard.getAttackBitmap(bitboard, current);
                    
                    if (result != 0) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public static int getMaterialValue(Piece piece) {
        int result = 0;
        
        if (piece != null) {
            switch (piece.getType()) {
                case Piece.Type.PAWN:
                    result = 250;
                    
                    break;
                case Piece.Type.KNIGHT:
                    result = 500;
                    
                    break;
                case Piece.Type.BISHOP:
                    result = 650;
                    
                    break;
                case Piece.Type.ROOK:
                    result = 750;
                    
                    break;
                case Piece.Type.QUEEN:
                    result = 900;
                    
                    break;
                case Piece.Type.KING:
                    result = 1000;
                    
                    break;
                default:
                    break;
            }
        }
        
        return result;
    }
    
    public static int evaluate(long[][] bitboard, int color) {
        int result = 0;
        
        /* if (Evaluator.isCheck(bitboard, color)) {
            return 0;
        } */
        
        /* Compute score on material and piece positioning. */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = Bitboard.getPieceAtPosition(bitboard, new Position(i, j));
                
                if ((current != null) && (current.getColor() == color)) {
                    int value = Evaluator.getMaterialValue(current);
                    
                    switch (current.getType()) {
                        case Piece.Type.PAWN:
                            value = value + (Strengths.Pawn[color][i][j] * 100);
                            
                            break;
                        case Piece.Type.KNIGHT:
                            value = value + (Strengths.Knight[color][i][j] * 100);
                            
                            break;
                        case Piece.Type.BISHOP:
                            value = value + (Strengths.Bishop[color][i][j] * 100);
                            
                            break;
                        case Piece.Type.ROOK:
                            value = value + (Strengths.Rook[color][i][j] * 100);
                            
                            break;
                        case Piece.Type.QUEEN:
                            value = value + (Strengths.Queen[color][i][j] * 100);
                            
                            break;
                        default:
                            value = value + (Strengths.King[color][i][j] * 100);
                        
                            break;
                    }
                    
                    /* Control of center bonus. */
                    if ((Bitboard.getAttackBitmap(bitboard, current) & Regions.CENTER) != 0) {
                        value = value + 1000;
                    }
                    
                    result = result + value;
                }
            }
        }
        
        return result;
    }
}
