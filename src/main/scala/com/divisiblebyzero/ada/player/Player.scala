package com.divisiblebyzero.ada.player

//
// Player.scala
// Ada Chess
//
// Created by Eric Czarny on August 4, 2011.
// Copyright 2011 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.component.Board

trait Player {
  def makeMove(board: Board, color: Int)
}
