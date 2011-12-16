package com.divisiblebyzero.ada.player

import com.divisiblebyzero.ada.view.component.Board

trait Player {
  def makeMove(board: Board, color: Int)
}
