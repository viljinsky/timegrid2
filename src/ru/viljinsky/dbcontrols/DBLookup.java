/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dbcontrols;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */
public class DBLookup extends JComboBox {
    Map<Object, Object> map = new HashMap<>();
    Object selected = null;
    Dataset dataset;
    
    public Dataset getDataset(){
        return dataset;
    }

    class DBLookupModel implements ComboBoxModel {

        @Override
        public void setSelectedItem(Object anItem) {
            for (Object value : map.keySet()) {
                if (map.get(value).equals(anItem)) {
                    selected = value;
                }
            }
        }

        @Override
        public Object getSelectedItem() {
            return map.get(selected);
        }

        @Override
        public int getSize() {
            return map.size();
        }

        @Override
        public Object getElementAt(int index) {
            Object[] keys = map.keySet().toArray();
            return map.get(keys[index]);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }

    public DBLookup(){
    }
    
    public void setDataset(Dataset dataset,String ColumnName,String[] values){
        this.dataset = dataset;
        String strValue;
        dataset.first();
        try {
            while (!dataset.eof()) {
                strValue = "";
                for (String s : values) {
                    if (!strValue.isEmpty()) {
                        strValue += " ";
                    }
                    strValue += dataset.getString(s);
                }
                map.put(dataset.getValue(ColumnName), strValue);
                dataset.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setModel(new DBLookupModel());
        
    }
    
    public DBLookup(Dataset dataset, String ColumnName, String[] values) {
        setPreferredSize(new Dimension(180,22));
        setDataset(dataset, ColumnName, values);
    }

    public Object getValue() {
        return selected;
    }
    
    public void setValue(Object value){
        this.selected=value;
    }
    
}
