package manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.ansj.domain.Result;
import org.ansj.domain.Term;

public class WordFilter {

    ArrayList<String> stopWords;

    public WordFilter(){
        stopWords = getStopWords();
    }

    public void filterStopWords(Result result){
        List<Term> terms = new ArrayList<Term>();

        for(Term term : result){
            if(!stopWords.contains(term.getName())){
                terms.add(term);
            }
        }
        result.setTerms(terms);
    }

    public List<String> filterStopWords(List<String> list){
        List<String> resultList = new ArrayList<String>();

        for(String str : list){
            if (str.length() == 1){
                continue;
            }
            if(!stopWords.contains(str)){
                resultList.add(str);
            }
        }

        return resultList;
    }

    public void filterSingleWords(Result result){
        List<Term> terms = new ArrayList<Term>();

        for(Term term : result){
            if(term.getName().length()>1){
                terms.add(term);
            }
        }
        result.setTerms(terms);
    }
    public ArrayList<String> getStopWords(){
        ArrayList<String> stopWords = new ArrayList<String>();

        try {
            FileInputStream fis = new FileInputStream("/Users/dongyixuan/workspace/Ö¤¾ÝÁ´/stopWords.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine())!=null) {
                String[] str = tempString.split(",");
                stopWords.add(str[0]);
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stopWords;
    }
}