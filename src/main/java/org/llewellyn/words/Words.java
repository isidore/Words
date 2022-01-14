package org.llewellyn.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.spun.util.ObjectUtils;
import com.spun.util.StringUtils;
import org.lambda.query.Queryable;

public class Words
{

    public static Queryable<String> get() throws Error {
        return importWords("words.txt");
    }

    public static Queryable<String> importWords(String filename)
    {
        Queryable<String> words = new Queryable<>(String.class);
        InputStream resourceAsStream = Words.class.getResourceAsStream("../../../"+filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        try
        {
            while (reader.ready())
            {
                String word = reader.readLine().trim();
                if (!StringUtils.isEmpty(word))
                {
                    words.add(word);
                }
            }
            reader.close();
        }
        catch (IOException e)
        {
            ObjectUtils.throwAsError(e);
        }
        return words;
    }
}