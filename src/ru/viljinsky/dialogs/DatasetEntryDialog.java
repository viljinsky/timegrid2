/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;
import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;
/**
 *
 * @author вадик
 */
public class DatasetEntryDialog extends BaseDialog {
    Dataset dataset;
    Container content;
    EntryControl[] entryes;

    class EntryControl extends JTextField{
        String columnName;
        Map<Object,Object> lookup;
        public EntryControl(String columnName){
           super(10);
           lookup = getLookup(columnName);
           this.columnName =columnName;
        }
        
        public void setValue(Object value){
            if (value==null)
                setText("");
            else{
                if (lookup!=null){
                    setText((String)lookup.get(value));
                } else 
                    setText(value.toString());
            }
        }
        
        public Object getValue(){
            if (getText().isEmpty())
                return null;
            else
                return getText();
        }
        
    }
    
    public DatasetEntryDialog(JComponent owner) {
        super(owner);
    }

    @Override
    public void initComponents(Container content) {
        this.content=content;
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    }

    public void setValues(Map<String, Object> values) {
        for (String columnName:values.keySet()){
            System.out.println(values.get(columnName));
            for (int i=0;i<entryes.length;i++){
                entryes[i].setValue(values.get(entryes[i].columnName));
            }
        }
    }
    
    public Map<String,Object> getValues(){
        Map<String,Object> result = new HashMap<>();
        
        for (int i=0;i<entryes.length;i++)
            result.put(entryes[i].columnName, entryes[i].getValue());
        
        return result;
    }

    
    protected Map<Object,Object> getLookup(String columnName){
        String lookup;
        String t1,t2,t3;
        String[] f;
        DataModule dm = DataModule.getInsatnce();
        Dataset ds;
        Map<Object,Object> map = new HashMap<>();
        String[] primary;
        
        lookup=dataset.getLookup(columnName);
        if (lookup!=null){
            f=lookup.split(";");
            t1=f[0].split("\\.")[0];
            t2=f[0].split("\\.")[1];
            try{
                ds=dm.getTable(t1);
                primary = ds.getPrimary();
                if (primary.length==0){
                    System.err.println("Примари для \""+t1+"\" - не найден");
                    return null;
                }
                t3 = ds.getPrimary()[0];
                ds.first();
                while (!ds.eof()){
                    map.put(ds.getValue(t3), ds.getValue(t2));
                    ds.next();
                }
  
                System.out.println(map);
            } catch (Exception e){
                e.printStackTrace();
            }
            return map;
        }
        return null;
    }
    
    public void setDataset(Dataset dataset){
        this.dataset=dataset;
        entryes = new EntryControl[dataset.getColumnCount()];
        setTitle(dataset.getTableName());
        for (int i=0;i<entryes.length;i++){
            entryes[i]= new EntryControl(dataset.getColumnName(i));
            
            Box box  = Box.createHorizontalBox();
            box.add(new JLabel(entryes[i].columnName));
            box.add(Box.createHorizontalStrut(6));
            box.add(entryes[i]);
            box.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
            content.add(box);
            content.add(Box.createVerticalStrut(12));
        }
        Border b = BorderFactory.createEmptyBorder(10,10, 10,10);
        ((JPanel)content).setBorder(b);
        
        //----------------------
    }
    
    
}
