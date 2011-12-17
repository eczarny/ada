package com.divisiblebyzero.ada.view.component

import java.awt.Color
import java.awt.Graphics
import java.awt.MediaTracker
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JPanel

import com.divisiblebyzero.ada.Ada
import com.divisiblebyzero.ada.player.Player
import com.divisiblebyzero.chess.Bitboard
import com.divisiblebyzero.chess.Move
import com.divisiblebyzero.chess.Piece
import com.divisiblebyzero.chess.Pieces
import com.divisiblebyzero.chess.Position
import com.divisiblebyzero.chess.Square
import com.divisiblebyzero.chess.ai.Evaluator
import com.divisiblebyzero.ada.common.Logging

@SerialVersionUID(-4785349736776306753L)
class Board extends JPanel with Logging {
  import Board._

  private var ada: Ada = null
  private var bitboard: Array[Array[Long]] = Bitboard.generateBitboard
  private var squares: Array[Array[Square]] = null
  private val pieces: Pieces = new Pieces
  private var controller: Controller = null
  private var state = UNDECIDED
  private var color = Piece.Color.WHITE

  def this(ada: Ada) {
    this()

    this.ada = ada

    initialize()
  }

  private def initialize() {
    controller = new Controller(this)

    // Add the mouse action listeners...
    addMouseListener(this.controller)
    addMouseMotionListener(this.controller)

    squares = new Array[Array[Square]](8, 8)

    info("Constructing the underlying board.")

    // Construct a new board, no pieces.
    for (i <- 0 until 8) {
      for (j <- 0 until 8) {
        if ((i % 2) == 0) {
          if ((j % 2) == 0) {
            squares(i)(j) = new Square(Piece.Color.WHITE, new Position(i, j))
          } else {
            squares(i)(j) = new Square(Piece.Color.BLACK, new Position(i, j))
          }
        } else {
          if ((j % 2) == 0) {
            squares(i)(j) = new Square(Piece.Color.BLACK, new Position(i, j))
          }
          else {
            squares(i)(j) = new Square(Piece.Color.WHITE, new Position(i, j))
          }
        }
      }
    }

    // Load the pieces, caching their respective images.
    pieces.load(new MediaTracker(this))

    // Reset to a playable board.
    reset()
  }

  private def reset() {
    for (i <- 0 until 8) {
      for (j <- 0 until 8) {
        if (i == 0) {
          squares(i)(j).setPiece(pieces.getPiece(Piece.Color.BLACK, j))
        } else if (i == 1) {
          squares(i)(j).setPiece(pieces.getPiece(Piece.Color.BLACK, j + 8))
        }

        if (i == 7) {
          squares(i)(j).setPiece(pieces.getPiece(Piece.Color.WHITE, j))
        } else if (i == 6) {
          squares(i)(j).setPiece(pieces.getPiece(Piece.Color.WHITE, j + 8))
        }

        if (squares(i)(j).getPiece != null) {
          squares(i)(j).getPiece.setPosition(new Position(i, j))
        }
      }
    }

    // Make sure that white can move first.
    locked(Piece.Color.BLACK)
  }

  def isLocked: Boolean = {
    Lock.locked && (Lock.color == -1)
  }

  def isColorLocked(color: Int): Boolean = {
    Lock.locked && (Lock.color == color)
  }

  private def locked(locked: Boolean) {
    Lock.locked = locked
    Lock.color = -1
  }

  private def locked(color: Int) {
    Lock.locked = true
    Lock.color = color
  }

  def getState = state

  def setState(state: Int) {
    this.state = state
  }

  def getBitboard = bitboard

  def getPiece(rank: Int, file: Int) = squares(rank)(file).getPiece
  def getPieceAtPosition(position: Position) = squares(position.getRank)(position.getFile).getPiece

  def setPieceAtPosition(piece: Piece, position: Position) {
    squares(position.getRank)(position.getFile).setPiece(piece)
  }

  def unsetPieceAtPosition(position: Position) {
    squares(position.getRank)(position.getFile).setPiece(null)
  }

  def getColor: Int = color

  def setColor(color: Int) {
    this.color = color
  }

