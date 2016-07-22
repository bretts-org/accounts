package accounts.viewmodel

import accounts.core.viewmodel.ViewModel
import accounts.core.viewmodel.ViewModel._
import accounts.model.GridModel

import scalafx.beans.property.ObjectProperty
import scalafx.collections.transformation.SortedBuffer

class GridViewModel(model: GridModel) extends ViewModel {

  val records: SortedBuffer[RecordViewModel] =
    CalculatedBuffer.sorted(model.records.map(new RecordViewModel(_)))(Ordering.by(_.reference()))

  val selectedRecord = ObjectProperty[Option[RecordViewModel]](None)

  def delete(r: RecordViewModel): Unit = update {
    model.delete(r.record)
  }

}
