/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

/**
 *
 * @author вадик
 */

import java.net.URL;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class Parser extends DefaultHandler{

    
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
                addDataset(dataset);
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String[] s;
        switch (qName){
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
            case "foreigh":
//                System.out.println("foreigh");
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

    public void addDataset(Dataset dataset){
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
