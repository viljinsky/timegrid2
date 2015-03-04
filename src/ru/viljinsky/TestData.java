/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import ru.viljinsky.dialogs.BaseDialog;
import ru.viljinsky.dialogs.DatasetEntryDialog;

/**
 *
 * @author вадик
 */

//class DatasetCover {
//    Dataset dataset;
//    public DatasetCover(Dataset dataset){
//        this.dataset=dataset;
//    }
//
//    public void first() {
//        dataset.first();
//    }
//
//    public void next() {
//        dataset.next();
//    }
//
//    public void prior() {
//        dataset.prior();
//    }
//
//    public void last() {
//        dataset.last();
//    }
//
//    public boolean eof() {
//        return dataset.eof();
//    }
//
//    public boolean bof() {
//        return dataset.bof();
//    }
//
//    int getColumnCount() {
//        return dataset.getColumnCount();
//    }
//
//    public Integer getRowCount() {
//        return dataset.getRowCount();
//    }
//
//    public String getColumnName(Integer columnIndex) {
//        return dataset.getColumnName(columnIndex);
//    }
//
//    public Object[] getRowset(Integer rowIndex) {
//        return dataset.getRowset(rowIndex);
//    }
//    
//}

class GridModel extends AbstractTableModel{
    Dataset dataset;
    
    public GridModel(Dataset dataset){
        this.dataset=dataset;
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
        Object[] recordset = dataset.getRowset(rowIndex);
        return recordset[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         Object[] rowset = dataset.get(rowIndex);
         rowset[columnIndex]=aValue;
    }

    @Override
    public String getColumnName(int column) {
        return dataset.getColumnName(column);
    }
    
}

class Grid extends JTable{
    DataModule dataModule = DataModule.getInsatnce();
    Dataset dataset;
    public Grid(String tableName){
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataset = dataModule.getTable(tableName);
        GridModel model = new GridModel(dataset);
        setModel(model);
        
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    System.out.println(dataset.getTableName()+Grid.this.getSelectedRow());
                    dataset.index=Grid.this.getSelectedRow();
                }
            }
        });
    }
}

