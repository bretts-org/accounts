package accounts.core.util

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class TraversableOpsTest extends WordSpec with TypeCheckedTripleEquals {

  "An empty sequence" should {
    "return None from singleOption" in {
      assert(Seq[String]().singleOption === Option.empty[String])
    }
  }

  "An single-element sequence" should {
    "return Some from singleOption" in {
      assert(Seq("a").singleOption === Some("a"))
    }
  }

  "An two-element sequence" should {
    "throw in singleOption" in {
      intercept[IllegalStateException] {
        Seq("a", "b").singleOption
      }
    }
  }
}
