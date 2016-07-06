package accounts.model

import accounts.record.TransactionCategory._
import accounts.record.TransactionType._
import accounts.record.repository.RecordRepositoryStub
import accounts.test.util.TestUtils._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, WordSpec}

import scala.language.reflectiveCalls

class FiltersModelTest extends WordSpec with Matchers with TypeCheckedTripleEquals {

  private def fixture = new {
    val records = new RecordRepositoryStub
    val filters = new FiltersModel
    val model = new GridModel(records, filters)
  }

  "A filter model" should {
    val all = RecordRepositoryStub.all
    val onlyHotel = all.slice(0, 4) ++ all.slice(5, 7)

    "include only hotel records by default" in {
      val f = fixture
      assert(f.model.records === onlyHotel)
    }

    "not apply any filters when account filter is cleared" in {
      val f = fixture
      f.filters.accountTypeFilter = None
      assert(f.model.records === all)
    }

    "filter on standard transaction types" in {
      val f = fixture
      f.filters.transactionTypeFilter = Some(IceCream)
      assert(f.model.records === Seq(all(3)))
    }

    "filter on unknown transaction types" in {
      val f = fixture
      f.filters.transactionTypeFilter = Some(Unknown(18, Food))
      assert(f.model.records === Seq(all(1)))
    }

    "filter on transaction categories" in {
      val f = fixture
      f.filters.transactionCategoryFilter = Some(Food)
      assert(f.model.records === Seq(all(1), all(5)))
    }

    "not filter on category if type is also specified" in {
      val f = fixture
      f.filters.transactionCategoryFilter = Some(Wages)
      f.filters.transactionTypeFilter = Some(OrangeJuiceOrMilk)
      assert(f.model.records === Seq(all(5)))
    }

    "exclude all brought forward records if no start date is specified" in {
      val f = fixture
      assert(f.model.filterAll(f.filters.broughtForwardPredicates) === Seq())
    }

    "filter inclusively on start date" in {
      val f = fixture
      f.filters.startDateFilter = Some(date("2016-04-15"))
      assert(f.model.records === onlyHotel.drop(3))
      assert(f.model.filterAll(f.filters.broughtForwardPredicates) === Seq(onlyHotel(2)))
    }

    "filter inclusively on end date" in {
      val f = fixture
      f.filters.endDateFilter = Some(date("2016-04-15"))
      assert(f.model.records === onlyHotel.take(4))
      assert(f.model.filterAll(f.filters.broughtForwardPredicates) === Seq())
    }

    "filter on start and end date" in {
      val f = fixture
      f.filters.startDateFilter = Some(date("2016-04-15"))
      f.filters.endDateFilter = Some(date("2016-05-11"))
      assert(f.model.records === onlyHotel.slice(3, 5))
      assert(f.model.filterAll(f.filters.broughtForwardPredicates) === Seq(onlyHotel(2)))
    }

    "filter on one-off items" in {
      val f = fixture
      f.filters.includeOneOffs = false
      assert(f.model.records === onlyHotel.dropRight(1))
    }
  }
}
