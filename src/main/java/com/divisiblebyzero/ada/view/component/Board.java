package com.divisiblebyzero.ada.view.component;

//
// Board.java
// Ada Chess
//
// Created by Eric Czarny on March 17, 2006.
// Copyright 2010 Divisible by Zero. All rights reserved.
//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.Ada;
import com.divisiblebyzero.ada.player.Player;
import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Pieces;
import com.divisiblebyzero.chess.Position;
import com.divisiblebyzero.chess.Square;
import com.divisiblebyzero.chess.ai.Evaluator;

public class Board extends JPanel {
    private static final long serialVersionUID = -4785349736776306753L;
    
    private Ada ada;
    private long[][] bitboard;
    private Square[][] squares;
    private Pieces pieces;
    private Controller controller;
    private int color;
    private int state;
    
    private static Logger logger = Logger.getLogger(Board.class);
    
    /* Possible states of the board. */
    public static final int UNDECIDED = 0;
    public static final int CHECK     = 1;
    public static final int CHECKMATE = 2;
    
    /* Board lock object, nothing really special. */
    private static class Lock {
        private static boolean locked;
        private static int color;
    }
    
    public Board(Ada ada) {
        this.ada      = ada;
        this.bitboard = Bitboard.generateBitboard();
        this.pieces   = new Pieces();
        this.color    = Piece.Color.WHITE;
        this.state    = UNDECIDED;
        
        this.initialize();
    }
    
