package ru.viljinsky;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author вадик
 */

public class XMLExport {
    DataModule dm = DataModule.getInsatnce();
    StringBuilder xml;


    private void saveTable(String tableName) throws Exception {
        Dataset ds = dm.getTable(tableName);
        ds.first();
        xml.append(String.format("\n\t<!--                  %s                        -->\n\n", tableName.toUpperCase()));
        xml.append(String.format("\t<table name=\"%s\">\n", tableName));
        
        for (String s : ds.getColumns()) {
            xml.append(String.format("\t\t<column name=\"%s\" type=\"%s\"></column>\n", s, "java.lang.String"));
        }
        
        String[] primary = ds.getPrimary();
        if (primary.length > 0) {
            String primaryString = "";
            for (String s : primary) {
                if (!primaryString.isEmpty()) {
                    primaryString += ";";
                }
                primaryString += s;
            }
            xml.append(String.format("\t\t<primary key=\"%s\"></primary>\n", primaryString));
        }
        
        Map<String,String> lookup = ds.lookupMap;
        if (!lookup.isEmpty()){
            for (String s:lookup.keySet()){
                xml.append(String.format("\t\t<lookup column=\"%s\" references =\"%s\"></lookup>\n",s,lookup.get(s)));
            }
        }
        
        if (!ds.foreignMap.isEmpty()){
            for (String k:ds.foreignMap.keySet()){
                xml.append(String.format("\t\t<foreign key=\"%s\" references=\"%s\"></foreign>\n", k,ds.foreignMap.get(k)));
            }
        }
        
        if (ds.unique.size()>0){
            String res = "";
            for (String s:ds.unique){
                if (!res.isEmpty())
                    res+=";";
                res+=s;
            }
            xml.append(String.format("\t\t<unique columns=\"%s\"></unique>\n", res));
        }
        
        xml.append("\n\t\t<!--          data                   -->\n");
        while (!ds.eof()) {
            xml.append("\t\t<rec ");
            for (String columnName : ds.getColumns()) {
                xml.append(String.format("%s=\"%s\" ", columnName, ds.getValue(columnName)));
            }
            xml.append("></rec>\n");
            System.out.println();
            ds.next();
        }
        xml.append("\t</table>\n");
    }

    public void execute() throws Exception{
        xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n");
            for (String tableName : dm.getTableNames()) {
                saveTable(tableName);
            }
        xml.append("</root>");
    }
    public void save(String fileName){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(xml.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception{
        DataModule dm = DataModule.getInsatnce();
        dm.open();
        XMLExport xmlExport = new XMLExport();
        xmlExport.execute();
        xmlExport.save("test31.xml");
        System.out.println("ExportOK");
                
    }
}
