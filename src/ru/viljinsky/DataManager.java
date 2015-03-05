/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.viljinsky.dialogs.BaseDialog;
import ru.viljinsky.dialogs.DatasetEntryDialog;

/**
 *
 * @author вадик
 */

public class DataManager extends JFrame{
    
    TabPanel tabPanel = new TabPanel();
    DataModule dataModule = DataModule.getInsatnce();
    File dataFile = new File(".");
    String dataPath = null;
    TabControl selected = null;
    
    
    public DataManager(){
        getContentPane().setPreferredSize(new Dimension(600,700));
        add(tabPanel);
        updateActionList();
    }
    
    class TabControl extends JPanel{
        
        Grid grid;
        Dataset dataset;
        
        public TabControl(Dataset dataset){
            setLayout(new BorderLayout());
            this.dataset=dataset;
            grid = new Grid(dataset);
            add(new JScrollPane(grid));
        }
        
        public void append(){
            Map<String,Object> values;
            DatasetEntryDialog dlg = new DatasetEntryDialog(rootPane);
            dlg.setDataset(dataset);
            dlg.setVisible(true);
            if (dlg.getModalResult()==BaseDialog.RESULT_OK){
                try{
                    values = dlg.getValues();
                    dataset.append(values);
                    grid.updateUI();
                    JOptionPane.showMessageDialog(rootPane, "OK");
                } catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                }
            }
        }
        
        public void update(){
            Map<String,Object> values;
            DatasetEntryDialog dlg = new DatasetEntryDialog(rootPane);
            dlg.setDataset(dataset);
            dlg.setValues(dataset.getValue());
            dlg.setVisible(true);
            if (dlg.getModalResult()==BaseDialog.RESULT_OK){
                try{
                    values = dlg.getValues();
                    dataset.update(values);
                    grid.updateUI();
                    JOptionPane.showMessageDialog(rootPane, "OK");
                } catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                }
            }
        }
        
        public void delete(){
        }
        
        public void refresh(){
        }
        
    }
    
    class TabPanel extends JTabbedPane{
        public TabPanel(){
            addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    int selectedIndex = TabPanel.this.getSelectedIndex();
                    if (selectedIndex>=0)
                        selected = (TabControl)tabPanel.getComponent(selectedIndex);
                    else 
                        selected = null;
                }
            });
        }
    }

    public void opennew(){
        dataModule.open();
        setTitle("new schedule");
        afterOpen();
        dataPath="schedule.xml";
        dataFile=new File("schedule.xml");
    }
    
    public void open(){
        JFileChooser fc = new JFileChooser(dataFile);
        int retVal = fc.showOpenDialog(DataManager.this);
        if (retVal== JFileChooser.APPROVE_OPTION){
            tabPanel.removeAll();
            dataModule.close();
            dataFile = fc.getSelectedFile();
            dataModule.open(dataFile.getPath());
            dataPath=dataFile.getPath();
            setTitle(dataPath);
            afterOpen();
        }
    }
    
    protected void afterOpen(){
        tabPanel.removeAll();
        for (Dataset dataset:dataModule.tables){
//            tabPanel.addTab(dataset.tableName,new JScrollPane( new Grid(dataset)));
            tabPanel.add(dataset.tableName,new TabControl(dataset));
        }
    }
    
    public void save(){
        if (dataPath == null)
            saveAs();
        try{
            dataModule.save(dataPath);
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
            
        }
    }
    
    public void saveAs(){
        JFileChooser fc = new JFileChooser(dataFile);
        int retVal = fc.showSaveDialog(DataManager.this);
        if (retVal==JFileChooser.APPROVE_OPTION){
            dataFile = fc.getSelectedFile();
            try{
                dataModule.save(dataFile.getPath());
                dataPath=dataFile.getPath();
            } catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        }
    }
    
    public void append(){
        if (selected!=null)
            selected.append();
    }
    
    public void edit(){
        if (selected!=null)
            selected.update();
    }
    
    public void delete(){
    }
    
    public void refresh(){
    }
    
    public void exit(){
        System.exit(0);
    }
    

    
    public void updateActionList(){
        for (Action act:dataAction){
            if (act!=null)
                updateAction(act);
        }
        
        for (Action act:datasetAction){
            updateAction(act);
        }
    }
    
    public void updateAction(Action act){
//        boolean b = selectedDataset!=null;
//        
//        boolean b1 = (b?selectedDataset.index>=0:false);
        
        String command = (String)act.getValue(AbstractAction.ACTION_COMMAND_KEY);
        switch (command){
            
//            case "save":
//            case "saveAs":
//                act.setEnabled(dataPath!=null);
//                break;
                
            case "append":
//                act.setEnabled(b);
                break;    
            case "edit":
//                act.setEnabled(b1);
                break;    
            case "delete":
//                act.setEnabled(b1);
                break;    
            case "refresh":
//                act.setEnabled(b);
                break;    
        }
    }
    
    public void doCommand(String command){
        try{
        switch(command){
            
            case "fill_shift":
                TestDS.fillShift();
                break;
                
            case "fill_curriculumn":
                TestDS.fillCurriculum();
                break;
                
            case "new":
                opennew();
                break;
                
            case "open":
                open();
                break;
                
            case "close":
                dataModule.close();
                tabPanel.removeAll();
                break;
                
            case "save":
                save();
                break;
                
            case "saveAs":
                saveAs();
                break;
                
            case "exit":
                exit();
                break;
                
            case "append":
                append();
            break;
                
            case "edit":
                
                edit();
                break;
                
            case "delete":
                delete();
                break;
                
            case "refresh":
                refresh();
                break;
                
            default:
                    System.err.println(String.format("неизвестная комманда \"%s\"",command));
        }
            updateActionList();
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
            
        }
    }
    
    class Act extends AbstractAction{

        public Act(String name) {
            super(name);
            putValue(ACTION_COMMAND_KEY, name);
        }
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    Action[] dataAction = { new Act("new"),new Act("open"),new Act("save"),new Act("saveAs"),null,new Act("close"), new Act("exit")};
    Action[] datasetAction = {new Act("append"),new Act("edit"),new Act("delete"),new Act("refresh")};
    Action[] testAction = {new Act("fill_shift"),new Act("fill_curriculumn")};
    
    public JMenu getDataMenu(){
        JMenu menu = new JMenu("Data");
        for (Action act:dataAction){
            if (act==null)
                menu.addSeparator();
            else
                menu.add(act);
        }
        return menu;
    }
    
    public JMenu getDatasetMenu(){
        JMenu menu = new JMenu("Dataset");
        for (Action act:datasetAction){
            menu.add(act);
        }
        return menu;
    }
    
    public static void showTestData(){
        DataManager frame = new DataManager();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(frame.getDataMenu());
        menuBar.add(frame.getDatasetMenu());
        
        
        JMenu menuTest = new JMenu("Test");
        for (Action act:frame.testAction){
            menuTest.add(act);
        }
        menuBar.add(menuTest);
        
        
        frame.setJMenuBar(menuBar);
        
        frame.pack();
        frame.setVisible(true);
        frame.opennew();
        
    }
    
    
    public static void main(String[] args){
        showTestData();
    }
    
}
