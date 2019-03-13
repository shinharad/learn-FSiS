import org.scalacheck.Prop._
import org.scalacheck._

import scala.language.higherKinds

abstract class MonadInstanceTest[F[_]](name: String)(
  implicit
  F: Monad[F],
  eqFInt: Equal[F[Int]],
  eqFStr: Equal[F[String]],
  eqFLong: Equal[F[Long]],
  arbFInt: Arbitrary[F[Int]],
  arbFString: Arbitrary[F[String]],
  arbFLong: Arbitrary[F[Long]],
) extends Properties(s"Monad[$name]") {

  val laws = MonadLaws[F]

  property("flatMap associativity") = forAll { (fa: F[Int], f: Int => F[String], g: String => F[Long]) =>
    laws.flatMapAssociativity(fa, f, g).isEqual
  }

  property("monad left identity") = forAll { (a: Int, f: Int => F[String]) =>
    laws.leftIdentity(a, f).isEqual
  }

  property("monad right identity") = forAll { fa: F[Int] =>
    laws.rightIdentity(fa).isEqual
  }
}

object ListMonadTest   extends MonadInstanceTest[List]("List")
object OptionMonadTest extends MonadInstanceTest[Option]("Option")
