object Cats {


  case class IO[A]( f: () => A) {
    def executeImpure():A = f()
  }

  val e: IO[Int] = IO{ () =>
    Thread.sleep(10)
    println("lhjksd")
    23
  }

  //val f: IO[String] = e.map(x => x.toString)


  e.executeImpure()
  ////////////////////////////////////////////////////
  trait Functor[F[_]] {
    def pure[T](v: T): F[T]
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object FunctorLaws {
    def identityLaw[F[_], A](fa: F[A])(implicit functor: Functor[F]): Boolean =
      functor.map(fa)(identity) == fa

    def compositionLaw[F[_]: Functor, A, B, C](fa: F[A])(fab: A => B,  fbc: B => C): Boolean = {
      val func = implicitly[Functor[F]]
      func.map(func.map(fa)(fab))(fbc) == func.map(fa)( fab andThen fbc)
    }
  }


  trait Monad[F[_]] extends Functor[F] {
    def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
  }

  trait Applicative[F[_]] extends Functor[F]{
    def ap[A, B](fa: F[A])(f: F[A=>B]): F[B]

    def mapN[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] = {
      val r: F[B => Z] = ap(fa)(pure(f.curried)) // F[A => B => Z] applied to F[A] gives F[B => Z]
      ap(fb)(r) // Apply F[B => Z] to F[B] to get F[Z]
    }

    //def mapN[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z] = ap(fb)(ap(fa)(pure(f.curried)))
  }


  object OptionApplicative extends Applicative[Option] {

    override def ap[A, B](fa: Option[A])(f: Option[A => B]): Option[B] =
      (f, fa) match {
        case (Some(f), Some(a)) => Some(f(a))
        case _ => None
      }

    override def pure[T](v: T): Option[T] = Some(v)

    override def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa match {
        case Some(value) => Option(f(value))
        case None => None
      }
  }
}
