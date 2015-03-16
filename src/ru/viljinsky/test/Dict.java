/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import ru.viljinsky.dbcontrols.Grid;
import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;

/**
 *
 * @author вадик
 */
public class Dict  extends JFrame{

    static void showDictionary(Component owner) {
        Dict frame = new Dict();
        frame.open();
        frame.pack();
        
        Dimension d = owner.getSize();
        Point p = owner.getLocation();
        
        int x,y;
        x = p.x +(d.width-frame.getWidth())/2;
        y= p.y +(d.height-frame.getHeight())/2;
        frame.setLocation(x, y);
        
        frame.setVisible(true);
    }
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    public Dict(){
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(tabbedPane);
    }
    
    public void open(){
        tabbedPane.removeAll();
        for (Dataset dataset:dataModule.getTables()){
            if (!dataset.hasReferences()){
                Grid grid = new Grid();
                grid.setDataset(dataset);
                tabbedPane.addTab(dataset.getTableName(), new JScrollPane(grid));
            }
        }
        
    }
    
    public static void main(String[] args){
        DataModule.getInsatnce().open();
        Dict frame = new Dict();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,600));
        frame.pack();
        frame.setVisible(true);
        frame.open();
    }
    
}
