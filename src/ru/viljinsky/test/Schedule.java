/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import ru.viljinsky.dbcontrols.DBLookup;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.print.attribute.HashAttributeSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ru.viljinsky.timegrid.Chip;
import ru.viljinsky.xmldb.DataModule;
import ru.viljinsky.xmldb.Dataset;
import ru.viljinsky.timegrid.TimeGrid;

/**
 *
 * @author вадик
 */

class ScheduleChip extends Chip{
    
    public ScheduleChip(Dataset dataset){
        try{
            setCell(dataset.getInteger("day_id"), dataset.getInteger("lesson_id"));
            setTeacher_id(dataset.getInteger("teacher_id"));
            setRoom_id(dataset.getInteger("room_id"));
            setGroup_id(dataset.getInteger("group_id"));

            setTeacher_name(dataset.getLookupValue("teacher_id").toString());
            setSubject_name(dataset.getLookupValue("subject_id").toString());
            setRoom_no(dataset.getLookupValue("room_id").toString());
            setGroup_title(dataset.getLookupValue("group_id").toString());
        } catch (Exception e){
            System.out.println("!!!!! Schedule chip error :"+e.getMessage());
        }
    }
}


public class Schedule extends JFrame{
    Grid timeGrid = new Grid();
    Dataset dataset = null;
    FilterPanel filterPanel = new FilterPanel();
    
    class FilterPanel extends JPanel{
        
        String[][] tables = {
            {"teacher","id","last_name","first_name","patronymic"},
            {"room","id","number"},
            {"depart","id","skill_id","label"}
        };
        
        DBLookup[] filters ;
        Dataset lookUpDataset ;
        public FilterPanel(){
            super(new FlowLayout(FlowLayout.LEFT));
            
            
            DataModule dm= DataModule.getInsatnce();
            try{

                filters = new DBLookup[tables.length];
                for (int i=0;i<filters.length;i++){
                    String[] ss = tables[i];
                    lookUpDataset = dm.getTable(ss[0]);
                    String[] ss2 = new String[ss.length-2];
                    for (int j=0;j<ss.length-2;j++){
                        ss2[j]=ss[j+2];
                    }
                    filters[i]= new DBLookup(lookUpDataset,ss[1],ss2);
                    filters[i].addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DBLookup lu = (DBLookup)e.getSource();
                            onFilterChanged(lu);
                        }
                    });
                    add(new JLabel(lookUpDataset.getTableName()));
                    add(filters[i]);
                }
                
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
        public void onFilterChanged(DBLookup lookup){
                System.out.println("Filter-->"+lookup.getDataset().getTableName()+" " +lookup.getValue());
        }
    }
    
    class Grid extends TimeGrid{

        @Override
        public void load() {
            clear();
            ScheduleChip chip;
           
            if (dataset!=null){
                try{
                dataset.first();
                while (!dataset.eof()){
                    chip = new ScheduleChip(dataset);
                    
                    addElement(chip);
                    dataset.next();
                }
                } catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, e.getMessage());
                }
            }
            
        }
        
    }

    static void showSchedule(Component owner,Dataset dataset) {
        Schedule f = new Schedule();
        f.dataset=dataset;
        f.pack();
        f.setVisible(true);
        f.timeGrid.load();
    }
    
    public Schedule(){
        super("Расписание");
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(timeGrid);
        content.add(filterPanel,BorderLayout.PAGE_START);
    }
    
    public static void main(String[] args){
        Schedule frame = new Schedule();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
