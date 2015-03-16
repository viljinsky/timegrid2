/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dbcontrols;

import java.util.HashMap;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import ru.viljinsky.xmldb.Dataset;

//----------------------- G R I D   M O D E L ----------------------------------

class GridModel extends AbstractTableModel {
    Dataset dataset;
    Map<Integer, Map<Object, Object>> lookup = new HashMap<>();

    public GridModel(Dataset dataset) {
        this.dataset = dataset;
        try {
            for (String column : dataset.getColumns()) {
                if (dataset.isLookup(column)) {
                    lookup.put(dataset.getColumnIndex(column), dataset.getLookup(column));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка в конструкторе GridModel :\n" + e.getMessage());
        }
    }

    @Override
    public int getRowCount() {
        return dataset.getRowCount();
    }

    @Override
    public String getColumnName(int column) {
        return dataset.getColumnName(column);
    }

    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] rowset = dataset.getRowset(rowIndex);
        if (lookup.containsKey(columnIndex)) {
            Map lu = lookup.get(columnIndex);
            return lu.get(rowset[columnIndex]);
        }
        return rowset[columnIndex];
    }
    
}
