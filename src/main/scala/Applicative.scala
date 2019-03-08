import simulacrum._
import scala.language.higherKinds
import scala.language.implicitConversions

@typeclass trait Applicative[F[_]] extends Functor[F] {

  def pure[A](a: A): F[A]

  def apply[A, B](fa: F[A])(ff: F[A => B]): F[B]

  override def map[A, B](fa: F[A])(f: A => B): F[B] =
    apply(fa)(pure(f))

  // f(_, b): A => Z
  // map(fb)(b => f(_, b)): F[A => Z]
  def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
    apply(fa)(map(fb)(b => f(_, b)))


}

object Applicative {

  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
    def pure[A](a: A): Option[A] = Some(a)
    def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] = (fa, ff) match {
      case (None, _)          => None
      case (Some(a), None)    => None
      case (Some(a), Some(f)) => pure(f(a))
    }
  }

  implicit val listApplicative: Applicative[List] = new Applicative[List] {
    def pure[A](a: A): List[A] = List(a)
    def apply[A, B](fa: List[A])(ff: List[A => B]): List[B] =
      for {
        a <- fa
        f <- ff
      } yield f(a)
  }

}

//@typeclass trait Applicative[F[_]] extends Functor[F] { self =>
//
//  def pure[A](a: A): F[A]
//
//  def apply[A, B](fa: F[A])(ff: F[A => B]): F[B]
//
//  def apply2[A, B, Z](fa: F[A], fb: F[B])(ff: F[(A, B) => Z]): F[Z] =
//    apply(fa)(apply(fb)(map(ff)(f => b => a => f(a, b))))
//
//  override def map[A, B](fa: F[A])(f: A => B): F[B] =
//    apply(fa)(pure(f))
//
//  def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] =
//    apply(fa)(map(fb)(b => f(_, b)))
//
//  def map3[A, B, C, Z](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => Z): F[Z] =
//    apply(fa)(map2(fb, fc)((b, c) => a => f(a, b, c)))
//
//  def map4[A, B, C, D, Z](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => Z): F[Z] =
//    map2(tuple2(fa, fb), tuple2(fc, fd)) { case ((a, b), (c, d)) => f(a, b, c, d) }
////    apply(fa)(map3(fb, fc, fd)((b, c, d) => a => f(a, b, c, d)))
//
//  def tuple2[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
//    map2(fa, fb)((a, b) => (a, b))
//
//  def tuple3[A, B, C](fa: F[A], fb: F[B], fc: F[C]): F[(A, B, C)] =
//    map3(fa, fb, fc)((a, b, c) => (a, b, c))
//
//  // 第一引数を ff: F[A => B] にするとSimuracrumのコンパイルエラーになる
//  def flip[A, B]()(ff: F[A => B]): F[A] => F[B] =
//    fa => apply(fa)(ff)
//
//  def compose[G[_]](implicit G: Applicative[G]): Applicative[Lambda[X => F[G[X]]]] =
//    new Applicative[Lambda[X => F[G[X]]]] {
//      def pure[A](a: A): F[G[A]] = self.pure(G.pure(a))
//      def apply[A, B](fga: F[G[A]])(ff: F[G[A => B]]): F[G[B]] = {
//        val x: F[G[A] => G[B]] = self.map(ff)(gab => G.flip()(gab))
//        // flipを使わないのならこれ
////        val x: F[G[A] => G[B]] = self.map(ff)(gab => ff => G.apply(ff)(gab))
//        self.apply(fga)(x)
//      }
//    }
//
//}
//
//object Applicative {
//
//  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
//    def pure[A](a: A): Option[A] = Some(a)
//    def apply[A, B](fa: Option[A])(ff: Option[A => B]): Option[B] = (fa, ff) match {
//      case (None, _)          => None
//      case (Some(a), None)    => None
//      case (Some(a), Some(f)) => Some(f(a))
//    }
//  }
//  implicit val listApplicative: Applicative[List] = new Applicative[List] {
//    def pure[A](a: A): List[A] = List(a)
//    def apply[A, B](fa: List[A])(ff: List[A => B]): List[B] =
//      for {
//        a <- fa
//        f <- ff
//      } yield f(a)
//  }
//}
//
//trait ApplicativeLaws[F[_]] {
//
//  import Applicative.ops._
//  import Equal.IsEq._
//
//  implicit def F: Applicative[F]
//
//  def applicativeIdentity[A](fa: F[A]) =
//    fa.apply(F.pure((a: A) => a)) =?= fa
//
//  def applicativeHomomorphism[A, B](a: A, f: A => B) =
//    F.pure(a).apply(F.pure(f)) =?= F.pure(f(a))
//
//  def applicativeInterchange[A, B](a: A, ff: F[A => B]) =
//    F.pure(a).apply(ff) =?= ff.apply(F.pure((f: A => B) => f(a)))
//
//  def applicativeMap[A, B](fa: F[A], f: A => B) =
//    fa.map(f) =?= fa.apply(F.pure(f))
//
//}
//
//object ApplicativeLaws {
//
//  def apply[F[_]](implicit F0: Applicative[F]): ApplicativeLaws[F] = new ApplicativeLaws[F] {
//    def F = F0
//  }
//
//}
