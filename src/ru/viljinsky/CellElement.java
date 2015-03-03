/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author вадик
 */
public class CellElement {
    int col;
    int row;
    boolean selected = false;
    Rectangle bound;
    public static Integer WIDTH = 60;
    public static Integer HEIGHT = 40;
    Color color = new Color(255, 255, 200);

    public void draw(Graphics g, Rectangle b) {
        g.setColor(color);
        g.fillRect(b.x, b.y, b.width, b.height);
        if (selected) {
            g.setColor(Color.BLACK);
            g.drawRect(b.x, b.y, b.width, b.height);
        }
    }

    public boolean hitTest(int x, int y) {
        return bound.contains(x, y);
    }

    public void setCell(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
}