  def makeMove(move: Move) {
    val x: Position = move.getX
    val y: Position = move.getY

    // Get the piece being moved.
    val piece: Piece = squares(x.getRank)(x.getFile).getPiece

    // Remember any piece that may be captured by making this move.
    val capturedPiece: Piece = squares(y.getRank)(y.getFile).getPiece

    // Are we capturing a piece? If so, remove it from the bitboard.
    if (capturedPiece != null) {
      bitboard = Bitboard.unsetPieceAtPosition(bitboard, capturedPiece, y)
    }

    // Remove the piece from the bitboard...
    bitboard = Bitboard.unsetPieceAtPosition(bitboard, piece, x)

    // And place it at the new position on the bitboard.
    bitboard = Bitboard.setPieceAtPosition(bitboard, piece, y)

    if (Evaluator.isCheck(this.bitboard, getColor)) {
      Board.this.setState(Board.CHECK)

      // Remove the piece from its new position on the bitboard...
      bitboard = Bitboard.unsetPieceAtPosition(bitboard, piece, y)

      // And place it as its old position.
      bitboard = Bitboard.setPieceAtPosition(bitboard, piece, x)

      // If we captured a piece we should put it back.
      if (capturedPiece != null) {
        bitboard = Bitboard.setPieceAtPosition(bitboard, capturedPiece, y)
      }

      return
    } else {
      Board.this.setState(Board.UNDECIDED)
    }

    // Pick up the piece, removing it from old position.
    unsetPieceAtPosition(x)

    // Let the piece know we moved...
    piece.setPosition(y)

    // Move the piece to new square.
    setPieceAtPosition(piece, y)

    // Highlight the prior move...
    squares(x.getRank)(x.getFile).setHighlighted(true)
    squares(y.getRank)(y.getFile).setHighlighted(true)

    repaint()

    // Toggle control of the board.
    if (getColor == Piece.Color.WHITE) {
      val currentPlayer: Player = ada.getBlackPlayer

      locked(true)
      setColor(Piece.Color.BLACK)

      currentPlayer.makeMove(this, Piece.Color.BLACK)
    } else {
      val currentPlayer: Player = ada.getWhitePlayer

      locked(Piece.Color.BLACK)
      setColor(Piece.Color.WHITE)

      currentPlayer.makeMove(this, Piece.Color.WHITE)
    }
  }

  protected override def paintComponent(g: Graphics) {
    super.paintComponent(g)

    val attacks: Long = Bitboard.getAttackBitmap(bitboard, controller.getSelectedPiece)

    for (i <- 0 until squares.length) {
      for (j <- 0 until squares(i).length) {
        val square: Square = squares(i)(j)
        val selectedPiece: Piece = controller.getSelectedPiece

        g.setColor(square.getBackgroundColor)

        // Draw the actual square.
        g.fillRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE)

        // If the current square is selected, highlight it.
        if (square.isSelected) {
          g.draw3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE - 1, Square.SIZE - 1, true)
        }

        // If the current square is a viable attack/move, highlight it.
        if (Bitboard.isPositionAttacked(attacks, square.getPosition)) {
          if (square.getColor == Piece.Color.WHITE) {
            g.setColor(new Color(128, 212, 13))
          } else {
            g.setColor(new Color(128, 255, 13))
          }

          // If an enemy piece occupies current square, highlight as attacked.
          if (!square.isEmpty && (square.getPiece.getColor != selectedPiece.getColor)) {
            g.setColor(new Color(255, 20, 27))
          }

          // Draw the raised highlighted square.
          g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true)

          // Mark the square as attacked.
          square.isAttacked(true)
        }

        if (square.isHovering || square.isHighlighted) {
          g.setColor(new Color(58, 172, 216))

          g.fill3DRect((j * Square.SIZE) + 10, (i * Square.SIZE) + 10, Square.SIZE, Square.SIZE, true)

          square.setHighlighted(false)
        }

