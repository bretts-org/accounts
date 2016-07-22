package accounts.record.repository

import accounts.record.AccountType.{Hotel, House}
import accounts.record.IncomeType.{Cash, Cheque, DirectDebit}
import accounts.record.TransactionCategory._
import accounts.record.TransactionType._
import accounts.record.repository.file.{Add, Delete, Delta}
import accounts.record.{OpeningBalance, Record, Transaction}
import accounts.test.util.TestUtils._

import scala.collection.mutable

object RecordRepositoryStub {
  val all: Seq[Record] = Seq(
    OpeningBalance(date("2015-12-01"), "opening balance" , debit = 123.45, credit = 0 , 1, Hotel),
    Transaction(date("2015-12-23"), "Cheeseburgers", Unknown(18, Food), debit = 252.75, credit = 0, Cash, 2, Hotel),
    Transaction(date("2016-04-02"), "Vodafone", Phone, debit = 313.10, credit = 0, DirectDebit, 3, Hotel),
    Transaction(date("2016-04-15"), "Cornettos", IceCream, debit = 0, credit = 54.50, Cash, 4, Hotel),
    Transaction(date("2016-05-06"), "Sausages", Generic(Food), debit = 26.42, credit = 0, Cheque, 5, House),
    Transaction(date("2016-05-08"), "Orange Juice", OrangeJuiceOrMilk, debit = 36.42, credit = 0, Cash, 5, Hotel),
    Transaction(date("2016-05-12"), "Roof repair", CortijoExteriorOneOff, debit = 842.63, credit = 0, Cash, 6, Hotel)
  )
}

class RecordRepositoryStub extends RecordRepository {

  override def all: Seq[Record] = deltas.foldLeft(loaded)((accum, d) => d match {
    case Add(r) => accum :+ r
    case Delete(r) => accum.filter(_.id != r.id)
  })

  val loaded: mutable.Buffer[Record] = RecordRepositoryStub.all.toBuffer

  val deltas: mutable.Buffer[Delta] = mutable.Buffer()

  override def add(r: Record): Unit = { deltas += Add(r) }

  override def delete(r: Record): Unit = { deltas += Delete(r) }
}
