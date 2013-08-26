package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._

import java.math.MathContext
import net.liftweb.mapper._
import net.liftweb.common.Empty

import org.joda.time._

case class Batch(name: String)

case class Job(name: String, 
                pages: Int, 
                batch: Batch, 
                scanningEvents: List[ScanningEvent]) {

  def pagesScanned = scanningEvents.foldLeft(0) ((b, a) => a match {
                       case s: ScannedEvent => b + s.pages
                       case _ => b
                     })
  
  def add(event: ScanningEvent) = {
    Job(name, pages, batch, event :: scanningEvents)
  }
}


sealed trait ScanningEvent {
  def date: DateTime
}

case class ScannedEvent(date: DateTime, time: Period, pages: Int) extends ScanningEvent


object JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job] = Full(
    Job("A Job", 628, Batch(""), Nil)
    .add(ScannedEvent(new DateTime, new Period(1, 30, 0, 0), 364))
    .add(ScannedEvent(new DateTime, new Period(1, 30, 0, 0), 23))
    .add(ScannedEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
}
