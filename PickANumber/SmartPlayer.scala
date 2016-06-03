// (c) Ryan Patton

import scala.actors.Actor;
import Actor._;


class SmartPlayer(id : String, game: Game, bound : Int) extends Player {
  var high = bound;
  var low = 0;
  var guess = bound / 2;

  override def act  = {
    loop {
      react {
        case Shoutout =>
          // Obvious marker of intelligence
          println("Greetings, it is I, " + id);

        case Connect =>
          game !? Greeting(this, id);
          sender ! Some;

        case Play(dir) =>
          // Smart player uses binary search to guess the number
          if (dir == "low") {
            low = guess;
            guess = (high + guess) / 2;
          } else if (dir == "high") {
            high = guess;
            guess = guess / 2;
          } 

          println(id + " guesses " + guess);
          game ! Guess(guess, id);

        case Stop =>
          exit();
      }
    }
  }
}
