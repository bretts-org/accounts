package accounts.core.cats.std

import cats._
import cats.syntax.show._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.language.higherKinds

trait SeqInstances extends accounts.core.cats.kernel.std.SeqInstances {

  implicit val seqInstance: Traverse[Seq] with MonadCombine[Seq] with CoflatMap[Seq] =
    new Traverse[Seq] with MonadCombine[Seq] with CoflatMap[Seq] {

      def empty[A]: Seq[A] = Seq()

      def combineK[A](x: Seq[A], y: Seq[A]): Seq[A] = x ++ y

      def pure[A](x: A): Seq[A] = x +: Seq()

      override def map[A, B](fa: Seq[A])(f: A => B): Seq[B] =
        fa.map(f)

      def flatMap[A, B](fa: Seq[A])(f: A => Seq[B]): Seq[B] =
        fa.flatMap(f)

      override def map2[A, B, Z](fa: Seq[A], fb: Seq[B])(f: (A, B) => Z): Seq[Z] =
        fa.flatMap(a => fb.map(b => f(a, b)))

      def coflatMap[A, B](fa: Seq[A])(f: Seq[A] => B): Seq[B] = {
        @tailrec def loop(buf: mutable.Buffer[B], as: Seq[A]): Seq[B] =
          as match {
            case Seq() => buf.toSeq
            case _ +: rest => loop(buf += f(as), rest)
          }
        loop(mutable.Buffer.empty[B], fa)
      }

      def foldLeft[A, B](fa: Seq[A], b: B)(f: (B, A) => B): B =
        fa.foldLeft(b)(f)

      def foldRight[A, B](fa: Seq[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
        def loop(as: Seq[A]): Eval[B] =
          as match {
            case Seq() => lb
            case h +: t => f(h, Eval.defer(loop(t)))
          }
        Eval.defer(loop(fa))
      }

      def traverse[G[_], A, B](fa: Seq[A])(f: A => G[B])(implicit G: Applicative[G]): G[Seq[B]] =
        foldRight[A, G[Seq[B]]](fa, Always(G.pure(Seq.empty))){ (a, lglb) =>
          G.map2Eval(f(a), lglb)(_ +: _)
        }.value

      override def exists[A](fa: Seq[A])(p: A => Boolean): Boolean =
        fa.exists(p)

      override def forall[A](fa: Seq[A])(p: A => Boolean): Boolean =
        fa.forall(p)

      override def isEmpty[A](fa: Seq[A]): Boolean = fa.isEmpty
    }

  implicit def seqShow[A:Show]: Show[Seq[A]] =
    new Show[Seq[A]] {
      def show(fa: Seq[A]): String = fa.map(_.show).mkString("Seq(", ", ", ")")
    }
}

