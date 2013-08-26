package code
package snippet

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.matchers.Matcher
import code.model._



class ScanningEventsTest extends FunSpec with ShouldMatchers with XMLMatchers {
  describe("Scanning Events") {
    it("should show an empty list if there are no events") {
      val html = <table><tr class="event-row">
            <td class="datetime"></td>
            <td class="type"></td>
            <td class="time-taken"></td>
            <td class="newly-scanned"></td>
            <td class="running-total"></td>
            <td class="running-percent"></td></tr></table>

      val job = Job("A Job", 1, Batch("A Batch"), Nil)
      
      val snippet = new Scanning(job)
      
      val result = snippet.events(html)
      
      assert(like(result, 
        <table><tr class="event-row"><td colspan="6">No Events</td></tr></table>))
    }
    
    it("should show list of events if there are any") {
      import org.joda.time._

      val html = 
        <table>
          <tr class="event-row">
            <td class="datetime"></td>
            <td class="type"></td>
            <td class="time-taken"></td>
            <td class="newly-scanned"></td>
            <td class="running-total"></td>
            <td class="running-percent"></td>
          </tr>
        </table>

      val job = Job("A Job", 100, Batch("A Batch"), Nil)
                .add(ScannedEvent(new DateTime(0), new Period(1, 15, 0, 0), 27))
                .add(ScannedEvent(new DateTime(0), new Period(1, 30, 0, 0), 48))
      
      val snippet = new Scanning(job)
      
      val result = snippet.events(html)
      
      assert(like(result, 
        <table>
          <tr class="event-row">
            <td class="datetime">{ new DateTime(0) }</td>
            <td class="type">Scanning</td>
            <td class="time-taken">1h 30mins</td>
            <td class="newly-scanned">48</td>
            <td class="running-total">75</td>
            <td class="running-percent">75%</td>
          </tr>
          <tr class="event-row">
            <td class="datetime">{ new DateTime(0) }</td>
            <td class="type">Scanning</td>
            <td class="time-taken">1h 15mins</td>
            <td class="newly-scanned">27</td>
            <td class="running-total">27</td>
            <td class="running-percent">27%</td>
          </tr>
        </table>))
    }
  }
}
