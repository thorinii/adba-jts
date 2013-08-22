package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._

import java.math.MathContext
import net.liftweb.mapper._
import net.liftweb.common.Empty

case class Batch(name: String)

case class Job(name: String, pages: Int, batch: Batch) {
  def pagesScanned = 369
}

case class Scanning(events: List[ScanningEvent]) {
}

sealed trait ScanningEvent

object JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job] = Full(Job("A Job", 628, Batch("")))
}
