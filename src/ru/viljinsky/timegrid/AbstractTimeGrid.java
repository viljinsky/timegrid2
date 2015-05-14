/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.timegrid;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author вадик
 */
abstract class AbstractTimeGrid extends Container {
    Set<DragObject> dragObjects = null;
    protected Cells cells;
    int colCount = 10;
    int rowCount = 7;
    Integer[] rowHeights;
    Integer[] colWidths;
    int selectedRow = -1;
    int selectedCol = -1;
    int shadowCol = -1;
    int shadowRow = -1;
    int startX;
    int startY;
    boolean dragged = false;
    Cell overCell = null;

    private Integer getMaxRowCount(int row) {
        Integer result = 1;
        Integer[] count = new Integer[colCount];
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }
        for (CellElement ce : cells) {
            if (ce.row == row) {
                count[ce.col] += 1;
            }
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] > result) {
                result = count[i];
            }
        }
        return result;
    }

    private void calcRowHeight() {
        rowHeights = new Integer[rowCount];
        for (int row = 0; row < rowCount; row++) {
            rowHeights[row] = getMaxRowCount(row) * CellElement.HEIGHT;
        }
        for (int i = 0; i < rowHeights.length; i++) {
            System.out.println(i + "-->" + rowHeights[i]);
        }
    }

    public void setOverCell(Cell cell) {
        if (!cell.equals(overCell)) {
            overCell = cell;
            System.out.println(overCell);
        }
    }

    public Cell getCellOver(int x, int y) {
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                if (getBound(col, row).contains(x, y)) {
                    return new Cell(col, row);
                }
            }
        }
        return null;
    }

    public Cell getSelectedCell() {
        return new Cell(selectedCol, selectedRow);
    }

    public AbstractTimeGrid() {
        setPreferredSize(new Dimension(800, 600));
        cells = new Cells();
        calcRowHeight();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Cell cell = getCellOver(e.getX(), e.getY());
                if (cell == null) {
                    selectedCol = -1;
                    selectedRow = -1;
                } else {
                    if (dragged) {
                        stopDrag(cell.col, cell.row);
                    }
                    selectedCol = cell.col;
                    selectedRow = cell.row;
                }
                if (dragged) {
                    dragged = false;
                    dragObjects = null;
                }
                calcRowHeight();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                Cell cell = getCellOver(startX, startY);
                if (cell != null) {
                    selectedCol = cell.col;
                    selectedRow = cell.row;
                    cellClick(cell.col, cell.row);
                }
                if (!e.isControlDown()) {
                    for (CellElement ce : cells) {
                        ce.selected = false;
                    }
                }
                for (CellElement ce : cells) {
                    if (ce.hitTest(startX, startY)) {
                        ce.selected = !ce.selected;
                        cellElementClick(ce);
                    }
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Cell cell = getCellOver(e.getX(), e.getY());
                if (cell != null) {
                    setOverCell(cell);
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragged) {
                    startDrag(selectedCol, selectedRow);
                    //                    List<DragObject> lst = new ArrayList<>();
                    //
                    //                    for (CellElement ce:cells){
                    //                        if (ce.selected){
                    //                            lst.add(new DragObject(ce));
                    //                        }
                    //                    }
                    //                    dragObjects = lst.toArray(new DragObject[lst.size()]);
                    dragObjects = new HashSet<>();
                    for (CellElement ce : cells) {
                        if (ce.selected) {
                            dragObjects.add(new DragObject(ce));
                        }
                    }
                    dragged = true;
                }
                int x = e.getX();
                int y = e.getY();
                drag(x - startX, y - startY);
                for (DragObject ce : dragObjects) {
                    ce.bound.x += x - startX;
                    ce.bound.y += y - startY;
                }
                startX = x;
                startY = y;
                repaint();
            }
        });
    }

    protected Rectangle getBound(int col, int row) {
        Rectangle result = new Rectangle();
        result.x = col * CellElement.WIDTH;
        result.width = CellElement.WIDTH;
        result.y = 0;
        for (int i = 0; i < row; i++) {
            result.y += rowHeights[i];
        }
        result.height = rowHeights[row];
        return result;
    }

    private boolean CellElementIsDragged(CellElement ce) {
        if (dragObjects == null) {
            return false;
        }
        for (DragObject dro : dragObjects) {
            if (dro.element == ce) {
                return true;
            }
        }
        return false;
    }

    public void drawCell(Graphics g, int col, int row) {
        Rectangle r;
        Color color = Color.pink;
        r = getBound(col, row);
        g.setColor(Color.red);
        g.drawRect(r.x, r.y, r.width, r.height);
        if (col == selectedCol && row == selectedRow) {
            color = Color.white;
        }
        if (overCell != null) {
            if (col == overCell.col && row == overCell.row) {
                color = Color.yellow;
            }
        }
        g.setColor(color);
        g.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
        //----------------------------------
        if (cells.isExists(col, row)) {
            Set<CellElement> list = cells.getCells(col, row);
            Rectangle r1 = new Rectangle(r);
            r1.height = r.height / list.size();
            for (CellElement ce : list) {
                if (!CellElementIsDragged(ce)) {
                    ce.bound = new Rectangle(r1.x + 1, r1.y + 1, r1.width - 2, r1.height - 2);
                    ce.draw(g, ce.bound);
                    r1.y += r1.height;
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                drawCell(g, col, row);
            }
        }
        if (dragObjects != null) {
            for (DragObject dro : dragObjects) {
                dro.draw(g);
            }
        }
    }

    public abstract void cellClick(int col, int row);

    public abstract void cellElementClick(CellElement ce);

    public abstract void startDrag(int col, int row);

    public abstract void stopDrag(int col, int row);

    public abstract void drag(int dx, int dy);
    
}
