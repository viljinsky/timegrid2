/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
        
        addMouseListener(new MouseAdapter() {
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
                    for (Action a : actions) {
                        popup.add(a);
                    }
                    int x = e.getX();
                    int y = e.getY();
                    popup.show(DefaultGrid.this, x, y);
                }
            }
        });
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

    @Override
    public void append() {
        DatasetEntryDialog dlg = new DatasetEntryDialog(this);
        dlg.setDataset(model.dataset);
        dlg.setVisible(true);
        if (dlg.getModalResult() == DatasetEntryDialog.RESULT_OK) {
            try{
                model.dataset.append(dlg.getValues());
                model.fireTableDataChanged();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "OK");
            }
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit() {
        DatasetEntryDialog dlg = new DatasetEntryDialog(this);
        dlg.setDataset(model.dataset);
        dlg.setValues(model.dataset.getValues());
        dlg.setVisible(true);
        if (dlg.getModalResult() == DatasetEntryDialog.RESULT_OK) {
            try{
                model.dataset.update(dlg.getValues());
                model.fireTableDataChanged();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    @Override
    public void insert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
