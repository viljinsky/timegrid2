/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author вадик
 */
public class TestDialog extends JFrame implements ActionListener{
    String[] dialogs = {"SelectDialog1",
                        "SelectDialog2",
                        "SelectDialog3",
                        "SelectDialog4",
                        "SelectDialog5"};
    public TestDialog(){
        super("TestDialog");
//        Container content = getContentPane();
//        content.setPreferredSize(new Dimension(400, 500));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        for (String s:dialogs){
            Box box = Box.createVerticalBox();
            JButton button = new JButton(s);
            button.addActionListener(this);
            box.add(button);
            content.add(box);
        }
        content.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(content);
        pack();
    }
    public static void main(String[] args){
        TestDialog frame = new TestDialog();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()){
            case "SelectDialog1":
                SelectDialog dlg = new SelectDialog(null);
                dlg.setVisible(true);
                break;
            case "SelectDialog2":
                SelectDialog2 dlg2 = new SelectDialog2(null);
                dlg2.setVisible(true);
                break;
            case "SelectDialog3":
                break;
            case "SelectDialog4":
                break;
            case "SelectDialog5":
                
                break;
        }
    }
    
}
