package accounts.view

import java.time.LocalDate

import accounts.model.{AddRecordModel, FiltersModel, GridModel}
import accounts.record.repository.RecordRepositoryStub
import accounts.test.view.ViewTest
import accounts.viewmodel.AddRecordViewModel
import org.testfx.api.FxAssert._

import scalafx.scene.Parent
import scalafx.scene.input.KeyCode

class AddRecordViewTest extends ViewTest {

  var currentModel: Option[AddRecordModel] = None

  override def rootNode: Parent = {
    val repo = new RecordRepositoryStub
    val filtersModel = new FiltersModel
    val gridModel = new GridModel(repo, filtersModel)
    val model = new AddRecordModel(gridModel, filtersModel)
    currentModel = Some(model)

    val vm = new AddRecordViewModel(model)

    val view = new AddRecordView(vm)
    view.button
  }

  private def openWindow(): Unit = clickOn("#addTransactionButton")
  private def closeWindow(): Unit = clickOn("#addRecordCancelButton")

  private def enterText(node: String, s: String, eraseAmount: Int = 0): Unit = {
    clickOn(node)
    if (eraseAmount > 0) {
      push(KeyCode.End)
      eraseText(eraseAmount)
    }
    write(s)
    push(KeyCode.Enter)
  }

  private def recordWindow(f: => Unit): Unit = {
    openWindow()
    try {
      f
    } finally {
      closeWindow()
    }
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

  "Date field" should {
    "contain today's date" in recordWindow {
      val now = LocalDate.now
      verifyThat("#addRecordDatePicker", hasDateText(f"${now.getDayOfMonth}%02d-${now.getMonthValue}%02d-${now.getYear}"))
    }
    "accept a valid date" in recordWindow {
      enterText("#addRecordDatePicker", "01-01-2016", 10)
      verifyThat("#addRecordDatePicker", hasDateText("01-01-2016"))
    }
    "accept an empty string" in recordWindow {
      enterText("#addRecordDatePicker", "", 10)
      verifyThat("#addRecordDatePicker", hasDateText(""))
    }
    "ignore an invalid entry" in recordWindow {
      enterText("#addRecordDatePicker", "23-02-2016", 10)
      enterText("#addRecordDatePicker", "wibble", 10)
      verifyThat("#addRecordDatePicker", hasDateText("23-02-2016"))
    }
  }

  "Reference field" should {
    "contain the next increment" in recordWindow {
      verifyThat("#addRecordReferenceField", hasText("7"))
    }
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
    "initially be empty" in recordWindow {
      verifyThat("#addRecordTransactionTypeCombo", hasComboText(""))
    }
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
    "initially be empty" in recordWindow {
      verifyThat("#addRecordIncomeTypeCombo", hasComboText(""))
    }
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
    "initially be empty" in recordWindow {
      verifyThat("#addRecordCreditField", hasText(""))
    }
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
    "initially be empty" in recordWindow {
      verifyThat("#addRecordDebitField", hasText(""))
    }
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
    "initially be Hotel" in recordWindow {
      verifyThat("#addRecordAccountTypeCombo", hasComboText("Hotel"))
    }
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
    "initially be empty" in recordWindow {
      verifyThat("#addRecordDescriptionField", hasText(""))
    }
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
}
