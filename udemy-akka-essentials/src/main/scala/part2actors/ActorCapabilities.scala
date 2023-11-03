package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorCapabilities extends App {
  val system = ActorSystem("actorCapabilities")

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message: String => println(s"s[SimpleActor] I have received $message")
      case number: Int => println(s"s[SimpleActor] I have received $number")
      case SpecialMessage(msg) => println(s"s[SimpleActor] I have received special msg $msg")
    }
  }

  val simpleActor = system.actorOf(SimpleActor.props)

  simpleActor ! "hey this is a tex message"
  simpleActor ! 4
  simpleActor ! SpecialMessage("super Special")


  object SimpleActor {
    def props = Props(new SimpleActor)
  }
  case class SpecialMessage(msg:String)

}
