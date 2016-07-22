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

  private def findItemInView(comboPopup: NodeQuery, itemText: String) =
    Option(from(comboPopup).lookup(itemText).query[Node]).filter(isClickable)

  private def findItem(
    comboPopup: NodeQuery,
    itemText: String,
    boundaryText: String,
    scrollDirection: VerticalDirection
  ): Option[Node] = findItemInView(comboPopup, itemText) orElse {
    val scrollButtonClass = scrollDirection match {
      case VerticalDirection.Up => ".decrement-button"
      case VerticalDirection.Down => ".increment-button"
      case d => throw new IllegalStateException(s"Unexpected direction: $d")
    }
    val scrollButton = from(comboPopup).lookup(scrollButtonClass).query[Node]
    moveTo(scrollButton)
    scrollAndFindItem(comboPopup, itemText, boundaryText, 1000)
  }

  private def scrollAndFindItem(
    comboPopup: NodeQuery,
    itemText: String,
    boundaryText: String,
    remainingCount: Int
  ): Option[Node] = {
    findItemInView(comboPopup, itemText) orElse {
      // Scroll until we find the item, hit the boundary or reach the count limit
      findItemInView(comboPopup, boundaryText) match {
        case None if remainingCount > 0 =>
          (1 to 10).foreach(_ => clickOn())
          scrollAndFindItem(comboPopup, itemText, boundaryText, remainingCount - 1)
        case _ =>
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
