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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

class Dataset extends ArrayList<Object[]>{
    String name ;
    Map<Integer,String> columns;
    
    protected Integer index;

    public String getTableName(){
        return name;
    }
    
    public void first(){
        index = 0;
    }
    
    public void next(){
        if (index<size()){
            index+=1;
        }
    }
    
    public void prior(){
        if (index>0){
            index-=1;
        }
    }
    
    public void last(){
        index = size()-1;
    }
    
    public boolean eof(){
        return index>=size();
    }
    
    public boolean bof(){
        return index<=0;
    }
    
    public Object getValue(String columnName){
        Object[] rowset = get(index);
        int n= getColumnIndex(columnName);
        return rowset[n];
    }
    
    public Integer getInteger(String columnName){
        Object value = getValue(columnName);
        if (value!=null){
            return Integer.parseInt((String)value);
        }
        return null;
    }
    
    public Object lookUp(String columnName,Object columnValue,String searchName) throws Exception{
        int n = getColumnIndex(columnName),k=getColumnIndex(searchName);
        if (n<0)
            new Exception("column '"+columnName+"' not found");
        if (k<0)
            new Exception("column '"+searchName+"' not found");
        
        Integer value;
        for (Object[] recordset:this){
            value = Integer.parseInt((String)recordset[n]);
            if (value.equals(columnValue)){
                return recordset[k];
            }
        }
        return null;
    }

    public Dataset(String name){
        this.name=name;
        columns = new HashMap<>();
    }

    public int getColumnIndex(String columnName){
        for (int n:columns.keySet()){
            if (columns.get(n)==columnName){
                return n;
            }
        }
        return -1;
    }

    public Integer addColumn(String columnName){
        int i = 0,n;
        for (Iterator it=columns.keySet().iterator();it.hasNext();){
            n=(Integer)it.next();
            if (n>=i) i= n+1;
        }
        columns.put(i, columnName);
        return i;
    }

    public String[] getColumns(){
        String[] result = new String[columns.size()];
        for (Iterator<Integer> it=columns.keySet().iterator();it.hasNext();){
            int n= it.next();
            result[n]=columns.get(n);
        }
        return result;
    }

    public void addRecord(Attributes attr){
        String columnName ;
        Integer columnIndex;
        for (int i=0;i<attr.getLength();i++){
            columnName = attr.getQName(i);
            columnIndex = getColumnIndex(columnName);
            if (columnIndex<0)
                columnIndex = addColumn(columnName);
        }

        Object[] record = new Object[columns.size()];
        for (int i=0;i<record.length;i++){
            record[i]=attr.getValue(i);
        }
        super.add(record);

    }
}

public class Parser extends DefaultHandler{

    
    Dataset dataset;
    List<Dataset> tables;
    
    
    public Dataset getDataset(String tableName){
        for (Dataset dataset: tables){
            if (dataset.name.equals(tableName)){
                return dataset;
            }
        }
        return null;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "table":
                tables.add(dataset);
                break;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case "table":
                dataset = new Dataset(attributes.getValue("name"));
                break;
            case "rec":
                dataset.addRecord(attributes);
                break;
        }
        
    }

    @Override
    public void startDocument() throws SAXException {
        tables = new ArrayList<>();    
        System.out.println("Start document");
    }
    

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Stop document");
//        for (Dataset s:tables){
//            System.out.println(s.name);
//            System.out.println("----------");
//            for (String columnName:s.getColumns()){
//                System.out.println("column ->"+columnName);
//            }
//            for (Object[] rec:s){
//                for (int i=0;i<rec.length;i++){
//                    System.out.println(rec[i].toString());
//                }
//            }
//        }
    }
    
    XMLReader reader;
    
    public Parser() throws Exception{
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        reader = sp.getXMLReader();
        reader.setContentHandler(this);
    }
    
    public void open(String path) throws Exception{
        reader.parse(path);
    }
    
    

    public static void main(String[] args) {
        Parser parser;
        
        URL url = Parser.class.getResource("schedule.xml");
        if (url!=null){
            try{
                parser = new Parser();
                parser.open(url.getPath());

             Dataset ds = parser.getDataset("teacher");
             Object[] rs;
             for (int i=0;i<ds.size();i++){
                 rs = ds.get(i);
                 System.out.println(rs);
             }
             
             
             ds.first();
             while (!ds.eof()){
                 System.out.println(ds.getValue("id")+" "+ ds.getValue("first_name"));
                 ds.next();
             }
                
                
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
        
        
    }
    
}
