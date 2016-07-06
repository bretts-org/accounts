package accounts.view

import java.time.LocalDate

import accounts.model.{AddRecordModel, FiltersModel, GridModel}
import accounts.record.repository.RecordRepositoryStub
import accounts.test.view.ViewTest
import accounts.viewmodel.AddRecordViewModel
import org.testfx.api.FxAssert._

import scalafx.scene.Parent

class AddRecordViewTest extends ViewTest {

  def model = {
    val repo = new RecordRepositoryStub
    val filtersModel = new FiltersModel
    val gridModel = new GridModel(repo, filtersModel)
    new AddRecordModel(gridModel, filtersModel)
  }
  def vm = new AddRecordViewModel(model)
  def view = new AddRecordView(vm)

  override def rootNode: Parent = view.button

  private def openWindow(): Unit = clickOn("#addTransactionButton")

  "Add record window" when {
    "view created" should {
      "not be visible" in {
        assert(listWindows.size === 1)
      }
    }
  }

  "Add record button" when {
    "clicked" should {
      "display the add record window" in {
        openWindow()
        assert(window("Add Record").isShowing)
      }
    }
  }

  "Date field" should {
    "contain today's date" in {
      openWindow()
      val now = LocalDate.now
      verifyThat("#addRecordDatePicker", hasDateText(f"${now.getDayOfMonth}%02d-${now.getMonthValue}%02d-${now.getYear}"))
    }
  }
}
