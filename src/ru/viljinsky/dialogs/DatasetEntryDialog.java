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
import javax.swing.event.ListDataListener;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;
/**
 *
 * @author вадик
 */

interface IMyControl{
    public void setValue(Object value);
    public Object getValue();
    public JComponent getComponent();
    public String getColumnName();
}

class TextControl extends JTextField implements IMyControl{
    String columnName;
    public TextControl(String columnName){
        super(10);
        this.columnName=columnName;
    }
    
    @Override
    public String getColumnName(){
        return columnName;
    }

    @Override
    public void setValue(Object value) {
        if (value==null)
            setText("");
        else setText(value.toString());
    }

    @Override
    public Object getValue() {
        if (getText().isEmpty())
            return null;
        else
            return getText();
                    
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}

class ListControl extends JComboBox implements IMyControl{
    
    Object value;
    String columnName;
    Map<Object,Object> lookup;
    
    class ListModel implements ComboBoxModel{

        @Override
        public void setSelectedItem(Object anItem) {
            for (Object key:lookup.keySet()){
               if (lookup.get(key).equals(anItem)){
                   value = key;
                   break;
               }
            }
        }

        @Override
        public Object getSelectedItem() {
            return lookup.get(value);
        }

        @Override
        public int getSize() {
            return lookup.size();
        }

        @Override
        public Object getElementAt(int index) {
            int n=0;
            for (Object key:lookup.keySet()){
                if (n==index)
                    return lookup.get(key);
                n+=1;
            }
            return null;
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
    
    public ListControl(String columnName,Map<Object,Object> lookup){
        super();
        this.columnName = columnName;
        this.lookup=lookup;
        setModel(new ListModel());
    }

    @Override
    public void setValue(Object value) {
        this.value=value;
    }

    @Override
    public Object getValue() {
        return  value ;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }
}

public class DatasetEntryDialog extends BaseDialog {
    Dataset dataset;
    Container content;
    IMyControl[] entryes;
    
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
                entryes[i].setValue(values.get(entryes[i].getColumnName()));
            }
        }
    }
    
    public Map<String,Object> getValues(){
        Map<String,Object> result = new HashMap<>();
        
        for (int i=0;i<entryes.length;i++)
            result.put(entryes[i].getColumnName(), entryes[i].getValue());
        
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
        Map<Object,Object> lookup;
        String columnName;
        this.dataset=dataset;
        entryes = new IMyControl[dataset.getColumnCount()];
        setTitle(dataset.getTableName());
        for (int i=0;i<entryes.length;i++){
            columnName = dataset.getColumnName(i);
            lookup= getLookup(columnName);
            
            if (lookup==null)
                entryes[i]= new TextControl(columnName) ;
            else
                entryes[i]= new ListControl(columnName,lookup);
            
            Box box  = Box.createHorizontalBox();
            box.add(new JLabel(entryes[i].getColumnName()));
            box.add(Box.createHorizontalStrut(6));
            box.add(entryes[i].getComponent());
            box.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
            content.add(box);
            content.add(Box.createVerticalStrut(12));
        }
        Border b = BorderFactory.createEmptyBorder(10,10, 10,10);
        ((JPanel)content).setBorder(b);
        
        //----------------------
    }
    
    
}
