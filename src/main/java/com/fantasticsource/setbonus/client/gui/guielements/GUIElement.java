package com.fantasticsource.setbonus.client.gui.guielements;

public abstract class GUIElement
{
    public double x, y;

    public GUIElement(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public abstract boolean isWithin(double x, double y);

    public void draw(double screenWidth, double screenHeight)
    {
    }

    public void mouseWheel(double x, double y, int delta)
    {
    }

    public void mousePressed(double x, double y, int button)
    {
    }

    public void mouseReleased(double x, double y, int button)
    {
    }

    public void mouseDrag(double x, double y, int button)
    {
    }
}
