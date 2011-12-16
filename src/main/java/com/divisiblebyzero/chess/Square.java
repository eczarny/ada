package com.divisiblebyzero.chess;

import java.awt.Color;
import java.io.Serializable;

public class Square implements Serializable {
    private static final long serialVersionUID = -7614199741224409815L;
    
    private int color;
    private Position position;
    private Piece piece;
    private boolean attacked;
    private boolean selected;
    private boolean hovering;
    private boolean highlighted;
    
    /* Default square size */
    public static final int SIZE = 50;
    
    /* Possible background colors */
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0);
    
    public Square() {
        this.color       = -1;
        this.position    = null;
        this.piece       = null;
        this.attacked    = false;
        this.selected    = false;
    }
    
    public Square(int color, Position position) {
        this.color       = color;
        this.position    = position;
        this.piece       = null;
        this.attacked    = false;
        this.selected    = false;
    }
    
    public Square(int color, Position position, Piece piece) {
        this.color       = color;
        this.position    = position;
        this.piece       = piece;
        this.attacked    = false;
        this.selected    = false;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
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
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }
    
    public boolean isHighlighted() {
        return this.highlighted;
    }
    
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
    
    public boolean isEmpty() {
        return this.piece == null;
    }
    
    public Color getBackgroundColor() {
        Color backgroundColor = Square.WHITE;
        
        if (this.color == Piece.Color.BLACK) {
            backgroundColor = Square.BLACK;
        }
        
        return backgroundColor;
    }
}
