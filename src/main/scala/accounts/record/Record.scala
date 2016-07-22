package accounts.record

import java.time.LocalDate
import java.util.UUID

sealed trait Record {
  val id: UUID = UUID.randomUUID
  def date: LocalDate
  def description: String
  def debit: BigDecimal
  def credit: BigDecimal
  def reference: Int
  def accountType: AccountType
}

case class Transaction(
  date: LocalDate,
  description: String,
  transactionType: TransactionType,
  debit: BigDecimal,
  credit: BigDecimal,
  incomeType: IncomeType,
  reference: Int,
  accountType: AccountType
) extends Record

case class OpeningBalance(
  date: LocalDate,
  description: String,
  debit: BigDecimal,
  credit: BigDecimal,
  reference: Int,
  accountType: AccountType
) extends Record
