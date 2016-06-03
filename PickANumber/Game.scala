// (c) Ryan Patton

import scala.actors.Actor;
import Actor._;

// Game object for this simple 'Pick a Number' game, where players try to guess the right number
class Game(num : Int) extends Actor {
  // Maintains list of connected players
  var players = List[Player]();

  def act = {
    loop {
      react {
        case Greeting(p, id) =>
          players = players :+ p;
          println("Player \"" + id + "\" connected");
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

  // Notifies all players that the game has ended
  def endGame {
    for (i <- players) i ! Stop;
    exit();
  }

  start();
}
