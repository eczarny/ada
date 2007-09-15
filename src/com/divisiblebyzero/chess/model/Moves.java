package com.divisiblebyzero.chess.model;

//
//  chess.model.Moves.java
//  Ada Chess
//
//  Created by Eric Czarny on April 30, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

public class Moves {
	private Node front, current;
	private int length;
	
	private class Node {
		public Node next;
		public Move payload;
	}
	
	public Moves() {
		this.front = new Node();
		this.front.next = null;
		this.current = this.front;
		this.length = 0;
	}
	
	public boolean insert(Move move) {
		if (this.current == null) {
			return false;
		}
		
		Node p = new Node();
		p.payload = move;
		p.next = null;
		
		p.next = this.current.next;
		
		this.current.next = p;
		
		this.length++;
		
		this.next();
		
		return true;
	}
	
	public boolean remove() {
		if (this.current == null) {
			return false;
		}
		
		Node p = this.front;
		
		while (p.next != this.current) {
			p = p.next;
		}
		
		Node q = this.current.next;
		
		this.current = p;
		
		p.next = q;
		
		this.length--;
		
		return false;
	}
	
	public Move getCurrentMove() {
		if ((this.current == null) || (this.current.payload == null)) {
		 	return null;
		}
		
		return this.current.payload;
	}
	
	public void next() {
		if (this.current == null) {
		 	return;
		}
		
		this.current = this.current.next;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void reset() {
		if (this.front.next == null) {
			this.current = this.front;
		} else {
			this.current = this.front.next;
		}
	}
	
	public boolean isEmpty() {
		return (this.current == this.front);
	}
	
	public String toString() {
		String result = "Moves - Length: " + this.getLength() + ":" + "\n";
		
		this.reset();
		
		int current = 1;
		
		while (this.getCurrentMove() != null) {
			if (current < 10) {
				result = result + "0";
			}
			
			result = result + current + ". " + this.getCurrentMove().toString() + "\n";
			
			current++;
			
			this.next();
		}
		
		this.reset();
		
		return result;
	}
}
