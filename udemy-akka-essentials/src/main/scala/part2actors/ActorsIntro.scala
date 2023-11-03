package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {

  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  class WordCounterActor extends Actor {
    var totalWord = 0
    override def receive: Receive = {
      case message: String => totalWord += message.split(" ").length
          println(totalWord)
      case msg => println(s"what append ${msg.toString}")
    }
  }
  val wordCounterActor = actorSystem.actorOf(Props[WordCounterActor], "wordCounter")

  wordCounterActor ! "this actor works fine"

  object Person {
    def props(name:String) = Props(new Person(name))
  }

  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"hi my name is ${name}")
      case _ =>
    }
  }
  val person = actorSystem.actorOf(Person.props("Emiliano"))
  person ! "hi"

}
