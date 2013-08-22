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

case class Job(name: String, pages: Int, batch: Batch, scanning: Scanning) {
  def pagesScanned = scanning.pagesScanned
  
  def add(event: ScanningEvent) = {
    Job(name, pages, batch, Scanning(event :: scanning.events))
  }
}

case class Scanning(events: List[ScanningEvent]) {
  def pagesScanned = events.filter(_.isInstanceOf[Scanned])
                           .foldLeft(0) ((b, a) => a match {
                               case s: Scanned => b + s.pages
                               case _ => b
                             })
}


sealed class ScanningEvent(date: DateTime)
case class Scanned(date: DateTime, pages: Int) extends ScanningEvent(date)


object JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job] = Full(
    Job("A Job", 628, Batch(""), Scanning(Nil))
    .add(Scanned(new DateTime, 364))
  )
}
