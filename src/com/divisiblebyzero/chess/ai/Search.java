package com.divisiblebyzero.chess.ai;

//
//  chess.ai.Search.java
//  Ada Chess
//
//  Created by Eric Czarny on October 28, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.Analysis;
import com.divisiblebyzero.ada.view.Board;
import com.divisiblebyzero.chess.Move;
import com.divisiblebyzero.chess.Moves;
import com.divisiblebyzero.chess.Piece;

public class Search extends Thread {
	private Board board;
	private Analysis analysis;
	private int color;
	private int nodes;
	
	public static int DEPTH = 0;
	
	public Search(Board board, Analysis analysis, int color) {
		this.board = board;
		this.analysis = analysis;
		this.color = color;
		
		this.nodes = 0;
		
		this.start();
	}
	
	public void run() {
		Move result;
		
		Moves moves = Generator.generateLegalMoves(this.board, this.color);
		
		this.analysis.setMessage("Evaluating " + moves.getLength() + " Moves...");
		
		moves.reset();
		
		/* */
		while (moves.getCurrentMove() != null) {
			Piece current, capture;
			
			Move move = moves.getCurrentMove();
			
			current = this.board.getPieceAtPosition(move.getX());
			
			this.board.getBitboard().unsetPieceAtPosition(current, move.getX());
			
			this.board.unsetPieceAtPosition(move.getX());
			
			/* Remember any possible captures... */
			capture = this.board.getPieceAtPosition(move.getY());
			
			this.board.getBitboard().setPieceAtPosition(current, move.getY());
			
			this.board.setPieceAtPosition(current, move.getY());
			
			int value = -this.search(this.board, this.analysis, Search.invertColor(this.color), Search.DEPTH, -10000, 10000);
			
			board.getBitboard().unsetPieceAtPosition(current, move.getY());
			
			board.unsetPieceAtPosition(move.getY());
			
			/* Ada tested a capture situation, make sure to put the piece back. */
			if (capture != null) {
				board.getBitboard().setPieceAtPosition(capture, move.getY());
				
				board.setPieceAtPosition(capture, move.getY());
			}
			
			board.getBitboard().setPieceAtPosition(current, move.getX());
			
			board.setPieceAtPosition(current, move.getX());
			
			move.setScore(value);
			
			this.analysis.setMessage("Evaluated: " + move);
			
			moves.next();
		}
		
		moves.reset();
		
		int score = moves.getCurrentMove().getScore();
		
		result = moves.getCurrentMove();
		
		/* */
		while (moves.getCurrentMove() != null) {
			moves.next();
			
			Move move = moves.getCurrentMove();
			
			if (move == null) {
				break;
			}
			
			if (move.getScore() == -10000) {
				continue;
			}
			
			if (move.getScore() > score) {
				score = move.getScore();
				
				result = move;
			}
		}
		
		this.analysis.setAnalysis("Total Evaluated: " + this.getNodes());
		
		if (score == -10000) {
			this.analysis.setMessage("Checkmate...");
		} else {
			if (result != null) {
				this.board.move(result, true);
			}
			
			this.analysis.setMessage("Found: " + result);
		}
	}
	
	private static int invertColor(int color) {
		if (color != Piece.WHITE) {
			return Piece.WHITE;
		} else {
			return Piece.BLACK;
		}
	}
	
	public int search(Board board, Analysis analysis, int color, int depth, int alpha, int beta) {
		this.nodes++;
		
		this.update();
		
		if (Evaluator.isCheck(board, color)) {
			return -10000;
		}
		
		/* We are at a leaf, evaluate position. */
		if (depth <= 0) {
			return Evaluator.evaluate(board, color);
		}
		
		Moves available = Generator.generateLegalMoves(board, color);
		
		available.reset();
		
		while (available.getCurrentMove() != null) {
			Piece current, capture;
			
			Move move = available.getCurrentMove();
			
			current = board.getPieceAtPosition(move.getX());
			
			board.getBitboard().unsetPieceAtPosition(current, move.getX());
			
			board.unsetPieceAtPosition(move.getX());
			
			/* Remember any possible captures... */
			capture = board.getPieceAtPosition(move.getY());
			
			board.getBitboard().setPieceAtPosition(current, move.getY());
			
			board.setPieceAtPosition(current, move.getY());
			
			int value = -search(board, analysis, Search.invertColor(color), depth - 1, -beta, -alpha);
			
			board.getBitboard().unsetPieceAtPosition(current, move.getY());
			
			board.unsetPieceAtPosition(move.getY());
			
			/* Ada tested a capture situation, make sure to put the piece back. */
			if (capture != null) {
				board.getBitboard().setPieceAtPosition(capture, move.getY());
				
				board.setPieceAtPosition(capture, move.getY());
			}
			
			board.getBitboard().setPieceAtPosition(current, move.getX());
			
			board.setPieceAtPosition(current, move.getX());
			
			available.next();
			
			if (value >= beta) {
				return beta;
			}
			
			if (value > alpha) {
				alpha = value;
			}
		}
		
		return alpha;
	}
	
	public int getNodes() {
		return this.nodes;
	}
	
	public void update() {
		this.analysis.setAnalysis("Nodes Evaluated: " + this.getNodes());
	}
}
