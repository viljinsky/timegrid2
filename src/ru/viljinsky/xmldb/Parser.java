/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.xmldb;

/**
 *
 * @author вадик
 */

import ru.viljinsky.xmldb.Dataset;
import java.net.URL;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

interface IParserListener{
    public void addTable(Dataset dataset);
}

public class Parser extends DefaultHandler{

    IParserListener listener = null;
    
    private Dataset dataset;
    
    public void open(String path) throws Exception{
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader reader = sp.getXMLReader();
        reader.setContentHandler(this);
        reader.parse(path);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "table":
                if (listener!=null){
                    listener.addTable(dataset);
                }
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String[] s;
        switch (qName){
            case "root": case "columns":
                break;
            case "table":
                dataset = new Dataset(attributes.getValue("name"));
                if (attributes.getValue("primary")!=null){
                    s=attributes.getValue("primary").split(";");
                    for (String ss1:s){
                        dataset.primary.add(ss1);
                    }
                    
                }
                break;
            case "rec":
                dataset.addRecord(attributes);
                /*
                // !! Заменить
                Map<String,Object> map = new HashMap<>();
                for (int i=0;i<attributes.getLength();i++){
                    map.put(attributes.getQName(i),attributes.getValue(i));
                }
                try{
                    dataset.append(map);
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
                */
                break;
                
            case "column":
                
                break;
                
            case "primary":
                s = attributes.getValue("key").split(";");
                for (String ss1:s){
                    dataset.primary.add(ss1);
                }
                
                break;
            case "lookup":
                dataset.lookupMap.put(attributes.getValue("column"), attributes.getValue("references"));
                break;
                
            case "foreign":
                dataset.foreignMap.put(attributes.getValue("key"), attributes.getValue("references"));
//                System.out.println(dataset.tableName+" "+ dataset.foreignMap.toString());
                break;
                
            case "unique":
                if (attributes.getValue("columns")==null){
                    System.err.println(dataset.tableName+" Неверный аттрибут unique");
                    break;
                }
                
                s=attributes.getValue("columns").split(";");
                for (String ss:s){
                    dataset.unique.add(ss);
                }
                break;
            default:
                System.err.println("unknow '"+qName+"'");
        }
        
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start document");
    }
    

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Stop document");
    }


    public static void main(String[] args) {
        Parser parser;
        
        URL url = Parser.class.getResource("schedule.xml");
        if (url!=null){
            try{
                parser = new Parser();
                parser.open(url.getPath());
                
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
    }
    
}
