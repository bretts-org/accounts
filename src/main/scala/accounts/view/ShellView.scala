package accounts.view

import accounts.core.view.View
import accounts.viewmodel.ShellViewModel
import ShellView._

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.image.Image
import scalafx.scene.layout._

object ShellView {
  val WindowWidth = Region.USE_COMPUTED_SIZE
  val WindowHeight = 800
}

class ShellView(vm: ShellViewModel) extends View {

  val addRecord = new AddRecordView(vm.addRecord)
  val header = new HeaderView(vm.filters, vm.grid, addRecord)

  val grid = new GridView(vm.grid)
  val pnl = new PnlView(vm.pnl)

  val footer = new FooterView(vm.totals)

  val stage = new PrimaryStage {
    title = "Cortijo Rosario Accounts"

    icons += new Image(getClass.getResourceAsStream("/icon/coins-euro.png"))

    scene = new Scene(width = WindowWidth, height = WindowHeight) {
      root = new BorderPane {
        top = header.content
        center = new TabPane {
          tabs = Seq(
            new Tab {
              text = "Transactions"
              closable = false
              content = grid.content
            },
            new Tab {
              text = "P/L"
              closable = false
              content = pnl.pnlTable
            }
          )
        }
        bottom = footer.content
      }
    }
  }

  Platform.runLater {
    // Workaround for grid column misalignment issue
    stage.width = stage.width() + 1
    // Set focus to the transaction code field
    header.transactionCodeField.requestFocus()
  }
}
