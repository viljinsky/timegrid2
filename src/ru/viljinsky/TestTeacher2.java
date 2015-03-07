/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author вадик
 */

interface IGridPanel {
    public void onRecordChange(Dataset dataset);
}

class GridPanel extends JPanel implements IGridPanel{
    DataModule dataModule = DataModule.getInsatnce();
    String tableName;
    Grid grid;
    List<IGridPanel> listeners = new ArrayList<>();
    
    public GridPanel(String tableName){
       super(new BorderLayout());
       this.tableName=tableName;
       grid =new Grid();
       add(new JScrollPane(grid));
       grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

           @Override
           public void valueChanged(ListSelectionEvent e) {
               if (!e.getValueIsAdjusting())
                   recordChange();
           }
       });
       
    }
    
   public void open() throws Exception{
       grid.setDataset(dataModule.getTable(tableName));
   } 
   
   public void recordChange(){
       grid.dataset.setIndex(grid.getSelectedRow());
       for (IGridPanel ig:listeners){
           ig.onRecordChange(grid.dataset);
       }
   } 
   public void addGridPanelListener(IGridPanel listener){
       listeners.add(listener);
   } 
    
    public void setDataset(Dataset dataset){
        grid.setDataset(dataset);
    }
    
    public Dataset getDataset(){
        return grid.dataset;
    }

    public String getTableName(){
        return grid.dataset.getTableName();
    }
    
    public void setMaster(GridPanel master){
        master.addGridPanelListener(this);
    }
    
    @Override
    public void onRecordChange(Dataset dataset) {
        System.out.println("filter "+dataset.getIndex());
        try{
        Map<String,Integer> m1 = new HashMap<>();
        Map<Integer,Object> m2 = new HashMap<>();
        String references;
        
        Dataset[] refDataset = dataset.getRefTables();
        for (Dataset ds:refDataset){
            if (ds.tableName.equals(tableName)){
//                    System.out.println("---->"+ds.tableName);
            
                    references = ds.getReferences(dataset.tableName);
//                    System.out.println(references);
                    m1.put(references.split("=")[1], ds.getColumnIndex(references.split("=")[0]));
//                    System.out.println(m1);

                    for (String s:m1.keySet()){
                        m2.put(m1.get(s),dataset.getValue(s));
                    }

//                    System.out.println(m2);
                    ds.open(m2);
                    grid.setDataset(ds);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
}

public class TestTeacher2  extends JFrame{
    DataModule dataModule=DataModule.getInsatnce();
    GridPanel panelMaster;
    GridPanel panelSlave;
    
            
    public TestTeacher2(){
        super("TestTeacher2");
        
        getContentPane().setPreferredSize(new Dimension(800,600));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panelMaster = new GridPanel("teacher");
        panelSlave = new GridPanel("schedule");
        panelSlave.setMaster(panelMaster);
        
        splitPane.setTopComponent(new JScrollPane(panelMaster));
        splitPane.setBottomComponent(new JScrollPane(panelSlave));
        getContentPane().add(splitPane);
        splitPane.setResizeWeight(.5);
        
    }
    
    public void open() {
        try{
            dataModule.open();
            panelMaster.open();
        
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }
    
    public static void main(String[] args){
        TestTeacher2 frame  = new TestTeacher2();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.open();
    }
    
}
