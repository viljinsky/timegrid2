/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JMenu;

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
        return list;//.toArray(new CellElement[list.size()]);
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


abstract class AbstractTimeGrid extends Container{

    Set<DragObject> dragObjects = null;
    Cells cells;
    
    int colCount = 10;
    int rowCount = 7;
    Integer[] rowHeights;
    Integer[] colWidths;
    
    int selectedRow = -1;
    int selectedCol = -1;
    
    int shadowCol = -1;
    int shadowRow = -1;

    int startX,startY;
    boolean dragged=false;
    
    Cell overCell = null;
    
    private Integer getMaxRowCount(int row){
        Integer result = 1;
        Integer[] count = new Integer[colCount];
        for (int i=0;i<count.length;i++){
            count[i]=0;
        }
        
        for (CellElement ce:cells){
            if (ce.row==row){
                count[ce.col]+=1;
            }
        }
        for (int i=0;i<count.length;i++){
            if(count[i]>result){
                result=count[i];
            }
        }
        return result;
    }
    
    private void calcRowHeight(){
        rowHeights = new Integer[rowCount];
        for (int row=0;row<rowCount;row++){
            rowHeights[row]= getMaxRowCount(row)*CellElement.HEIGHT; 
        }
        for (int i=0;i<rowHeights.length;i++){
            System.out.println(i+"-->"+rowHeights[i]);
        }
    }
    
    public void setOverCell(Cell cell){

        if (!cell.equals(overCell)){
            overCell=cell;
            System.out.println(overCell);
        }
       
    }
    
    public Cell getCellOver(int x,int y){
        
        for (int col=0;col<colCount;col++){
            for (int row=0;row<rowCount;row++){
                if (getBound(col, row).contains(x,y)){
                    return new Cell(col,row);
                }
            }
        }
        return null;
    }
    
    public Cell getSelectedCell(){
        return new Cell(selectedCol,selectedRow);
    }
    
