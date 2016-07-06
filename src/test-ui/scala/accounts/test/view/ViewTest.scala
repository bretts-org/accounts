package accounts.test.view

import accounts.test.view.matchers.ViewMatchers
import com.typesafe.scalalogging.StrictLogging
import io.scalatestfx.api.SfxRobot
import io.scalatestfx.framework.scalatest.JFXAppFixture
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

import scalafx.scene.{Parent, Scene}
import scalafx.stage.Stage

trait ViewTest
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
  protected var gcProtectedRoot: Parent = _

  override final def start(stage: Stage): Unit = {
    gcProtectedRoot = rootNode
    stage.scene = new Scene {
      root = gcProtectedRoot
    }
    stage.show()
  }

  def rootNode: Parent
}
