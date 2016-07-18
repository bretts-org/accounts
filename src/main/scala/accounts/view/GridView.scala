package accounts.view

import java.time.LocalDate

import accounts.core.view.{CellFactory, View}
import accounts.record.{AccountType, IncomeType, TransactionType}
import accounts.viewmodel.{GridViewModel, RecordViewModel}

import scalafx.geometry.Pos
import scalafx.scene.control._
import scalafx.scene.control.TableColumn._

import GridView._

object GridView {
  // Ensure the sum of the max width of the columns is larger than the maximum reasonable window size
  val ColumnScaleFactor = 10
}

class GridView(vm: GridViewModel) extends View {

  val content = new TableView[RecordViewModel](vm.records) {
    columnResizePolicy = TableView.ConstrainedResizePolicy
    vm.records.comparator <== comparator

    columns += new TableColumn[RecordViewModel, LocalDate] {
      text = "Date"
      cellValueFactory = {
        _.value.date
      }
      comparator = Ordering.fromLessThan(_.isBefore(_))
      cellFactory = CellFactory[LocalDate](View.formatDate(_))
      maxWidth = 120 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, Int] {
      text = "Reference"
      cellValueFactory = {
        _.value.reference
      }
      comparator = Ordering[Int]
      cellFactory = CellFactory[Int](_.toString)
      maxWidth = 100 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, Option[TransactionType]] {
      text = "Transaction Type"
      cellValueFactory = {
        _.value.transactionType
      }
      comparator = Ordering.by(_.map(_.displayString))
      cellFactory = CellFactory.optionalWithTooltip[TransactionType](
        _.displayString,
        tt => s"Code: ${tt.value}"
      )
      maxWidth = 300 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, Option[IncomeType]] {
      text = "Income Type"
      cellValueFactory = {
        _.value.incomeType
      }
      cellFactory = CellFactory.optional[IncomeType](_.displayString)
      maxWidth = 150 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, BigDecimal] {
      text = "Credit"
      cellValueFactory = {
        _.value.credit
      }
      comparator = Ordering[BigDecimal]
      cellFactory = CellFactory[BigDecimal](View.formatDecimal(_), alignment = Some(Pos.CenterRight))
      maxWidth = 100 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, BigDecimal] {
      text = "Debit"
      cellValueFactory = {
        _.value.debit
      }
      comparator = Ordering[BigDecimal]
      cellFactory = CellFactory[BigDecimal](View.formatDecimal(_), alignment = Some(Pos.CenterRight))
      maxWidth = 100 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, AccountType] {
      text = "Account"
      cellValueFactory = {
        _.value.accountType
      }
      cellFactory = CellFactory[AccountType](_.toString)
      maxWidth = 100 * ColumnScaleFactor
    }
    columns += new TableColumn[RecordViewModel, String] {
      text = "Description"
      cellValueFactory = {
        _.value.description
      }
      maxWidth = 300 * ColumnScaleFactor
    }
  }

}
