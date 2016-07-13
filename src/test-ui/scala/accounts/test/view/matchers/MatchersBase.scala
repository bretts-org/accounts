package accounts.test.view.matchers

import java.io.{PrintWriter, StringWriter}

import com.typesafe.scalalogging.StrictLogging
import org.hamcrest.{BaseMatcher, Description}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.Assertions
import org.scalatest.exceptions.TestFailedException

import scala.util.{Failure, Success, Try}

trait MatchersBase extends Assertions with TypeCheckedTripleEquals with StrictLogging {

  protected def matcher[A](assertion: A => Unit) = new BaseMatcher[A] {
    private var result: Try[Unit] = Success(())

    override def matches(item: Any): Boolean = {
      result = Try(assertion(item.asInstanceOf[A]))
      result.isSuccess
    }

    override def describeTo(description: Description): Unit = description.appendText("Success")

    override def describeMismatch(item: Any, description: Description): Unit = result match {
      case Failure(e: TestFailedException) =>
        logger.trace("Matcher failure:", e)
        description.appendText(e.getMessage)
      case Failure(e) =>
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        description.appendText(s"threw\n${sw.toString}")
      case _ => // do nothing
    }
  }
}
