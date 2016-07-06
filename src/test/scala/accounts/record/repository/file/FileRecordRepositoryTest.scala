package accounts.record.repository.file

import java.io.File

import accounts.record.AccountType.{Hotel, House}
import accounts.record.IncomeType._
import accounts.record.{OpeningBalance, Transaction}
import accounts.record.TransactionCategory.Food
import accounts.record.TransactionType.{Generic, IceCream, Phone, Unknown}
import accounts.test.util.TestUtils._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec

class FileRecordRepositoryTest extends WordSpec with TypeCheckedTripleEquals {

  private val expected = Seq(
    OpeningBalance(date("2016-02-01"), "opening balance" , debit = 123.45, credit = 0 , 1, Hotel),
    Transaction(date("2016-03-23"), "Cheeseburgers", Unknown(18, Food), debit = 252.75, credit = 0, Cash, 2, Hotel),
    Transaction(date("2016-04-02"), "Vodafone", Phone, debit = 313.10, credit = 0, DirectDebit, 3, Hotel),
    Transaction(date("2016-04-15"), "Cornettos", IceCream, debit = 0, credit = 54.50, Cash, 4, Hotel),
    Transaction(date("2016-05-24"), "Sausages", Generic(Food), debit = 26.42, credit = 0, Cheque, 5, House)
  )

  "A file repository" when {
    val fileUrl = getClass.getResource(s"${getClass.getSimpleName}.TRANS")
    val repo = new FileRecordRepository(new File(fileUrl.toURI))

    "records loaded" should {
      val records = repo.all

      "contain the expected records" in {
        assert(records === expected)
      }
    }

    "records stored" should {
      val storeFile = File.createTempFile(getClass.getSimpleName, ".TRANS")
      val storeRepo = new FileRecordRepository(storeFile)

      "save files such that they can be reloaded" in {
        repo.all.foreach(storeRepo.save)

        val loadRepo = new FileRecordRepository(storeFile)
        assert(loadRepo.all === expected)
      }


    }
  }
}
