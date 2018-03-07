package manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.ansj.domain.Result;
import org.ansj.domain.Term;

public class WordFilter {

    ArrayList<String> stopWords;
    ArrayList<String> remainWords;

    public WordFilter(){
        stopWords = getWords("/Users/dongyixuan/workspace/证据链/stopWords.txt");
        remainWords = getWords("/Users/dongyixuan/workspace/证据链/remainWords.txt");
    }

    public List<String> filterStopWords(List<String> list){
        List<String> resultList = new ArrayList<String>();

        for(String str : list){
            if (str.length() == 1 && !remainWords.contains(str)){
                continue;
            }
            if(!stopWords.contains(str)){
                resultList.add(str);
            }
        }

        return resultList;
    }

    public boolean isStopWords(String word) {
        return stopWords.contains(word);
    }

    public ArrayList<String> getWords(String fileUrl){
        ArrayList<String> result = new ArrayList<String>();

        try {
            FileInputStream fis = new FileInputStream(fileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine())!=null) {
                result.add(tempString);
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}