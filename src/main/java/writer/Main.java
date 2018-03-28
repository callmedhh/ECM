package writer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import manager.ChainCreator;
import model.FactModel;
import model.EvidenceModel;
import reader.JsonReader;


public class Main {

    public static void main(String[] args) throws IOException{
        //遍历文件夹下所有的文件,生成中间计算结果
        String path = "/Users/dongyixuan/workspace/证据链/裁判文书/故意杀人罪/2015";
        File folder = new File(path);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            HashSet<String> typeList = new HashSet<>();
            for (File file2: files) {
                if (file2.isFile()) {
                    System.out.println("processing:"+file2.getName());
                    generateIntermediateResult("2015",file2.getName(),typeList);
                }
            }
            for (String type: typeList){
                System.out.println("type"+type);
            }
        }

        //遍历中间结果文件夹下的所有文件，生成Excel
//        String jsonPath = "/Users/dongyixuan/workspace/证据链/裁判文书/JsonResult";
//        File jsonFolder = new File(jsonPath);
//        if (jsonFolder.exists()) {
//            File[] files = jsonFolder.listFiles();
//            for (File file: files) {
//                if (file.isFile() && file.getAbsolutePath().endsWith("xmlfact.json")) {
//                    String name = file.getName();
//                    String fileName = name.split("\\.")[0];
//                    writeIntermediateResultToExcel(file.getPath(),fileName);
//                }
//            }
//        }
    }

    public static void generateIntermediateResult(String childFolderName,String fileName,HashSet<String> typeList) {
        ChainCreator chainCreator = new ChainCreator();
        ArrayList<FactModel> fList = new ArrayList<FactModel>();
        ArrayList<EvidenceModel> eList = new ArrayList<EvidenceModel>();
        String folder = "/Users/dongyixuan/workspace/证据链/裁判文书/故意杀人罪";
        chainCreator.creatChain(folder,childFolderName,fileName,fList,eList,typeList);
    }

    public static void writeIntermediateResultToExcel(String jsonFilePath, String fileName) throws IOException{
        ExcelWriter excelWriter = new ExcelWriter();
        JsonReader jsonReader = new JsonReader();

        ArrayList<FactModel> fList = new ArrayList<FactModel>();

        fList = jsonReader.readFactModelListFromFile(jsonFilePath);
        excelWriter.writeExcel(fileName, fList);
    }

//    public static void oldWayToGenerateExcel() {
//        ChainCreator chainCreator = new ChainCreator();
//        ExcelWriter excelWriter = new ExcelWriter();
//
//        ArrayList<FactModel> fList = new ArrayList<>();
//        ArrayList<EvidenceModel> eList = new ArrayList<>();
//
//        String path = "";
//        File file = new File(path);
//        String fileName = "444.xml";
//        String filePath = path + "/" +fileName;
//        fList = chainCreator.creatChain(filePath, fList, eList);
//        excelWriter.writeExcel(fileName,fList);
//    }
}