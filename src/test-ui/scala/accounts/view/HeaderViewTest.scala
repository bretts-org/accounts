package accounts.view

import accounts.model.FiltersModel
import accounts.test.view.{Fixture, ViewTest}
import accounts.viewmodel.FiltersViewModel
import org.testfx.api.FxAssert.verifyThat
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scalafx.scene.control.Button
import scalafx.scene.input.KeyCode

case class HeaderViewTestFixture(addRecordView: AddRecordView) extends Fixture {
  val filtersModel = new FiltersModel()
  val filtersVm = new FiltersViewModel(filtersModel)

  val header = new HeaderView(filtersVm, addRecordView)
  val root = header.content
}

class HeaderViewTest extends ViewTest[HeaderViewTestFixture] with MockitoSugar {

  override def createFixture = {
    val addRecordView = mock[AddRecordView]
    when(addRecordView.button).thenReturn(new Button)

    HeaderViewTestFixture(addRecordView)
  }

  "Header panel" when {
    "created" should {
      "be empty" in {
        verifyThat("#transactionCodeField", hasText(""))
        verifyThat("#transactionCategoryCombo", hasComboText("All Categories"))
        verifyThat("#transactionTypeCombo", hasComboText("All Types"))
      }
    }
  }

  "Transaction code field" when {

    def enterCodeString(s: String) = {
      clickOn("#transactionCodeField")
      write(s)
      push(KeyCode.Enter)
    }
    def enterCode(i: Int) = enterCodeString(i.toString)

    "a known type is entered" should {
      "update fields" in {
        enterCode(113)
        verifyThat("#transactionCodeField", hasText("113"))
        verifyThat("#transactionCategoryCombo", hasComboText("Food"))
        verifyThat("#transactionTypeCombo", hasComboText("Food: Fish"))
      }
    }

    "an unknown type is entered" should {
      "update fields" in {
        enterCode(234)
        verifyThat("#transactionCodeField", hasText("234"))
        verifyThat("#transactionCategoryCombo", hasComboText("Local Payment"))
        verifyThat("#transactionTypeCombo", hasComboText("Local Payment: 234"))
      }
    }

    "a known category is entered" should {
      "update fields" in {
        enterCode(3)
        verifyThat("#transactionCodeField", hasText("3"))
        verifyThat("#transactionCategoryCombo", hasComboText("Wages"))
        verifyThat("#transactionTypeCombo", hasComboText("All Types"))
      }
    }

    "an unknown category is entered" should {
      "leave combos unchanged" in {
        enterCode(113)
        eraseText(3)
        enterCode(54)
        verifyThat("#transactionCodeField", hasText("54"))
        verifyThat("#transactionCategoryCombo", hasComboText("Food"))
        verifyThat("#transactionTypeCombo", hasComboText("Food: Fish"))
      }
    }

    "an unparseable string is entered" should {
      "leave fields unchanged" in {
        enterCode(113)
        eraseText(3)
        enterCodeString("foo")
        verifyThat("#transactionCodeField", hasText("113"))
        verifyThat("#transactionCategoryCombo", hasComboText("Food"))
        verifyThat("#transactionTypeCombo", hasComboText("Food: Fish"))
      }
    }

    "an empty string is entered" should {
      "set fields to empty" in {
        enterCode(113)
        eraseText(3)
        push(KeyCode.Enter)
        verifyThat("#transactionCodeField", hasText(""))
        verifyThat("#transactionCategoryCombo", hasComboText("All Categories"))
        verifyThat("#transactionTypeCombo", hasComboText("All Types"))
      }
    }
  }

  "Month/Year fields" when {
    def enterMonth(s: String) = {
      clickOn("#monthCombo")
      write(s)
      push(KeyCode.Enter)
    }

    def enterYearString(s: String) = {
      clickOn("#yearField")
      write(s)
      push(KeyCode.Enter)
    }

    def enterYear(i: Int) = enterYearString(i.toString)

    "year is entered" should {
      "update start and end dates" in {
        enterYear(2015)
        verifyThat("#yearField", hasText("2015"))
        verifyThat("#startDatePicker", hasDateText("01-01-2015"))
        verifyThat("#endDatePicker", hasDateText("31-12-2015"))
      }
    }

    "month string and year are entered" should {
      "update start and end dates" in {
        enterMonth("March")
        enterYear(2015)
        verifyThat("#yearField", hasText("2015"))
        verifyThat("#startDatePicker", hasDateText("01-03-2015"))
        verifyThat("#endDatePicker", hasDateText("31-03-2015"))
      }
    }

    "numeric month and year are entered" should {
      "update start and end dates" in {
        enterMonth("2")
        enterYear(2012)
        verifyThat("#yearField", hasText("2012"))
        verifyThat("#startDatePicker", hasDateText("01-02-2012"))
        verifyThat("#endDatePicker", hasDateText("29-02-2012"))
      }
    }
  }

  "Transaction type combo" should {

    "update category and code" in {
      clickOn("#transactionTypeCombo", "Food: Fish")
      verifyThat("#transactionTypeCombo", hasComboText("Food: Fish"))
      verifyThat("#transactionCodeField", hasText("113"))
      verifyThat("#transactionCategoryCombo", hasComboText("Food"))
    }

    "update category and code when type updated twice" in {
      clickOn("#transactionTypeCombo", "Maintenance: Cortijo Kitchen")
      clickOn("#transactionTypeCombo", "Income: Bar")
      verifyThat("#transactionTypeCombo", hasComboText("Income: Bar"))
      verifyThat("#transactionCodeField", hasText("509"))
      verifyThat("#transactionCategoryCombo", hasComboText("Income"))
    }

    "revert category and code when type is reset to All Types" in {
      clickOn("#transactionTypeCombo", "Food: Fish")
      clickOn("#transactionTypeCombo", "All Types")
      verifyThat("#transactionTypeCombo", hasComboText("All Types"))
      verifyThat("#transactionCodeField", hasText("1"))
      verifyThat("#transactionCategoryCombo", hasComboText("Food"))
    }
  }

}
