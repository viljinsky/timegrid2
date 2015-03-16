/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/**
 *
 * @author вадик
 */



public class SelectGridDialog  extends BaseDialog{
    JTable table ;
    DataModule dataModule = DataModule.getInsatnce();
    
    class Model extends AbstractTableModel{
        Dataset dataset;

        @Override
        public int getRowCount() {
            return dataset.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object[] rowset = dataset.get(rowIndex);
            switch (columnIndex){
                case 0:
                    return true;
                default:
                    return rowset[0];
                    
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex){
                case 0:
                    return Boolean.class;
                default:    
                    return super.getColumnClass(columnIndex); //To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
             return  columnIndex==0;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex){
                case 0:
                    super.setValueAt(aValue, rowIndex, columnIndex); //To change body of generated methods, choose Tools | Templates.
                    
            }
        }
        
        
        
        
        
        
    }
    
    

    public SelectGridDialog(JComponent owner) {
        super(owner);
    }

    
    @Override
    public void initComponents(Container content) {
        table = new JTable(10, 10);
        content.setLayout(new BorderLayout());
        content.add(new JScrollPane(table));
    }
    
    public void setTableName(String tableName){
        try{
            Dataset dataset = dataModule.getTable(tableName);
            Model model = new Model();
            model.dataset=dataset;
            table.setModel(model);
        } catch (Exception e){
        }
    }

    
    public static void main(String[] args){
        SelectGridDialog dlg = new SelectGridDialog(null);
        dlg.setTableName("subject");
        dlg.setVisible(true);
    }
    
    
}
