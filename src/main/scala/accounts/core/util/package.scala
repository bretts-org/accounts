package accounts.core

import _root_.cats.data.OneAnd
package object util {

  val seq = accounts.core.cats.std.seq

  type NonEmptySeq[A] = OneAnd[Seq, A]
  def NonEmptySeq[A](head: A, tail: A*): NonEmptySeq[A] = OneAnd(head, tail)
  def NonEmptySeq[A](head: A, tail: Iterable[A]): NonEmptySeq[A] = OneAnd(head, tail.toSeq)

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
