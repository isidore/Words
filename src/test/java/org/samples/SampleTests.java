package org.samples;


import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.reporters.macosx.DiffMergeReporter;
import org.junit.jupiter.api.Test;
import org.lambda.functions.Function1;
import org.lambda.query.OrderBy;
import org.lambda.query.Queryable;
import org.llewellyn.words.Words;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UseReporter(DiffMergeReporter.class)
public class SampleTests
{

  //@Test
  public void test()
  {
      Queryable<String> words = wordle("easclin")
             .where(letterAt('o', 2))
              //.where(letterAt('i', 3))
              .where(letterNotAt('r',4))
              .where(letterNotAt('t', 1))
              ;
      Approvals.verifyAll("", words);
  }

    @Test
    public void testLetterDistribution()
    {
        Queryable<Map.Entry<String, Queryable<String>>> words = getLetterDistribution();
        Approvals.verifyAll("", words, m -> String.format("%s = %s", m.getKey(), m.getValue().size()));
    }

    private Queryable<Map.Entry<String, Queryable<String>>> getLetterDistribution() {
        Queryable<Map.Entry<String, Queryable<String>>> words = wordle(" ")
                .selectManyArray(w -> w.split("")).groupBy(l -> l)
                .orderBy(OrderBy.Order.Descending, m -> m.getValue().size())
                ;
        return words;
    }

    @Test
    public void testSecondBest()
    {
        var distribution = getLetterDistribution();


        var words = wordle("tears")
               // .where(w -> include.all(l -> w.contains(l)))
                .where(containsNoDuplicates())
                .orderBy(OrderBy.Order.Descending, w -> toLetters(w).sum(l -> distribution.where(d -> d.getKey().equals(l)).first().getValue().size()))
                ;
        words = take(words, 20);
        Approvals.verifyAll("", words);
    }

    private Queryable<String> take(Queryable<String> words, int number) {
        return  Queryable.as(words.subList(0, number),words.getType());
    }

    private Queryable<String> toLetters(String possible) {
        return Queryable.as(possible.split(""));
    }

    private Function1<String, Boolean> containsNoDuplicates() {
      return w -> toLetters(w).groupBy(l -> l).size() ==5 ;
    }
    private Function1<String, Boolean> letterAt(char letter, int position) {
      return w -> w.charAt(position-1) == letter;
    }

    private Function1<String, Boolean> letterNotAt(char letter, int position) {
        return w -> w.charAt(position-1) != letter && w.contains("" + letter);
    }
    private Queryable<String> wordle(String excludingLetters) {
        var exclude = toLetters(excludingLetters);
        return Words.get()
                .where(w -> w.length() == 5)
                .where(w -> w.equals(w.toLowerCase(Locale.ROOT)))
                .where(w -> exclude.all(l -> !w.contains(l)));
    }
}
