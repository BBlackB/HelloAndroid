package com.example;


/**
 * Created by Administrator on 2016/9/21.
 */
public class Color {
    private short r;
    private short g;
    private short b;

    public Color(short r, short g, short b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color()
    {
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public Color(Color color)
    {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
    }

    public short getR()
    {
        return r;
    }

    public short getG()
    {
        return g;
    }

    public short getB()
    {
        return b;
    }

    public void setR(short r)
    {
        this.r = r;
    }

    public void setG(short g)
    {
        this.g = g;
    }

    public void setB(short b)
    {
        this.b = b;
    }

    public void setColor(short r, short g, short b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
