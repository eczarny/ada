package com.divisiblebyzero.ada;

//
//  ada.Application.java
//  Ada Chess
//
//  Created by Eric Czarny on February 26, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import javax.swing.UIManager;

class Application {
	private Application() {
		new Ada();
	}
	
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			
		}
		
		new Application();
	}
}
