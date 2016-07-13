package accounts.core.cats.kernel.std

import cats.kernel.{Eq, Monoid, Order, PartialOrder}

import scala.annotation.tailrec
import scala.collection.mutable

package object seq extends SeqInstances

trait SeqInstances extends SeqInstances1 {
  implicit def seqOrder[A: Order]: Order[Seq[A]] =
    new SeqOrder[A]
  implicit def seqMonoid[A]: Monoid[Seq[A]] =
    new SeqMonoid[A]
}

trait SeqInstances1 extends SeqInstances2 {
  implicit def seqPartialOrder[A: PartialOrder]: PartialOrder[Seq[A]] =
    new SeqPartialOrder[A]
}

trait SeqInstances2 {
  implicit def seqEq[A: Eq]: Eq[Seq[A]] =
    new SeqEq[A]
}

class SeqOrder[A](implicit ev: Order[A]) extends Order[Seq[A]] {
  def compare(xs: Seq[A], ys: Seq[A]): Int = {
    @tailrec def loop(xs: Seq[A], ys: Seq[A]): Int =
      xs match {
        case Seq() =>
          if (ys.isEmpty) 0 else -1
        case x +: xs =>
          ys match {
            case Seq() => 1
            case y +: ys =>
              val n = ev.compare(x, y)
              if (n != 0) n else loop(xs, ys)
          }
      }
    if (xs eq ys) 0 else loop(xs, ys)
  }
}

class SeqPartialOrder[A](implicit ev: PartialOrder[A]) extends PartialOrder[Seq[A]] {
  def partialCompare(xs: Seq[A], ys: Seq[A]): Double = {
    @tailrec def loop(xs: Seq[A], ys: Seq[A]): Double =
      xs match {
        case Seq() =>
          if (ys.isEmpty) 0.0 else -1.0
        case x +: xs =>
          ys match {
            case Seq() => 1.0
            case y +: ys =>
              val n = ev.partialCompare(x, y)
              if (n != 0.0) n else loop(xs, ys)
          }
      }
    if (xs eq ys) 0.0 else loop(xs, ys)
  }
}

class SeqEq[A](implicit ev: Eq[A]) extends Eq[Seq[A]] {
  def eqv(xs: Seq[A], ys: Seq[A]): Boolean = {
    def loop(xs: Seq[A], ys: Seq[A]): Boolean =
      xs match {
        case Seq() =>
          ys.isEmpty
        case x +: xs =>
          ys match {
            case y +: ys =>
              if (ev.eqv(x, y)) loop(xs, ys) else false
            case Seq() =>
              false
          }
      }
    (xs eq ys) || loop(xs, ys)
  }
}

class SeqMonoid[A] extends Monoid[Seq[A]] {
  def empty: Seq[A] = Seq()
  def combine(x: Seq[A], y: Seq[A]): Seq[A] = x ++ y

  override def combineN(x: Seq[A], n: Int): Seq[A] = {
    val buf = mutable.Buffer.empty[A]
    var i = n
    while (i > 0) {
      buf ++= x
      i -= 1
    }
    buf.toSeq
  }

  override def combineAll(xs: TraversableOnce[Seq[A]]): Seq[A] = {
    val buf = mutable.Buffer.empty[A]
    xs.foreach(buf ++= _)
    buf.toSeq
  }
}
