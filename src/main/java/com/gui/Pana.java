package com.gui;

import java.awt.EventQueue;
/**
 *
 * @author axe
 */
public class Pana {


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ButtonPanel();
            }
        });
    }
}