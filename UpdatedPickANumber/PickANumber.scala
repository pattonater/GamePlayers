// (c) Ryan Patton

//import scala.actors.Actor;
//import Actor._;
import akka.actor._;
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._

import scala.util.Random;

// Messages for players and games defined here 
sealed trait Message;
case object Stop extends Message;

abstract class PlayerMessage extends Message;
case object Connect extends PlayerMessage;
case object Shoutout extends PlayerMessage;
case class Play(dir : String) extends PlayerMessage;

abstract class GameMessage extends Message;
case class Guess(g : Int, id : String) extends GameMessage;
case class Greeting(p : Player, id : String) extends GameMessage;


// Superclass for 'Dumb' and 'Smart' players so they can both be referred to as 'Player' objects
class Player() extends Actor() {
  def recieve {}
  start()
}


// Controller for the game and player objects for this 'Pick a Number' game
object PickANumber {
  // Bound for game set to 25 unless user provides their own
  def main (args : Array[String]) : Unit = {
    var bound = 25;
    if (args.length > 0) {
      bound = Integer.parseInt(args(0));
    }

    // Creates game with randomly chosen number below bound
    val game = new Game(Random.nextInt(bound));

    // Could theoretically create an arbitrary large number of players but for now just two
    val dumb = new DumbPlayer("Zeus", game, bound);
    val smart = new SmartPlayer("Poseidon", game, bound);

    // Wait for all players to connect before continuing
    dumb !? Connect;
    smart !? Connect;

    dumb ! Shoutout;
    smart ! Shoutout;

    dumb ! Play("");
    smart ! Play("");
  }
}
