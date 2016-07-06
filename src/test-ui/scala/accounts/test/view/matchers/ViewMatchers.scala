package accounts.test.view.matchers

import javafx.scene.control._
import javafx.scene.Node

import org.hamcrest.Matcher

trait ViewMatchers extends MatchersBase with TableMatchers {

  private def text(n: Node): String = n match {
    case t: TextField => t.getText
  }

  def hasText(expected: String): Matcher[TextField] = matcher[TextField] { tf =>
    assert(text(tf) === expected)
  }

  private def comboText[A](combo: ComboBox[A]): String = combo.getConverter.toString(combo.getValue)

  def hasComboText[A](expected: String): Matcher[ComboBox[A]] = matcher[ComboBox[A]] { cb =>
    assert(comboText(cb) === expected)
  }

  private def dateText[A](dp: DatePicker): String = dp.getConverter.toString(dp.getValue)

  def hasDateText[A](expected: String): Matcher[DatePicker] = matcher[DatePicker] { dp =>
    assert(dateText(dp) === expected)
  }

}

object ViewMatchers extends ViewMatchers
