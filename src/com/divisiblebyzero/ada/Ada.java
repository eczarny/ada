package com.divisiblebyzero.ada;

//
//  ada.Ada.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2006 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.ui.*;
import com.divisiblebyzero.chess.model.*;
import com.divisiblebyzero.network.*;

public class Ada {
	private Table table;
	private Analysis analysis;
	private Notifier notifier;
	private boolean searching;
	
	public Ada() {
		this.searching = false;
		
		this.initialize();
	}
	
	private void initialize() {
		this.getTable();
		
		/* White will be the initial owner of the Board. */
		// this.getTable().getBoard().setColor(Piece.WHITE);
		
		/* For development reasons leave the Board unlocked. */
		// this.getTable().getBoard().isLocked(true);
		
		// this.getTable().setStatus("...");
		
		/* Create and display the computer analysis window. */
		// this.getAnalysis();
		
		// new Setup(this);
	}
	
	public Table getTable() {
		if (this.table == null) {
			this.table = new Table(this);
		} else {
			this.table.setVisible(true);
		}
		
		return this.table;
	}
	
	public Analysis getAnalysis() {
		if (this.analysis == null) {
			this.analysis = new Analysis();
		} else {
			this.analysis.setVisible(true);
		}
		
		return this.analysis;
	}
	
	public Notifier getNotifier() {
		if (this.notifier == null) {
			this.notifier = new Notifier();
		}
		
		return this.notifier;
	}
	
	public boolean isSearching() {
		return this.searching;
	}
	
	public void isSearching(boolean searching) {
		this.searching = searching;
	}
}
