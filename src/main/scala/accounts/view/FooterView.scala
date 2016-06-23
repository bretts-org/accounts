package accounts.view

import accounts.core.view.View
import accounts.viewmodel.{RecordViewModel, TotalsViewModel}

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, TextField, TextFormatter}
import scalafx.scene.layout._
import scalafx.util.StringConverter

class FooterView(vm: TotalsViewModel) extends View {

  val content = new GridPane {
    padding = Insets(5)
    hgap = 50
    vgap = 5
    columnConstraints = Seq(
      new ColumnConstraints(Region.USE_COMPUTED_SIZE) {
        percentWidth = 20
      },
      new ColumnConstraints(Region.USE_COMPUTED_SIZE) {
        percentWidth = 40
      },
      new ColumnConstraints(Region.USE_COMPUTED_SIZE) {
        percentWidth = 40
      }
    )

    add(new Label {
      text = "Totals"
      style = "-fx-font-size: 16pt"
    }, columnIndex = 0, rowIndex = 0, colspan = 3, rowspan = 1)

    add(new Label {
      text = "Period"
      style = "-fx-font-size: 12pt"
    }, columnIndex = 0, rowIndex = 1)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterRight
      children = Seq(
        new Label {
          text = "Credit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            RecordViewModel.formatDecimal(_)
          )) {
            value <== vm.periodCredit
          }
        }
      )
    }, columnIndex = 1, rowIndex = 1)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterLeft
      children = Seq(
        new Label {
          text = "Debit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            d => RecordViewModel.formatDecimal(d * -1)
          )) {
            value <== vm.periodDebit
          }
        }
      )
    }, columnIndex = 2, rowIndex = 1)

    add(new Label {
      text = "Brought Forward"
      style = "-fx-font-size: 12pt"
    }, columnIndex = 0, rowIndex = 2)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterRight
      children = Seq(
        new Label {
          text = "Credit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            RecordViewModel.formatDecimal(_)
          )) {
            value <== vm.broughtForwardCredit
          }
        }
      )
    }, columnIndex = 1, rowIndex = 2)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterLeft
      children = Seq(
        new Label {
          text = "Debit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            d => RecordViewModel.formatDecimal(d * -1)
          )) {
            value <== vm.broughtForwardDebit
          }
        }
      )
    }, columnIndex = 2, rowIndex = 2)

    add(new Label {
      text = "Year To Date"
      style = "-fx-font-size: 12pt"
    }, columnIndex = 0, rowIndex = 3)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterRight
      children = Seq(
        new Label {
          text = "Credit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            RecordViewModel.formatDecimal(_)
          )) {
            value <== vm.toDateCredit
          }
        }
      )
    }, columnIndex = 1, rowIndex = 3)

    add(new HBox {
      spacing = 5
      alignment = Pos.CenterLeft
      children = Seq(
        new Label {
          text = "Debit:"
        },
        new TextField {
          alignment = Pos.CenterRight
          editable = false
          textFormatter = new TextFormatter(StringConverter.toStringConverter[BigDecimal](
            d => RecordViewModel.formatDecimal(d * -1)
          )) {
            value <== vm.toDateDebit
          }
        }
      )
    }, columnIndex = 2, rowIndex = 3)
  }
}
