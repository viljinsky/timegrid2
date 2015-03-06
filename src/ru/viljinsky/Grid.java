/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author вадик
 */


class GridModel extends AbstractTableModel{
    Dataset dataset;
    Map<String,Map<Object,Object>> lookups;
    
    public GridModel(Dataset dataset){
        this.dataset=dataset;
        lookups = new HashMap();
        
        for (String column:dataset.getColumns()){
            if (dataset.isLookup(column)){
                lookups.put(column,dataset.getLookup(column));
            }
        }
    }
    
    @Override
    public int getRowCount() {
        return dataset.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         Object[] rowset = dataset.get(rowIndex);
         String columnName = dataset.getColumnName(columnIndex);
         if (lookups.containsKey(columnName)){
             Map lu = lookups.get(columnName);
             return lu.get(rowset[columnIndex]);
         }        
         return rowset[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object[] rowset = dataset.get(rowIndex);
         rowset[columnIndex]=aValue;
    }

    @Override
    public String getColumnName(int colIndex) {
        return dataset.getColumnName(colIndex);
    }
    
}

class Grid extends JTable{
    DataModule dataModule = DataModule.getInsatnce();
    Dataset dataset;
    public Grid(Dataset dataset){
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.dataset = dataset;
//        dataset = dataModule.getTable(tableName);
        GridModel model = new GridModel(dataset);
        setModel(model);
        
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    System.out.println(Grid.this.dataset.getTableName()+Grid.this.getSelectedRow());
                    Grid.this.dataset.index=Grid.this.getSelectedRow();
                }
            }
        });
    }
}

