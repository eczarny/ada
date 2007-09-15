package com.divisiblebyzero.chess.model;

//
//  chess.model.Position.java
//  Ada Chess
//
//  Created by Eric Czarny on February 28, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

public class Position {
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
		return (((Position)object).getRank() == this.rank) && (((Position)object).getFile() == this.file);
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
		}
		
		return result;
	}
}
