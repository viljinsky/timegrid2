/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.timegrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author вадик
 */
public  class Chip extends CellElement{
    int subject_id;
    int teacher_id;
    int room_id;
    int group_id;
    String teacher_name="???";
    String subject_name="???";
    String room_no = "???";
    String group_title="???";
    String depart_name;
    Color subjectColor = new Color(255,200,220);
        

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }
        

    @Override
    public void draw(Graphics g,Rectangle b) {
        super.draw(g,b);

        Image image = new BufferedImage(b.width-1, b.height-1, BufferedImage.TYPE_INT_RGB);
        Graphics g1 = image.getGraphics();
        g1.setColor(subjectColor);
        g1.fillRect(0, 0, b.width, b.height);

        int h = g1.getFontMetrics().getHeight();
        g1.setColor(Color.black);
        g1.drawString(subject_name, 0,1*h);

        g1.drawString(teacher_name,0,2*h);

        g1.drawString(room_no,0,3*h);

        g1.drawString(group_title,0,4*h);

        g.drawImage(image, b.x+1, b.y+1, null);


    }
        
}

