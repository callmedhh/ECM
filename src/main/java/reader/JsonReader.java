package reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.FactModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongyixuan on 2017/12/28.
 */
public class JsonReader {
    public ArrayList<FactModel> readFactModelListFromFile(String filePath) throws IOException{
        Gson gson = new GsonBuilder().create();
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
        String content = "";
        String temp;
        if ((temp = reader.readLine())!=null){
            content += temp;
        }
        reader.close();
        FactModel[] arr = gson.fromJson(content, FactModel[].class);
        ArrayList<FactModel> result = new ArrayList<>();
        for (FactModel fact: arr){
            result.add(fact);
        }
        return result;
    }
}
