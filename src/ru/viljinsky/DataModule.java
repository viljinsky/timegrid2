/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.net.URL;
import java.util.List;


/**
 *
 * @author вадик
 */
public class DataModule {
    
    boolean active = false;
    List<Dataset> tables = null;
    
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
    
    public boolean isActive(){
        return active;
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
    
    public Dataset getTable(String tableName){
        for (Dataset dataset:tables){
            if (dataset.getTableName().equals(tableName)){
                return dataset;
            }
        }
        return null;
    }
    
    public void open(){
        URL url = DataModule.class.getResource("schedule.xml");
        if (url!=null){
            try{
                Parser parser = new Parser();
                parser.open(url.getPath());
                tables = parser.tables;
                active = true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
    }

    
    public Object lookUp(String tableName,String keyName,Object keyValue,String searchColumn) throws Exception{
        Dataset dataset = getTable(tableName);
        if (dataset!=null){
            return dataset.lookUp(keyName, keyValue, searchColumn);
        } else {
            new Exception("Dataset '"+tableName+"'not found");
        }
        return null;
    }
    
    public static void main(String[] args){
        
        DataModule dataModule = DataModule.getInsatnce();
        dataModule.open();
        
    }
    
    
}
