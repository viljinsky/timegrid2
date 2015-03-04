/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author вадик
 */

class Cell{
    int col;
    int row;
    public Cell(int col,int row){
        this.col=col;
        this.row=row;
    }
    public String toString(){
        return String.format("col %d row %d",col,row);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null)
            return false;
        if (obj==this)
            return true;
        if (obj instanceof Cell){
            Cell o =(Cell)obj;
            return o.col==col && o.row==row;
        }
        return  false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.col;
        hash = 71 * hash + this.row;
        return hash;
    }
}

class Cells extends ArrayList<CellElement>{
    public CellElement addElement(int col,int row){
        CellElement element = new CellElement();
        element.col=col;
        element.row=row;
        this.add(element);
        return element;
    }
    
    public int elementCount(int col,int row){
        Integer result = 0;
        for (CellElement ce:this){
            if (ce.col==col && ce.row==row){
                result+=1;
            }
        }
        return result;
    }

    boolean isExists(int col, int row) {
        for (CellElement ce :this)
            if (ce.col==col && ce.row==row)
                return true;
        return false;
    }

    Set<CellElement> getCells(int col,int row){
        Set<CellElement> list = new HashSet<>();
        for (CellElement ce:this){
            if (ce.col==col && ce.row==row)
                list.add(ce);
        }
        return list;
    }

    public Set<CellElement> getSelected(){
        Set<CellElement> set = new HashSet<>();
        for (CellElement ce:this){
            if (ce.selected){
                set.add(ce);
            }
        }
        return set;
    }
    
    
    void setSelected(CellElement[] list) {
        for (CellElement element:this){
            element.selected = false;
        }
        for (CellElement ce:list){
            ce.selected=true;
        }
    }

    
}

class DragObject{
    Rectangle bound;
    CellElement element;
    public DragObject(CellElement element){
        this.element=element;
        bound = new Rectangle(element.bound);
    }
    public void draw(Graphics g){
        element.draw(g, bound);
    }
}

public class TimeGrid extends AbstractTimeGrid{
    
    @Override
    public void cellClick(int col,int row){
        System.out.println(String.format("Cell click %d %d ", col,row));
    }
    
    @Override
    public void cellElementClick(CellElement ce){
        System.out.println(String.format("Element click %s", ce.toString()));
    }
    
    @Override
    public void startDrag(int col,int row){
        System.out.println("Start Drag");
    }
    
    @Override
    public void stopDrag(int col,int row){
        Cell cell = getSelectedCell();
        for (CellElement ce:cells.getSelected()){
            ce.col += col-cell.col;
            ce.row += row-cell.row;
        }
    }
    
    @Override
    public void drag(int dx ,int dy){
        System.out.println(String.format("%d %d",dx,dy));
    }

    
    protected void doCommand(String command){
        switch (command){
            case "add":
                add();
                break;
            case "delete":
                delete();
                break;
            case "load":
                load();
                break;
            case "save":
                save();
                break;
            case "clear":
                clear();
                break;
                
        }
        System.out.println("command : '"+command+"'");
        repaint();
    }
    
    public void add(){
        CellElement ce = new CellElement();
        ce.col = selectedCol;
        ce.row = selectedRow;
        cells.add(ce);
        cells.setSelected(new CellElement[]{ce});
    }
    
    public void delete(){
        
    }
    
    public void clear(){
        cells.clear();
    }
    
    
    public void addElement(CellElement element){
        cells.add(element);
    }
    
    
    public void load(){
        Chip chip ;
        cells.clear();
        
        chip = new Chip(1,1,1,1);
        chip.setCell(1,1);
        cells.add(chip);
        
        chip = new Chip(2,2,1,1);
        chip.setCell(2,1);
        cells.add(chip);

        chip = new Chip(3,3,1,1);
        chip.setCell(1,4);
        cells.add(chip);

        chip = new Chip(4,1,1,1);
        chip.setCell(2,4);
        cells.add(chip);
        
    }
    
    public void save(){
    }
    
    class TimeGridAction extends AbstractAction{

        public TimeGridAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    public JMenu getGridMenu() {
        JMenu menu = new JMenu("Grid");
        
        menu.add(new TimeGridAction("save"));
        menu.add(new TimeGridAction("load"));
        menu.add(new TimeGridAction("clear"));
        
        return menu;
    }
    
    public JMenu getCellMenu(){
        JMenu menu = new JMenu("Cells");
        menu.add(new TimeGridAction("add"));
        menu.add(new TimeGridAction("delete"));
        menu.add(new TimeGridAction("enabled"));
        menu.add(new TimeGridAction("selectAll"));
        menu.add(new TimeGridAction("deselectAll"));
        
        return menu;
    }
    
    public static void main(String[] args){
        TimeGrid timeGrid = new TimeGrid();
        
        JFrame frame = new JFrame("Test 'TimeGrid'");
        frame.setContentPane(timeGrid);
        timeGrid.addElement(new CellElement());
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(timeGrid.getGridMenu());
        menuBar.add(timeGrid.getCellMenu());
        frame.setJMenuBar(menuBar);
        
        frame.pack();
        frame.setVisible(true);
    }
    
}
