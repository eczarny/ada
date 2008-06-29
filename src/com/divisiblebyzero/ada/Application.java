package com.divisiblebyzero.ada;

//
//  ada.Application.java
//  Ada Chess
//
//  Created by Eric Czarny on February 26, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.common.Ada;

class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    
    private Application() {
        new Ada();
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            logger.error("Unable to modify application look and feel.");
        }
        
        new Application();
    }
}
