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
        public EntryControl(String columnName){
           super(10);
           this.columnName =columnName;
        }
        
        public void setValue(Object value){
            if (value==null)
                setText("");
            else
                setText(value.toString());
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

    
    public void setDataset(Dataset dataset){
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
        
    }
    
    
}
