/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky;

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
        String depart_name;
        
        DataModule dm = DataModule.getInsatnce();
        
        public Chip(int subject_id,int teacher_id,int room_id,int group_id){
            this.subject_id=subject_id;
            this.teacher_id=teacher_id;
            this.room_id=room_id;
            this.group_id=group_id;
            
            try{
                teacher_name = (String) dm.lookUp("teacher", "id", teacher_id, "first_name");
                subject_name = (String) dm.lookUp("subject", "id", subject_id, "name");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void draw(Graphics g,Rectangle b) {
            super.draw(g,b);
//            String subject_name = dm.getSubjectName(subject_id);
//            String teacher_name = dm.getTeacherName(teacher_id);
            String room_no = dm.getRoomNo(room_id);
            String group_title = dm.getGroupTitle(group_id);
            

            Image image = new BufferedImage(b.width-1, b.height-1, BufferedImage.TYPE_INT_RGB);
            Graphics g1 = image.getGraphics();
            g1.setColor(dm.getSubjectColor(subject_id));
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

