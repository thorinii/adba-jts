package code
package snippet

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.matchers.Matcher
import code.model._

class ScanningInfoTest extends FunSpec with ShouldMatchers with XMLMatchers {
  describe("Scanning Info") {
    it("should show 1 total pages when there are 1 total pages") {
      val html = <span id="total-pages">here</span>
      val job = Job("A Job", 1, Batch("A Batch"), JobScanning(Nil))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(like(result, <span id="total-pages">1</span>))
    }
    it("should show 145 total pages when there are 145 total pages") {
      val html = <span id="total-pages">here</span>
      val job = Job("A Job", 145, Batch("A Batch"), JobScanning(Nil))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(like(result, <span id="total-pages">145</span>))
    }
    
    it("should show 0 and 0% scanned pages when there are 0 scanned and 200 total pages") {
      val html = (<span id="total-scanned">here</span><div class="bar"></div>)
      val job = Job("A Job", 200, Batch("A Batch"), JobScanning(Nil))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(like(result, <span id="total-scanned">0</span><div class="bar" style="width: 0%"></div>))
    }
    it("should show 50 and 25% scanned pages when there are 50 scanned and 200 total pages") {
      import org.joda.time._

      val html = (<span id="total-scanned">here</span><div class="bar"></div>)
      val job = Job("A Job", 200, Batch("A Batch"), JobScanning(Nil))
                .add(JobScannedEvent(new DateTime, new Period, 50))
      
      val snippet = new Scanning(job)
      
      val result = snippet.info(html)
      
      assert(like(result, <span id="total-scanned">50</span><div class="bar" style="width: 25%"></div>))
    }
  }
}

trait XMLMatchers  {
  import scala.xml._
  import scala.xml.Utility.trim
  
  lazy val prettyPrinter = new PrettyPrinter(80, 2)

  def like(result: NodeSeq, expected: NodeSeq): Option[String] = {
    val resultCanon = canonicalise(result)
    val expectedCanon = canonicalise(expected)
  
    if(resultCanon.toString == expectedCanon.toString) None
    else Some(
         prettyPrinter.format(resultCanon)
       + " is not like expected "
       + prettyPrinter.format(expectedCanon))
  }
  
  def canonicalise(nodeseq: NodeSeq): Node = nodeseq match {
      case n: Node => trim(n)
      case _ => canonicalise(nodeseq(0))
  }
}