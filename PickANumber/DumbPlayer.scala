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
          println("Yo yo, it's " + id);

        case Connect =>
          game !? Greeting(this, id);
          sender ! Some;

        case Play(dir) =>
          // Dumb player ignores provided information and guesses randomly every damn time
          var guess = Random.nextInt(bound);
          println(id + " guesses " + guess);
          game ! Guess(guess, id);

        case Stop =>
          exit();
      }
    }
  }
}