    private void initialize() {
        this.controller = new Controller(this);
        
        /* Add the mouse action listeners... */
        this.addMouseListener(this.controller);
        this.addMouseMotionListener(this.controller);
        
        this.squares = new Square[8][8];
        
        logger.info("Constructing the underlying board.");
        
        /* construct a new board, no pieces */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2) == 0) {
                    if ((j % 2) == 0) {
                        this.squares[i][j] = new Square(Piece.Color.WHITE, new Position(i, j));
                    } else {
                        this.squares[i][j] = new Square(Piece.Color.BLACK, new Position(i, j));
                    }
                } else {
                    if ((j % 2) == 0) {
                        this.squares[i][j] = new Square(Piece.Color.BLACK, new Position(i, j));
                    } else {
                        this.squares[i][j] = new Square(Piece.Color.WHITE, new Position(i, j));
                    }
                }
            }
        }
        
        /* load the pieces, caching their respective images */
        this.pieces.load(new MediaTracker(this));
        
        /* okay, reset to a playable board */
        this.reset();
    }
    
    private void reset() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0) {
                    this.squares[i][j].setPiece(this.pieces.getPiece(Piece.Color.BLACK, j));
                } else if (i == 1) {
                    this.squares[i][j].setPiece(this.pieces.getPiece(Piece.Color.BLACK, j + 8));
                }
                
                if (i == 7) {
                    this.squares[i][j].setPiece(this.pieces.getPiece(Piece.Color.WHITE, j));
                } else if (i == 6) {
                    this.squares[i][j].setPiece(this.pieces.getPiece(Piece.Color.WHITE, j + 8));
                }
                
                if (this.squares[i][j].getPiece() != null) {
                    this.squares[i][j].getPiece().setPosition(new Position(i, j));
                }
            }
        }
        
        /* Make sure that white can move first. */
        this.isLocked(Piece.Color.BLACK);
    }
    
    private static int getCoordinate(int offset) {
        return 15 + ((Square.SIZE * (offset + 1)) - Square.SIZE);
    }
    
    private static int getRankOrFile(int coordinate) {
        return ((coordinate + (Square.SIZE - 10)) / Square.SIZE) - 1;
    }
    
    private static Position getPosition(int x, int y) {
        return new Position(getRankOrFile(y), getRankOrFile(x));
    }
    
    public boolean isLocked() {
        return Lock.locked && (Lock.color == -1);
    }
    
    public boolean isColorLocked(int color) {
        return Lock.locked && (Lock.color == color);
    }
    
    public void isLocked(boolean locked) {
        Lock.locked = locked;
        Lock.color  = -1;
    }
    
    public void isLocked(int color) {
        Lock.locked = true;
        Lock.color  = color;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public int getState() {
        return this.state;
    }
    
    public long[][] getBitboard() {
        return this.bitboard;
    }
    
    public Piece getPieceAtPosition(Position position) {
        return this.squares[position.getRank()][position.getFile()].getPiece();
    }
    
    public Piece getPiece(int rank, int file) {
        return this.squares[rank][file].getPiece();
    }
    
    public void setPieceAtPosition(Piece piece, Position position) {
        squares[position.getRank()][position.getFile()].setPiece(piece);
    }
    
    public void unsetPieceAtPosition(Position position) {
        squares[position.getRank()][position.getFile()].setPiece(null);
    }
    
    private int getColor() {
        return this.color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public void makeMove(Move move) {
        Position x = move.getX();
        Position y = move.getY();
        
        /* Get the piece being moved. */
        Piece piece = this.squares[x.getRank()][x.getFile()].getPiece();
        
        /* Remember any piece that may be captured by making this move. */
        Piece capturedPiece = squares[y.getRank()][y.getFile()].getPiece();
        
        /* Are we capturing a piece? If so, remove it from the bitboard. */
        if (capturedPiece != null) {
            this.bitboard = Bitboard.unsetPieceAtPosition(this.bitboard, capturedPiece, y);
        }
        
        /* Remove the piece from the bitboard... */
        this.bitboard = Bitboard.unsetPieceAtPosition(this.bitboard, piece, x);
        
        /* And place it at the new position on the bitboard. */
        this.bitboard = Bitboard.setPieceAtPosition(this.bitboard, piece, y);
        
        if (Evaluator.isCheck(this.bitboard, this.getColor())) {
            Board.this.setState(Board.CHECK);
            
            /* Remove the piece from its new position on the bitboard... */
            this.bitboard = Bitboard.unsetPieceAtPosition(this.bitboard, piece, y);
            
            /* And place it as its old position. */
            this.bitboard = Bitboard.setPieceAtPosition(this.bitboard, piece, x);
            
            /* If we captured a piece we should put it back. */
            if (capturedPiece != null) {
                this.bitboard = Bitboard.setPieceAtPosition(this.bitboard, capturedPiece, y);
            }
            
            return;
        } else {
            Board.this.setState(Board.UNDECIDED);
        }
        
        /* Pick up the piece, removing it from old position. */
        this.unsetPieceAtPosition(x);
        
        /* Let the piece know we moved... */
        piece.setPosition(y);
        
        /* Move the piece to new square. */
        this.setPieceAtPosition(piece, y);
        
        /* Highlight the prior move... */
        this.squares[x.getRank()][x.getFile()].setHighlighted(true);
        this.squares[y.getRank()][y.getFile()].setHighlighted(true);
        
        this.repaint();
        
        /* Toggle control of the board. */
        if (this.getColor() == Piece.Color.WHITE) {
            Player currentPlayer = this.ada.getBlackPlayer();
            
        	this.isLocked(true);
            
            this.setColor(Piece.Color.BLACK);
            
            currentPlayer.makeMove(this, Piece.Color.BLACK);
        } else {
            Player currentPlayer = this.ada.getWhitePlayer();
            
            this.isLocked(Piece.Color.BLACK);
            
            this.setColor(Piece.Color.WHITE);
            
            currentPlayer.makeMove(this, Piece.Color.WHITE);
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        /* Use the bitboard to discover all legal moves for the selected piece. */
        long attacks = Bitboard.getAttackBitmap(this.bitboard, this.controller.getSelectedPiece());
        
        for (int i = 0; i < this.squares.length; i++) {
            for (int j = 0; j < this.squares[i].length; j++) {
                Square square = this.squares[i][j];
                Piece selectedPiece = this.controller.getSelectedPiece();
                
                g.setColor(square.getBackgroundColor());
                
                /* Draw the actual square. */
                g.fillRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE);
                
                /* If the current square is selected, highlight it. */
                if (square.isSelected()) {
                    g.draw3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE - 1, Square.SIZE - 1, true);
                }
                
                /* If the current square is a viable attack/move, highlight it. */
                if (Bitboard.isPositionAttacked(attacks, square.getPosition())) {
                    if (square.getColor() == Piece.Color.WHITE) {
                        g.setColor(new Color(128, 212, 13));
                    } else {
                        g.setColor(new Color(128, 255, 13));
                    }
                    
                    /* If an enemy piece occupies current square, highlight as attacked. */
                    if (!square.isEmpty() && (square.getPiece().getColor() != selectedPiece.getColor())) {
                        g.setColor(new Color(255, 20, 27));
                    }
                    
                    /* Draw the raised highlighted square. */
                    g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true);
                    
                    /* Mark the square as attacked. */
                    square.isAttacked(true);
                }
                
                if (square.isHovering() || square.isHighlighted()) {
                    g.setColor(new Color(58, 172, 216));
                    
                    g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true);
                    
                    square.setHighlighted(false);
                }
                
                /* Draw the piece on the board. */
                if (!square.isEmpty()) {
                    g.drawImage(square.getPiece().getImage(), getCoordinate(j), getCoordinate(i), null);
                }
            }
       }
        
        g.setColor(new Color(128, 128, 128));
        
        g.drawRect(10, 10, (Square.SIZE * 8), (Square.SIZE * 8));
    }
    
    class Controller extends JPanel implements MouseListener, MouseMotionListener {
        private static final long serialVersionUID = -2076001089265149020L;
        
        private Position position;
        private Move move;
        private Piece selectedPiece;
        
        public Controller(JPanel panel) {
            this.add(panel);
        }
        
        public Move getMove() {
            return this.move;
        }
        
        public Piece getSelectedPiece() {
            return this.selectedPiece;
        }
        
        public void mouseClicked(MouseEvent event) {
            
        }
        
        public void mousePressed(MouseEvent event) {
            int x = getRankOrFile(event.getY());
            int y = getRankOrFile(event.getX());
            
            /* We haven't made a move yet, reflect it. */
            this.move = null;
            
            if (!Board.this.isLocked()) {
                if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
                    if (Board.this.squares[x][y].getPiece() != null) {
                        if (!Board.this.isColorLocked(Board.this.squares[x][y].getPiece().getColor())) {
                            this.position = Board.getPosition(event.getX(), event.getY());
                            
                            /* Select the current square, show the selection. */
                            Board.this.squares[x][y].setSelected(true);
                            
                            /* Set the currently selected piece. */
                            this.selectedPiece = Board.this.squares[x][y].getPiece();
                            
                            /* Reset previously highlighted squares. */
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    Board.this.squares[i][j].setHovering(false);
                                }
                            }
                            
                            /* Now highlight the currently selected piece. */
                            Board.this.squares[x][y].setHovering(true);
                            
                            /* Display the changes... */
                            Board.this.repaint();
                        }
                    }
                }
            }
        }
        
        public void mouseReleased(MouseEvent event) {
            int x = getRankOrFile(event.getY());
            int y = getRankOrFile(event.getX());
            
            if ((!Board.this.isLocked()) && (Board.this.getState() != CHECKMATE)) {
                if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
                    if ((this.selectedPiece != null) && (Board.this.squares[x][y].isAttacked())) {
                        this.move = new Move(this.position, new Position(x, y));
                        
                        /* Make the move. */
                        Board.this.makeMove(this.move);
                    } else {
                        logger.info("No move is available, mouse event ignored.");
                        
                        /* Reset all selected squares. Kind of ugly... */
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                Board.this.squares[i][j].setHovering(false);
                            }
                        }
                        
                        /* Display the changes... */
                        Board.this.repaint();
                    }
                }
            }
            
            /* Always make sure we aren't holding a piece. */
            this.selectedPiece = null;
            
            /* Hide the selection, if one was made. */
            if (this.position != null) {
                squares[this.position.getRank()][this.position.getFile()].setSelected(false);
            }
            
            /* Also, it would be a good idea to release the focused position. */
            this.position = null;
            
            /* Reset all attacked squares. Kind of ugly... */
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Board.this.squares[i][j].isAttacked(false);
                }
            }
            
            /* Display the changes... */
            Board.this.repaint();
        }
        
        public void mouseEntered(MouseEvent event) {
            
        }
        
        public void mouseExited(MouseEvent event) {
            
        }
        
        public void mouseDragged(MouseEvent event) {
            int x = getRankOrFile(event.getY());
            int y = getRankOrFile(event.getX());
            
            /* Reset all selected squares. Kind of ugly...*/
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Board.this.squares[i][j].setHovering(false);
                }
            }
            
            if (this.selectedPiece != null) {
                if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
                    Board.this.squares[x][y].setHovering(true);
                }
            }
            
            /* Display the changes... */
            Board.this.repaint();
        }
        
        public void mouseMoved(MouseEvent event) {
            
        }
    }
}
