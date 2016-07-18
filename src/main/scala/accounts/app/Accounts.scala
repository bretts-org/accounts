package accounts.app

import java.io.File

import accounts.core.util._
import accounts.model.ShellModel
import accounts.record.repository.file.FileRecordRepository
import accounts.view.ShellView
import accounts.viewmodel.ShellViewModel
import com.typesafe.scalalogging.StrictLogging

import scalafx.application.JFXApp

object Accounts extends JFXApp with StrictLogging {
  logger.info("Starting application")

  val transFileName = parameters.named.getOrElse("transfile",
    sys.props("user.home") / "Documents" / "Accounts" / "TRANS")
  logger.info(s"Transaction file: $transFileName")

  val transFile = new File(transFileName)
  val recordRepo = new FileRecordRepository(transFile)

  val model = new ShellModel(recordRepo)
  val viewModel = new ShellViewModel(model)
  val view = new ShellView(viewModel)

  stage = view.stage
}
