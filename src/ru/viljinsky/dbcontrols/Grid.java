/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dbcontrols;

import java.util.HashMap;
import java.util.Map;
import ru.viljinsky.xmldb.Dataset;

//----------------------- G R I D   M O D E L ----------------------------------

//------------------------  G R I D - ------------------------------------------
public class Grid extends DefaultGrid {
    Map<String, String> map = null;

    public void setDataset(Dataset dataset) {
        model = new GridModel(dataset);
        setModel(model);
    }

    public Dataset getDataset() {
        if (model != null) {
            return model.dataset;
        }
        return null;
    }

    public void open() {
        if (model != null) {
            try {
                model.dataset.open();
                model.fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void open(Map<String, Object> keys) {
        if (map != null) {
            System.out.println("map->" + map);
        }
        System.out.println("keys->" + keys);
        Map<Integer, Object> filter = new HashMap<>();
        if (model != null) {
            try {
                Dataset dataset = model.dataset;
                for (String s : map.keySet()) {
                    filter.put(dataset.getColumnIndex(s), keys.get(map.get(s)));
                }
                System.out.println("filter->" + filter);
                model.dataset.open(filter);
                model.fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
