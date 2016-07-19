package accounts.test.view

import java.util.concurrent.atomic.AtomicBoolean

import accounts.test.view.matchers.ViewMatchers
import com.typesafe.scalalogging.StrictLogging
import io.scalatestfx.framework.scalatest.JFXAppFixture
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

import scalafx.scene.{Parent, Scene}
import scalafx.stage.Stage

trait Fixture {
  def root: Parent
}

object ViewTest extends StrictLogging {
  private val initialised = new AtomicBoolean(false)

  def init(): Unit = if (initialised.compareAndSet(false, true)) {
    Thread.setDefaultUncaughtExceptionHandler { (t, e) =>
      logger.warn(s"Uncaught exception in thread '$t':", e)
    }
  }
}

trait ViewTest[A <: Fixture]
  extends WordSpec
  with ViewRobot
  with JFXAppFixture
  with ViewMatchers
  with TypeCheckedTripleEquals {

  ViewTest.init()

  // Capture the root node, otherwise it may be garbage collected
  // and cause test failures
  protected final var fixture: A = _

  override final def start(stage: Stage): Unit = {
    fixture = createFixture
    stage.scene = new Scene {
      root = fixture.root
    }
    stage.show()
  }

  def createFixture: A
}
