package com.divisiblebyzero.ada;

//
//  ada.Ada.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.Table;
import com.divisiblebyzero.network.Notifier;

public class Ada {
	private Table table;
	private Notifier notifier;
	
	public Ada() {
		this.initialize();
	}
	
	private void initialize() {
		this.getTable();
		
		/* White will be the initial owner of the Board. */
		// this.getTable().getBoard().setColor(Piece.WHITE);
		
		/* For development reasons leave the Board unlocked. */
		// this.getTable().getBoard().isLocked(true);
		
		// this.getTable().setStatus("...");
	}
	
	public Table getTable() {
		if (this.table == null) {
			this.table = new Table(this);
		} else {
			this.table.setVisible(true);
		}
		
		return this.table;
	}
	
	public Notifier getNotifier() {
		if (this.notifier == null) {
			this.notifier = new Notifier();
		}
		
		return this.notifier;
	}
}
