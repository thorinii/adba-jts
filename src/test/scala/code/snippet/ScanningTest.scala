package code
package snippet

import org.scalatest.FunSpec
import code.model.{Job, Batch}

class ScanningTest extends FunSpec {
  describe("Scanning Info") {
    it("should show 1 total pages when there are 1 total pages") {
      val html = <span id="total-pages">here</span>
      val job = Job("A Job", 1, Batch("A Batch"))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(result xml_== <span id="total-pages">1</span>)
    }
    it("should show 145 total pages when there are 145 total pages") {
      val html = <span id="total-pages">here</span>
      val job = Job("A Job", 145, Batch("A Batch"))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(result xml_== <span id="total-pages">145</span>)
    }
    
    it("should show 0 and 0% scanned pages when there are 0 scanned and 200 total pages") {
      val html = (<span id="total-scanned">here</span><div class="bar"></div>)
      val job = Job("A Job", 1, Batch("A Batch"))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(result xml_== <span id="total-scanned">0</span><div class="bar" style="width: 0%"></div>)
    }
    it("should show 50 and 25% scanned pages when there are 50 scanned and 200 total pages") (pending)
  }
}
