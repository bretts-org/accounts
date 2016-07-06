package accounts.core.util

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class TraversableTupleOpsTest extends WordSpec with TypeCheckedTripleEquals {

  "A tuple sequence" should {
    val s = Seq(("a", 1), ("b", 2), ("a", 3))

    "group values" in {
      assert(s.groupValues === Map("a" -> Seq(1, 3), "b" -> Seq(2)))
    }

    "group values with function" in {
      assert(s.groupValues(_.sum) === Map("a" -> 4, "b" -> 2))
    }
  }
}
