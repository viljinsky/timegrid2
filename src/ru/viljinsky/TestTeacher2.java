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

interface IMasterDetail {
    public void onRecordsetOpen(Dataset dataset);
    public void onRecordChange(Dataset dataset);
}

class GridPanel extends JPanel implements IMasterDetail{
    DataModule dataModule = DataModule.getInsatnce();
    String tableName;
    Grid grid;
    
    Map<String,Integer> refMap;
    Dataset refDataset;
    
    List<IMasterDetail> listeners = new ArrayList<>();
    
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
       for (IMasterDetail listener:listeners){
           listener.onRecordsetOpen(grid.dataset);
       }
       
   } 
   
   public void recordChange(){
       grid.dataset.setIndex(grid.getSelectedRow());
       for (IMasterDetail ig:listeners){
           ig.onRecordChange(grid.dataset);
       }
   } 
   public void addGridPanelListener(IMasterDetail listener){
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
    public void onRecordsetOpen(Dataset dataset) {
        String references;
        
        try{
            for (Dataset ds:dataset.getRefTables()){
                if (ds.tableName.equals(tableName)){
                    references = ds.getReferences(dataset.tableName);
                    refDataset = ds;
                    refMap = new HashMap<>();
                    refMap.put(references.split("=")[1], ds.getColumnIndex(references.split("=")[0]));
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void onRecordChange(Dataset dataset) {
        Map<Integer,Object> m2 = new HashMap<>();

        try{
        
            for (String s:refMap.keySet()){
                m2.put(refMap.get(s),dataset.getValue(s));
            }
            refDataset.open(m2);
            grid.setDataset(refDataset);

        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
}

public class TestTeacher2  extends JFrame{
    DataModule dataModule=DataModule.getInsatnce();
    GridPanel panelMaster;
    GridPanel slave1;
    GridPanel slave2;
    
            
    public TestTeacher2(){
        super("TestTeacher2");
        
        getContentPane().setPreferredSize(new Dimension(800,600));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panelMaster = new GridPanel("teacher");
        slave1 = new GridPanel("schedule");
        slave1.setMaster(panelMaster);
        
        slave2 = new GridPanel("work_plan");
        slave2.setMaster(panelMaster);
        
        JSplitPane p1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        p1.setLeftComponent(slave1);
        p1.setRightComponent(slave2);
        p1.setResizeWeight(0.5);
        
        splitPane.setTopComponent(panelMaster);
        splitPane.setBottomComponent(p1);//(new JScrollPane(slave1));
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
