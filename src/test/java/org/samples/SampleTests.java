package org.samples;


import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.lambda.functions.Function1;
import org.lambda.query.Queryable;
import org.llewellyn.words.Words;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTests
{

  @Test
  public void test()
  {
      Queryable<String> words = wordle(" ")
              .where(letterAt('t',1))
              .where(letterNotAt('a', 3))
              .where(letterAt('y', 5))
              ;
      Approvals.verifyAll("", words);
  }

    private Function1<String, Boolean> letterAt(char letter, int position) {
      return w -> w.charAt(position-1) == letter;
    }

    private Function1<String, Boolean> letterNotAt(char letter, int position) {
        return w -> w.charAt(position-1) != letter && w.contains("" + letter);
    }
    private Queryable<String> wordle(String excludingLetters) {
        var exclude = Queryable.as(excludingLetters.split(""));
        return Words.get()
                .where(w -> w.length() == 5)
                .where(w -> exclude.all(l -> !w.contains(l)));
    }
}
