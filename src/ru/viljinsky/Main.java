/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 *
 * @author вадик
 */
public class Main {
    public static void main(String[] args){
        DataModule dataModule = DataModule.getInsatnce();
        dataModule.open();
        
        
        
        TimeGrid timeGrid = new TimeGrid();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(timeGrid.getMenu());
        
        JFrame frame = new JFrame("TimeGrid2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(timeGrid);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
        timeGrid.load();
    }    
}
