package accounts.model

import accounts.record.repository.RecordRepository

class ShellModel(records: RecordRepository) {

  val filters = new FiltersModel
  val grid = new GridModel(records, filters)
  val totals = new TotalsModel(grid, filters)
}