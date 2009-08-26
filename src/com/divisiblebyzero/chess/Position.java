package com.divisiblebyzero.chess;

//
// Position.java
// Ada Chess
//
// Created by Eric Czarny on February 28, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = -9142730880358796774L;
    
	private int rank, file;
    
    public Position() {
        this.rank = 0;
        this.file = 0;
    }
    
    public Position(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }
    
    public int getRank() {
        return this.rank;
    }
    
    public int getFile() {
        return this.file;
    }
    
    public void setRank(int rank) {
        this.rank = rank;
    }
    
    public void setFile(int file) {
        this.file = file;
    }
    
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        
        if (object == null) {
            return false;
        }
        
        if (object instanceof Position) {
            Position position = (Position)object;
            
            return (position.getRank() == this.rank) && (position.getFile() == this.file);
        }
        
        return false;
    }
    
    public int hashCode() {
        int hash = 42;
        
        hash = 31 * hash + rank;
        hash = 31 * hash + file;
        
        return hash;
    }
    
    public String toString() {
        String result = "";
        
        switch (this.file) {
            case 0:
                result = result + "a";
                
                break;
            case 1:
                result = result + "b";
                
                break;
            case 2:
                result = result + "c";
                
                break;
            case 3:
                result = result + "d";
                
                break;
            case 4:
                result = result + "e";
                
                break;
            case 5:
                result = result + "f";
                
                break;
            case 6:
                result = result + "g";
                
                break;
            case 7:
                result = result + "h";
                
                break;
            default:
                break;
        }
        
        switch (this.rank) {
            case 0:
                result = result + "8";
                
                break;
            case 1:
                result = result + "7";
                
                break;
            case 2:
                result = result + "6";
                
                break;
            case 3:
                result = result + "5";
                
                break;
            case 4:
                result = result + "4";
                
                break;
            case 5:
                result = result + "3";
                
                break;
            case 6:
                result = result + "2";
                
                break;
            case 7:
                result = result + "1";
                
                break;
            default:
                break;
        }
        
        return result;
    }
}
