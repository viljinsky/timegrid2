/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

/**
 *
 * @author вадик
 */
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import ru.viljinsky.*;

public class Test1 extends JFrame{
    Set<CellElement> dragObjects = null;
    public Test1(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        CellElement ce = new CellElement();
        dragObjects = new HashSet<>();
        dragObjects.add(new CellElement());
        dragObjects.add(new CellElement());
        dragObjects.add(new CellElement());
        dragObjects.add(new CellElement());
        dragObjects.add(ce);
        
        System.out.println(dragObjects.size());
        System.out.println(dragObjects.contains(ce));
        
        for (CellElement d:dragObjects){
            System.out.println(d);
        }
    }
    public static void main(String[] args){
        Test1 frame = new Test1();
        frame.setContentPane(new TimeGrid());
        frame.pack();
        frame.setVisible(true);
        
    }
    
}
