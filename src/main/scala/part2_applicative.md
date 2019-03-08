# FSiS Part 2 - Applicative type class

```scala
scala> import Functor.ops._
import Functor.ops._

scala> Functor[List]
res0: Functor[List] = Functor$$anon$2@7864183a

scala> Functor[List].void(List(1, 2, 3))
res1: List[Unit] = List((), (), ())

scala> List(1, 2, 3).void
res2: List[Unit] = List((), (), ())

scala> Functor[List] compose Functor[Option] compose Functor[List]
res3: Functor[[X]List[Option[List[X]]]] = Functor$$anon$1@1520bad3
```

```scala
scala> Applicative[Option]
res0: Applicative[Option] = Applicative$$anon$1@16b82965

scala> Applicative[Option].map(None: Option[Int])(_ + 1)
res1: Option[Int] = None

scala> Applicative[List].map(List(1, 2, 3))(_ + 1)
res2: List[Int] = List(2, 3, 4)
```

```scala
scala> Applicative[Option].map2(Option(1), Option(2))(_ + _)
res0: Option[Int] = Some(3)

scala> Applicative[List].map2(List(1, 2, 3), List(4, 5, 6))(_ + _)
res1: List[Int] = List(5, 6, 7, 6, 7, 8, 7, 8, 9)
```

```scala
scala> Applicative[Option].map3(Option(1), Option(2), Option(3))(_ + _ + _)
res1: Option[Int] = Some(6)
```

```scala
scala> Applicative[Option].tuple2(Option(1), Option(2))
res0: Option[(Int, Int)] = Some((1,2))

scala> Applicative[List].tuple2(List(1, 2, 3), List(4, 5, 6))
res1: List[(Int, Int)] = List((1,4), (1,5), (1,6), (2,4), (2,5), (2,6), (3,4), (3,5), (3,6))
```

```scala
scala> Applicative[Option].map4(Option(1), Option(2), Option(3), Option(4))(_ + _ + _ + _)
res0: Option[Int] = Some(10)
```

```scala
scala> for { x <- Option(1); y <- Option(2); z <- Option(3) } yield x + y + z
res1: Option[Int] = Some(6)

scala> Option(1).flatMap { x => Option(2).flatMap { y => Option(3).map { z => x + y + z } } }
res3: Option[Int] = Some(6)
```

```scala
scala> Applicative[List] compose Applicative[Option]
res0: Applicative[[X]List[Option[X]]] = Applicative$$anon$1@7445753c

scala> res0.map2(List(Some(1), None, Some(2)), List(Some(2), Some(1)))(_ + _)
res1: List[Option[Int]] = List(Some(3), Some(2), None, None, Some(4), Some(3))
```


