/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/**
 *
 * @author вадик
 */


//----------------------- G R I D   M O D E L ----------------------------------
class GridModel extends AbstractTableModel{
    Dataset dataset;
    Map<Integer,Map<Object,Object>> lookup = new HashMap<>();
    
    public GridModel(Dataset dataset) {
        this.dataset = dataset;
        try{
            for (String column:dataset.getColumns()){
                if (dataset.isLookup(column)){
                    lookup.put(dataset.getColumnIndex(column), dataset.getLookup(column));
                }
            }
        } catch (Exception e){
            System.err.println("Ошибка в конструкторе GridModel :\n"+e.getMessage());
        }
    }

    @Override
    public int getRowCount() {
        return dataset.getRowCount();
    }

    @Override
    public String getColumnName(int column) {
        return dataset.getColumnName(column);
    }

    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] rowset  = dataset.getRowset(rowIndex);
        if (lookup.containsKey(columnIndex)){
            Map lu = lookup.get(columnIndex);
            return lu.get(rowset[columnIndex]);
        }
        return rowset[columnIndex];
    }
}

//------------------------  G R I D - ------------------------------------------

class Grid extends DefaultGrid{
    Map<String,String> map = null;
    
    public void setDataset(Dataset dataset){
        model = new GridModel(dataset);
        setModel(model);
    }
    
    public Dataset getDataset(){
        if (model!=null)
            return model.dataset;
        return null;
    }
    
    public void open(){
        if (model!=null)
            try{
                model.dataset.open();
                model.fireTableDataChanged();
            } catch (Exception e){
                e.printStackTrace();
            }
        }   
    
    public void open(Map<String,Object> keys){
        if (map!=null)
            System.out.println("map->"+map);
        System.out.println("keys->"+keys);
        Map<Integer,Object> filter = new HashMap<>();
        if (model!=null) {
            try{
                Dataset dataset = model.dataset;
                for (String s:map.keySet()){
                    filter.put(dataset.getColumnIndex(s), keys.get(map.get(s)));
                }
                System.out.println("filter->"+filter);
                
                model.dataset.open(filter);
                model.fireTableDataChanged();
            } catch (Exception e){
                e.printStackTrace();
            }
        }   
        
    }
}


//--------------------- G R I D   P A N E L ------------------------------------
class GridPanel extends JPanel{
    JTabbedPane tabbedPane = new JTabbedPane();
    Grid grid = new Grid();
    Grid[] refGrids;
    
    public GridPanel(){
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(grid));
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane);
        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    onRecordChange();
                }
            }
        });
        
    }
    
    public void onRecordChange(){
        int row = grid.getSelectedRow();
        if (row>=0){
            Dataset dataset = grid.getDataset();
            dataset.setIndex(row);
            Map<String,Object> keys= new HashMap<>();
            try{
                for (String s:dataset.getPrimary()){
                    keys.put(s, dataset.getValue(s));
                }
                System.out.println(keys);
                for (Grid g:refGrids){
                    g.open(keys);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void setDataset(Dataset dataset){
        tabbedPane.removeAll();
        grid.setDataset(dataset);
        Dataset[] refDatasets = dataset.getRefTables();
        Dataset ds;
        refGrids = new Grid[refDatasets.length];
        for (int i =0 ; i<refGrids.length;i++){
            ds = refDatasets[i];
            refGrids[i]= new Grid();
            refGrids[i].setDataset(ds);
            tabbedPane.addTab(ds.getTableName(),new JScrollPane(refGrids[i]));
            try{
                String refs = ds.getReferences(dataset.getTableName());
                Map<String,String> map = new HashMap<>();
                map.put(refs.split("=")[0], refs.split("=")[1]);
                refGrids[i].map = map;
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
    }
    
}
//----------------------------   T E S T 1 -------------------------------------
public class Main extends JFrame{
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();

    public Main(){
        super("Main");
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(800, 600));
        content.add(tabbedPane);
    }
    
    public void open() {
        GridPanel panel;
        String tableName;
        dataModule.open();
        for (Dataset dataset:dataModule.getTables()){
            if (dataset.hasReferences()){
                tableName = dataset.getTableName();
                panel = new GridPanel();
                panel.setDataset(dataset);
                tabbedPane.addTab(tableName, panel);
            }
        }
    }
    
    public static void main(String[] args){
        Main frame = new Main();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.open();
        
    }

}
