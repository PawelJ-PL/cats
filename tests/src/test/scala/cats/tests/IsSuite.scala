package cats
package tests

import cats.arrow._
import cats.kernel.laws.discipline.SerializableTests
import cats.laws.discipline.CategoryTests
import org.scalacheck.{Arbitrary, Gen}

class IsSuite extends CatsSuite {
  import evidence._

  implicit def arbIs[A, B](implicit ev: A Is B): Arbitrary[A Is B] = Arbitrary(Gen.const(ev))
  implicit def eqIs[A, B]: Eq[A Is B] = Eq.fromUniversalEquals

  trait Top {
    def foo: String = this.getClass.getName
  }
  case class Bottom() extends Top

  checkAll("Is[Bottom, Bottom]", CategoryTests[Is].category[Bottom, Bottom, Bottom, Bottom])
  checkAll("Category[Is]", SerializableTests.serializable(Category[Is]))

  test("syntax") {
    trait Bar

    val lifted: Bar Is Bar = Is.refl[Bar]
    val andThen: Leibniz[Bar, Bar] = lifted.andThen(lifted)
    val compose: Leibniz[Bar, Bar] = lifted.compose(lifted)
    val flip: Leibniz[Bar, Bar] = lifted.flip
    val lift: Leibniz[List[Bar], List[Bar]] = lifted.lift[List]
    val coerce: Bar = lifted.coerce(new Bar {})
    val predefEq: =:=[Bar, Bar] = lifted.predefEq
  }

}
