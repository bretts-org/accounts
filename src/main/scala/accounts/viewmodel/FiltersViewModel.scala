package accounts.viewmodel

import java.time.{LocalDate, Month}

import accounts.core.viewmodel.ViewModel
import accounts.core.viewmodel.ViewModel._
import accounts.model.FiltersModel
import accounts.record.{AccountType, TransactionCategory, TransactionType}

import scalafx.collections.ObservableBuffer

class FiltersViewModel(filters: FiltersModel) extends ViewModel {

  val accountTypeFilters = ObservableBuffer(
    None +: AccountType.values.map(Some(_))
  )
  val accountTypeFilter = Binding[Option[AccountType]](filters.accountTypeFilter) {
    filters.accountTypeFilter = _
  }

  val includeOneOffs = Binding.boolean(filters.includeOneOffs) {
    filters.includeOneOffs = _
  }

  val textFilter = Property[Option[Int]](None)
  textFilter.onUiChange { o =>
    o match {
      case Some(i) if i < 100 =>
        transactionCategoryFilter() = Some(TransactionCategory.withValue(i))
        transactionTypeFilter() = None
      case Some(i) =>
        val tt = TransactionType.withValue(i)
        transactionCategoryFilter() = Some(tt.category)
        transactionTypeFilter() = Some(tt)
      case None =>
        transactionCategoryFilter() = None
        transactionTypeFilter() = None
    }
  }

  val transactionCategoryFilters = ObservableBuffer(
    None +: TransactionCategory.values.sortBy(_.value).map(Some(_))
  )
  val transactionCategoryFilter = Binding[Option[TransactionCategory]](filters.transactionCategoryFilter) {
    filters.transactionCategoryFilter = _
  }
  transactionCategoryFilter.onUiChange { tc =>
    textFilter() = tc.map(_.value)
    transactionTypeFilter() = None
  }

  val transactionTypeFilters = ObservableBuffer(
    None +: TransactionType.values.sortBy(_.value).map(Some(_))
  )
  val transactionTypeFilter = Binding[Option[TransactionType]](filters.transactionTypeFilter) {
    filters.transactionTypeFilter = _
  }
  transactionTypeFilter.onUiChange(_ match {
    case Some(tt) =>
      textFilter() = Some(tt.value)
      transactionCategoryFilter() = Some(tt.category)
    case None =>
      textFilter() = transactionCategoryFilter().map(_.value)
  })

  val startDateFilter = Binding[Option[LocalDate]](filters.startDateFilter) {
    filters.startDateFilter = _
  }
  startDateFilter.onUiChange { d =>
    if (d.isDefined) {
      monthFilter() = None
      yearFilter() = None
    }
  }

  val endDateFilter = Binding[Option[LocalDate]](filters.endDateFilter) {
    filters.endDateFilter = _
  }
  endDateFilter.onUiChange { d =>
    if (d.isDefined) {
      monthFilter() = None
      yearFilter() = None
    }
  }

  val monthFilters = ObservableBuffer(
    None +: Month.values.map(Some(_)).toSeq
  )
  val monthFilter = Property[Option[Month]](None)
  monthFilter.onUiChange { m =>
    updateStartEndFilters(m, yearFilter())
  }

  val yearFilter = Property[Option[Int]](None)
  yearFilter.onUiChange { y =>
    updateStartEndFilters(monthFilter(), y)
  }

  private def updateStartEndFilters(month: Option[Month], year: Option[Int]): Unit = (month, year) match {
    case (Some(m), Some(y)) =>
      val monthStart = LocalDate.of(y, m, 1)
      startDateFilter() = Some(monthStart)
      endDateFilter() = Some(monthStart.withDayOfMonth(monthStart.lengthOfMonth))
    case (None, Some(y)) =>
      startDateFilter() = Some(LocalDate.of(y, 1, 1))
      endDateFilter() = Some(LocalDate.of(y, 12, 31))
    case _ =>
      startDateFilter() = None
      endDateFilter() = None
  }
}
