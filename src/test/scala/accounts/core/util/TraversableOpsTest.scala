package accounts.core.util

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class TraversableOpsTest extends WordSpec with TypeCheckedTripleEquals {

  "An empty sequence" should {
    val s = Seq[Int]()
    "return None from singleOption" in {
      assert(s.singleOption === Option.empty[Int])
    }
    "return None from maxOption" in {
      assert(s.maxOption === Option.empty[Int])
    }
    "return None from toNonEmpty" in {
      assert(s.toNonEmpty === Option.empty[NonEmptySeq[Int]])
    }
  }

  "An single-element sequence" should {
    val s = Seq(1)
    "return Some from singleOption" in {
      assert(s.singleOption === Some(1))
    }
    "return the element from maxOption" in {
      assert(s.maxOption === Some(1))
    }
    "return a non-empty seq from toNonEmpty" in {
      assert(s.toNonEmpty === Some(NonEmptySeq(1)))
    }
  }

  "An two-element sequence" should {
    val s = Seq(1, 2)
    "throw in singleOption" in {
      intercept[IllegalStateException] {
        s.singleOption
      }
    }
    "return the max from maxOption" in {
      assert(s.maxOption === Some(2))
    }
    "return a non-empty seq from toNonEmpty" in {
      assert(s.toNonEmpty === Some(NonEmptySeq(1, 2)))
    }
  }
}
