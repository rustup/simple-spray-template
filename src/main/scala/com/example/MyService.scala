package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport
import scala.concurrent.duration.Duration
import spray.json.DefaultJsonProtocol
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{Future, Await}

import slick.relational._
import scala.reflect.ClassTag
//import slick.jdbc.JdbcBackend.Table
import spray.json._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
case class Color(name: String, red: Int, green: Int, blue: Int)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val colorFormat = jsonFormat4(Color)
}

class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with SprayJsonSupport{

  //org.example.html.hello("Bob", 22).text
  import MyJsonProtocol._

  val myRoute =
    path("name") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            dbutil.getNameSql
            org.example.html.hello("zhangsan", 22).toString()
          }
        }
      }
    } ~
   path("nameA") {
     get {
       respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
         complete {
           Color("CadetBlue", 95, 158, 160)
         }
       }
     }

   }

}

object dbutil {
  class Spray_Test_Tbl(tag: Tag) extends Table[( Int, String)](tag, "TEST_SPRAY_TBL") {
    def id = column[Int]("ID")
    def name  = column[String]("NAME", O.PrimaryKey)
    def * = (id, name)
  }

  val db = Database.forURL("jdbc:mysql://127.0.0.1/test_spray",
    driver = "com.mysql.jdbc.Driver",
    user="root",
    password="12345678")

  val spray = TableQuery[Spray_Test_Tbl]

  def getNameSql:String = {
    val q = spray.filter(s=>s.name==="zhangsan")

    //val x = q.result.statements.head
    //println(x)
    //x 内容为
    //select `ID`, `NAME` from `TEST_SPRAY_TPL` where `NAME` = 'zhangsan'
/*    val action=q.result
    Await.result(db.run(action),Duration.Inf)*/
    val zz = for (x<-q ) yield (x.name )
    val zd = Await.result( db.run(zz.result), Duration.Inf)
    println(zd.head)
    zd.head
  }
}

