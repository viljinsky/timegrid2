/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Container;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author вадик
 */
public class SelectDialog2 extends BaseDialog{
    
    Model model ;
    
    class Model extends DefaultListModel<String>{
        
        
    }
    
    
    public SelectDialog2(JComponent owner){
        super(owner);
    }

    @Override
    public void initComponents(Container content) {
        content.setLayout(new BorderLayout());
        
        model = new Model();
        JList list = new JList(model);
        content.add(new JScrollPane(list));
                
    }
    
    public void setData(String[] data){
        for (String s:data)
            model.addElement(s);
       
    }
    
    public static void main(String[] args){
        SelectDialog2 dlg = new SelectDialog2(null);
        dlg.setData(new String[]{"one","two","three"});
        dlg.setVisible(true);
        System.out.println("OK");
        dlg.dispose();
        dlg=null;
    }
    
}
