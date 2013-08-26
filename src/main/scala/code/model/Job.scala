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
                scanningEvents: List[ProducerEvent]) {

  def pagesScanned = scanningEvents.foldLeft(0) ((b, a) => a match {
                       case s: ScanningProducerEvent => b + s.pages
                       case _ => b
                     })
  
  def add(event: ProducerEvent) = {
    Job(name, pages, batch, event :: scanningEvents)
  }
}


sealed trait ProducerEvent {
  def date: DateTime
}

case class ScanningProducerEvent(date: DateTime, time: Period, pages: Int) extends ProducerEvent


object JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job] = Full(
    Job("A Job", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
}
