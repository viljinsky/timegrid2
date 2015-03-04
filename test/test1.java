
import java.io.BufferedWriter;
import java.io.FileWriter;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



public class test1 {
    
    static DataModule dm = DataModule.getInsatnce();
    StringBuilder xml;
    
    void saveTable(String tableName){
        Dataset ds = dm.getTable(tableName);
        ds.first();
        
        
        xml.append(String.format("\t<table name=\"%s\">\n",tableName));
        
        for (String s:ds.getColumns()){
            xml.append(String.format("\t\t<column name=\"%s\" type=\"%s\"></column>\n",s,"java.lang.String"));
        }
        
        String[] primary = ds.getPrimary();
        
        if (primary.length>0){
            String primaryString = "";
            for (String s:primary){
                if (!primaryString.isEmpty()){
                    primaryString+=";";
                }
                primaryString+=s;
            }
            xml.append(String.format("\t\t<primary key=\"%s\"></primary>\n", primaryString));
        }
        
        while (!ds.eof()){
            xml.append("\t\t<rec ");
            for (String columnName:ds.getColumns()){
                xml.append(String.format("%s=\"%s\" ",columnName,ds.getValue(columnName)));
            }
            xml.append("></rec>\n");
            System.out.println();
            ds.next();
        }
        
        xml.append("\t</table>\n");
    }
    
    void execute(){
        xml = new StringBuilder();
        
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
        
        dm.open();
        for (String tableName:dm.getTableNames()){
            saveTable(tableName);
        }
        
        xml.append("</root>");
        
        System.out.println(xml);
        
        
        BufferedWriter bw = null;
        try{ 
            bw = new BufferedWriter(new FileWriter("test.xml"));
        
            bw.write(xml.toString());
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args){
        dm.open();
        new test1().execute();
        
    }
}
