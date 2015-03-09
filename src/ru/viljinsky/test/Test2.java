/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;

/**
 *
 * @author вадик
 */


public class Test2 extends JFrame{
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    
    public Test2(){
        super("Test2");
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(800,600));
        content.setLayout(new BorderLayout());
        content.add(tabbedPane);
        
    }
    
    public void open(){
        dataModule.open();
        tabbedPane.removeAll();
        for (Dataset dataset:dataModule.getTables()){
            if (dataset.hasReferences()){
                GridPanel panel = new GridPanel();
                panel.setDataset(dataset);
                tabbedPane.addTab(dataset.getTableName(), panel);
            }
        }
    }
    
    public static void main(String[] args){
        Test2 frame = new Test2();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.open();
    }
}
