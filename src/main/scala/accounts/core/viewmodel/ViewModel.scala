package accounts.core.viewmodel

import com.typesafe.scalalogging.StrictLogging

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.language.implicitConversions
import scalafx.beans.property.{BooleanProperty, ObjectProperty, Property}
import scalafx.collections.ObservableBuffer
import scalafx.collections.transformation.SortedBuffer

object ViewModel extends StrictLogging {

  def displayString(rawString: String): String = rawString.replaceAll("(.)([A-Z](?=[a-z]))", "$1 $2")

  sealed trait Calculation {
    def refresh(): Unit
  }

  case class PropertyCalculation[A, B](property: Property[A, B], calculation: () => A)
  extends Calculation {
    def refresh(): Unit = {
      property() = calculation()
    }
  }

  case class BufferCalculation[A](buffer: ObservableBuffer[A], calculation: () => Seq[A])
  extends Calculation {
    def refresh(): Unit = {
      buffer.setAll(calculation().asJava)
    }
  }

  object Property {
    def apply[A](value: A)(implicit vmState: VmState): ObjectProperty[A] = {
      val p = ObjectProperty(value)
      p.onChange {
        if (!vmState.updating) {
          logger.debug(s"Property.onChange: $p")
          vmState.update {}
        } else {
          logger.trace(s"Skipping Property.onChange: $p")
        }
      }
      p
    }
  }

  object Binding {

    def apply[A](to: => A)(from: A => Unit)(implicit vmState: VmState): ObjectProperty[A] = {
      val p = ObjectProperty(to)
      p.onChange(handleChange(p, from))
      p
    }

    def boolean(to: => Boolean)(from: Boolean => Unit)(implicit vmState: VmState): BooleanProperty = {
      val p = BooleanProperty(to)
      p.onChange(handleChange(p, from))
      p
    }

    private def handleChange[A, B](p: Property[A, B], from: A => Unit)(implicit vmState: VmState): Unit = {
      logger.trace(s"Binding.onChange: $p (updating: ${vmState.updating})")
      vmState.update(from(p()))
    }
  }

  object CalculatedProperty {
    def apply[A](calculation: => A)(implicit vmState: VmState): ObjectProperty[A] = {
      val p = ObjectProperty(calculation)
      vmState.calculations += PropertyCalculation(p, () => calculation)
      p
    }
  }

  object CalculatedBuffer {
    def apply[A](calculation: => Seq[A])(implicit vmState: VmState): ObservableBuffer[A] = {
      val b = ObservableBuffer(calculation)
      vmState.calculations += BufferCalculation(b, () => calculation)
      b
    }

    def sorted[A](calculation: => Seq[A])(ordering: Ordering[_ >: A])(implicit vmState: VmState): SortedBuffer[A] = {
      new SortedBuffer[A](apply(calculation), ordering)
    }
  }

  class VmState {
    private var _updating: Boolean = false
    def updating: Boolean = _updating
    val calculations = mutable.Buffer[Calculation]()

    def refresh(): Unit = {
      logger.debug("Refreshing all calculations")
      calculations.foreach { c =>
        logger.trace(s"Refreshing calculation: $c")
        c.refresh()
      }
    }

    def update(f: => Unit) = {
      val previouslyUpdating = updating
      try {
        _updating = true
        f
        if (!previouslyUpdating) refresh()
      } finally {
        _updating = previouslyUpdating
      }
    }

  }

  class RichProperty[A](p: ObjectProperty[A]) {
    def onUiChange(f: A => Unit)(implicit vmState: VmState): Unit = p.onChange { (_, _, value) =>
      if (!vmState.updating) {
        logger.trace(s"RichProperty.onUiChange: $p")
        vmState.update(f(value))
      } else {
        logger.info(s"Skipping RichProperty.onUiChange: $p")
      }
    }

    def onUiChange(f: => Unit)(implicit vmState: VmState): Unit = onUiChange(_ => f)

  }

  implicit def toRichProperty[A](p: ObjectProperty[A]): RichProperty[A] = new RichProperty(p)

  val singletonVmState = new VmState
}

trait ViewModel {
  implicit protected val vmState = ViewModel.singletonVmState

  protected def update(f: => Unit) = vmState.update(f)
}
