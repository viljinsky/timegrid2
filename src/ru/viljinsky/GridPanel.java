/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author вадик
 * 
 */

interface IMasterDetail {
    public void onRecordsetOpen(Dataset dataset);
    public void onRecordChange(Dataset dataset);
}

class GridPanel extends JPanel implements IMasterDetail {
    DataModule dataModule = DataModule.getInsatnce();
    String tableName;
    Grid grid = new Grid();
    
    Map<String, Integer> refMap;
    Dataset refDataset;
    List<IMasterDetail> listeners = new ArrayList<>();

    public GridPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 300));
    }

    public GridPanel(String tableName) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(600, 300));
        
        
        this.tableName = tableName;
        add(new JScrollPane(grid));
        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    recordChange();
                }
            }
        });
    }

//    public void setTableName(String tableName) {
//        this.tableName = tableName;
//        add(new JScrollPane(grid));
//        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    recordChange();
//                }
//            }
//        });
//    }

    public void open() throws Exception {
        grid.setDataset(dataModule.getTable(tableName));
        for (IMasterDetail listener : listeners) {
            listener.onRecordsetOpen(grid.dataset);
        }
    }

    public void open(String tableName) throws Exception {
        this.tableName = tableName;
        add(new JScrollPane(grid));
        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    recordChange();
                }
            }
        });
        open();
    }

    public void recordChange() {
        grid.dataset.setIndex(grid.getSelectedRow());
        for (IMasterDetail ig : listeners) {
            ig.onRecordChange(grid.dataset);
        }
    }

    public void addGridPanelListener(IMasterDetail listener) {
        listeners.add(listener);
    }

    public void setDataset(Dataset dataset) {
        grid.setDataset(dataset);
    }

    public Dataset getDataset() {
        return grid.dataset;
    }

    public String getTableName() {
        return grid.dataset.getTableName();
    }

    public void setMaster(GridPanel master) {
        master.addGridPanelListener(this);
    }

    @Override
    public void onRecordsetOpen(Dataset dataset) {
        String references;
        try {
            for (Dataset ds : dataset.getRefTables()) {
                if (ds.tableName.equals(tableName)) {
                    references = ds.getReferences(dataset.tableName);
                    refDataset = ds;
                    refMap = new HashMap<>();
                    refMap.put(references.split("=")[1], ds.getColumnIndex(references.split("=")[0]));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordChange(Dataset dataset) {
        Map<Integer, Object> m2 = new HashMap<>();
        try {
            for (String s : refMap.keySet()) {
                m2.put(refMap.get(s), dataset.getValue(s));
            }
            refDataset.open(m2);
            grid.setDataset(refDataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
