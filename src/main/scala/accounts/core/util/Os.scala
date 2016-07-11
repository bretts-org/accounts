package accounts.core.util

import enumeratum._

sealed trait Os extends EnumEntry

object Os extends Enum[Os] {
  case object Linux extends Os
  case object MacOs extends Os
  case object Windows extends Os

  val values = findValues
}