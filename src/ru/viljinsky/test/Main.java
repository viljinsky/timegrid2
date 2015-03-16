/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import ru.viljinsky.dbcontrols.GridPanel;


import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */


//----------------------- G R I D   M O D E L ----------------------------------

//------------------------  G R I D - ------------------------------------------



//--------------------- G R I D   P A N E L ------------------------------------
//----------------------------   T E S T 1 -------------------------------------
public class Main extends JFrame{
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    File file = new File(".");


    public Main(){
        super("Main");
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(800, 600));
        content.add(tabbedPane);
        int x,y;
        Dimension d = getToolkit().getScreenSize();
        pack();
        setLocation((d.width-getWidth())/2,(d.height-getHeight())/2);
    }
    
    public void open(String fileName) {
        
        GridPanel panel;
        String tableName;
        if (dataModule.isActive()){
            dataModule.close();
        }
        tabbedPane.removeAll();
        if (fileName==null)
            dataModule.open();
        else
            dataModule.open(fileName);
        
        for (Dataset dataset:dataModule.getTables()){
            if (dataset.hasReferences()){
                tableName = dataset.getTableName();
                panel = new GridPanel();
                panel.setDataset(dataset);
                tabbedPane.addTab(tableName, panel);
            }
        }
    }
    
    class Act extends AbstractAction{

        public Act(String name) {
            super(name);
            putValue(Action.ACTION_COMMAND_KEY, name);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    
    protected void doCommand(String command){
        JFileChooser fc;
        int retVal;
        try{
            switch(command){
                
                case "open":
                     fc = new JFileChooser(file);
                     retVal = fc.showOpenDialog(rootPane);
                     if (retVal==JFileChooser.APPROVE_OPTION){
                         open(fc.getSelectedFile().getPath());
                         file = fc.getSelectedFile();
                     }
                    break;
                case "save":
                    fc = new JFileChooser(file);
                    fc.setSelectedFile(new File("test.xml"));
                    retVal = fc.showSaveDialog(fc);
                    if (retVal==JFileChooser.APPROVE_OPTION){
                        dataModule.save(fc.getSelectedFile().getPath());
                        file = fc.getSelectedFile();
                        JOptionPane.showMessageDialog(rootPane, "Файл "+file.getName()+" успешно сохранён");
                    }
                    break;
                case "saveAs":
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
            
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }
    
    public JMenu getFileMenu(){
        Action[] actions = {new Act("open"),new Act("save"),new Act("saveAs"),null,new Act("exit")};
        JMenu menu = new JMenu("File");
        for (Action a:actions){
            if (a==null)
                menu.addSeparator();
            else
                menu.add(a);
        }
        return menu;
    }
    
    public static void main(String[] args){
        final Main frame = new Main();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(frame.getFileMenu());
        frame.setJMenuBar(menuBar);
        
        JMenu menu = new JMenu("Dictionary");
        menu.add(new AbstractAction("Dict") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Dict.showDictionary(frame);
            }
        });
        
        menu.add(new AbstractAction("Schedue") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                Schedule.showSchedule(frame,frame.dataModule.getTable("schedule"));
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        
        menu.add(new AbstractAction("Model") {

            @Override
            public void actionPerformed(ActionEvent e) {
                ErrModel.showModel();
            }
        });
        menuBar.add(menu);
        
        frame.pack();
        frame.setVisible(true);
        frame.open(null);
        
    }

}
