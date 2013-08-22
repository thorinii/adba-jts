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

case class Job(name: String, pages: Int, batch: Batch, scanning: JobScanning) {
  def pagesScanned = scanning.pagesScanned
  
  def add(event: JobScanningEvent) = {
    Job(name, pages, batch, JobScanning(event :: scanning.events))
  }
}

case class JobScanning(events: List[JobScanningEvent]) {
  def pagesScanned = events.foldLeft(0) ((b, a) => a match {
                               case s: JobScannedEvent => b + s.pages
                               case _ => b
                             })
}


sealed trait JobScanningEvent
case class JobScannedEvent(date: DateTime, time: Period, pages: Int) extends JobScanningEvent


object JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job] = Full(
    Job("A Job", 628, Batch(""), JobScanning(Nil))
    .add(JobScannedEvent(new DateTime, new Period(1, 30, 0, 0), 364))
    .add(JobScannedEvent(new DateTime, new Period(1, 30, 0, 0), 23))
    .add(JobScannedEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
}
