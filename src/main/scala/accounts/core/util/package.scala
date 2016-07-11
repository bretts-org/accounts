package accounts.core

import accounts.core.util.Os._

package object util {

  private[util] def os(osName: String): Option[Os] = osName.split(" ").head.toLowerCase match {
    case "linux" => Some(Linux)
    case "mac" => Some(MacOs)
    case "windows" => Some(Windows)
    case _ => None
  }

  def os: Option[Os] = os(sys.props("os.name"))

  implicit class TraversableOps[A](s: Seq[A]) {
    def singleOption: Option[A] = s match {
      case Seq() => None
      case Seq(a) => Some(a)
      case _ => throw new IllegalStateException(s"Expected 0 or 1 elements, but got: $s")
    }
  }

  implicit class TraversableTupleOps[A, +B](s: Seq[(A, B)]) {

    def groupValues: Map[A, Seq[B]] = groupValues(identity)

    def groupValues[C](f: Seq[B] => C): Map[A, C] =
      s.groupBy { case (k, v) => k }
       .mapValues(s => f(s.map { case (k, v) => v }))
  }
}
