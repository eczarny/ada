package com.divisiblebyzero.ada.player;

//
// Player.java
// Ada Chess
//
// Created by Eric Czarny on August 21, 2009.
// Copyright 2010 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.component.Board;

public interface Player {
    
    public void makeMove(Board board, int color);
}
