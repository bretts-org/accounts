package accounts.view

import accounts.core.view.View
import accounts.record.{AccountType, IncomeType, TransactionType}
import accounts.viewmodel.AddRecordViewModel

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.stage.Stage
import scalafx.util.StringConverter

import AddRecordView._

object AddRecordView {
  val PositiveIntRegex = "([0-9]+)".r
}

class AddRecordView(vm: AddRecordViewModel) extends View {

  val button = new Button {
    id = "addTransactionButton"
    text = "Add Transaction"
    onAction = handle {
      vm.reset()
      window.show()
      datePicker.requestFocus()
    }
  }

  private val datePicker = new DatePicker {
    id = "addRecordDatePicker"
    converter = View.dateConverter
    focused.onChange(View.selectOnFocus(editor()) _)
    value <==> vm.date
  }

  val window: Stage = new Stage {
    title = "Add Record"
    scene = new Scene {
      root = new GridPane {
        padding = Insets(top = 5, bottom = 15, left = 15, right = 15)
        hgap = 5
        vgap = 5

        add(new Label {
          text = "Add Transaction"
          style = "-fx-font-size: 16pt"
        }, columnIndex = 0, rowIndex = 0, colspan = 2, rowspan = 1)

        add(new Label {
          text = "Date:"
        }, columnIndex = 0, rowIndex = 1)
        add(datePicker, columnIndex = 1, rowIndex = 1)

        add(new Label {
          text = "Reference:"
        }, columnIndex = 0, rowIndex = 2)
        add(new TextField {
          id = "addRecordReferenceField"
          textFormatter = new TextFormatter(View.optionIntConverter) {
            value <==> vm.reference
          }
        }, columnIndex = 1, rowIndex = 2)

        add(new Label {
          text = "Transaction Type:"
        }, columnIndex = 0, rowIndex = 3)
        add(new ComboBox[Option[TransactionType]](vm.transactionTypes) {
          id = "addRecordTransactionTypeCombo"
          editable = true
          converter = StringConverter[Option[TransactionType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => TransactionType.withValue(s.toInt)
              case s => TransactionType.withNameOption(s).getOrElse {
                TransactionType.values.sortBy(_.displayString)
                  .find(_.displayString.toLowerCase.contains(s.toLowerCase))
                  .getOrElse(throw new IllegalArgumentException(s"Unrecognised type: $s"))
              }
            }
          },
          {
            case None => ""
            case Some(tt) => tt.displayString
          })
          focused.onChange(View.selectOnFocus(editor()) _)
          tooltip <== vm.transactionTypeTooltip
          value <==> vm.transactionType
        }, columnIndex = 1, rowIndex = 3)

        add(new Label {
          text = "Income Type:"
        }, columnIndex = 0, rowIndex = 4)
        add(new ComboBox[Option[IncomeType]](vm.incomeTypes) {
          id = "addRecordIncomeTypeCombo"
          editable = true
          converter = StringConverter[Option[IncomeType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => IncomeType.withValue(s.toInt)
              case s =>
                IncomeType.values.sortBy(_.displayString)
                  .find(_.displayString.toLowerCase.contains(s.toLowerCase))
                  .getOrElse(throw new IllegalArgumentException(s"Unrecognised type: $s"))
            }
          },
          {
            case None => ""
            case Some(it) => it.displayString
          })
          focused.onChange(View.selectOnFocus(editor()) _)
          value <==> vm.incomeType
        }, columnIndex = 1, rowIndex = 4)

        add(new Label {
          text = "Credit:"
        }, columnIndex = 0, rowIndex = 5)
        add(new TextField {
          id = "addRecordCreditField"
          textFormatter = new TextFormatter(View.optionPositiveBigDecimalConverter) {
            value <==> vm.credit
          }
        }, columnIndex = 1, rowIndex = 5)

        add(new Label {
          text = "Debit:"
        }, columnIndex = 0, rowIndex = 6)
        add(new TextField {
          id = "addRecordDebitField"
          textFormatter = new TextFormatter(View.optionPositiveBigDecimalConverter) {
            value <==> vm.debit
          }
        }, columnIndex = 1, rowIndex = 6)

        add(new Label {
          text = "Account Type:"
        }, columnIndex = 0, rowIndex = 7)
        add(new ComboBox[Option[AccountType]](vm.accountTypes) {
          id = "addRecordAccountTypeCombo"
          editable = true
          converter = StringConverter[Option[AccountType]]({
            Option(_).filter(!_.isEmpty).map {
              case PositiveIntRegex(s) => AccountType.withValue(s.toInt)
              case s =>
                AccountType.values.sortBy(_.toString)
                  .find(_.toString.toLowerCase.contains(s.toLowerCase))
                  .getOrElse(throw new IllegalArgumentException(s"Unrecognised type: $s"))
            }
          },
          {
            case None => ""
            case Some(at) => at.toString
          })
          focused.onChange(View.selectOnFocus(editor()) _)
          value <==> vm.accountType
        }, columnIndex = 1, rowIndex = 7)

        add(new Label {
          text = "Description:"
        }, columnIndex = 0, rowIndex = 8)
        add(new TextField {
          id = "addRecordDescriptionField"
          textFormatter = new TextFormatter(View.optionStringConverter) {
            value <==> vm.description
          }
        }, columnIndex = 1, rowIndex = 8)

        add(new HBox {
          spacing = 5
          alignment = Pos.CenterRight
          children = Seq(
            new Button {
              id = "addRecordNextButton"
              text = "Next"
              defaultButton = true
              disable <== vm.incomplete
              focusTraversable = false
              onAction = handle {
                vm.saveAndReset()
                datePicker.requestFocus()
              }
            },
            new Button {
              id = "addRecordOkButton"
              text = "OK"
              disable <== vm.incomplete
              focusTraversable = false
              onAction = handle {
                close()
                vm.save()
              }
            },
            new Button {
              id = "addRecordCancelButton"
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
