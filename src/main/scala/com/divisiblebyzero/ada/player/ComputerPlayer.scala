package com.divisiblebyzero.ada.player

//
// ComputerPlayer.scala
// Ada Chess
//
// Created by Eric Czarny on August 7, 2011.
// Copyright 2011 Divisible by Zero. All rights reserved.
//

import com.divisiblebyzero.ada.view.component.Board
import com.divisiblebyzero.chess.Move
import com.divisiblebyzero.chess.ai.Search

class ComputerPlayer extends Player {
  class SearchThread extends Thread {
    def this(board: Board, color: Int) {
      this ()

      this.board = board
      this.color = color
    }

    override def run() {
      val search: Search = new Search
      val move: Move = search.searchForMove(board.getBitboard, color)

      board.makeMove(move)
    }

    private var board: Board = null
    private var color: Int = 0
  }

  override def makeMove(board: Board, color: Int) {
    val searchThread = new SearchThread(board, color)

    searchThread.start()
  }
}
