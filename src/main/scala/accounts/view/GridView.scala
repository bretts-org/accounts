package accounts.view

import java.time.{LocalDate, Month}

import accounts.core.view.{CellFactory, View}
import accounts.record.{AccountType, IncomeType, TransactionCategory, TransactionType}
import accounts.viewmodel.{GridViewModel, RecordViewModel}

import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.control.TableColumn._
import scalafx.scene.layout._
import scalafx.util.StringConverter

object GridView {
  val WindowWidth = 1000
  val WindowHeight = 800
  val HeaderHeight = 100
  val GridHeight = WindowHeight - HeaderHeight

  val NumericMonthRegex = "([0-9]{1,2})".r
}

import GridView._

class GridView(vm: GridViewModel) extends View {
  val stage = new PrimaryStage {
    title = "Cortijo Rosario Accounts"

    scene = new Scene(width = WindowWidth, height = WindowHeight) {
      root = new BorderPane {

        val textFilterField = new TextField {
          textFormatter = new TextFormatter(StringConverter[Option[Int]](
            Some(_).filter(!_.isEmpty).map(_.toInt),
            _.map(_.toString).getOrElse("")
          )) {
            value <==> vm.textFilter
          }
        }

        val transactionCategoryCombo = new ComboBox[Option[TransactionCategory]](vm.transactionCategoryFilters) {
          converter = StringConverter.toStringConverter {
            case None => "All"
            case Some(tc) => tc.displayString
          }
          value <==> vm.transactionCategoryFilter
        }

        val transactionTypeCombo = new ComboBox[Option[TransactionType]](vm.transactionTypeFilters) {
          converter = StringConverter.toStringConverter {
            case None => "All"
            case Some(tt) => tt.displayString
          }
          value <==> vm.transactionTypeFilter
        }

        val header = new HBox {
          children = Seq(
            textFilterField,
            transactionCategoryCombo,
            transactionTypeCombo,
            new DatePicker {
              promptText = "Start Date"
              value <==> vm.startDateFilter
            },
            new DatePicker {
              promptText = "End Date"
              value <==> vm.endDateFilter
            },
            new ComboBox[Option[Month]](vm.monthFilters) {
              editable = true
              promptText = "Month"
              converter = StringConverter[Option[Month]]({
                Some(_).filter(s => !s.isEmpty && !s.equalsIgnoreCase("All")).map {
                  case NumericMonthRegex(s) => Month.of(s.toInt)
                  case s => Month.valueOf(s.toUpperCase)
                }
              },
                {
                  case None => "All"
                  case Some(m) => m.toString.toLowerCase.capitalize
                })
              // Workaround for https://bugs.openjdk.java.net/browse/JDK-8129400
              focused.onChange { (_, _, focusGained) =>
                if (focusGained) {
                  Platform.runLater {
                    editor().selectAll()
                  }
                }
              }
              value <==> vm.monthFilter
            },
            new TextField {
              promptText = "Year"
              textFormatter = new TextFormatter(StringConverter[Option[Int]](
                Some(_).filter(!_.isEmpty).map(_.toInt),
                _.map(_.toString).getOrElse("")
              )) {
                value <==> vm.yearFilter
              }
            }
          )
        }
        top = header

        val table = new TableView[RecordViewModel](vm.records) {
          columnResizePolicy = TableView.ConstrainedResizePolicy

          columns += new TableColumn[RecordViewModel, LocalDate] {
            text = "Date"
            cellValueFactory = { _.value.date }
            comparator = Ordering.fromLessThan(_.isBefore(_))
            cellFactory = CellFactory[LocalDate](RecordViewModel.formatDate(_))
            maxWidth = 120
          }
          columns += new TableColumn[RecordViewModel, Option[TransactionType]] {
            text = "Type"
            cellValueFactory = { _.value.transactionType }
            comparator = Ordering.by(_.map(_.displayString))
            cellFactory = CellFactory.optionalWithTooltip[TransactionType](
              _.displayString,
              TransactionType.toInt(_).toString
            )
            maxWidth = 300
          }
          columns += new TableColumn[RecordViewModel, String] {
            text = "Description"
            cellValueFactory = { _.value.description }
            maxWidth = 300
          }
          columns += new TableColumn[RecordViewModel, Option[IncomeType]] {
            text = "Income Type"
            cellValueFactory = { _.value.incomeType }
            cellFactory = CellFactory.optional[IncomeType](_.displayString)
            maxWidth = 150
          }
          columns += new TableColumn[RecordViewModel, Option[AccountType]] {
            text = "Account"
            cellValueFactory = { _.value.accountType }
            cellFactory = CellFactory.optional[AccountType](_.toString)
            maxWidth = 100
          }
          columns += new TableColumn[RecordViewModel, Option[Int]] {
            text = "Reference"
            cellValueFactory = { _.value.reference }
            comparator = Ordering[Option[Int]]
            cellFactory = CellFactory.optional[Int](_.toString)
            maxWidth = 100
          }
          columns += new TableColumn[RecordViewModel, BigDecimal] {
            text = "Credit"
            cellValueFactory = { _.value.credit }
            comparator = Ordering[BigDecimal]
            cellFactory = CellFactory[BigDecimal](RecordViewModel.formatDecimal(_))
            maxWidth = 100
          }
          columns += new TableColumn[RecordViewModel, BigDecimal] {
            text = "Debit"
            cellValueFactory = { _.value.debit }
            comparator = Ordering[BigDecimal]
            cellFactory = CellFactory[BigDecimal](RecordViewModel.formatDecimal(_))
            maxWidth = 100
          }
        }
        center = table
      }
    }
  }

  // Hack to workaround dodgy column alignment issue
  stage.width = stage.width() + 200
  stage.sizeToScene()
  stage.resizable = false
  stage.resizable = true
  stage.delegate.setWidth(stage.width() + 200)
}
