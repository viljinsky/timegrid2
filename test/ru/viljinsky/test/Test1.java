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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import ru.viljinsky.*;

class WorkPlanItem extends CellElement{
    String subject_name;
    BufferedImage image;
    public WorkPlanItem(int day,int lesson,String subject_name){
        this.subject_name=subject_name;
        setCell(day, lesson);
        image =   new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.red);
        g.fillRect(1, 1, WIDTH, HEIGHT);
        g.setColor(Color.blue);
        g.drawString(subject_name, 1, 10 );
    }
    

    @Override
    public void draw(Graphics g, Rectangle b) {
        super.draw(g, b); 
        g.drawImage(image, b.x, b.y, b.width, b.height, null);
    }
    
    
}

public class Test1 extends JFrame{
    TimeGrid grid = new TimeGrid();
    
    public Test1(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(grid);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(grid.getMenu());
        setJMenuBar(menuBar);
        
        WorkPlanItem item ;
        
        item = new WorkPlanItem(1,1,"Русский яз.");
        grid.addElement(item);
    }
    public static void main(String[] args){
        Test1 frame = new Test1();
        frame.pack();
        frame.setVisible(true);
        
    }
    
}
