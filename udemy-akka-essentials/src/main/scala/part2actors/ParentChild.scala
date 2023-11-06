package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ParentChild.Parent.{CreateChild, TellChild}

object ParentChild extends App {

  object Parent {
    case class CreateChild(mame:String)
    case class TellChild(msg: String)
  }
  class Parent extends Actor{
    import Parent._
    override def receive: Receive = {
      case CreateChild(mane) =>
        println(s"${self.path} creating child")
        val childRef = context.actorOf(Props[Child], mane)
        context.become(withChild(childRef))
    }

    def withChild(childRed: ActorRef): Receive = {
      case TellChild(msg) => childRed forward msg
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case msg => println(s"${self.path} hey recibe a $msg")
    }
  }

  val system = ActorSystem("ParentChild")
  val parent = system.actorOf(Props[Parent], "parent")
  parent ! CreateChild("Juan")
  parent ! TellChild("hello my son")

  /**
   * Actor selection
   */
  val actorSelector = system.actorSelection("/user/parent/Juan")
  actorSelector ! "hello my name es Juan"
}
