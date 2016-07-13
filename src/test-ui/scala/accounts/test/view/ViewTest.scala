package accounts.test.view

import accounts.test.view.matchers.ViewMatchers
import com.typesafe.scalalogging.StrictLogging
import io.scalatestfx.api.SfxRobot
import io.scalatestfx.framework.scalatest.JFXAppFixture
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

import scalafx.scene.{Parent, Scene}
import scalafx.stage.Stage

trait Fixture {
  def root: Parent
}

trait ViewTest[A <: Fixture]
  extends WordSpec
  with SfxRobot
  with JFXAppFixture
  with ViewMatchers
  with TypeCheckedTripleEquals
  with StrictLogging {

  Thread.setDefaultUncaughtExceptionHandler { (t, e) =>
    logger.error(s"Uncaught exception in thread '$t':", e)
  }

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
