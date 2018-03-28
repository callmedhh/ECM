package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import model.FactModel;
import model.EvidenceModel;

public class KeyWordCalculator {

    static WordFilter wordFilter = new WordFilter();

    public void calcKeyWord(ArrayList<FactModel> fList, ArrayList<EvidenceModel> eList){
        calcFactKeyWord(fList);
        calcEviKeyWord(eList);
    }

    public void calcFactKeyWord(ArrayList<FactModel> fList){
        for(FactModel fact : fList){
            fact.setKeyWordMap(calcKeyWord(fact.getContent()));
            calcEviKeyWord(fact.getEvidenceList());
        }
    }

    public void calcEviKeyWord(ArrayList<EvidenceModel> eList){
        for(EvidenceModel evi : eList){
            evi.setKeyWordMap(calcKeyWord(evi.getContent()));
        }
    }

    private HashMap<String, List<String>> calcKeyWord(String content){
        HashMap<String, List<String>> keyWordMap = new HashMap<String, List<String>>();

        //How Much
        List<String> hm = calcHowMuch(content);
        keyWordMap.put("how much", hm);

        //when
        List<String> when = calcWhen(content);
        keyWordMap.put("when", when);

        //who
        List<String> who = calcWho(content);
        keyWordMap.put("who", who);

        //what
        List<String> what = calcWhat(content);
        keyWordMap.put("what", what);

        //where
        List<String> where = calcWhere(content);
        where = filterWhere(where,what);
        keyWordMap.put("where", where);

        return keyWordMap;
    }

    private List<String> filterWhere(List<String> where, List<String> what) {
        List<String> result = new ArrayList<String>();
        if (what!=null && what.size()>0 && where != null && where.size() > 0) {
            for (String value: where) {
                if (!what.contains(value)) {
                    result.add(value);
                }
            }
        }
        return result;
    }

    private List<String> calcHowMuch(String content){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[0-9]+(.[0-9]+)?+元");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            String hm = matcher.group();
            if(!list.contains(hm))
                list.add(hm);
        }

