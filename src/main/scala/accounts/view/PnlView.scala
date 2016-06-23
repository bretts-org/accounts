package accounts.view

import java.time.LocalDate

import accounts.core.view.{CellFactory, View}
import accounts.record.TransactionCategory
import accounts.viewmodel.{PnlSummaryViewModel, PnlViewModel, RecordViewModel}

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TableCell, TableColumn, TableView}
import scalafx.scene.control.TableColumn._
import scalafx.stage.Stage

object PnlView {
  val WindowWidth = 1200
  val WindowHeight = 900

  val ColumnScaleFactor = 10
}

import PnlView._

class PnlView(vm: PnlViewModel) extends View {

  val content = new Button {
    text = "P/L"
    padding = Insets(top = 0, bottom = 0, left = 0, right = 5)
    minWidth = 50
    onAction = handle {
      pnlWindow.show()
    }
  }


  val pnlWindow = new Stage {
    scene = new Scene(width = WindowWidth, height = WindowHeight) {
      root = new TableView[PnlSummaryViewModel](vm.all) {
        columnResizePolicy = TableView.ConstrainedResizePolicy

        columns += new TableColumn[PnlSummaryViewModel, String] {
          //text = ""
          cellValueFactory = {
            _.value.name
          }
          maxWidth = 100 * ColumnScaleFactor
        }

        vm.categories.foreach { tc =>
          columns += new TableColumn[PnlSummaryViewModel, BigDecimal] {
            text = tc.shortString
            cellValueFactory = {
              _.value.profit(tc)
            }
            comparator = Ordering[BigDecimal]
            cellFactory = CellFactory[BigDecimal](PnlViewModel.formatDecimal(_), alignment = Some(Pos.CenterRight))
            maxWidth = 100 * ColumnScaleFactor
          }
        }

        columns += new TableColumn[PnlSummaryViewModel, BigDecimal] {
          text = "Total"
          cellValueFactory = {
            _.value.totalProfit
          }
          comparator = Ordering[BigDecimal]
          cellFactory = _ => {
            new TableCell[PnlSummaryViewModel, BigDecimal] {
              item.onChange { (_, _, newValue) =>
                text = if (newValue != null) PnlViewModel.formatDecimal(newValue) else ""
                style = "-fx-font-weight: bold"
                alignment = Pos.CenterRight
              }
            }
          }
          maxWidth = 100 * ColumnScaleFactor
        }
      }
    }
  }
}
