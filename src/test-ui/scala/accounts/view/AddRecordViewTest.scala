package accounts.view

import java.time.LocalDate

import accounts.model.{AddRecordModel, FiltersModel, GridModel}
import accounts.record._
import accounts.record.repository.RecordRepositoryStub
import accounts.test.view.{Fixture, ViewTest}
import accounts.viewmodel.AddRecordViewModel
import org.testfx.api.FxAssert._
import org.testfx.matcher.base.NodeMatchers._

import scala.collection.mutable
import scala.util.Try
import scalafx.scene.input.KeyCode

case class AddRecordViewTestFixture() extends Fixture {
  val repo = new RecordRepositoryStub
  val filtersModel = new FiltersModel
  val gridModel = new GridModel(repo, filtersModel)
  val model = new AddRecordModel(gridModel, filtersModel)
  val vm = new AddRecordViewModel(model)

  val view = new AddRecordView(vm)
  val root = view.button
}

class AddRecordViewTest extends ViewTest[AddRecordViewTestFixture] {

  override def createFixture = AddRecordViewTestFixture()

  private def openWindow(): Unit = clickOn("#addTransactionButton")
  private def closeWindow(): Unit = Try {
    clickOn("#addRecordCancelButton")
  }

  private def enterText(node: String, s: String, eraseAmount: Int = 0): Unit = {
    clickOn(node)
    if (eraseAmount > 0) {
      push(KeyCode.End)
      eraseText(eraseAmount)
    }
    write(s)
    push(KeyCode.Tab)
  }

  private def recordWindow(f: => Unit): Unit = {
    openWindow()
    try {
      f
    } finally {
      closeWindow()
    }
  }

  // deliberately construct the string mechanically rather than using a formatter,
  // to test for the correct formatting
  def currentDateString = {
    val now = LocalDate.now
    f"${now.getDayOfMonth}%02d-${now.getMonthValue}%02d-${now.getYear}"
  }

  private def verifyInitialWindowState(expectedReference: Int): Unit = {
    verifyThat("#addRecordDatePicker", hasDateText(currentDateString))
    verifyThat("#addRecordReferenceField", hasText(expectedReference.toString))
    verifyThat("#addRecordTransactionTypeCombo", hasComboText(""))
    verifyThat("#addRecordIncomeTypeCombo", hasComboText(""))
    verifyThat("#addRecordCreditField", hasText(""))
    verifyThat("#addRecordDebitField", hasText(""))
    verifyThat("#addRecordAccountTypeCombo", hasComboText("Hotel"))
    verifyThat("#addRecordDescriptionField", hasText(""))
    push(KeyCode.Delete)
    push(KeyCode.Enter)
    verifyThat("#addRecordDatePicker", hasDateText(""))
  }

  "Add record window" when {
    "view created" should {
      "not be visible" in {
        assert(listWindows.size === 1)
      }
    }
  }

  "Add record button" when {
    "clicked" should {
      "display the add record window" in recordWindow {
        assert(window("Add Record").isShowing)
      }
    }
  }

  "Add record window" should {
    "initially be in the correct state" in recordWindow {
      verifyInitialWindowState(7)
    }
  }

