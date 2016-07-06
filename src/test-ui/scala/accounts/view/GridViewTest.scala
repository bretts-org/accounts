package accounts.view

import accounts.model.GridModel
import accounts.record.repository.RecordRepositoryStub
import accounts.test.view.ViewTest
import accounts.viewmodel.GridViewModel
import org.scalatest.mockito.MockitoSugar
import org.testfx.api.FxAssert.verifyThat
import org.mockito.Mockito._

import scalafx.scene.Parent

class GridViewTest extends ViewTest with MockitoSugar {

  val model = mock[GridModel]

  def vm = new GridViewModel(model)
  def grid = new GridView(vm)

  override def rootNode: Parent = {
    when(model.records).thenReturn(RecordRepositoryStub.all)

    grid.content
  }

  "A grid" should {
    "have the correct number of rows" in {
      verifyThat(".table-view", hasRowsSize(7))
    }

    "contain the date for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 0, row = 0, "01-12-2015"))
    }
    "contain the date for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 0, row = 1, "23-12-2015"))
    }
    "contain the reference for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 1, row = 0, "1"))
    }
    "contain the reference for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 1, row = 3, "4"))
    }
    "contain an empty transaction type for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 2, row = 0, ""))
    }
    "contain the transaction type for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 2, row = 5, "Food: Orange Juice Or Milk"))
    }
    "contain an empty income type for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 3, row = 0, ""))
    }
    "contain the income type for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 3, row = 2, "Direct Debit"))
    }
    "contain the credit for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 4, row = 0, "0.00"))
    }
    "contain the credit for a credit transaction" in {
      verifyThat(".table-view", hasCellText(column = 4, row = 3, "54.50"))
    }
    "contain the credit for a debit transaction" in {
      verifyThat(".table-view", hasCellText(column = 4, row = 4, "0.00"))
    }
    "contain the debit for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 5, row = 0, "123.45"))
    }
    "contain the debit for a credit transaction" in {
      verifyThat(".table-view", hasCellText(column = 5, row = 3, "0.00"))
    }
    "contain the debit for a debit transaction" in {
      verifyThat(".table-view", hasCellText(column = 5, row = 6, "842.63"))
    }
    "contain the account type for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 6, row = 0, "Hotel"))
    }
    "contain the account type for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 6, row = 4, "House"))
    }
    "contain the description for an opening balance" in {
      verifyThat(".table-view", hasCellText(column = 7, row = 0, "opening balance"))
    }
    "contain the description for a transaction" in {
      verifyThat(".table-view", hasCellText(column = 7, row = 2, "Vodafone"))
    }


  }
}
