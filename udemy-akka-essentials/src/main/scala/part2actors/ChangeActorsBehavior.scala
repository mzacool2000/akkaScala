package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChangeActorsBehavior.Mom.MomStart

object ChangeActorsBehavior extends App {
  class FussyKid extends Actor {
    import Mom._
    import FussyKid._
    var state = HAPPY
    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(msg) =>
        println(s"my mom ask :$msg")
        if (state == HAPPY) sender() ! KidAccept
        else sender() ! KidReject
    }
  }


  class Mom extends Actor{
    import Mom._
    import FussyKid._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("Do you want to play")
      case KidAccept => println("yei my son is happy")
      case KidReject => println("my kid is sad but as he's healthy deserve a chocolate")
        sender() ! Food(CHOCOLATE)
        sender() ! Ask("Now how you feeling?")
    }
  }
  class StateLessFussyKid extends Actor {
    import Mom._
    import FussyKid._
    override def receive: Receive = happyReceive
    def happyReceive:Receive = {
      case Food(VEGETABLE) => context.become(sadReceive)
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(VEGETABLE) =>
      case Food(CHOCOLATE) => context.become(happyReceive)
      case Ask(_) => sender() ! KidReject
    }
  }

  val system = ActorSystem("ChangeActorsBehavior")
  val mom = system.actorOf(Props[Mom])
  val fussyKid = system.actorOf(Props[FussyKid])
  val stateLessFussyKid = system.actorOf(Props[StateLessFussyKid])

  mom ! MomStart(fussyKid)

  println("---------------------------")

  mom ! MomStart(stateLessFussyKid)


  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message:String)
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  object FussyKid{
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

}
