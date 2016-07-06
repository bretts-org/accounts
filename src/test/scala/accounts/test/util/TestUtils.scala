package accounts.test.util

import java.time.LocalDate

object TestUtils {
  def date(s: String): LocalDate = LocalDate.parse(s)
}
