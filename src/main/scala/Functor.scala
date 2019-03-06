import simulacrum._
import scala.language.higherKinds
import scala.language.implicitConversions

@typeclass trait Functor[F[_]] { self =>

  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)

  def as[A, B](fa: F[A], b: => B): F[B] =
    map(fa)(_ => b)

  def void[A](fa: F[A]): F[Unit] =
    as(fa, ())

  def compose[G[_]](implicit G: Functor[G]): Functor[Lambda[X => F[G[X]]]] =
    new Functor[Lambda[X => F[G[X]]]] {
      def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
        self.map(fga)(ga => G.map(ga)(a => f(a)))
    }

}

trait FunctorLaws {

  def identity[F[_], A](fa: F[A])(implicit F: Functor[F]) =
    F.map(fa)(a => a) == fa

  def composition[F[_], A, B, C](fa: F[A], f: A => B, g: B => C)(implicit F: Functor[F]) =
    F.map(F.map(fa)(f))(g) == F.map(fa)(f andThen g)

}

object Functor {

  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  implicit def function1Functor[X]: Functor[X => ?] = new Functor[X => ?] {
    def map[A, B](fa: X => A)(f: A => B): X => B = fa andThen f
  }

  // kind-projector
  //
  // scala> Functor[({type l[a] = Function1[Int, a]})#l]
  // res1: com.github.shinharad.part1.Functor[[a]Int => a] = com.github.shinharad.part1.Functor$$anon$4@2f7f0ca2
  //
  // scala> Functor[Function1[Int, ?]]
  // res2: com.github.shinharad.part1.Functor[[β$0$]Int => β$0$] = com.github.shinharad.part1.Functor$$anon$4@11e92d25
  //
  // scala> Functor[Lambda[X => Function1[Int, X]]]
  // res3: com.github.shinharad.part1.Functor[[X]Int => X] = com.github.shinharad.part1.Functor$$anon$4@6b96a73c

}
