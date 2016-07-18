package accounts.viewmodel

import accounts.core.viewmodel.ViewModel
import accounts.core.viewmodel.ViewModel._
import accounts.model.GridModel

import scalafx.collections.transformation.SortedBuffer

class GridViewModel(model: GridModel) extends ViewModel {

  val records: SortedBuffer[RecordViewModel] =
    CalculatedBuffer.sorted(model.records.map(new RecordViewModel(_)))

}
