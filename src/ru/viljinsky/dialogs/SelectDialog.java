/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author вадик
 */
public class SelectDialog extends BaseDialog {

    public SelectDialog(JComponent owner) {
        super(owner);
        setSize(new Dimension(500,400));
    }

    @Override
    public void initComponents(Container content) {
    String[] cmd ={"inc","incall","excl","excall"};
        
    JList<String> list1 = new JList(new String[]{"one","two","three"});
    JList<String> list2 = new JList<>(new String[]{"four","five"});
    JPanel buttons =new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
    buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
    for (String s:cmd){
        JButton button= new JButton(s);
        button.setMaximumSize(new Dimension(80,25));
        button.setMinimumSize(new Dimension(80,25));
        Box box = Box.createVerticalBox();
        box.add(button);
        buttons.add(box);
    }
        
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        Box box = Box.createHorizontalBox();
        box.add(new JScrollPane(list1));
        content.add(box);
        
        box = Box.createHorizontalBox();
        box.add(buttons);
        content.add(box);
        
        box= Box.createHorizontalBox();
        box.add(new JScrollPane(list2));
        content.add(box);
        
        ((JPanel)content).setBorder(new EmptyBorder(10,10,10,10));
        
    }
    
    

    public static void main(String[] args){
        SelectDialog dlg = new SelectDialog(null);
//        dlg.setDefaultCloseOperation(EXIT_ON_CLOSE);
        dlg.setVisible(true);
        System.out.println("OK");
        dlg.dispose();
        dlg=null;
    }
    
}
