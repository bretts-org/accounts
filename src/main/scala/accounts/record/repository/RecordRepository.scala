package accounts.record.repository

import accounts.record.Record

trait RecordRepository {
  def all: Seq[Record]
  def add(r: Record): Unit
  def delete(r: Record): Unit
}
