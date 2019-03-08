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
    map(fa)(_ => ())

  def compose[G[_]](implicit G: Functor[G]): Functor[Lambda[X => F[G[X]]]] =
    new Functor[Lambda[X => F[G[X]]]] {
      def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
        self.map(fga)(ga => G.map(ga)(a => f(a)))
    }

}

object Functor {

  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  // F[_}に対してFunctionは2つの型パラメータを取るのでそのままでは指定できない
  // そこで、partially appliedを使うが記述が冗長なので、Kind Projectorで簡潔な技術をするみたいな
  // https://youtu.be/Dsd4pc99FSY?list=PLFrwDVdSrYE6dy14XCmUtRAJuhCxuzJp0&t=2300
  implicit def function1Functor[X]: Functor[X => ?] = new Functor[X => ?] {
    def map[A, B](fa: X => A)(f: A => B): X => B = fa andThen f
  }

//  implicit def function1Functor[X]: Functor[({ type l[a] = Function1[X, a] })#l] =
//    new Functor[({ type l[a] = Function1[X, a] })#l] {
//      def map[A, B](fa: X => A)(f: A => B): X => B = fa andThen f
//    }

}

trait FunctorLaws[F[_]] {

  import Equal.IsEq._

  implicit def F: Functor[F]

  def identity[A](fa: F[A]) =
    F.map(fa)(a => a) =?= fa

  def composition[A, B, C](fa: F[A], f: A => B, g: B => C) =
    F.map(F.map(fa)(f))(g) =?= F.map(fa)(f andThen g)

}

object FunctorLaws {
  def apply[F[_]](implicit F0: Functor[F]): FunctorLaws[F] = new FunctorLaws[F] {
    def F = F0
  }
}
