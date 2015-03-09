/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

/**
 *
 * @author вадик
 */

interface IColumn{
    public String getColumnName();
    public String getColumnLabel();
}

interface IRecordSet{
    public Integer getRecordCount();
    public Integer getColumnCount();
    public Object[] getRowset();
    public IColumn getColumn(int columnIndex);
}

interface IDataset{
    public void append();
    public void update();
    public void delete();
    public void insert();
    public void first();
    public void next();
    public void prior();
    public void last();
    public boolean bof();
    public boolean eof();
    public void open();
    public void close();
}
