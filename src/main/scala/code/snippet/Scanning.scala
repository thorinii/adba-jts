package code 
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.sitemap._
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._

import code.model._
import org.joda.time.format._

class Scanning(job: Job) {
  lazy val dateFormat = (new DateTimeFormatterBuilder)
                           .appendHourOfDay(2)
                           .appendLiteral(':')
                           .appendMinuteOfHour(2)
                           .appendLiteral(" - ")
                           .appendDayOfMonth(2)
                           .appendLiteral(' ')
                           .appendMonthOfYearShortText()
                           .appendLiteral(' ')
                           .appendYear(2, 2)
                           .toFormatter
                           
  lazy val timeFormat = (new PeriodFormatterBuilder)
                           .printZeroAlways
                           .appendHours
                           .appendSuffix("h ", "h ")
                           .appendMinutes
                           .appendSuffix("min", "mins")
                           .toFormatter

  def info = {
    val pagesScanned = job.pagesScanned
    val percentScanned = pagesScanned * 100 / job.pages
    
    (".bar [style]" #> ("width: " + percentScanned + "%")
      & "#total-scanned *" #> pagesScanned
      & "#total-pages *" #> job.pages)
  }
  
  def events = {
    val events = job.scanningEvents
    var runningTotal = job.pagesScanned;
                     
    
    if(events.isEmpty) ".event-row *" #> <td colspan='6'>No Events</td>
    else ".event-row" #> events.map (_ match { 
      case ScanningProducerEvent(date, time, pages) => {
        val t = runningTotal
        runningTotal -= pages
      
         (".datetime *" #> dateFormat.print(date)
        & ".type *" #> "Scanning"
        & ".time-taken *" #> timeFormat.print(time)
        & ".newly-scanned *" #> pages
        & ".running-total *" #> t
        & ".running-percent *" #> (t * 100 / job.pages +"%"))}
      case e => (
          ".datetime *" #> dateFormat.print(e.date)
        & ".type *" #> "?"
        & ".time-taken *" #> "?"
        & ".newly-scanned *" #> "-"
        & ".running-total *" #> runningTotal
        & ".running-percent *" #> (runningTotal * 100 / job.pages +"%"))
    })
  }
}

