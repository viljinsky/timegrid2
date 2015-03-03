/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *
 * @author вадик
 */
public class DataModule {
    String[] subjects = {"Русский","Математика","Физика","Литература","Биология"};
    Color[] subjectColor={Color.CYAN,Color.MAGENTA,Color.PINK,Color.ORANGE,Color.WHITE};
    
    String[] teachers = {"Иванова","Петрова","Сидорова","Романова","Галкина"};
    String[] rooms = {"каб 1","каб 2","каб 3","каб 4","каб 5","каб 6","каб 7"};
    String[] group ={"8-а","8-б","8-в","9-а","9-б","9-в","10-а","10-б"};
    
    String[] depart={"8-a","8-б","8-в","9-а","9-б","9-в","10-а","10-б"};
    
    String[] work_plan ={};
    
    private static DataModule instance = null;
    protected DataModule(){
    }
    
    public static DataModule getInsatnce(){
        if (instance==null){
            instance = new DataModule();
        }
        return instance;
    }
    
    public String getTeacherName(int teacher_id){
        return teachers[teacher_id];
    }
    
    public String getSubjectName(int subject_id){
        return subjects[subject_id];
    }
    
    public Color getSubjectColor(int subject_id){
        return subjectColor[subject_id];
    }
    
    public String getRoomNo(int room_id){
        return rooms[room_id];
    }
    
    public String getGroupTitle(int group_id){
        return group[group_id];
    }
    

    static class Rdr extends DefaultHandler{

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println(localName);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            System.out.println(localName);
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("end document");
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("start document");
        }
        
        public void execute(String path) throws Exception{
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.parse(path);        
            
        }
        
    }
    
    
    public static void main(String[] args){
        URL url = DataModule.class.getResource("schedule.xml");
        if (url!=null){
            String path =url.getPath();
            try{
                BufferedReader br = new BufferedReader(new FileReader(path));
                String l;
                while ((l=br.readLine())!=null){
                    System.out.println(l);
                }
                br.close();

//                Rdr rdr = new Rdr();
//                rdr.execute(path);

        
            } catch (Exception e){
                System.err.println(e.getMessage());
                e.printStackTrace();
                        
            }
        
        }
        
    }
    
    
}