        // Draw the piece on the board.
        if (!square.isEmpty) {
          g.drawImage(square.getPiece.getImage, getCoordinate(j), getCoordinate(i), null)
        }
      }
    }

    g.setColor(new Color(128, 128, 128))
    g.drawRect(10, 10, (Square.SIZE * 8), (Square.SIZE * 8))
  }

  @SerialVersionUID(-2076001089265149020L)
  private[component] class Controller extends JPanel with MouseListener with MouseMotionListener {
    private var position: Position = null
    private var move: Move = null
    private var selectedPiece: Piece = null

    def this(panel: JPanel) {
      this()

      add(panel)
    }

    def getMove: Move = move
    def getSelectedPiece: Piece = selectedPiece

    def mouseClicked(event: MouseEvent) {

    }

    def mousePressed(event: MouseEvent) {
      val x: Int = getRankOrFile(event.getY)
      val y: Int = getRankOrFile(event.getX)

      // We haven't made a move yet, reflect it.
      move = null

      if (!Board.this.isLocked) {
        if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
          if (Board.this.squares(x)(y).getPiece != null) {
            if (!Board.this.isColorLocked(Board.this.squares(x)(y).getPiece.getColor)) {
              this.position = Board.getPosition(event.getX, event.getY)

              // Select the current square, show the selection.
              Board.this.squares(x)(y).setSelected(true)

              // Set the currently selected piece.
              selectedPiece = Board.this.squares(x)(y).getPiece

              // Reset previously highlighted squares.
              for (i <- 0 until 8) {
                for (j <- 0 until 8) {
                  Board.this.squares(i)(j).setHovering(false)
                }
              }

              // Now highlight the currently selected piece.
              Board.this.squares(x)(y).setHovering(true)

              // Display the changes...
              Board.this.repaint()
            }
          }
        }
      }
    }

    def mouseReleased(event: MouseEvent) {
      val x: Int = getRankOrFile(event.getY)
      val y: Int = getRankOrFile(event.getX)

      if ((!Board.this.isLocked) && (Board.this.getState != CHECKMATE)) {
        if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
          if ((selectedPiece != null) && (Board.this.squares(x)(y).isAttacked)) {
            move = new Move(position, new Position(x, y))

            // Make the move.
            Board.this.makeMove(move)
          } else {
            info("No move is available, mouse event ignored.")

            // Reset all selected squares. Kind of ugly...
            for (i <- 0 until 8) {
              for (j <- 0 until 8) {
                Board.this.squares(i)(j).setHovering(false)
              }
            }

            // Display the changes...
            Board.this.repaint()
          }
        }
      }

      // Always make sure we aren't holding a piece.
      selectedPiece = null

      // Hide the selection, if one was made.
      if (position != null) {
        squares(position.getRank)(position.getFile).setSelected(false)
      }

      // Also, it would be a good idea to release the focused position.
      position = null

      for (i <- 0 until 8) {
        for (j <- 0 until 8) {
          Board.this.squares(i)(j).isAttacked(false)
        }
      }

      // Display the changes...
      Board.this.repaint()
    }

    def mouseEntered(event: MouseEvent) {
      
    }

    def mouseExited(event: MouseEvent) {
      
    }

    def mouseDragged(event: MouseEvent) {
      val x: Int = getRankOrFile(event.getY)
      val y: Int = getRankOrFile(event.getX)

      // Reset all selected squares. Kind of ugly...
      for (i <- 0 until 8) {
        for (j <- 0 until 8) {
          Board.this.squares(i)(j).setHovering(false)
        }
      }

      if (selectedPiece != null) {
        if (((x > -1) && (y > -1)) && ((x < 8) && (y < 8))) {
          Board.this.squares(x)(y).setHovering(true)
        }
      }

      // Display the changes...
      Board.this.repaint()
    }

    def mouseMoved(event: MouseEvent) {
      
    }
  }
}

object Board {
  val UNDECIDED: Int = 0
  val CHECK: Int = 1
  val CHECKMATE: Int = 2

  private[component] def getCoordinate(offset: Int) = {
    15 + ((Square.SIZE * (offset + 1)) - Square.SIZE)
  }

  private[component] def getRankOrFile(coordinate: Int) = {
    ((coordinate + (Square.SIZE - 10)) / Square.SIZE) - 1
  }

  private[component] def getPosition(x: Int, y: Int) = {
    new Position(getRankOrFile(y), getRankOrFile(x))
  }

  private[component] object Lock {
    var locked = false
    var color = 0
  }
}
