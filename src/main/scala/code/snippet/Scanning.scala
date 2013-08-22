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
  def info = {
    val pagesScanned = job.pagesScanned
    val percentScanned = pagesScanned * 100 / job.pages
    
    (".bar [style]" #> ("width: " + percentScanned + "%")
      & "#total-scanned *" #> pagesScanned
      & "#total-pages *" #> job.pages)
  }
  
  def events = {
    val events = job.scanning.events
    var runningTotal = job.pagesScanned;
    
    val dateFormat = ISODateTimeFormat.dateTime;
    val timeFormat = (new PeriodFormatterBuilder)
                     .printZeroAlways
                     .appendHours
                     .appendSuffix("h ", "h ")
                     .appendMinutes
                     .appendSuffix("min", "mins")
                     .toFormatter
                     
    
    if(events.isEmpty) ".event-row *" #> <td colspan='6'>No Events</td>
    else ".event-row" #> events.map (_ match { 
      case JobScannedEvent(date, time, pages) => {
        val t = runningTotal
        runningTotal -= pages
      (
          ".datetime *" #> dateFormat.print(date)
        & ".type *" #> "Scanning"
        & ".time-taken *" #> timeFormat.print(time)
        & ".newly-scanned *" #> pages
        & ".running-total *" #> t
        & ".running-percent *" #> (t * 100 / job.pages +"%"))}
      case _ => (
          ".datetime *" #> "?"
        & ".type *" #> "?"
        & ".time-taken *" #> "?"
        & ".newly-scanned *" #> "-"
        & ".running-total *" #> runningTotal
        & ".running-percent *" #> (runningTotal * 100 / job.pages +"%"))
    })
  }
  
  
  /*<tr class="row">
    <td class="datetime">16 Aug 12:34</td>
    <td class="type">Scanned</td>
    <td class="time-taken">1.5h</td>
    <td class="newly-scanned">124</td>
    <td class="running-total">369</td>
    <td class="running-percent">59.5%</td>
  </tr>*/
}

