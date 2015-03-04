/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author вадик
 */

class GridModel extends AbstractTableModel{
    Dataset dataset;
    
    public GridModel(Dataset dataset){
        this.dataset=dataset;
    }
    
    @Override
    public int getRowCount() {
        return dataset.size();
    }

    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] recordset = dataset.get(rowIndex);
        String columnName = dataset.columns.get(columnIndex);
        if (dataset.getLookup(columnName)!=null){
            System.out.println("lookup field "+columnName+" = '"+recordset[columnIndex] +"' references '" +dataset.getLookup(columnName)+"'");
        }
        return recordset[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return dataset.columns.get(column);
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
                }
            }
        });
    }
}

class DataPanel extends JTabbedPane{
    DataModule dataModule = DataModule.getInsatnce();
    public DataPanel(){
        getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("!!!! ->"+DataPanel.this.getSelectedIndex());
            }
        });
        
        try{
            for (Dataset dataset:dataModule.tables){
                addTab(dataset.getTableName(),new JScrollPane(new Grid(dataset.getTableName())));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


public class TestData  extends JFrame {
    static DataModule dataModule = DataModule.getInsatnce();
    
    public TestData(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dataModule.open();
        
        
        
    }
    
    public static void main(String[] args){
        TestData frame = new TestData();
        frame.setContentPane(new DataPanel());
        frame.pack();
        frame.setVisible(true);
        
    }
    
}
