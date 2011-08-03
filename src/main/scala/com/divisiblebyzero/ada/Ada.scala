package com.divisiblebyzero.ada

//
// Ada.java
// Ada Chess
//
// Created by Eric Czarny on August 2, 2011.
// Copyright 2011 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.player.{ ComputerPlayer, HumanPlayer, Player }
import com.divisiblebyzero.ada.view.Table
import com.divisiblebyzero.chess.Piece

class Ada {
  private val table = new Table(this)
  private val players = List(new HumanPlayer(), new ComputerPlayer())

  def getTable(): Table = table

  def getWhitePlayer(): Player = getPlayerForColor(Piece.Color.WHITE)
  def getBlackPlayer(): Player = getPlayerForColor(Piece.Color.BLACK)

  def getPlayerForColor(color: Int): Player = players(color)
}
