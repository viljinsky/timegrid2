/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ru.viljinsky.dialogs.DatasetEntryDialog;

/**
 *
 * @author вадик
 */

interface IDefaultGrid{
    public void append();
    public void delete();
    public void edit();
    public void insert();
}


class DefaultGrid extends JTable implements IDefaultGrid {
    JComponent rootPane = null;
    GridModel model = null;
    Act[] actions = {new Act("append"), new Act("edit"), new Act("delete"), new Act("insert")};

    class Act extends AbstractAction {

        public Act(String command) {
            super(command);
            putValue(Action.ACTION_COMMAND_KEY, command);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }

    public DefaultGrid() {
        
        super(10, 10);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    int row = getSelectedRow();
                    if (model!=null){
                        model.dataset.setIndex(row);
                    }
                }
            }
        });
        
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_INSERT:
                        append();
                        break;
                    case KeyEvent.VK_DELETE:
                        delete();
                        break;
                    case KeyEvent.VK_CONTEXT_MENU:
                        context();
                        break;
                    case KeyEvent.VK_ENTER:
                        edit();
                        break;
                }
            }
            
        });
        
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                edit();}
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mousePressed(e);
                showPupup(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                showPupup(e);
            }

            public void showPupup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popup = new JPopupMenu();
                    createPopup(popup);
                    int x = e.getX();
                    int y = e.getY();
                    popup.show(DefaultGrid.this, x, y);
                }
            }
        });
    }

    
    public void createPopup(JPopupMenu popup){
        for (Action a : actions) {
            popup.add(a);
        }
    }
    
    protected void doCommand(String command) {
        try {
            switch (command) {
                case "append":
                    append();
                    break;
                case "insert":
                    insert();
                    break;
                case "delete":
                    delete();
                    break;
                case "edit":
                    edit();
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void context(){
        JPopupMenu popup = new JPopupMenu();
        for (Action a:actions){
            popup.add(a);
        }
        popup.show(this, 0,0);
        
    }
    
    class AppendDiaog extends DatasetEntryDialog{

        public AppendDiaog(JComponent owner) {
            super(owner);
        }

        @Override
        public void doEnter() throws Exception {
            int row = model.dataset.append(getValues());
            model.fireTableDataChanged();
            model.dataset.setIndex(row);
        }
        
    }
    
    @Override
    public void append() {
        AppendDiaog dlg = new AppendDiaog(rootPane);
        dlg.setDataset(model.dataset);
        dlg.setVisible(true);
        if (dlg.getModalResult() == DatasetEntryDialog.RESULT_OK) {
            int row = model.dataset.getIndex();
            getSelectionModel().setSelectionInterval(row, row);
        }
    }

    @Override
    public void delete() {
        if (JOptionPane.showConfirmDialog(rootPane, "Удалить запись","Внимание",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try{
                model.dataset.delete();
                model.fireTableDataChanged();
            } catch (Exception e){
                JOptionPane.showMessageDialog(rootPane, e.getMessage());
            }
        }
    }

    class EditDialog extends DatasetEntryDialog{

        public EditDialog(JComponent owner) {
            super(owner);
        }

        @Override
        public void doEnter() throws Exception {
            int row = model.dataset.getIndex();
            model.dataset.edit(getValues());
            model.fireTableDataChanged();
            model.dataset.setIndex(row);
        }
    }
    
    @Override
    public void edit() {
        EditDialog dlg = new EditDialog(rootPane);
        dlg.setDataset(model.dataset);
        dlg.setValues(model.dataset.getValues());
        dlg.setVisible(true);
        if (dlg.getModalResult() == DatasetEntryDialog.RESULT_OK) {
            int row = model.dataset.getIndex();
            getSelectionModel().setSelectionInterval(row,row);
        }
    }

    class InsertDialog extends DatasetEntryDialog{

        public InsertDialog(JComponent owner) {
            super(owner);
        }

        @Override
        public void doEnter() throws Exception {
            int row = model.dataset.insert(getValues());
            model.fireTableDataChanged();
            model.dataset.setIndex(row);
        }
        
    }
    
    @Override
    public void insert() {
        InsertDialog dlg = new InsertDialog(rootPane);
        dlg.setDataset(model.dataset);
        dlg.setVisible(true);
        if (dlg.getModalResult() == DatasetEntryDialog.RESULT_OK) {
            int row = model.dataset.getIndex();
            getSelectionModel().setSelectionInterval(row, row);
        }
        
        
    }
    
}
