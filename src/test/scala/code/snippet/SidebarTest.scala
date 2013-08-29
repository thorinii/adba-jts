package code
package snippet

import net.liftweb.common._

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.matchers.Matcher
import code.model._
import code.repo._

class SidebarTest extends FunSpec with ShouldMatchers with XMLMatchers {
  val jobHTML = <div class="quick-job-status">
              <h4>Title</h4>
              <div class="progress scanning">
                <div class="bar" style="width: ?%">Scanning</div>
              </div>
              <div class="progress processing">
                <div class="bar" style="width: ?%">Pixedit</div>
              </div>
              <div class="progress mastering">
                <div class="bar" style="width: ?%">Mastering</div>
              </div>
              <p><a class="btn" href="?">link</a></p>
            </div>

  describe("The sidebar shortlist of Current Jobs") {
    it("should say 'there are no current jobs' when there are none") {
      val html = <div id="quick-job-status">error message here</div>
      
      RepositoryInjector.jobRepository.doWith(new NullJobRepository) {
        val snippet = new Sidebar        
        val result = snippet.currentJobs(html)
        
        assert(like(result, <div id="no-jobs">There are no current jobs</div>))
      }
    }
    
    it("should show an error message when there is a problem getting the jobs") {
      val html = <div id="quick-job-status">error message here</div>
      
      // injector stuff here
      RepositoryInjector.jobRepository.doWith(new FailingJobRepository) {
        val snippet = new Sidebar        
        val result = snippet.currentJobs(html)
        
        assert(like(result, <div id="error">There was a problem getting jobs: Strange disconnection: java.io.IOException</div>))
      }
    }
    
    it("should show one job when there is one job") {
      RepositoryInjector.jobRepository.doWith(new SingleJobRepository) {
        val snippet = new Sidebar        
        val result = snippet.currentJobs(jobHTML)
        
        assert(like(result, 
          <div class="quick-job-status">
            <h4>Australian Baptist 1925</h4>
            <div class="progress scanning">
              <div class="bar" style="width: 20%">Scanning</div>
            </div>
            <div class="progress processing">
              <div class="bar" style="width: 0%">Pixedit</div>
            </div>
            <div class="progress mastering">
              <div class="bar" style="width: 0%">Mastering</div>
            </div>
            <p><a class="btn" href="/job/australian-baptist-1925">link</a></p>
          </div>
        ))
      }
    }
    
    it("should show several jobs when there is more than 1") {
      RepositoryInjector.jobRepository.doWith(new MultiJobRepository) {
        val snippet = new Sidebar        
        val result = snippet.currentJobs(jobHTML)
        
        assert(like(result, 
          <div class="quick-job-status">
            <h4>Australian Baptist 1925</h4>
            <div class="progress scanning">
              <div class="bar" style="width: 20%">Scanning</div>
            </div>
            <div class="progress processing">
              <div class="bar" style="width: 0%">Pixedit</div>
            </div>
            <div class="progress mastering">
              <div class="bar" style="width: 0%">Mastering</div>
            </div>
            <p><a class="btn" href="/job/australian-baptist-1925">link</a></p>
          </div><div class="quick-job-status">
            <h4>Australian Baptist 1926</h4>
            <div class="progress scanning">
              <div class="bar" style="width: 0%">Scanning</div>
            </div>
            <div class="progress processing">
              <div class="bar" style="width: 0%">Pixedit</div>
            </div>
            <div class="progress mastering">
              <div class="bar" style="width: 0%">Mastering</div>
            </div>
            <p><a class="btn" href="/job/australian-baptist-1926">link</a></p>
          </div><div class="quick-job-status">
            <h4>Australian Baptist 1927</h4>
            <div class="progress scanning">
              <div class="bar" style="width: 0%">Scanning</div>
            </div>
            <div class="progress processing">
              <div class="bar" style="width: 0%">Pixedit</div>
            </div>
            <div class="progress mastering">
              <div class="bar" style="width: 0%">Mastering</div>
            </div>
            <p><a class="btn" href="/job/australian-baptist-1927">link</a></p>
          </div>
        ))
      }
    }
  }
  
  class SingleJobRepository extends JobRepository {
    import org.joda.time._
  
    override def getByID(id : Long) = Empty

    override def getCurrent(max: Int) = Full(List(
      Job("Australian Baptist 1925", 390, Batch(""), Nil)
        .add(ScanningProducerEvent(new DateTime, new Period, 78))
    ))
  }
  class MultiJobRepository extends JobRepository {
    import org.joda.time._
  
    override def getByID(id : Long) = Empty

    override def getCurrent(max: Int) = Full(List(
      Job("Australian Baptist 1925", 390, Batch(""), Nil)
        .add(ScanningProducerEvent(new DateTime, new Period, 78)),
      Job("Australian Baptist 1926", 390, Batch(""), Nil),
      Job("Australian Baptist 1927", 390, Batch(""), Nil)
    ))
  }
}
