package com.divisiblebyzero.ada;

//
// Application.java
// Ada Chess
//
// Created by Eric Czarny on February 26, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.common.Ada;

class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    
    private static void launch() {
        new Ada();
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            logger.error("The requested look and feel is not supported.");
        }
        
        Application.launch();
    }
}
