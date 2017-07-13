// (c) Ryan Patton

//import scala.actors.Actor;
//import Actor._;
import akka.actor._

// Game object for this simple 'Pick a Number' game, where players try to guess the right number
class Game(num : Int) extends Actor {
  // maintains list of connected players
  var players = List[Player]();

  def act = {
    loop {
      react {
        case Greeting(p, id) =>
          players = players :+ p;
          println("Player \"" + id + "\" connected");
          // Pass back evidence of successful connection
          sender ! Some;

        case Guess(g, id) =>
          if (g == num) {
            println("It was " + g + "! Good work " + id);
            sender ! Stop;
            endGame;
          } else {
            println("Nope, try again " + id);

            // Provide player with information about whether guess was too high or low
            if (g < num) {
              sender ! Play("low");
            } else {
              sender ! Play("high");
            }
          }

        case Stop =>
          endGame;
      }
    }
  }

  // Notify all players that game has ended
  def endGame {
    for (i <- players) i ! Stop;
    exit();
  }

  start();
}
