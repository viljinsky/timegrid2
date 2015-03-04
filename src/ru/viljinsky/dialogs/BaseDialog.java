/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author вадик
 */
public class BaseDialog extends JDialog {
    public static final int RESULT_NONE = 0;
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 2;
    public static final int RESULT_ABORT = 3;
    public static final int RESULT_RETRY = 4;
    public int modalResult = RESULT_NONE;
    
    public BaseDialog(){
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setModal(true);
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(300,400));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btn ;
        btn = new JButton(new AbstractAction("OK") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                modalResult=RESULT_OK;
                BaseDialog.this.setVisible(false);
            }
        });
        buttonPanel.add(btn);
        
        btn = new JButton(new AbstractAction("Cancel") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                modalResult=RESULT_CANCEL;
                BaseDialog.this.setVisible(false);
            }
        });
        
        buttonPanel.add(btn);
        
        content.add(buttonPanel,BorderLayout.PAGE_END);
        JPanel panel = new JPanel();
        initComponents(panel);
        content.add(panel,BorderLayout.CENTER);
        
        
    }
    
    public void initComponents(Container content){
    }
    
}
