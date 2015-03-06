/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;

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
