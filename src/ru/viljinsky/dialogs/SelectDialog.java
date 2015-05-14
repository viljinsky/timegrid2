/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author вадик
 */
public class SelectDialog extends BaseDialog {
    
    ListModel modelSrc;
    ListModel modelDest;
    JList<String>    listSrc;
    JList<String>    listDest;
    Action[] actions = new Action[]{};
    class ListModel extends DefaultListModel<String>{
    }

    public SelectDialog(JComponent owner) {
        super(owner);
        setSize(new Dimension(500,400));
    }

    @Override
    public void initComponents(Container content) {
        
    actions = new Action[]{new Act("include"),new Act("exclude"),new Act("includeAll"),new Act("excludeAll")};    
    listSrc = new JList();
    listSrc.setPreferredSize(new Dimension(120,200));
    listDest = new JList();
    listDest.setPreferredSize(new Dimension(120,200));
    JPanel buttons =new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
    buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
    for (Action a:actions){
        JButton button= new JButton(a);
        Box box = Box.createVerticalBox();
        box.add(button);
        buttons.add(box);
    }
        
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        Box box = Box.createHorizontalBox();
        box.add(new JScrollPane(listSrc));
        content.add(box);
        
        box = Box.createHorizontalBox();
        box.add(buttons);
        content.add(box);
        
        box= Box.createHorizontalBox();
        box.add(new JScrollPane(listDest));
        content.add(box);
        
        ((JPanel)content).setBorder(new EmptyBorder(10,10,10,10));
        
        modelSrc = new ListModel();
        modelSrc.addElement("one");
        modelSrc.addElement("two");
        modelSrc.addElement("three");
        modelSrc.addElement("fore");
        
        listSrc.setModel(modelSrc);
        
        modelDest = new ListModel();
        listDest.setModel(modelDest);
        
    }
    
    class Act extends AbstractAction{

        public Act(String name) {
            super(name);
            putValue(Action.ACTION_COMMAND_KEY, name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    public void updateAction(Action a){
        String command = (String)a.getValue(Action.ACTION_COMMAND_KEY);
        switch(command){
            case "include":
                a.setEnabled(listSrc.getSelectedIndices().length>0);
                break;
            case "exclude":
                a.setEnabled(listDest.getSelectedIndices().length>0);
                break;
            case "includeAll":
                break;
            case "excludeAll":
                break;
        }
    }
    
    public void doCommand(String command){
        switch (command){
            case "include":
                include();
                break;
            case "includeAll":
                includeAll();
                break;
            case "exclude":
                exclude();
                break;
            case "excludeAll":
                excludeAll();
                break;
        }
        for (Action a:actions){
            updateAction(a);
        }
    }
    
    public void include(){
        int[] indx = listSrc.getSelectedIndices();
        
        for (int i=0;i<indx.length;i++){
            String s= modelSrc.getElementAt(i);
            modelDest.addElement(s);
            modelSrc.removeElement(s);
        }
        
    }
    
    public void includeAll(){
        for (int i=0;i<modelSrc.size();i++){
            modelDest.addElement(modelSrc.getElementAt(i));
        }
        modelSrc.clear();
    }
    
    public void exclude(){
        String o;
        int[] indx = listDest.getSelectedIndices();
        for (int i=0;i<indx.length;i++){
            o=listDest.getSelectedValue();
            modelSrc.addElement(o);
            modelDest.removeElement(o);
        }
    }
    
    public void excludeAll(){
        for (int i=0;i<modelDest.size();i++)
            modelSrc.addElement(modelDest.getElementAt(i));
        modelDest.clear();
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
