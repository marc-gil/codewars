package dev.marcgil.codewars.kyu6;

class WhoLikesIt {

  /**
   * DESCRIPTION:
   * You probably know the "like" system from Facebook and other pages.
   * People can "like" blog posts, pictures or other items.
   * We want to create the text that should be displayed next to such an item.
   * <p>
   * Implement the function which takes an array containing the names of people that like an item.
   * It must return the display text as shown in the examples:
   * <p>
   * []                                -->  "no one likes this" <br>
   * ["Peter"]                         -->  "Peter likes this" <br>
   * ["Jacob", "Alex"]                 -->  "Jacob and Alex like this" <br>
   * ["Max", "John", "Mark"]           -->  "Max, John and Mark like this" <br>
   * ["Alex", "Jacob", "Mark", "Max"]  -->  "Alex, Jacob and 2 others like this" <br>
   * Note: For 4 or more names, the number in "and 2 others" simply increases.
   */
  public static String whoLikesIt(String... names) {
    return switch (names.length) {
      case 0 -> "no one likes this";
      case 1 -> String.format("%s likes this", names[0]);
      case 2 -> String.format("%s and %s like this", (Object[]) names);
      case 3 -> String.format("%s, %s and %s like this", (Object[]) names);
      default ->
          String.format("%s, %s and %d others like this", names[0], names[1], names.length - 2);
    };
  }

}
