package accounts.viewmodel

import accounts.core.viewmodel.ViewModel
import accounts.core.viewmodel.ViewModel.{Binding, CalculatedProperty}
import accounts.model.AddRecordModel
import accounts.record.{AccountType, IncomeType, TransactionType}

import scalafx.scene.control.Tooltip

class AddRecordViewModel(model: AddRecordModel) extends ViewModel {

  val transactionTypes = TransactionType.values.sortBy(TransactionType.toInt).map(Some(_))
  val incomeTypes = IncomeType.values.map(Some(_))
  val accountTypes = AccountType.values.map(Some(_))

  val date = Binding(model.date)(model.date = _)
  val description = Binding(model.description)(model.description = _)
  val debit = Binding(model.debit)(model.debit = _)
  val credit = Binding(model.credit)(model.credit = _)
  val transactionType = Binding(model.transactionType)(model.transactionType = _)
  val incomeType = Binding(model.incomeType)(model.incomeType = _)
  val reference = Binding(model.reference)(model.reference = _)
  val accountType = Binding(model.accountType)(model.accountType = _)

  val transactionTypeTooltip = CalculatedProperty {
    model.transactionType.map(tt => Tooltip(s"Code: ${TransactionType.toInt(tt)}").delegate)
  }

  val incomplete = CalculatedProperty.boolean(!model.complete)

  def save(): Unit = {
    try {
      vmState.updating = true
      model.save()
      model.reset()
      date() = model.date
      description() = model.description
      debit() = model.debit
      credit() = model.credit
      transactionType() = model.transactionType
      incomeType() = model.incomeType
      reference() = model.reference
      accountType() = model.accountType
      vmState.refresh()
    } finally {
      vmState.updating = false
    }
  }

}
