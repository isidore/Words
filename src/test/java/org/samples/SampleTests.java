package org.samples;


import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.lambda.query.Queryable;
import org.llewellyn.words.Words;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTests
{

  @Test
  public void test()
  {
      Queryable<String> words = Words.get()
              .where(w -> w.length() == 5)
//              .where(w -> w.charAt(0) == 't')
//              .where(w -> w.charAt(4) == 'y')
//              .where(w -> w.charAt(2) != 'a')
//              .where(w -> w.contains("a"))
              .where(w -> Queryable.as("pgh".split("")).all(l -> w.contains(l)))
              ;
      Approvals.verifyAll("", words);
  }
}
