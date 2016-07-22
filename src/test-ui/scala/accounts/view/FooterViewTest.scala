package accounts.view

import java.time.LocalDate

import accounts.core.viewmodel.ViewModel
import accounts.model.{FiltersModel, GridModel, TotalsModel}
import accounts.record.repository.RecordRepositoryStub
import accounts.test.view.{Fixture, ViewTest}
import accounts.viewmodel.TotalsViewModel
import org.testfx.api.FxAssert._

import scalafx.scene.Parent

class FooterViewTestFixture extends Fixture {
  val repo = new RecordRepositoryStub

  val filters = new FiltersModel
  val grid = new GridModel(repo, filters)

  val model = new TotalsModel(grid, filters)
  val vm = new TotalsViewModel(model)
  val footer = new FooterView(vm)

  override val root: Parent = footer.content
}

class FooterViewTest extends ViewTest[FooterViewTestFixture] {

  override def createFixture: FooterViewTestFixture = new FooterViewTestFixture

  "Footer panel" when {
    "created" should {
      "have full period totals" in {
        verifyThat("#periodCreditTotal", hasText("54.50"))
        verifyThat("#periodDebitTotal", hasText("-1568.35"))
        verifyThat("#broughtForwardCreditTotal", hasText("0.00"))
        verifyThat("#broughtForwardDebitTotal", hasText("0.00"))
        verifyThat("#yearToDateCreditTotal", hasText("54.50"))
        verifyThat("#yearToDateDebitTotal", hasText("-1568.35"))
      }
    }

    "filter dates modified" should {
      "have date range totals" in {
        ViewModel.singletonVmState.update {
          fixture.filters.startDateFilter = Some(LocalDate.parse("2016-05-05"))
          fixture.filters.endDateFilter = Some(LocalDate.parse("2016-05-09"))
        }
        verifyThat("#periodCreditTotal", hasText("0.00"))
        verifyThat("#periodDebitTotal", hasText("-36.42"))
        verifyThat("#broughtForwardCreditTotal", hasText("54.50"))
        verifyThat("#broughtForwardDebitTotal", hasText("-313.10"))
        verifyThat("#yearToDateCreditTotal", hasText("54.50"))
        verifyThat("#yearToDateDebitTotal", hasText("-349.52"))
      }
    }
  }
}
