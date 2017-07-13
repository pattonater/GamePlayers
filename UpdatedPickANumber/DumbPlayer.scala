// (c) Ryan Patton

import scala.actors.Actor;
import Actor._;
import scala.util.Random;


class DumbPlayer(val id : String, game : Game, bound : Int) extends Player {
  override def act = {
    loop {
      react {
        case Shoutout =>
          // Pity this fool
          println("Hey hey, it's " + id);

        case Connect =>
          game !? Greeting(this, id);
          // Pass back evidence of successful connection
          sender ! Some;

        case Play(dir) =>
          // Ignores provided direction information and guess randomly every time
          var guess = Random.nextInt(bound);
          println(id + " guesses " + guess);
          game ! Guess(guess, id);

        case Stop =>
          exit();
      }
    }
  }
}
