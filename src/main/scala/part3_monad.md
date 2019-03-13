# Monad

```scala
scala> Monad[List]
res0: Monad[List] = Monad$$anon$1@3fb02f53

scala> res0.flatMap(List(1, 2, 3))(x => List.fill(x)(x))
res1: List[Int] = List(1, 2, 2, 3, 3, 3)

scala> Monad[Option]
res2: Monad[Option] = Monad$$anon$2@65f0d41

scala> res2.flatMap(Option(1))(x => if (x > 0) Some(x) else None)
res3: Option[Int] = Some(1)

scala> res2.flatMap(None: Option[Int])(x => if (x > 0) Some(x) else None)
res4: Option[Int] = None
```