package com.fantasticsource.setbonus.client.gui.guielements;

import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;

public class VerticalScrollbar extends GUIElement
{
    private double progress = 0;
    private double height, sliderHeight;
    private GradientBorder background, slider;
    private GUIElement container;
    private GUIElement[] listedElements;
    private boolean active;

    public VerticalScrollbar(double left, double top, double right, double bottom, Color backgroundBorder, Color backgroundCenter, Color sliderBorder, Color sliderCenter, GUIRectElement container, GUIRectElement... listedElements)
    {
        super(left, top);
        this.container = container;
        this.listedElements = listedElements;

        double thickness = (right - left) / 3;
        height = bottom - top;
        sliderHeight = height / 10;

        background = new GradientBorder(left, top, right, bottom, thickness, backgroundBorder, backgroundCenter);
        slider = new GradientBorder(left, 0, right, sliderHeight, thickness, sliderBorder, sliderCenter);
    }

    @Override
    public void draw(double width, double height)
    {
        background.draw(width, height);

        if (progress >= 0 && progress <= 1)
        {
            slider.y = y + (this.height - sliderHeight) * progress;
            slider.draw(width, height);
        }
    }

    @Override
    public void mouseWheel(double x, double y, int delta)
    {
        if (isWithin(x, y) || container.isWithin(x, y))
        {
            if (delta < 0)
            {
                progress += 0.1;
                if (progress > 1) progress = 1;
            }
            else
            {
                progress -= 0.1;
                if (progress < 0) progress = 0;
            }
        }
    }

    public boolean isWithin(double x, double y)
    {
        return background.isWithin(x, y);
    }

    @Override
    public void mousePressed(double x, double y, int button)
    {
        if (progress != -1 && button == 0 && isWithin(x, y))
        {
            active = true;
            progress = Tools.min(Tools.max((y - this.y - slider.height * 0.5) / (height - slider.height), 0), 1);
        }
    }

    @Override
    public void mouseReleased(double x, double y, int button)
    {
        if (button == 0) active = false;
    }

    @Override
    public void mouseDrag(double x, double y, int button)
    {
        if (active && button == 0)
        {
            if (progress == -1) active = false;
            else progress = Tools.min(Tools.max((y - this.y - slider.height * 0.5) / (height - slider.height), 0), 1);
        }
    }
}
