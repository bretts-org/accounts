package accounts.test.view

import javafx.scene.Node
import javafx.scene.control.ComboBox

import accounts.record.TransactionType
import io.scalatestfx.api.SfxRobot
import org.testfx.service.query.NodeQuery

import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.VerticalDirection

trait ViewRobot extends SfxRobot {

  private def isClickable(n: Node) = {
    val bounds = n.boundsInParent()
    val parentHeight = n.getParent.getLayoutBounds.getHeight
    // Ensure that the node is within the visible bounds of the parent. Note that
    // for lists, the visible bounds is one item less than the total bounds (hence
    // the "- bounds.getHeight" below.
    n.isVisible && bounds.minY >= 0 && bounds.maxY < (parentHeight - bounds.getHeight)
  }

  private def findItem(
    comboPopup: NodeQuery,
    itemText: String,
    boundaryText: String,
    scrollDirection: VerticalDirection,
    remainingCount: Int = 1000
  ): Option[Node] = {
    // Scroll down until we find the item or hit the bottom
    Option(from(comboPopup).lookup(itemText).query[Node]).filter(isClickable).orElse {
      if (remainingCount > 0) {
        val boundaryItem = Option(from(comboPopup).lookup(boundaryText).query[Node]).filter(isClickable)
        boundaryItem match {
          case Some(b) =>
            None
          case None =>
            val scrollButtonClass = scrollDirection match {
              case VerticalDirection.Up => ".decrement-button"
              case VerticalDirection.Down => ".increment-button"
              case d => throw new IllegalStateException(s"Unexpected direction: $d")
            }
            clickOn(from(comboPopup).lookup(scrollButtonClass).query[Node])
            findItem(comboPopup, itemText, boundaryText, scrollDirection, remainingCount - 1)
        }
      } else {
        None
      }
    }
  }

  def clickOn(comboName: String, comboItem: String): Unit = {
    // Select combobox
    val combo = lookup(comboName).query[ComboBox[Option[TransactionType]]]
    clickOn(combo)
    val comboPopup = lookup(".combo-box-popup")

    // Scroll from current position to top, and then all the way down, searching
    // for the combo item
    val items: ObservableBuffer[Option[TransactionType]] = combo.items()
    val topText = combo.converter().toString(items.head)
    val bottomText = combo.converter().toString(items.last)
    val item =
      findItem(comboPopup, comboItem, topText, VerticalDirection.Up) orElse
      findItem(comboPopup, comboItem, bottomText, VerticalDirection.Down)

    item match {
      case Some(i) => clickOn(i)
      case None => throw new IllegalArgumentException(s"No item found with name $comboItem")
    }

  }
}