        return list;
    }

    private List<String> calcWhen(String content){
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(
                "\\d+年\\d+月\\d+日\\d+时\\d+分|"
                        + "\\d+年\\d+月\\d+日\\d+时|"
                        + "\\d+年\\d+月\\d+日|"
                        +"\\d+日|"
                        + "\\d+年\\d+月|"
                        + "\\d+月\\d+日|"
                        + "\\d+时\\d+分|"
                        + "\\d+时");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            String when = matcher.group();
            if(!list.contains(when))
                list.add(when);
        }

        return list;
    }

    //表示地点前的介词
    private ArrayList<String> preps = new ArrayList<String>(Arrays.asList("在","于","至","往","从","沿"));

    private List<String> calcWhere(String content){
        List<String> list = new ArrayList<String>();
        //使用ansj进行分析
        Result result = NlpAnalysis.parse(content);
        List<Term> originTerms = result.getTerms();
        int p = 0;
        while (p < originTerms.size()){
            Term term = originTerms.get(p);
            p ++;
            //找到地点词
            if (term.getNatureStr().equals("s")){
                String where = term.getRealName();
                if(!list.contains(where))
                    list.add(where);
            }
            //找到对应介词,表示之后的词可能是地点
            else if (term.getNatureStr().equals("p") && preps.contains(term.getName())){
                String where = "";
                while (p < originTerms.size()){
                    Term innerTerm = originTerms.get(p);
                    //连接名词、地点词,用于组成一个地点
                    if (innerTerm.getNatureStr().startsWith("n") || innerTerm.getNatureStr().equals("s")){
                        where += innerTerm.getName();
                        p ++;
                        continue;
                    }
                    break;
                }

                if(where.length()>0){
                    //测试备选短语的词性
                    Result placeResult = NlpAnalysis.parse(where);
                    //如果备选短语为一个单词，则不可以为名称词、名词性惯用语、名词性语素
                    if(placeResult.size()==1){
                        String nature = placeResult.get(0).getNatureStr();
                        if(!nature.startsWith("nr") && !("nl").equals(nature) && !("ng").equals(nature)){
                            if(!list.contains(where))
                                list.add(where);
                        }
                    }else{
                        if(!list.contains(where))
                            list.add(where);
                    }
                }
            }
        }

        //去除停用词
        List<String> resultList = wordFilter.filterStopWords(list);
        return resultList;
    }

    private List<String> calcWho(String content){
        List<String> list = new ArrayList<String>();

        Result parserResult = NlpAnalysis.parse(content);
        Iterator<Term> it = parserResult.iterator();
        while(it.hasNext()){
            Term t = it.next();
            String nature = t.getNatureStr();
            String who = t.getRealName();
            //名称应该不止一个字，且词性为名称词
            if(who.length()>1 && nature.startsWith("nr") && !list.contains(who)){
                list.add(who);
            }
        }

        return list;
    }

    private List<String> calcWhat(String content){
        List<String> list = new ArrayList<String>();


        System.out.println("parse ss: "+content);
        //用只提取书名的方式先试一下
        Pattern pattern = Pattern.compile("\\《(.*?)\\》");
        Matcher match = pattern.matcher(content);
        while(match.find()) {
            list.add(match.group(1));//m.group(1)不包括这两个字符
        }

        if(content.contains("(")||content.contains("（")){
            content.replace('(',' ');
            content.replace('（',' ');
            content.replace(')',' ');
            content.replace('）',' ');

        }
        list = hanlpParse(content, list);
        if (list != null) {
            List<String> resultList = wordFilter.filterStopWords(list);
            return resultList;
        }else {
            return new ArrayList<>();
        }
    }
    public static String findDescriptions(Map<String,String> link, String name){
        String result = name;
        while(link.containsKey(name)){
            String value = link.get(name);
            if (!wordFilter.isStopWords(value)) {
                if (link.containsKey(value)) {
                    result = value+result;
                    return result;
                }else{
                    result = link.get(name)+result;
                    name = link.get(name);
                }
            } else {
                if (link.containsKey(value)) {
                    return result;
                } else {
                    name = link.get(name);
                }
            }

        }
        return result;
    }
    private List<String> hanlpParse(String ss, List<String> list){
        try{
            CoNLLSentence sentence = HanLP.parseDependency(ss);
            // 遍历语义依存关系,如果发现带宾语的关系则提取宾语并完善该宾语（利用定中关系来完善）
            Map<String,String> describeLink = new HashMap<String, String>();
            for (CoNLLWord word : sentence)
            {
                if(word.DEPREL.equals("定中关系")){
                    describeLink.put(word.HEAD.LEMMA,word.LEMMA);
                }
                if(word.DEPREL.equals("动宾关系")||word.DEPREL.equals("介宾关系")){
                    // TODO 过滤掉 word.LEMMA 为停用词的词组
                    if(wordFilter.isStopWords(word.LEMMA)){
                        continue;
                    }
                    String tmp = findDescriptions(describeLink,word.LEMMA);
                    //测试备选短语的词性
                    if (tmp.equals("放在")){
                        continue;
                    }
                    Result result = NlpAnalysis.parse(tmp);
                    //如果备选短语为一个单词，则不可以为名称词、地点词或动词
                    if(result.size()==1){
                        String nature = result.get(0).getNatureStr();
                        if(!nature.startsWith("nr") && !nature.startsWith("nt") && !nature.startsWith("ns")
                                && !("s").equals(nature) && !nature.startsWith("v")){
                            if(!list.contains(tmp))
                                list.add(tmp);
                        }
                    }else{
                        if(!list.contains(tmp))
                            list.add(tmp);
                    }
                }
            }

            return list;
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        KeyWordCalculator keyWordCalculator = new KeyWordCalculator();
        List<String> list = new ArrayList<String>();
        String content = "10、乘车凭证、发票、收据5000元、销货清单，证实123.45元因周红喜遇害，各附带民事诉讼原告人开支各项实际费用共计人民币31640．5元。";
        List<String> result = keyWordCalculator.calcHowMuch(content);
        for (String value: result) {
            System.out.println(value);
        }

    }
}