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
  def getCurrent(max: Int) : Box[List[Job]]
}

class NullJobRepository extends JobRepository {
  override def getByID(id : Long) = Empty  
  override def getCurrent(max: Int) = Empty
}

class FailingJobRepository extends JobRepository {
  import java.io.IOException

  override def getByID(id : Long) = Failure("Weird error when getting " + id, Full(new IOException()), Empty)  
  override def getCurrent(max: Int) = Failure("Strange disconnection", Full(new IOException()), Empty)  
}

class DumbJobRepository extends JobRepository {
  override def getByID(id : Long) = Full(
    Job("Australian Baptist 1925", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
  
  override def getCurrent(max: Int) = Full(List(
    Job("Australian Baptist 1925", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43)),
    Job("Australian Baptist 1926", 657, Batch(""), Nil),
    Job("Australian Baptist 1927", 832, Batch(""), Nil)
  ))
}

class SQLJobRepository extends JobRepository {
  override def getByID(id : Long) = Full(
    Job("Australian Baptist 1925", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43))
  )
  
  override def getCurrent(max: Int) = Full(List(
    Job("Australian Baptist 1925", 628, Batch(""), Nil)
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 364))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 23))
      .add(ScanningProducerEvent(new DateTime, new Period(1, 30, 0, 0), 43)),
    Job("Australian Baptist 1926", 657, Batch(""), Nil),
    Job("Australian Baptist 1927", 832, Batch(""), Nil)
  ))
}
