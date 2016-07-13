package accounts.core.util

import accounts.core.util.seq._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class OneAndOpsTest extends WordSpec with TypeCheckedTripleEquals {

  "An non-empty sequence" should {
    val s = NonEmptySeq(1)
    "return a sequence from toSeq" in {
      assert(s.toSeq === Seq(1))
    }
    "return the element from max" in {
      assert(s.max === 1)
    }
  }

  "An two-element sequence" should {
    val s = NonEmptySeq(1, 2)
    "return a sequence from toSeq" in {
      assert(s.toSeq === Seq(1, 2))
    }
    "return the max element from max" in {
      assert(s.max === 2)
    }
  }
}
