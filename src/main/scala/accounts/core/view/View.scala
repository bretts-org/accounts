package accounts.core.view

import java.time.{LocalDate, Month}
import java.time.format.DateTimeFormatter
import javafx.beans.{value => jfx}

import scala.language.implicitConversions
import scalafx.application.Platform
import scalafx.beans.binding.{Bindings, ObjectBinding}
import scalafx.beans.property.Property
import scalafx.beans.value.ObservableValue
import scalafx.scene.control._
import scalafx.util.StringConverter

object View {
  private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
  private val numericMonthRegex = "([0-9]{1,2})".r
  private val positiveTwoDigitRegex = """\+?[0-9]+(\.[0-9]{0,2})?""".r

  def formatDate(ld: LocalDate): String = dateFormatter.format(ld)
  def toDate(s: String): LocalDate = dateFormatter.parse(s, LocalDate.from(_))

  def formatDecimal(b: BigDecimal): String = f"$b%.2f"

  val dateConverter: StringConverter[LocalDate] = StringConverter[LocalDate](
    s => Option(s).filter(!_.isEmpty).map(toDate).orNull,
    d => Option(d).map(formatDate).getOrElse("")
  )

  val optionIntConverter = StringConverter[Option[Int]](
    Option(_).filter(!_.isEmpty).map(_.toInt),
    _.map(_.toString).getOrElse("")
  )

  val optionPositiveBigDecimalConverter = StringConverter[Option[BigDecimal]](
    Option(_).filter(!_.isEmpty).map {
      case s @ positiveTwoDigitRegex(_*) => BigDecimal(s)
      case s => throw new IllegalArgumentException(s"Invalid positive amount: $s")
    },
    _.map(formatDecimal).getOrElse("")
  )

  val optionStringConverter = StringConverter[Option[String]](
    Option(_).filter(!_.isEmpty),
    _.getOrElse("")
  )

  val monthConverter = StringConverter[Month]({
    case numericMonthRegex(s) => Month.of(s.toInt)
    case s => Month.valueOf(s.toUpperCase)
  },
    _.toString.toLowerCase.capitalize
  )

  val optionMonthConverter = StringConverter[Option[Month]]({
    Option(_).filter(s => !s.isEmpty && !s.equalsIgnoreCase("all")).map {
      case numericMonthRegex(s) => Month.of(s.toInt)
      case s => Month.valueOf(s.toUpperCase)
    }
  },
  {
    case None => ""
    case Some(m) => m.toString.toLowerCase.capitalize
  })

  // Workaround for https://bugs.openjdk.java.net/browse/JDK-8129400
  def selectOnFocus[A](editor: TextField)(
    v: ObservableValue[Boolean, java.lang.Boolean],
    oldValue: java.lang.Boolean,
    focusGained: java.lang.Boolean
  ): Unit = {
    if (focusGained) {
      Platform.runLater {
        editor.selectAll()
      }
    }
  }
}

trait View {
  implicit protected def toFunction[A, B](cf: CellFactory[B]): TableColumn[A, B] => TableCell[A, B] = _ => {
    new TableCell[A, B] {
      item.onChange { (_, _, newValue) =>
        text = Option(newValue).map(cf.text).getOrElse("")
        cf.tooltip.foreach { ttFunc =>
          val newTooltip = for {
            v <- Option(newValue)
            ttString <- ttFunc(v)
          } yield Tooltip(ttString)
          tooltip = newTooltip.orNull
        }
        cf.alignment.foreach(alignment = _)
      }
    }
  }

  implicit protected def toFunction[A, B](f: => TableCell[A, B]): TableColumn[A, B] => TableCell[A, B] =
    _ => f

  implicit def asJava(v: ObservableValue[_, Boolean]): jfx.ObservableValue[java.lang.Boolean] =
    Bindings.createObjectBinding[java.lang.Boolean](() => v.delegate.getValue, v)

  implicit class RichBinding[A](p1: Property[_, A])(implicit ev: Null <:< A) {
    def <==>(p2: Property[_, Option[A]]): Unit = {
      NullableToOptionBidirectionalBinding.bind(p1, p2)
    }

    def <==(p2: ObservableValue[_, Option[A]]): Unit = {
      val b = Bindings.createObjectBinding(() => p2.delegate.getValue.orNull, p2)
      p1 <== b
    }


    def bind[B](f: A => B) = Bindings.createObjectBinding(() => f(p1.delegate.getValue), p1)
  }

  implicit class RichOptionBinding[A](p1: Property[_, Option[A]]) {
    def <==(p2: ObservableValue[_, A]): Unit = {
      val b = Bindings.createObjectBinding(() => Option(p2.delegate.getValue), p2)
      p1 <== b
    }

    def isDefined: ObjectBinding[Boolean] = Bindings.createObjectBinding(() => p1.delegate.getValue.isDefined, p1)
    def isEmpty: ObjectBinding[Boolean] = Bindings.createObjectBinding(() => p1.delegate.getValue.isEmpty, p1)
  }

}
