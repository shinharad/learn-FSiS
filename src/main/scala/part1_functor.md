# FSiS Part 1 - Type Constructors, Functors, and Kind Projector

## Type Constructors

```scala
scala> val x: Int = 1
x: Int = 1

scala> val xs: List[Int] = List(1, 2, 3)
xs: List[Int] = List(1, 2, 3)

scala> :k Int
Int's kind is A

scala> :k List
List's kind is F[+A]

scala> val ys: List = ???
<console>:11: error: type List takes type parameters
       val ys: List = ???
               ^

scala> def foo[A](x: A, y: A) = ???
foo: [A](x: A, y: A)Nothing

scala> def bar[F[_], A](x: F[A], y: F[A]) = ???
<console>:11: warning: higher-kinded type should be enabled
by making the implicit value scala.language.higherKinds visible.
This can be achieved by adding the import clause 'import scala.language.higherKinds'
or by setting the compiler option -language:higherKinds.
See the Scaladoc for value scala.language.higherKinds for a discussion
why the feature should be explicitly enabled.
       def bar[F[_], A](x: F[A], y: F[A]) = ???
               ^
bar: [F[_], A](x: F[A], y: F[A])Nothing

scala> def bar[F[_], A](x: F[A], y: F[A]) = null
<console>:11: warning: higher-kinded type should be enabled
by making the implicit value scala.language.higherKinds visible.
This can be achieved by adding the import clause 'import scala.language.higherKinds'
or by setting the compiler option -language:higherKinds.
See the Scaladoc for value scala.language.higherKinds for a discussion
why the feature should be explicitly enabled.
       def bar[F[_], A](x: F[A], y: F[A]) = null
               ^
bar: [F[_], A](x: F[A], y: F[A])Null

scala> bar(List(1, 2, 3), List(1))
res0: Null = null

scala> bar(Some(1), Some(2))
res1: Null = null

scala> bar(Some(1), List(1, 2, 3))
<console>:13: error: inferred kinds of the type arguments (Product with Serializable,Int) do not conform to the expected kinds of the type parameters (type F,type A).
Product with Serializable's type parameters do not match type F's expected parameters:
<refinement of Product> has no type parameters, but type F has one
       bar(Some(1), List(1, 2, 3))
       ^
<console>:13: error: type mismatch;
 found   : Some[Int]
 required: F[A]
       bar(Some(1), List(1, 2, 3))
               ^
<console>:13: error: type mismatch;
 found   : List[Int]
 required: F[A]
       bar(Some(1), List(1, 2, 3))
                        ^

scala> bar(1, 2)
res3: Null = null
```


## Functor

```scala
scala> implicitly[Functor[List]]
res0: Functor[List] = Functor$$anon$2@622584cd

scala> res0.map(List(1, 2, 3))(_ + 1)
res1: List[Int] = List(2, 3, 4)

scala> implicitly[Functor[Option]].map(Some(1))(_ + 1)
res2: Option[Int] = Some(2)
```

## Kind Projector

```scala
scala> implicitly[Functor[Int => ?]]
res0: Functor[[β$0$]Int => β$0$] = Functor$$anon$4@34d77b6f

scala> res0.map(_ + 1)(_ + 2)
res1: Int => Int = scala.Function1$$Lambda$5515/1758247102@4ce988b6

scala> res1(5)
res2: Int = 8
```

## Simulacrum

```scala
scala> Functor[List]
res0: Functor[List] = Functor$$anon$2@684c3bb2

scala> Functor[Option]
res1: Functor[Option] = Functor$$anon$3@746577d

scala> import Functor.ops._
import Functor.ops._

scala> List(1, 2, 3).void
res2: List[Unit] = List((), (), ())

scala> List(1, 2, 3).as(10)
res3: List[Int] = List(10, 10, 10)

scala> List(1, 2, 3, 4, 5, 6).as(10)
res4: List[Int] = List(10, 10, 10, 10, 10, 10)
```

## compose

```scala
scala> Functor[List] compose Functor[Option]
res0: Functor[[X]List[Option[X]]] = Functor$$anon$1@36ec1b92

scala> val xs: List[Option[Int]] = List(Some(1), None, Some(2))
xs: List[Option[Int]] = List(Some(1), None, Some(2))

scala> xs.map(_ + 1)
<console>:16: error: type mismatch;
 found   : Int(1)
 required: String
       xs.map(_ + 1)
                  ^
scala> res0.map(xs)(_ + 1)
res2: List[Option[Int]] = List(Some(2), None, Some(3))
```

## Kind Projector

```scala
scala> def add3(x: Int, y: Int, z: Int) = x + y + z
add3: (x: Int, y: Int, z: Int)Int

scala> add3(1, 2, _)
res0: Int => Int = $$Lambda$5477/1677942727@744556de

scala> add3(1, 2, (_: Int))
res1: Int => Int = $$Lambda$5479/591241689@472fbb82

scala> res1(3)
res2: Int = 6

scala> res1(4)
res3: Int = 7

scala> add3 _
res4: (Int, Int, Int) => Int = $$Lambda$5506/1807403284@2f37bd25

scala> Function1[X, ?]
<console>:15: error: not found: value Function1
       Function1[X, ?]
       ^
<console>:15: error: not found: type X
       Function1[X, ?]
                 ^
<console>:15: error: not found: type ?
       Function1[X, ?]
                    ^
scala> Functor[({type l[a] = Function1[Int, a]})#l]
res1: Functor[[a]Int => a] = Functor$$anon$4@4e570bcd

scala> Functor[Function1[Int, ?]]
res2: Functor[[β$0$]Int => β$0$] = Functor$$anon$4@168e80ce

scala> Functor[Lambda[X => Function1[Int, X]]]
res3: Functor[[X]Int => X] = Functor$$anon$4@37e4ba58
```

