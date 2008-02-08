package com.divisiblebyzero.ada.view.component;

//
//  ada.view.Board.java
//  Ada Chess
//
//  Created by Eric Czarny on March 17, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.view.Table;
import com.divisiblebyzero.chess.Bitboard;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Piece;
import com.divisiblebyzero.chess.Pieces;
import com.divisiblebyzero.chess.Position;
import com.divisiblebyzero.chess.Square;
import com.divisiblebyzero.chess.ai.Evaluator;
import com.divisiblebyzero.network.Notifier;

public class Board extends JPanel implements Cloneable {
	private Table table;
	private Bitboard bitboard;
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
		public static boolean locked;
		public static int color;
	}
	
	public Board(Table table) {
		this.table    = table;
		this.bitboard = new Bitboard();
		this.pieces   = new Pieces();
		this.color    = Piece.WHITE;
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
						this.squares[i][j] = new Square(new Position(i, j), Square.WHITE);
					} else {
						this.squares[i][j] = new Square(new Position(i, j), Square.BLACK);
					}
				} else {
					if ((j % 2) == 0) {
						this.squares[i][j] = new Square(new Position(i, j), Square.BLACK);
					} else {
						this.squares[i][j] = new Square(new Position(i, j), Square.WHITE);
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
					this.squares[i][j].setPiece(this.pieces.getPiece(Piece.BLACK, j));
				} else if (i == 1) {
					this.squares[i][j].setPiece(this.pieces.getPiece(Piece.BLACK, j + 8));
				}
				
				if (i == 7) {
					this.squares[i][j].setPiece(this.pieces.getPiece(Piece.WHITE, j));
				} else if (i == 6) {
					this.squares[i][j].setPiece(this.pieces.getPiece(Piece.WHITE, j + 8));
				}
				
				if (this.squares[i][j].getPiece() != null) {
					this.squares[i][j].getPiece().setPosition(new Position(i, j));
				}
			}
		}
		
		/* Make sure that white can move first. */
		this.isLocked(Piece.BLACK);
	}
	
	private static int getCoordinate(int offset) {
		return 15 + ((Square.SIZE * (offset + 1)) - Square.SIZE);
	}
	
	private static int calculateCoordinate(int coordinate) {
		return (((coordinate + (Square.SIZE - 10)) / Square.SIZE) - 1);
	}
	
	private static Position getPosition(int x, int y) {
		return new Position(calculateCoordinate(y), calculateCoordinate(x));
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
	
	public Bitboard getBitboard() {
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
	
	public void move(Move move, boolean relinquish) {
		Position x = move.getX();
		Position y = move.getY();
		
		logger.info("Moving the selected piece from " + x + " to " + y + ".");
		
		/* Get the piece being moved. */
		Piece piece = this.squares[x.getRank()][x.getFile()].getPiece();
		
		/* Pick up the piece, removing it from old position. */
		this.unsetPieceAtPosition(x);
		
		/* Let the piece know we moved... */
		piece.setPosition(y);
		
		/* Are we capturing a piece? If so, remove it from the bitboard. */
		if (squares[y.getRank()][y.getFile()].getPiece() != null) {
			bitboard.unsetPieceAtPosition(squares[y.getRank()][y.getFile()].getPiece(), y);
		}
		
		/* Move the piece to new square. */
		this.setPieceAtPosition(piece, y);
		
		/* Remove the piece from the bitboard... */
		bitboard.unsetPieceAtPosition(piece, x);
		
		/* And place it at the new position on the bitboard. */
		bitboard.setPieceAtPosition(piece, y);
		
		if (relinquish) {
			if (this.getColor() == Piece.BLACK) {
				if (Evaluator.isCheck(this, Piece.WHITE)) {
					Board.this.setState(CHECK);
				}
			}
			
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					this.squares[i][j].isHighlighted(false);
				}
			}
			
			/* Highlight the prior move... */
			this.squares[x.getRank()][x.getFile()].isHighlighted(true);
			
			this.squares[y.getRank()][y.getFile()].isHighlighted(true);
			
			this.repaint();
			
			/* Toggle control of the Board. */
			if (this.getColor() == Piece.WHITE) {
				if (this.isLocked()) {
					table.setStatus("White, it's your turn.");
					
					this.isLocked(Piece.BLACK);
					
					this.setColor(Piece.WHITE);
				} else {
					table.setStatus("Black, it's your turn.");
					
					this.isLocked(Piece.WHITE);
					
					this.setColor(Piece.BLACK);
				}
			} else {
				if (this.isLocked()) {
					table.setStatus("Black, it's your turn.");
					
					this.isLocked(Piece.WHITE);
					
					this.setColor(Piece.BLACK);
				} else {
					table.setStatus("White, it's your turn.");
					
					this.isLocked(Piece.BLACK);
					
					this.setColor(Piece.WHITE);
				}
			}
		}
	}
	
	public void networkUpdateNotification() {
		Notifier notifier = this.table.getAda().getNotifier();
		
		logger.debug("The Chess board received a network update notification.");
		
		if (notifier.isIncoming() && notifier.getMessage().isMove()) {
			this.move(notifier.getMessage().getMove(), true);
		}
		
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/* Use the bitboard to discover all legal moves for the selected piece. */
		long attacks = this.bitboard.getAttackBitmap(this.controller.getPiece());
		
		for (int i = 0; i < this.squares.length; i++) {
			for (int j = 0; j < this.squares[i].length; j++) {
				Square square = this.squares[i][j];
				
				g.setColor(square.getBackgroundColor());
				
				/* Draw the actual square. */
				g.fillRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE);
				
				/* If the current square is selected, highlight it. */
				if (square.isSelected()) {
					g.draw3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE - 1, Square.SIZE - 1, true);
				}
				
				/* If the current square is a viable attack/move, highlight it. */
				if (Bitboard.isPositionAttacked(attacks, square.getPosition())) {
					if (square.getBackgroundColor() == Square.WHITE) {
						g.setColor(new Color(128, 212, 13));
					} else {
						g.setColor(new Color(128, 255, 13));
					}
					
					/* If an enemy piece occupies current square, highlight as attacked. */
					if ((square.getPiece() != null) && (square.getPiece().getColor() != this.controller.getPiece().getColor())) {
						g.setColor(new Color(255, 20, 27));
					}
					
					/* Draw the raised highlighted square. */
					g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true);
					
					/* Mark the square as attacked. */
					square.isAttacked(true);
				}
				
				/* If the current square is hovered over, highlight it. */
				if (square.isHovering()) {
					g.setColor(new Color(12, 112, 173));

					g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true);
				}
				
				/* Finally, draw the piece on the board. */
				if (square.getPiece() != null) {
					g.drawImage(square.getPiece().getImage(), getCoordinate(j), getCoordinate(i), null);
				}
			}
		}
		
		g.setColor(new Color(128, 128, 128));
		
		g.drawRect(10, 10, (Square.SIZE * 8), (Square.SIZE * 8));
	}
	
	class Controller extends JPanel implements MouseListener, MouseMotionListener {
		private Position position;
		private Move move;
		private Piece piece;
		
		public Controller(JPanel panel) {
			this.add(panel);
		}
		
		public Move getMove() {
			return this.move;
		}
		
		public Piece getPiece() {
			return this.piece;
		}
		
		public void mouseClicked(MouseEvent event) {

		}
		
		public void mousePressed(MouseEvent event) {
			int x = calculateCoordinate(event.getY());
			int y = calculateCoordinate(event.getX());
			
			logger.info("Mouse pressed at (" + x + ", " + y + ").");
			
			/* We haven't made a move yet, reflect it. */
			this.move = null;
			
			if (Board.this.isLocked()) {
				logger.info("Board is locked, mouse event ignored.");
			} else {
				if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
					if (Board.this.squares[x][y].getPiece() != null) {
						if (!Board.this.isColorLocked(Board.this.squares[x][y].getPiece().getColor())) {
							this.position = Board.getPosition(event.getX(), event.getY());
							
							/* Select the current square, show the selection. */
							Board.this.squares[x][y].isSelected(true);
							
							/* Set the currently selected piece. */
							this.piece = Board.this.squares[x][y].getPiece();
							
							/* Reset previously highlighted squares. */
							for (int i = 0; i < 8; i++) {
								for (int j = 0; j < 8; j++) {
									Board.this.squares[i][j].isHovering(false);
								}
							}
							
							/* Now highlight the currently selected piece. */
							Board.this.squares[x][y].isHovering(true);
							
							/* Display the changes... */
							Board.this.repaint();
						} else {
							logger.info("Color selected is locked, mouse event ignored.");
						}
					}
				}
			}
		}
		
		public void mouseReleased(MouseEvent event) {
			int x = calculateCoordinate(event.getY());
			int y = calculateCoordinate(event.getX());
			
			logger.info("Mouse released at (" + x + ", " + y + ").");
			
			if ((!Board.this.isLocked()) && (Board.this.getState() != CHECKMATE)) {
				if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
					if ((this.piece != null) && (Board.this.squares[x][y].isAttacked())) {
						this.move = new Move(this.position, new Position(x, y));
						
						/* Make the move. */
						Board.this.move(this.move, false);
						
						if (Evaluator.isCheck(Board.this, piece.getColor())) {
							if (piece.getColor() == Piece.WHITE) {
								Board.this.table.setStatus("Your King is in check, illegal move!");
							}
							
							logger.info("King encountered check, move is illegal. Undo the previous move.");
							
							/* Undo the previous move. */
							Board.this.move(new Move(new Position(x, y), this.position), false);
						} else {
							Board.this.move(new Move(new Position(x, y), this.position), false);
							
							Board.this.move(this.move, true);
						}
					} else {
						logger.info("No move is available, mouse event ignored.");
						
						/* Reset all selected squares. Kind of ugly...*/
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								Board.this.squares[i][j].isHovering(false);
							}
						}
						
						/* Display the changes... */
						Board.this.repaint();
					}
				}
			}
			
			/* Always make sure we aren't holding a piece. */
			this.piece = null;
			
			/* Hide the selection, if one was made. */
			if (this.position != null) {
				squares[this.position.getRank()][this.position.getFile()].isSelected(false);
			}
			
			/* Also, it would be a good idea to release the focused position. */
			this.position = null;
			
			/* Reset all attacked squares. Kind of ugly...*/
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
			int x = calculateCoordinate(event.getY());
			int y = calculateCoordinate(event.getX());
			
			/* Reset all selected squares. Kind of ugly...*/
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Board.this.squares[i][j].isHovering(false);
				}
			}
			
			if (this.piece != null) {
				if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
					Board.this.squares[x][y].isHovering(true);
				}
			}
			
			/* Display the changes... */
			Board.this.repaint();
		}
		
		public void mouseMoved(MouseEvent event) {
			
		}
	}
}
