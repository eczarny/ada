package com.divisiblebyzero.ada

import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.metal.MetalLookAndFeel

import com.divisiblebyzero.ada.common.Logging

object Main extends Logging {
  def launch() { new Ada() }

  def main(args: Array[String]) {
    try {
      UIManager.setLookAndFeel(new MetalLookAndFeel())
    } catch {
      case e: UnsupportedLookAndFeelException => error("The requested look and feel is not supported.")
    }

    Main.launch()
  }
}
