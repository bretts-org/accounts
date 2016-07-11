package accounts.core.util

import accounts.core.util.Os._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class OsTest extends WordSpec with TypeCheckedTripleEquals {

  "A windows host" should {
    "match as Windows" in {
      assert(os("Windows 8.1") === Some(Windows))
    }
  }

  "A macOS host" should {
    "match as MacOs" in {
      assert(os("Mac OS X") === Some(MacOs))
    }
  }

  "A SunOS host" should {
    "not match" in {
      assert(os("SunOS") === None)
    }
  }
}
