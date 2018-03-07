package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EvidenceModel {

    private int id;				//���
    private String content;		//֤����ϸ
    private String name;		//֤������
    private String type;		//֤������
    private String submitter;	//�ύ��
    private String reason;		//��֤����
    private String result;		//��֤����
    private HashMap<String, List<String>> keyWordMap = new HashMap<String, List<String>>();
    private ArrayList<String> headList;

    public EvidenceModel(){
        headList = new ArrayList<String>();
    }

    public void setEvidence(Node node,HashSet<String> typeList){
        Element currentElement = (Element) node;
        this.setContent(currentElement.getAttribute("value")); //���֤������

        NodeList childNodes = node.getChildNodes();
        for(int j=0;j<childNodes.getLength();j++){
            Node childNode = childNodes.item(j);
            if(("ZJMX").equals(childNode.getNodeName())){
                NodeList grandChildNodes = childNode.getChildNodes();
                for(int m=0;m<grandChildNodes.getLength();m++){
                    Element grandChildElement = (Element) grandChildNodes.item(m);
                    String name = grandChildElement.getAttribute("nameCN");
                    if(("����").equals(name)){
                        this.setName(grandChildElement.getAttribute("value"));
                    }else if(("����").equals(name)){
                        this.setType(grandChildElement.getAttribute("value"));
                        typeList.add(grandChildElement.getAttribute("value"));
                    }else if(("�ύ��").equals(name)){
                        this.setSubmitter(grandChildElement.getAttribute("value"));
                    }
                }
            }
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HashMap<String, List<String>> getKeyWordMap() {
        return keyWordMap;
    }
    public void setKeyWordMap(HashMap<String, List<String>> keyWordMap) {
        this.keyWordMap = keyWordMap;
    }

    public ArrayList<String> getHeadList() {
        return headList;
    }
    public void setHeadList(ArrayList<String> headList) {
        this.headList = headList;
    }
    public void addHead(String head){
        headList.add(head);
    }
}