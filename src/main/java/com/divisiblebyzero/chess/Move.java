package com.divisiblebyzero.chess;

import java.io.Serializable;

//
// Move.java
// Ada Chess
//
// Created by Eric Czarny on April 30, 2006.
// Copyright 2010 Divisible by Zero. All rights reserved.
//

public class Move implements Serializable {
    private static final long serialVersionUID = 8308554798560064235L;
    
    private Position x, y;
    private long score;
    
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
    
    public long getScore() {
        return this.score;
    }
    
    public void setScore(long score) {
        this.score = score;
    }
    
    public String toString() {
        return this.x + " to " + this.y + " (" + this.score + ")";
    }
}