    public AbstractTimeGrid() {
        setPreferredSize(new Dimension(800,600));
        cells = new Cells();
        calcRowHeight();
        
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                
                Cell cell = getCellOver(e.getX(),e.getY());
                if (cell==null){
                    selectedCol = -1;
                    selectedRow = -1;
                } else {
                    
                    if (dragged){
                        stopDrag(cell.col,cell.row);
                    }
                    selectedCol=cell.col;
                    selectedRow= cell.row;
                }
                

               if (dragged){
                    dragged = false;
                    dragObjects = null;
               } 
                
               calcRowHeight();
               repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY=e.getY();
                Cell cell = getCellOver(startX,startY);
                if (cell!=null){
                    selectedCol = cell.col;
                    selectedRow = cell.row;
                    cellClick(cell.col, cell.row);
                    
                }

                if (!e.isControlDown())
                    for (CellElement ce:cells){
                        ce.selected=false;
                    }
                
                for (CellElement ce:cells){
                    if (ce.hitTest(startX,startY)){
                        ce.selected=!ce.selected;
                        cellElementClick(ce);
                    }
                }
                
                repaint();
            }
            
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Cell cell = getCellOver(e.getX(),e.getY());
                if (cell!=null)
                    setOverCell(cell);
                repaint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragged){
                    startDrag(selectedCol,selectedRow);
                    
//                    List<DragObject> lst = new ArrayList<>();
//                    
//                    for (CellElement ce:cells){
//                        if (ce.selected){
//                            lst.add(new DragObject(ce));
//                        }
//                    }
//                    dragObjects = lst.toArray(new DragObject[lst.size()]);
                    dragObjects = new HashSet<>();
                    for (CellElement ce:cells){
                        if (ce.selected){
                            dragObjects.add(new DragObject(ce));
                        }
                    }
                    dragged = true;
                }
                
                int x = e.getX();
                int y = e.getY();
                drag(x-startX, y-startY);
                
                for (DragObject ce:dragObjects){
                    ce.bound.x+=x-startX;
                    ce.bound.y+=y-startY;
                }
                
                startX = x;
                startY = y;
                
                repaint();
                
            }
        });
    }

    protected Rectangle getBound(int col,int row){
        Rectangle result = new Rectangle();
        result.x=col*CellElement.WIDTH;
        result.width=CellElement.WIDTH;

        result.y= 0;
        for (int i=0;i<row;i++){
            result.y += rowHeights[i];
        }
        result.height=rowHeights[row];
        
        
        return result;
    }
    
    private boolean CellElementIsDragged(CellElement ce){
        if (dragObjects==null)
            return false;
        for (DragObject dro:dragObjects){
            if (dro.element==ce)
                return true;
        }
        return false;
    }
    public void drawCell(Graphics g,int col,int row){
        Rectangle r;
        Color color = Color.pink;
        r=getBound(col, row);
        g.setColor(Color.red);
        g.drawRect(r.x, r.y, r.width,r.height);
        
        if (col == selectedCol && row == selectedRow){
            color =Color.white;
        }
        
        if (overCell!=null)
            if (col == overCell.col && row==overCell.row){
                color=Color.yellow;
            }
        
        g.setColor(color);
        g.fillRect(r.x+1, r.y+1, r.width-2, r.height-2);
        //----------------------------------
        if (cells.isExists(col,row)){
            Set<CellElement> list = cells.getCells(col, row);
            Rectangle r1 = new Rectangle(r);
            r1.height = r.height / list.size();
            
            for (CellElement ce:list){
                if (!CellElementIsDragged(ce)){
                    ce.bound=new Rectangle(r1.x+1,r1.y+1,r1.width-2,r1.height-2);
                    ce.draw(g,ce.bound);
                    r1.y+=r1.height;
                }
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        
        for (int col=0;col<colCount;col++)
            for (int row=0;row<rowCount;row++){
                drawCell(g, col, row);
                
            }
        
        if (dragObjects!=null){
            for (DragObject dro:dragObjects){
                dro.draw(g);
        }
        }
    }
    
    public abstract void cellClick(int col,int row);
    
    public abstract void cellElementClick(CellElement ce);
    
    public abstract void startDrag(int col,int row);
    
    public abstract void stopDrag(int col,int row);
    
    public abstract void drag(int dx ,int dy);
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
    
//    class Chip extends CellElement{
//        int subject_id;
//        int teacher_id;
//        int room_id;
//        int group_id;
//        DataModule dm = DataModule.getInsatnce();
//        
//        public Chip(int subject_id,int teacher_id,int room_id,int group_id){
//            this.subject_id=subject_id;
//            this.teacher_id=teacher_id;
//            this.room_id=room_id;
//            this.group_id=group_id;
//        }
//
//        @Override
//        public void draw(Graphics g,Rectangle b) {
//            super.draw(g,b);
//            String subject_name = dm.getSubjectName(subject_id);
//            String teacher_name = dm.getTeacherName(teacher_id);
//            String room_no = dm.getRoomNo(room_id);
//            String group_title = dm.getGroupTitle(group_id);
//            
//
//            Image image = new BufferedImage(b.width-1, b.height-1, BufferedImage.TYPE_INT_RGB);
//            Graphics g1 = image.getGraphics();
//            g1.setColor(dm.getSubjectColor(subject_id));
//            g1.fillRect(0, 0, b.width, b.height);
//            
//            int h = g1.getFontMetrics().getHeight();
//            g1.setColor(Color.black);
//            g1.drawString(subject_name, 0,1*h);
//            
//            g1.drawString(teacher_name,0,2*h);
//            
//            g1.drawString(room_no,0,3*h);
//            
//            g1.drawString(group_title,0,4*h);
//            
//            g.drawImage(image, b.x+1, b.y+1, null);
//            
//            
//        }
//        
//    }
    
    public void load(){
        Chip chip ;
        cells.clear();
        
//        for (int i=0;i<colCount;i++){
//            for (int j=0;j<rowCount;j++){
//                 chip = new Chip(1,1, 1, 1);
//                 chip.col=i;chip.row=j;
//                 cells.add(chip);
//            }
//        }
        
        
//        DataModule dm = DataModule.getInsatnce();
//        if (!dm.isActive()){
//            dm.open();
//        }
//        
//        Dataset ds = dm.getTable("work_plan");
//        ds.first();
//        while (!ds.eof()){
//            chip = new Chip(ds.getInteger("subject_id"),ds.getInteger("teacher_id"),
//                    ds.getInteger("room_id"),ds.getInteger("group_id"));
//            chip.setCell(ds.getInteger("day_id"), ds.getInteger("lesson_id"));
//            cells.add(chip);
//            ds.next();
//        }
        
        
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
    
    public JMenu getMenu() {
        JMenu menu = new JMenu("TimeGrid");
        
        menu.add(new TimeGridAction("add"));
        menu.add(new TimeGridAction("delete"));
        menu.add(new TimeGridAction("save"));
        menu.add(new TimeGridAction("load"));
        menu.add(new TimeGridAction("clear"));
        
        return menu;
    }
    
}
