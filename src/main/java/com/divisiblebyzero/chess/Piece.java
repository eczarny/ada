package com.divisiblebyzero.chess;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.divisiblebyzero.utilities.ResourceUtility;

public class Piece implements Serializable {
    private static final long serialVersionUID = -890551245339740761L;
    
    private transient Image image;
    private Position position;
    private int color;
    private int type;
    
    /* Constants for piece resources. */
    private static final String EXTENSION = ".png";
    private static final String PATH      = "/images/pieces/";
    private static final String STYLE     = "alpha";
    
    /* Possible piece types. */
    public static class Type {
        public static final int KING   = 0;
        public static final int QUEEN  = 1;
        public static final int ROOK   = 2;
        public static final int BISHOP = 3;
        public static final int KNIGHT = 4;
        public static final int PAWN   = 5;
    }
    
    /* Possible piece colors. */
    public static class Color {
        public static final int BLACK = 0;
        public static final int WHITE = 1;
    }
    
    private static Logger logger = Logger.getLogger(Piece.class);
    
    public Piece() {
        this.position = null;
        this.color    = -1;
        this.type     = -1;
    }
    
    public Piece(Position position) {
        this.position = position;
        this.color    = -1;
        this.type     = -1;
    }
    
    public Piece(int color, int type) {
        this.position = null;
        this.color    = color;
        this.type     = type;
    }
    
    public Position getPosition() {
        return this.position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public Image getImage() {
        if (this.image == null) {
            try {
                this.image = this.getImage(STYLE, this.getIdentifier());
            } catch (IOException e) {
                logger.error("Unable to load artwork from location: " + this.getImagePath(STYLE, this.getIdentifier()), e);
            }
        }
        
        return this.image;
    }
    
    public String toString() {
        String result = "Piece (Color: ";
        
        if (this.color == Piece.Color.WHITE) {
            result = result + "White";
        } else {
            result = result + "Black";
        }
        
        result = result + ", Type: ";
        
        switch(this.type) {
            case Piece.Type.KING:
                result = result + "King";
                
                break;
            case Piece.Type.QUEEN:
                result = result + "Queen";
                
                break;
            case Piece.Type.ROOK:
                result = result + "Rook";
                
                break;
            case Piece.Type.BISHOP:
                result = result + "Bishop";
                
                break;
            case Piece.Type.KNIGHT:
                result = result + "Knight";
                
                break;
            case Piece.Type.PAWN:
                result = result + "Pawn";
                
                break;
            default:
                result = result + "Unknown";
                
                break;
        }
        
        result = result + ", Position: " + this.position + ")";
        
        return result;
    }
    
    private String getIdentifier() {
        return (String.valueOf((char)(66 + ((this.color * 20) + (this.color * 1)))) + String.valueOf(this.type));
    }
    
    private Image getImage(String style, String identifier) throws IOException {
        return ResourceUtility.getImage(this.getImagePath(style, identifier));
    }
    
    private String getImagePath(String style, String identifier) {
        return PATH + style + "/" + identifier + EXTENSION;
    }
}
