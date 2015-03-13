/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import ru.viljinsky.DataModule;
import ru.viljinsky.Dataset;
import ru.viljinsky.TestDS;

/**
 *
 * @author вадик
 */


//----------------------- G R I D   M O D E L ----------------------------------
class GridModel extends AbstractTableModel{
    Dataset dataset;
    Map<Integer,Map<Object,Object>> lookup = new HashMap<>();
    
    public GridModel(Dataset dataset) {
        this.dataset = dataset;
        try{
            for (String column:dataset.getColumns()){
                if (dataset.isLookup(column)){
                    lookup.put(dataset.getColumnIndex(column), dataset.getLookup(column));
                }
            }
        } catch (Exception e){
            System.err.println("Ошибка в конструкторе GridModel :\n"+e.getMessage());
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
        Object[] rowset  = dataset.getRowset(rowIndex);
        if (lookup.containsKey(columnIndex)){
            Map lu = lookup.get(columnIndex);
            return lu.get(rowset[columnIndex]);
        }
        return rowset[columnIndex];
    }
}

//------------------------  G R I D - ------------------------------------------

class Grid extends DefaultGrid{
    Map<String,String> map = null;

    public void setDataset(Dataset dataset){
        model = new GridModel(dataset);
        setModel(model);
    }
    
    public Dataset getDataset(){
        if (model!=null)
            return model.dataset;
        return null;
    }
    
    public void open(){
        if (model!=null)
            try{
                model.dataset.open();
                model.fireTableDataChanged();
            } catch (Exception e){
                e.printStackTrace();
            }
        }   
    
    public void open(Map<String,Object> keys){
        if (map!=null)
            System.out.println("map->"+map);
        System.out.println("keys->"+keys);
        Map<Integer,Object> filter = new HashMap<>();
        if (model!=null) {
            try{
                Dataset dataset = model.dataset;
                for (String s:map.keySet()){
                    filter.put(dataset.getColumnIndex(s), keys.get(map.get(s)));
                }
                System.out.println("filter->"+filter);
                
                model.dataset.open(filter);
                model.fireTableDataChanged();
            } catch (Exception e){
                e.printStackTrace();
            }
        }   
        
    }
}


//--------------------- G R I D   P A N E L ------------------------------------
class GridPanel extends JPanel{
    JTabbedPane tabbedPane = new JTabbedPane();
    Grid grid = new MasterGrid();
    Grid[] refGrids;
    
    
    class MasterGrid extends Grid{
        
        Action[] masterAction ;

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
        
        @Override
        public void doCommand(String command){
            try{
                switch (command){
                    case "fillShift":
                        if (model.dataset.getTableName().equals("shift")){
                            TestDS.fillShift(model.dataset.getValue("id"));
                            model.fireTableDataChanged();
                        }
                        break;
                    case "fill1":case "fill2":case "fill3":
                        System.out.println("-->"+command+" ok "+ model.dataset.getTableName());
                        break;
                    default:    
                    super.doCommand(command);
                }
            } catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
        }
        
        @Override
        public void createPopup(JPopupMenu popup) {
            super.createPopup(popup);
            popup.addSeparator();
            
            masterAction = new Action[]{new Act("fillShift"),new Act("fill2"),new Act("fill3")};
            
            for (Action a:masterAction){
                popup.add(a);
            }
        }
        
    }
    
    public GridPanel(){
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(grid));
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane);
        grid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    onRecordChange();
                }
            }
        });
        
    }
    
    public void onRecordChange(){
        int row = grid.getSelectedRow();
        if (row>=0){
            Dataset dataset = grid.getDataset();
            dataset.setIndex(row);
            Map<String,Object> keys= new HashMap<>();
            try{
                for (String s:dataset.getPrimary()){
                    keys.put(s, dataset.getValue(s));
                }
                System.out.println(keys);
                for (Grid g:refGrids){
                    g.open(keys);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void setDataset(Dataset dataset){
        tabbedPane.removeAll();
        grid.setDataset(dataset);
        Dataset[] refDatasets = dataset.getRefTables();
        Dataset ds;
        refGrids = new Grid[refDatasets.length];
        for (int i =0 ; i<refGrids.length;i++){
            ds = refDatasets[i];
            refGrids[i]= new Grid();
            refGrids[i].setDataset(ds);
            tabbedPane.addTab(ds.getTableName(),new JScrollPane(refGrids[i]));
            try{
                String refs = ds.getReferences(dataset.getTableName());
                Map<String,String> map = new HashMap<>();
                map.put(refs.split("=")[0], refs.split("=")[1]);
                refGrids[i].map = map;
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
    }
    
}
//----------------------------   T E S T 1 -------------------------------------
public class Main extends JFrame{
    DataModule dataModule = DataModule.getInsatnce();
    JTabbedPane tabbedPane = new JTabbedPane();
    File file = new File(".");


    public Main(){
        super("Main");
        Container content = getContentPane();
        content.setPreferredSize(new Dimension(800, 600));
        content.add(tabbedPane);
        int x,y;
        Dimension d = getToolkit().getScreenSize();
        pack();
        setLocation((d.width-getWidth())/2,(d.height-getHeight())/2);
    }
    
    public void open(String fileName) {
        
        GridPanel panel;
        String tableName;
        if (dataModule.isActive()){
            dataModule.close();
        }
        tabbedPane.removeAll();
        if (fileName==null)
            dataModule.open();
        else
            dataModule.open(fileName);
        
        for (Dataset dataset:dataModule.getTables()){
            if (dataset.hasReferences()){
                tableName = dataset.getTableName();
                panel = new GridPanel();
                panel.setDataset(dataset);
                tabbedPane.addTab(tableName, panel);
            }
        }
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
    
    
    protected void doCommand(String command){
        JFileChooser fc;
        int retVal;
        try{
            switch(command){
                
                case "open":
                     fc = new JFileChooser(file);
                     retVal = fc.showOpenDialog(rootPane);
                     if (retVal==JFileChooser.APPROVE_OPTION){
                         open(fc.getSelectedFile().getPath());
                         file = fc.getSelectedFile();
                     }
                    break;
                case "save":
                    fc = new JFileChooser(file);
                    fc.setSelectedFile(new File("test.xml"));
                    retVal = fc.showSaveDialog(fc);
                    if (retVal==JFileChooser.APPROVE_OPTION){
                        dataModule.save(fc.getSelectedFile().getPath());
                        file = fc.getSelectedFile();
                        JOptionPane.showMessageDialog(rootPane, "Файл "+file.getName()+" успешно сохранён");
                    }
                    break;
                case "saveAs":
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
            
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }
    
    public JMenu getFileMenu(){
        Action[] actions = {new Act("open"),new Act("save"),new Act("saveAs"),null,new Act("exit")};
        JMenu menu = new JMenu("File");
        for (Action a:actions){
            if (a==null)
                menu.addSeparator();
            else
                menu.add(a);
        }
        return menu;
    }
    
    public static void main(String[] args){
        final Main frame = new Main();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(frame.getFileMenu());
        frame.setJMenuBar(menuBar);
        
        JMenu menu = new JMenu("Dictionary");
        menu.add(new AbstractAction("Dict") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Dict.showDictionary(frame);
            }
        });
        
        menu.add(new AbstractAction("Schedue") {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                Schedule.showSchedule(frame,frame.dataModule.getTable("schedule"));
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        
        menu.add(new AbstractAction("Model") {

            @Override
            public void actionPerformed(ActionEvent e) {
                ErrModel.showModel();
            }
        });
        menuBar.add(menu);
        
        frame.pack();
        frame.setVisible(true);
        frame.open(null);
        
    }

}
