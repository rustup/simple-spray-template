package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
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