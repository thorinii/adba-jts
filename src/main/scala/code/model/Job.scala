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
