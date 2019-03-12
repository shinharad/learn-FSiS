import org.scalacheck.Prop._
import org.scalacheck._

import scala.language.higherKinds

abstract class ApplicativeInstanceTest[F[_]](name: String)(implicit
                                                           F: Applicative[F],
                                                           arbFInt: Arbitrary[F[Int]],
                                                           eqFInt: Equal[F[Int]],
                                                           eqFString: Equal[F[String]])
    extends Properties(s"Applicative[$name]") {

  val laws = ApplicativeLaws[F]

  property("applicative identity") = forAll { xs: F[Int] =>
    laws.applicativeIdentity(xs).isEqual
  }

  property("applicative homomorphism") = forAll { (a: Int, f: Int => String) =>
    laws.applicativeHomomorphism(a, f).isEqual
  }
}

object ListApplicativeTest   extends ApplicativeInstanceTest[List]("List")
object OptionApplicativeTest extends ApplicativeInstanceTest[Option]("Option")

