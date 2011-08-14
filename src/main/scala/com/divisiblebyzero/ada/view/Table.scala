package com.divisiblebyzero.ada.view

//
// Table.scala
// Ada Chess
//
// Created by Eric Czarny on August 9, 2011.
// Copyright 2011 Divisible by Zero. All rights reserved.
//

import java.awt.BorderLayout
import java.awt.Container
import java.awt.Toolkit
import javax.swing.JFrame

import com.divisiblebyzero.ada.Ada
import com.divisiblebyzero.ada.common.i18n.LocalizedConstants
import com.divisiblebyzero.ada.view.component.Board
import com.divisiblebyzero.chess.Square
import com.divisiblebyzero.utilities.LocalizationUtility

@SerialVersionUID(-4562513949419438657L)
class Table extends JFrame(LocalizationUtility.getInstance.getLocalizedString(LocalizedConstants.TABLE_WINDOW_TITLE)) {
  private var ada: Ada = null
  private var board: Board = null

  def this(ada: Ada) {
    this()

    this.ada = ada

    setResizable(false)
    setVisible(true)

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    initialize()
  }

  private def initialize() {
    val x: Int = Toolkit.getDefaultToolkit.getScreenSize.width
    val y: Int = Toolkit.getDefaultToolkit.getScreenSize.height

    if (System.getProperty("os.name") == "Mac OS X") {
      setBounds(((x - ((Square.SIZE * 8) + 20) + 1) / 2), ((y - (((Square.SIZE * 9) + 15) + 15)) / 2),
                (((Square.SIZE * 8) + 20) + 1), ((Square.SIZE * 9) - 7))
    } else {
      setBounds(((x - ((Square.SIZE * 8) + 20) + 7) / 2), ((y - (((Square.SIZE * 9) + 20) + 15)) / 2),
                (((Square.SIZE * 8) + 20) + 7), ((Square.SIZE * 9) - 7))
    }

    val container: Container = this.getContentPane

    container.setLayout(new BorderLayout(10, 10))

    board = new Board(ada)

    container.add(board, BorderLayout.CENTER)
  }

  def getAda: Ada = ada
  def getBoard: Board = board
}