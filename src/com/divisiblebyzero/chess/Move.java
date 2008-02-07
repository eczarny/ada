package com.divisiblebyzero.chess;

//
//  chess.Move.java
//  Ada Chess
//
//  Created by Eric Czarny on April 30, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

public class Move {
	private Position x, y;
	private int score;
	
	public Move() {
		this.x = new Position();
		this.y = new Position();

		this.score = 0;
	}
	
	public Move(Position x, Position y) {
		this.x = x;
		this.y = y;
	}
	
	public Move(byte bytes[]) {
		this.x = new Position((int)bytes[0], (int)bytes[1]);
		this.y = new Position((int)bytes[2], (int)bytes[3]);
	}
	
	public Position getX() {
		return this.x;
	}
	
	public void setX(Position x) {
		this.x = x;
	}
	
	public Position getY() {
		return this.y;
	}
	
	public void setY(Position y) {
		this.y = y;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String toString() {
		return this.x + " to " + this.y + " (" + this.score + ")";
	}
}
