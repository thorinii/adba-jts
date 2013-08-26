package code
package repo

import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.http._

import code.model._
import org.joda.time._

object RepositoryInjector extends Factory {
  val jobRepository = new FactoryMaker(buildJobRepository _) {}
  
  def buildJobRepository: JobRepository = new DumbJobRepository
}

trait JobRepository {
  def getByID(id : String) : Box[Job] = try {
    getByID(id.toLong)
  } catch {
    case e: Exception => Failure("Error getting by id", Full(e), Empty)
  }
  
  def getByID(id : Long) : Box[Job]
}

class DumbJobRepository extends JobRepository {
  override def getByID(id : Long) : Box[Job] = Full(
    Job("A Job", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
}

class SQLJobRepository extends JobRepository {
  override def getByID(id : Long) : Box[Job] = Full(
    Job("Australian Baptist 1925", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
}
