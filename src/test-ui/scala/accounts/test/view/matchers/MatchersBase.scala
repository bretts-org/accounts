package accounts.test.view.matchers

import org.hamcrest.{BaseMatcher, Description}
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.Assertions

import scala.util.{Failure, Success, Try}

trait MatchersBase extends Assertions with TypeCheckedTripleEquals {

  protected def matcher[A](assertion: A => Unit) = new BaseMatcher[A] {
    private var result: Try[Unit] = Success(())

    override def matches(item: Any): Boolean = {
      result = Try(assertion(item.asInstanceOf[A]))
      result.isSuccess
    }

    override def describeTo(description: Description): Unit = description.appendText("Success")

    override def describeMismatch(item: Any, description: Description): Unit = result match {
      case Failure(e) => description.appendText(e.getMessage)
      case _ => // do nothing
    }
  }
}
