package com.divisiblebyzero.ada

//
// Ada.scala
// Ada Chess
//
// Created by Eric Czarny on August 1, 2011.
// Copyright 2011 Divisible by Zero. All rights reserved.
//

import org.apache.log4j.Logger

import javax.swing.UIManager
import javax.swing.plaf.metal.MetalLookAndFeel

object Ada {
  def launch(): Unit = new com.divisiblebyzero.ada.common.Ada()

  def main(args: Array[String]): Unit = {
    UIManager.setLookAndFeel(new MetalLookAndFeel())

    Ada.launch()
  }
}