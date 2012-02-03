package com.thomcc.nine;


import java.applet.Applet;
import java.awt.BorderLayout;

public class NineApplet extends Applet {
    private static final long serialVersionUID = 1L;
    private Nine nine = new Nine();
    public void init() {
        setLayout(new BorderLayout());
        add(nine, BorderLayout.CENTER);
    }

    public void start() { nine.start(); }
    public void stop() { nine.stop(); }
    
}