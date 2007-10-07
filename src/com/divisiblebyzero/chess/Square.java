package com.divisiblebyzero.chess;

//
//  chess.Square.java
//  Ada Chess
//
//  Created by Eric Czarny on February 27, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import java.awt.Color;

public class Square {
	private Position position;
	private Color background;
	private Piece piece;
	private boolean attacked;
	private boolean selected;
	private boolean hovering;
	private boolean highlighted;
	
	/* Default square size */
	public static int SIZE = 50;
	
	/* Possible background colors */
	public static Color WHITE = new Color(255, 255, 255);
	public static Color BLACK = new Color(  0,   0,   0);
	
	public Square() {
		this.position    = null;
		this.background  = null;
		this.piece       = null;
		this.attacked    = false;
		this.selected    = false;
	}
	
	public Square(Position position, Color background) {
		this.position    = position;
		this.background  = background;
		this.piece       = null;
		this.attacked    = false;
		this.selected    = false;
	}
	
	public Square(Position position, Color background, Piece piece) {
		this.position    = position;
		this.background  = background;
		this.piece       = piece;
		this.attacked    = false;
		this.selected    = false;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Color getBackgroundColor() {
		return this.background;
	}
	
	public void setBackgroundColor(Color background) {
		this.background = background;
	}
	
	public Piece getPiece() {
		return this.piece;
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	public boolean isAttacked() {
		return this.attacked;
	}
	
	public void isAttacked(boolean attacked) {
		this.attacked = attacked;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void isSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isHovering() {
		return this.hovering;
	}
	
	public void isHovering(boolean hovering) {
		this.hovering = hovering;
	}
	
	public boolean isHighlighted() {
		return this.highlighted;
	}
	
	public void isHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
}
