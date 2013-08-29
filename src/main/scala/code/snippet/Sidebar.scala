package code 
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.sitemap._
import net.liftweb.util._
import net.liftweb.common._
import code.lib._
import Helpers._

import code.model._
import code.repo._
import org.joda.time.format._

class Sidebar {
  lazy val jobRepo = RepositoryInjector.jobRepository.vend

  def currentJobs = {
    val jobs = jobRepo.getCurrent(3)
    
    jobs match {
      case Empty => 
        "*" #> <div id="no-jobs">There are no current jobs</div>
        
      case Failure(msg, Empty, _) => 
        "*" #> <div id="error">There was a problem getting jobs: {msg}</div>
        
      case Failure(msg, Full(exc), _) => 
        "*" #> <div id="error">There was a problem getting jobs: {msg}: {exc.toString}</div>
        
      case Full(jobsList) => 
        ".quick-job-status *" #> jobsList.map(job =>
            "h4 *" #> job.name
          & ".scanning .bar [style]" #> ("width: " + (job.scannedFraction * 100).toInt + "%")
          & ".processing .bar [style]" #> ("width: " + (job.processedFraction * 100).toInt + "%")
          & ".mastering .bar [style]" #> ("width: " + (job.masteredFraction * 100).toInt + "%")
          & ".btn [href]" #> ("/job/" + job.machineName)
        )
        
      case _ =>
        "*" #> <div id="error">An error in the processing happened</div>
    }
  }
  
  /*<div class="quick-job-status">
              <h4>?</h4>
              <div class="progress scanning">
                <div class="bar" style="width: ?%">Scanning</div>
              </div>
              <div class="progress processing">
                <div class="bar" style="width: ?%">Pixedit</div>
              </div>
              <div class="progress mastering">
                <div class="bar" style="width: ?%">Mastering</div>
              </div>
              <p><a class="btn" href="?">View details &raquo;</a></p>
            </div>*/
}

