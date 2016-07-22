package accounts.model

import java.time.LocalDate

import accounts.core.util._
import accounts.record.{AccountType, IncomeType, Transaction, TransactionType}

class AddRecordModel(grid: GridModel, filters: FiltersModel) {

  private def defaultDate = Some(LocalDate.now)

  private def defaultReference = grid.all.map(_.reference).maxOption.map(_ + 1)
  private def defaultAccountType = filters.accountTypeFilter

  var date: Option[LocalDate] = defaultDate
  var description: Option[String] = None
  var transactionType: Option[TransactionType] = None
  var debit: Option[BigDecimal] = None
  var credit: Option[BigDecimal] = None
  var incomeType: Option[IncomeType] = None
  var reference: Option[Int] = defaultReference
  var accountType: Option[AccountType] = defaultAccountType

  private def validatedDebit = (debit.filter(_ > 0), credit.filter(_ > 0)) match {
    case (Some(d), None) => Some(d)
    case (None, Some(c)) => Some(BigDecimal(0))
    case _ => None
  }

  private def validatedCredit = (debit.filter(_ > 0), credit.filter(_ > 0)) match {
    case (Some(d), None) => Some(BigDecimal(0))
    case (None, Some(c)) => Some(c)
    case _ => None
  }


  private def transaction = for {
    d <- date
    desc <- description
    tt <- transactionType
    deb <- validatedDebit
    cred <- validatedCredit
    it <- incomeType
    ref <- reference
    at <- accountType
  } yield Transaction(d, desc, tt, deb, cred, it, ref, at)

  def complete: Boolean = transaction.isDefined

  def reset(): Unit = {
    date = defaultDate
    description = None
    transactionType = None
    debit = None
    credit = None
    incomeType = None
    reference = defaultReference
    accountType = defaultAccountType
  }

  def save(): Unit = transaction.foreach(grid.add)

}
