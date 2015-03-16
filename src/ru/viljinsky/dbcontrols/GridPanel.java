/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dbcontrols;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ru.viljinsky.xmldb.TestDS;
import ru.viljinsky.dialogs.SelectGridDialog;
import ru.viljinsky.xmldb.Dataset;

//--------------------- G R I D   P A N E L ------------------------------------

public class GridPanel extends JPanel {
    JTabbedPane tabbedPane = new JTabbedPane();
    Grid grid = new GridPanel.MasterGrid();
    Grid[] refGrids;

    class MasterGrid extends Grid {

        Action[] masterAction;

        class Act extends AbstractAction {

            public Act(String name) {
                super(name);
                putValue(Action.ACTION_COMMAND_KEY, name);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                doCommand(e.getActionCommand());
            }
        }

        @Override
        public void doCommand(String command) {
            try {
                switch (command) {
                    case "fillShift":
                        if (model.dataset.getTableName().equals("shift")) {
                            TestDS.fillShift(model.dataset.getValue("id"));
                            model.fireTableDataChanged();
                        }
                        break;
                    case "fill1":
                    case "fill2":
                    case "fill3":
                        System.out.println("-->" + command + " ok " + model.dataset.getTableName());
                        break;
                    case "FillSubject":
                        SelectGridDialog dlg = new SelectGridDialog(GridPanel.this);
                        dlg.setTableName("subject");
                        dlg.pack();
                        dlg.setVisible(true);
                        break;
                    default:
                        super.doCommand(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        @Override
        public void createPopup(JPopupMenu popup) {
            super.createPopup(popup);
            popup.addSeparator();
            masterAction = new Action[]{
                new GridPanel.MasterGrid.Act("fillShift"),
                new GridPanel.MasterGrid.Act("fill2"), 
                new GridPanel.MasterGrid.Act("fill3"),
                new GridPanel.MasterGrid.Act("FillSubject")};
            for (Action a : masterAction) {
                popup.add(a);
            }
        }
    }

    public GridPanel() {
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(grid));
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane);
        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    onRecordChange();
                }
            }
        });
    }

    public void onRecordChange() {
        int row = grid.getSelectedRow();
        if (row >= 0) {
            Dataset dataset = grid.getDataset();
            dataset.setIndex(row);
            Map<String, Object> keys = new HashMap<>();
            try {
                for (String s : dataset.getPrimary()) {
                    keys.put(s, dataset.getValue(s));
                }
                System.out.println(keys);
                for (Grid g : refGrids) {
                    g.open(keys);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDataset(Dataset dataset) {
        tabbedPane.removeAll();
        grid.setDataset(dataset);
        Dataset[] refDatasets = dataset.getRefTables();
        Dataset ds;
        refGrids = new Grid[refDatasets.length];
        for (int i = 0; i < refGrids.length; i++) {
            ds = refDatasets[i];
            refGrids[i] = new Grid();
            refGrids[i].setDataset(ds);
            tabbedPane.addTab(ds.getTableName(), new JScrollPane(refGrids[i]));
            try {
                String refs = ds.getReferences(dataset.getTableName());
                Map<String, String> map = new HashMap<>();
                map.put(refs.split("=")[0], refs.split("=")[1]);
                refGrids[i].map = map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
