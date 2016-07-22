package accounts.model


import accounts.record.AccountType.Hotel
import accounts.record.IncomeType.Card
import accounts.record.Transaction
import accounts.record.TransactionType._
import accounts.record.repository.RecordRepositoryStub
import accounts.record.repository.file.{Add, Delete, Delta}
import accounts.test.util.TestUtils._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable
import scala.language.reflectiveCalls

class GridModelTest extends WordSpec with Matchers with TypeCheckedTripleEquals {

  private def fixture = new {
    val records = new RecordRepositoryStub
    val filters = new FiltersModel
    val model = new GridModel(records, filters)
  }

  private val newTrans = Transaction(
    date("2016-05-19"), "Heating Oil", HeatingOil,
    debit = 563.20, credit = 0, Card, 7, Hotel
  )

  "A grid model" should {
    val all = RecordRepositoryStub.all
    val onlyHotel = all.slice(0, 4) ++ all.slice(5, 7)

    "include only hotel records by default" in {
      val f = fixture
      assert(f.model.records === onlyHotel)
    }

    "optionally return all records" in {
      val f = fixture
      f.filters.transactionTypeFilter = Some(IceCream)
      assert(f.model.all === all)
    }

    "not automatically update from the repository" in {
      val f = fixture
      f.records.loaded.remove(3)
      assert(f.model.records === onlyHotel)
    }

    "add records to the repository" in {
      val f = fixture
      f.model.add(newTrans)
      assert(f.records.deltas === mutable.Buffer[Delta](Add(newTrans)))
    }

    "update from the repository on add" in {
      val f = fixture
      f.records.loaded.remove(3)
      f.model.add(newTrans)
      assert(f.model.records === onlyHotel.patch(3, Seq(), 1) :+ newTrans)
    }

    "delete records from the repository" in {
      val f = fixture
      val deleted = all(2)
      f.model.delete(deleted)
      assert(f.records.deltas === mutable.Buffer[Delta](Delete(deleted)))
    }

    "update from the repository on delete" in {
      val f = fixture
      f.records.loaded.remove(3)
      f.model.delete(all(2))
      assert(f.model.records === onlyHotel.patch(2, Seq(), 2))
    }

  }
}
