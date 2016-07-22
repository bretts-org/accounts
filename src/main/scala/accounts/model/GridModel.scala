package accounts.model

import accounts.record._
import accounts.record.repository.RecordRepository

class GridModel(recordRepository: RecordRepository, filters: FiltersModel) {

  // defensively copy here
  var all: Seq[Record] = Seq(recordRepository.all: _*)

  private[model] def filterAll(predicates: Seq[Option[Record => Boolean]]) = FiltersModel.combine(predicates) match {
    case Some(p) => all.filter(p)
    case None => all
  }

  def records: Seq[Record] = filterAll(filters.allPredicates)

  private def reload(): Unit = {
    all = recordRepository.all
  }

  def add(r: Record): Unit = {
    recordRepository.add(r)
    reload()
  }

  def delete(r: Record): Unit = {
    recordRepository.delete(r)
    reload()
  }

}
