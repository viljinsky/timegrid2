/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
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

class DatasetCover {
    Dataset dataset;
    public DatasetCover(Dataset dataset){
        this.dataset=dataset;
    }

    public void first() {
        dataset.first();
    }

    public void next() {
        dataset.next();
    }

    public void prior() {
        dataset.prior();
    }

    public void last() {
        dataset.last();
    }

    public boolean eof() {
        return dataset.eof();
    }

    public boolean bof() {
        return dataset.bof();
    }

    int getColumnCount() {
        return dataset.getColumnCount();
    }

    public Integer getRowCount() {
        return dataset.getRowCount();
    }

    public String getColumnName(Integer columnIndex) {
        return dataset.getColumnName(columnIndex);
    }

    public Object[] getRowset(Integer rowIndex) {
        return dataset.getRowset(rowIndex);
    }
    
}

class GridModel extends AbstractTableModel{
    DatasetCover dataset;
    
    public GridModel(Dataset dataset){
        this.dataset=new DatasetCover(dataset);
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
        String columnName = dataset.getColumnName(columnIndex);
        return recordset[columnIndex];
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

class DataPanel extends JTabbedPane{
    DataModule dataModule = DataModule.getInsatnce();
    public DataPanel(){
        getModel().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int n = DataPanel.this.getSelectedIndex();
                System.out.println("!!!! ->"+DataPanel.this.getSelectedIndex());
                Dataset dataset = dataModule.getTable(n);
                System.out.println(dataset.primary);
                System.out.println(dataset.lookup);
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
    DataPanel dataPanel ;
    
    class DataAction extends AbstractAction{

        public DataAction(String name) {
            super(name);
        }
        

        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
            
        }
    }
    
    public void doCommand(String command){
        System.out.println(command);
        int n = dataPanel.getSelectedIndex();
        Dataset ds = dataModule.getTable(n);
        
        
        switch (command){
            case "add":
                ds.addEmptyRecord();
                break;
            case "edit":
                
                
                
                DatasetEntryDialog dlg = new DatasetEntryDialog();
                dlg.setDataset(ds);
                dlg.pack();
                dlg.setVisible(true);
                if (dlg.modalResult==BaseDialog.RESULT_OK){
                    JOptionPane.showMessageDialog(rootPane, "OK");
                }
                break;
            case "delete":
                ds.delete();
                break;
        }
    }
    
    public TestData(){
        super("TestData");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dataModule.open();
        dataPanel = new DataPanel();
        setContentPane(dataPanel);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("data");
        menu.add(new DataAction("add"));
        menu.add(new DataAction("edit"));
        menu.add(new DataAction("delete"));
        
        menu.addSeparator();
        
        menu.add(new DataAction("open"));
        menu.add(new DataAction("save"));
        menuBar.add(menu);
        setJMenuBar(menuBar);
        
    }
    
    public static void showTestData(){
        TestData frame = new TestData();
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args){
        showTestData();
    }
    
}
