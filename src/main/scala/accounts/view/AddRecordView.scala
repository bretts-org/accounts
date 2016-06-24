package accounts.view

import accounts.core.view.View
import accounts.record.{AccountType, IncomeType, TransactionType}
import accounts.viewmodel.{AddRecordViewModel, RecordViewModel}

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.stage.Stage
import scalafx.util.StringConverter

object AddRecordView {
  val PositiveIntRegex = "([0-9]+)".r
}

import AddRecordView._

class AddRecordView(vm: AddRecordViewModel) extends View {

  val button = new Button {
    text = "Add Transaction"
    onAction = handle {
      pnlWindow.showAndWait()
    }
  }

  val pnlWindow: Stage = new Stage {
    scene = new Scene {
      root = new GridPane {
        padding = Insets(top = 5, bottom = 20, left = 15, right = 15)
        hgap = 5
        vgap = 5

        add(new Label {
          text = "Add Transaction"
          style = "-fx-font-size: 16pt"
        }, columnIndex = 0, rowIndex = 0, colspan = 2, rowspan = 1)

        add(new Label {
          text = "Date:"
        }, columnIndex = 0, rowIndex = 1)
        add(new DatePicker {
          converter = RecordViewModel.dateConverter
          // Workaround for https://bugs.openjdk.java.net/browse/JDK-8129400
          focused.onChange { (_, _, focusGained) =>
            if (focusGained) {
              Platform.runLater {
                editor().selectAll()
              }
            }
          }
          value <==> vm.date
        }, columnIndex = 1, rowIndex = 1)

        add(new Label {
          text = "Reference:"
        }, columnIndex = 0, rowIndex = 2)
        add(new TextField {
          textFormatter = new TextFormatter(RecordViewModel.optionIntConverter) {
            value <==> vm.reference
          }
        }, columnIndex = 1, rowIndex = 2)

        add(new Label {
          text = "Transaction Type:"
        }, columnIndex = 0, rowIndex = 3)
        add(new ComboBox[Option[TransactionType]](vm.transactionTypes) {
          editable = true
          converter = StringConverter[Option[TransactionType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => TransactionType.fromInt(s.toInt)
              case s => TransactionType.values.filter(_.displayString.toLowerCase.contains(s.toLowerCase)).head
            }
          },
          {
            case None => ""
            case Some(tt) => tt.displayString
          })
          tooltip <== vm.transactionTypeTooltip
          value <==> vm.transactionType
        }, columnIndex = 1, rowIndex = 3)

        add(new Label {
          text = "Income Type:"
        }, columnIndex = 0, rowIndex = 4)
        add(new ComboBox[Option[IncomeType]](vm.incomeTypes) {
          editable = true
          converter = StringConverter[Option[IncomeType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => IncomeType.withValue(s.toInt)
              case s => IncomeType.values.filter(_.toString.toLowerCase.contains(s.toLowerCase)).head
            }
          },
            {
              case None => ""
              case Some(it) => it.displayString
            })
          value <==> vm.incomeType
        }, columnIndex = 1, rowIndex = 4)

        add(new Label {
          text = "Credit:"
        }, columnIndex = 0, rowIndex = 5)
        add(new TextField {
          textFormatter = new TextFormatter(RecordViewModel.optionPositiveBigDecimalConverter) {
            value <==> vm.credit
          }
        }, columnIndex = 1, rowIndex = 5)

        add(new Label {
          text = "Debit:"
        }, columnIndex = 0, rowIndex = 6)
        add(new TextField {
          textFormatter = new TextFormatter(RecordViewModel.optionPositiveBigDecimalConverter) {
            value <==> vm.debit
          }
        }, columnIndex = 1, rowIndex = 6)

        add(new Label {
          text = "Account Type:"
        }, columnIndex = 0, rowIndex = 7)
        add(new ComboBox[Option[AccountType]](vm.accountTypes) {
          editable = true
          converter = StringConverter[Option[AccountType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => AccountType.withValue(s.toInt)
              case s => AccountType.values.filter(_.toString.toLowerCase.contains(s.toLowerCase)).head
            }
          },
            {
              case None => ""
              case Some(at) => at.toString
            })
          value <==> vm.accountType
        }, columnIndex = 1, rowIndex = 7)

        add(new Label {
          text = "Description:"
        }, columnIndex = 0, rowIndex = 8)
        add(new TextField {
          textFormatter = new TextFormatter(RecordViewModel.optionStringConverter) {
            value <==> vm.description
          }
        }, columnIndex = 1, rowIndex = 8)

        add(new HBox {
          spacing = 5
          alignment = Pos.CenterRight
          children = Seq(
            new Button {
              text = "Next"
              defaultButton = true
              disable <== vm.incomplete
              focusTraversable = false
              onAction = handle {
                vm.save()
              }
            },
            new Button {
              text = "OK"
              disable <== vm.incomplete
              focusTraversable = false
              onAction = handle {
                close()
                vm.save()
              }
            },
            new Button {
              text = "Cancel"
              cancelButton = true
              focusTraversable = false
              onAction = handle {
                close()
              }
            }
          )
        }, columnIndex = 0, rowIndex = 9, colspan = 2, rowspan = 1)
      }
    }
  }
}