  "Date field" should {
    "accept a valid date" in recordWindow {
      enterText("#addRecordDatePicker", "14-08-2015", 10)
      verifyThat("#addRecordDatePicker", hasDateText("14-08-2015"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordDatePicker", "", 10)
      verifyThat("#addRecordDatePicker", hasDateText(""))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordDatePicker", "23-04-2016", 10)
      enterText("#addRecordDatePicker", "wibble", 10)
      verifyThat("#addRecordDatePicker", hasDateText("23-04-2016"))
    }
  }

  "Reference field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordReferenceField", "8", 1)
      verifyThat("#addRecordReferenceField", hasText("8"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordReferenceField", "8", 1)
      enterText("#addRecordReferenceField", "", 1)
      verifyThat("#addRecordReferenceField", hasText(""))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordReferenceField", "foo", 1)
      verifyThat("#addRecordReferenceField", hasText("7"))
    }
  }

  "Transaction type field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "507")
      verifyThat("#addRecordTransactionTypeCombo", hasComboText("Income: VAT"))
    }
    "accept a valid string" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "Income: Local Payment Income")
      verifyThat("#addRecordTransactionTypeCombo", hasComboText("Income: Local Payment Income"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "113")
      enterText("#addRecordTransactionTypeCombo", "", 10)
      verifyThat("#addRecordTransactionTypeCombo", hasComboText(""))
    }
    "accept a valid substring" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "fish")
      verifyThat("#addRecordTransactionTypeCombo", hasComboText("Food: Fish"))
    }
    "accept the first valid lexically-ordered substring" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "kitchen")
      verifyThat("#addRecordTransactionTypeCombo", hasComboText("Maintenance: Cortijo Kitchen"))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordTransactionTypeCombo", "960")
      enterText("#addRecordTransactionTypeCombo", "wibble", 25)
      verifyThat("#addRecordTransactionTypeCombo", hasComboText("Maintenance: Cortijo Pool"))
    }
  }

  "Income type field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "2")
      verifyThat("#addRecordIncomeTypeCombo", hasComboText("Cheque"))
    }
    "accept a valid string" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "Direct Debit")
      verifyThat("#addRecordIncomeTypeCombo", hasComboText("Direct Debit"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "1")
      enterText("#addRecordIncomeTypeCombo", "", 4)
      verifyThat("#addRecordIncomeTypeCombo", hasComboText(""))
    }
    "accept a valid substring" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "tra")
      verifyThat("#addRecordIncomeTypeCombo", hasComboText("Transfer"))
    }
    "accept the first valid lexically-ordered substring" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "ca")
      verifyThat("#addRecordIncomeTypeCombo", hasComboText("Card"))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordIncomeTypeCombo", "6")
      enterText("#addRecordIncomeTypeCombo", "foo", 4)
      verifyThat("#addRecordIncomeTypeCombo", hasComboText("Coop"))
    }
  }

  "Credit field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordCreditField", "456")
      verifyThat("#addRecordCreditField", hasText("456.00"))
    }
    "accept a valid decimal" in recordWindow {
      enterText("#addRecordCreditField", "12.34")
      verifyThat("#addRecordCreditField", hasText("12.34"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordCreditField", "12.34")
      enterText("#addRecordCreditField", "", 5)
      verifyThat("#addRecordCreditField", hasText(""))
    }
    "ignore a long decimal" in recordWindow {
      enterText("#addRecordCreditField", "12.34")
      enterText("#addRecordCreditField", "56.789", 5)
      verifyThat("#addRecordCreditField", hasText("12.34"))
    }
    "ignore a negative decimal" in recordWindow {
      enterText("#addRecordCreditField", "12.34")
      enterText("#addRecordCreditField", "-56.78", 5)
      verifyThat("#addRecordCreditField", hasText("12.34"))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordCreditField", "12.34")
      enterText("#addRecordCreditField", "foo", 5)
      verifyThat("#addRecordCreditField", hasText("12.34"))
    }
  }

  "Debit field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordDebitField", "456")
      verifyThat("#addRecordDebitField", hasText("456.00"))
    }
    "accept a valid decimal" in recordWindow {
      enterText("#addRecordDebitField", "12.34")
      verifyThat("#addRecordDebitField", hasText("12.34"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordDebitField", "12.34")
      enterText("#addRecordDebitField", "", 5)
      verifyThat("#addRecordDebitField", hasText(""))
    }
    "ignore a long decimal" in recordWindow {
      enterText("#addRecordDebitField", "12.34")
      enterText("#addRecordDebitField", "56.789", 5)
      verifyThat("#addRecordDebitField", hasText("12.34"))
    }
    "ignore a negative decimal" in recordWindow {
      enterText("#addRecordDebitField", "12.34")
      enterText("#addRecordDebitField", "-56.78", 5)
      verifyThat("#addRecordDebitField", hasText("12.34"))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordDebitField", "12.34")
      enterText("#addRecordDebitField", "foo", 5)
      verifyThat("#addRecordDebitField", hasText("12.34"))
    }
  }

  "Account type field" should {
    "accept a valid integer" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "2", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText("House"))
    }
    "accept a valid string" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "House", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText("House"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText(""))
    }
    "accept a valid substring" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "hou", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText("House"))
    }
    "accept the first valid lexically-ordered substring" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "2", 5)
      enterText("#addRecordAccountTypeCombo", "h", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText("Hotel"))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordAccountTypeCombo", "2", 5)
      enterText("#addRecordAccountTypeCombo", "foo", 5)
      verifyThat("#addRecordAccountTypeCombo", hasComboText("House"))
    }
  }

  "Description field" should {
    "accept a valid string" in recordWindow {
      enterText("#addRecordDescriptionField", "foo bar")
      verifyThat("#addRecordDescriptionField", hasText("foo bar"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordDescriptionField", "foo")
      enterText("#addRecordDescriptionField", "", 3)
      verifyThat("#addRecordDescriptionField", hasText(""))
    }
  }

  "Buttons" should {

    def populateCoreFields() = {
      enterText("#addRecordTransactionTypeCombo", "113")
      enterText("#addRecordIncomeTypeCombo", "2")
      enterText("#addRecordDescriptionField", "foo bar")
    }

    "initially have only Cancel enabled" in recordWindow {
      verifyThat("#addRecordNextButton", isDisabled)
      verifyThat("#addRecordOkButton", isDisabled)
      verifyThat("#addRecordCancelButton", isEnabled)
    }

    "all be enabled if only debit field is blank" in recordWindow {
      populateCoreFields()
      enterText("#addRecordCreditField", "12.34")
      verifyThat("#addRecordNextButton", isEnabled)
      verifyThat("#addRecordOkButton", isEnabled)
      verifyThat("#addRecordCancelButton", isEnabled)
    }

    "all be enabled if only crebit field is blank" in recordWindow {
      populateCoreFields()
      enterText("#addRecordDebitField", "12.34")
      verifyThat("#addRecordNextButton", isEnabled)
      verifyThat("#addRecordOkButton", isEnabled)
      verifyThat("#addRecordCancelButton", isEnabled)
    }

    "have only Cancel enabled if both crebit and debit fields are populated" in recordWindow {
      populateCoreFields()
      enterText("#addRecordCreditField", "12.34")
      enterText("#addRecordDebitField", "56.78")
      verifyThat("#addRecordNextButton", isDisabled)
      verifyThat("#addRecordOkButton", isDisabled)
      verifyThat("#addRecordCancelButton", isEnabled)
    }

    val expectedTransaction = Transaction(
      LocalDate.now,
      "foo bar",
      TransactionType.Fish,
      BigDecimal("0"),
      BigDecimal("12.34"),
      IncomeType.Cheque,
      7,
      AccountType.Hotel
    )

    "save, close and reset the window if OK is pushed" in {
      openWindow()
      try {
        populateCoreFields()
        enterText("#addRecordCreditField", "12.34")
        clickOn("#addRecordOkButton")
        assert(fixture.repo.saved === mutable.Buffer[Record](expectedTransaction))
        assert(listWindows.size === 1)
        openWindow()
        verifyInitialWindowState(8)
      } finally {
        closeWindow()
      }
    }

    "save and reset the window if Next is pushed" in {
      openWindow()
      try {
        populateCoreFields()
        enterText("#addRecordCreditField", "12.34")
        clickOn("#addRecordNextButton")
        assert(fixture.repo.saved === mutable.Buffer[Record](expectedTransaction))
        verifyInitialWindowState(8)
      } finally {
        closeWindow()
      }
    }

    "close and reset the window if Cancel is pushed" in {
      openWindow()
      try {
        populateCoreFields()
        enterText("#addRecordCreditField", "12.34")
        clickOn("#addRecordCancelButton")
        assert(fixture.repo.saved === mutable.Buffer[Record]())
        assert(listWindows.size === 1)
        openWindow()
        verifyInitialWindowState(7)
      } finally {
        closeWindow()
      }

    }
  }
}
