package com.divisiblebyzero.chess;

//
// Pieces.java
// Ada Chess
//
// Created by Eric Czarny on March 23, 2006.
// Copyright 2010 Divisible by Zero. All rights reserved.
//

import java.awt.MediaTracker;
import java.io.Serializable;

import org.apache.log4j.Logger;

public class Pieces implements Serializable {
    private static final long serialVersionUID = 2217117961210308284L;

    private Piece[][] pieces;
    
    private static Logger logger = Logger.getLogger(Pieces.class);
    
    public Pieces() {
        this.pieces = new Piece[16][2];
    }
    
    public void load(MediaTracker tracker) {
        int[] order = {
                2, 4, 3, 1, 0, 3, 4, 2,
                5, 5, 5, 5, 5, 5, 5, 5
        };
        
        logger.info("Loading artwork for Chess pieces.");
        
        for (int i = 0; i < this.pieces.length; i++) {
            for (int j = 0; j < this.pieces[i].length; j++) {
                this.pieces[i][j] = new Piece(j, order[i]);
                
                tracker.addImage(this.pieces[i][j].getImage(), 0);
                
                try {
                    tracker.waitForAll();
                } catch (Exception e) {
                    logger.error("Loading the image has failed for the following piece: " + this.pieces[i][j]);
                }
            }
        }
    }
    
    public Piece getPiece(int color, int type) {
        return this.pieces[type][color];
    }
}
