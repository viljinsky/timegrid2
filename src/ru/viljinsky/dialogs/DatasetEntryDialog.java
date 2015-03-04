/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.Border;
import ru.viljinsky.Dataset;
/**
 *
 * @author вадик
 */
public class DatasetEntryDialog extends BaseDialog {
    Dataset dataset;
    Container content;

    @Override
    public void initComponents(Container content) {
        this.content=content;
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    }
    
    
    class MyTextField extends JTextField{
        public MyTextField(){
            super(10);
            setPreferredSize(new Dimension(120,24));
        }
    }
    
    public void setDataset(Dataset dataset){
        for (String s:dataset.getColumns()){
            Box box  = Box.createHorizontalBox();
            box.add(new JLabel(s));
            box.add(Box.createHorizontalStrut(6));
            box.add(new MyTextField());
            box.setMaximumSize(new Dimension(Integer.MAX_VALUE,20));
            content.add(box);
            content.add(Box.createVerticalStrut(12));
        }
        Border b = BorderFactory.createEmptyBorder(10,10, 10,10);
        ((JPanel)content).setBorder(b);
        
    }
    
    
}
