/**
 *  AbstractCell.java
 *
 Copyright (c) 2013, Innovatics Inc.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice,
 this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and / or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.pdfjet;


/**
 * A cell in a table row.
 */
public abstract class AbstractCell {

    protected Point point;
    protected float width = 70.0f;
    protected float height = 0.0f;
    protected float lineWidth = 0.0f;
    protected int background = Color.white;
    protected int pen = Color.black;
    protected int brush = Color.black;
    // Cell properties
    // Colspan:
    // bits 0 to 15
    // Border:
    // bit 16 - top
    // bit 17 - bottom
    // bit 18 - left
    // bit 19 - right
    // Text Alignment:
    // bit 20
    // bit 21
    // Text Decoration:
    // bit 22 - underline
    // bit 23 - strikeout
    // Future use:
    // bits 24 to 31
    protected int properties = 0x000F0001;


    /**
     * Returns the column span private variable value.
     *
     * @return the column span value.
     */
    public int getColSpan() {
        return (this.properties & 0x0000FFFF);
    }


    /**
     * Returns the cell width.
     *
     * @return the cell width.
     */
    public float getWidth() {
        return this.width;
    }


    public abstract void computeWidth();


    /**
     * Sets the width of this cell.
     *
     * @param width the specified width.
     */
    public void setWidth(float width) {
        this.width = width;
    }


    /**
     * Sets the cell border object.
     *
     * @param border the border object.
     */
    public void setBorder(int border, boolean visible) {
        if (visible) {
            this.properties |= border;
        } else {
            this.properties &= (~border & 0x00FFFFFF);
        }
    }


    /**
     * Sets the cell text alignment.
     *
     * @param alignment the alignment code.
     *                  Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public void setTextAlignment(int alignment) {
        this.properties &= 0x00CFFFFF;
        this.properties |= (alignment & 0x00300000);
    }


    /**
     * Sets the brush color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBrushColor(int color) {
        this.brush = color;
    }


    /**
     * Returns the cell point.
     *
     * @return the point.
     */
    public Point getPoint() {
        return this.point;
    }


    /**
     * Returns the border line width.
     *
     * @return the border line width.
     */
    public float getLineWidth() {
        return this.lineWidth;
    }


    /**
     * Returns the cell height.
     *
     * @return the cell height.
     */
    public float getHeight() {
        return this.height;
    }


    /**
     * Returns the background color of this cell.
     */
    public int getBgColor() {
        return this.background;
    }


    /**
     * Returns the pen color.
     */
    public int getPenColor() {
        return pen;
    }


    /**
     * Returns the brush color.
     *
     * @return the brush color.
     */
    public int getBrushColor() {
        return brush;
    }


    protected int getProperties() {
        return this.properties;
    }


    /**
     * Sets all border object parameters to false.
     * This cell will have no borders when drawn on the page.
     */
    public void setNoBorders() {
        this.properties &= 0x00F0FFFF;
    }


    /**
     * Sets the pen color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setPenColor(int color) {
        this.pen = color;
    }


    /**
     * Sets the border line width.
     *
     * @param lineWidth the border line width.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }


    /**
     * Draws the point, text and borders of this cell.
     */
    protected abstract void paint(
            Page page, float x, float y, float cell_w, float cell_h, float margin)
            throws Exception;


    /**
     * Computes the height (in page units) necessary for this cell.
     * E.g. for text it is a single line of text without word wrapping and multiple
     * lines of text with word wrapping. For an image it is the height of the image in
     * relation to the available width.
     *
     * @param xmargin the added margin on the left and right side
     * @return the height in page units
     */
    public abstract float getComputedHeight(float xmargin);


    /**
     * Sets the point inside this cell.
     * See the Point class and Example_09 for more information.
     *
     * @param point the point.
     */
    public void setPoint(Point point) {
        this.point = point;
    }


    /**
     * Sets the width of this cell.
     *
     * @param width the specified width.
     */
    public void setWidth(double width) {
        this.width = (float) width;
    }


    /**
     * Sets the height of this cell.
     *
     * @param height the specified height.
     */
    public void setHeight(double height) {
        this.height = (float) height;
    }


    /**
     * Sets the height of this cell.
     *
     * @param height the specified height.
     */
    public void setHeight(float height) {
        this.height = height;
    }


    /**
     * Sets the border line width.
     *
     * @param lineWidth the border line width.
     */
    public void setLineWidth(double lineWidth) {
        this.lineWidth = (float) lineWidth;
    }


    /**
     * Sets the background to the specified color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBgColor(int color) {
        this.background = color;
    }


    /**
     * Sets the pen and brush colors to the specified color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int color) {
        this.pen = color;
        this.brush = color;
    }


    protected void setProperties(int properties) {
        this.properties = properties;
    }


    /**
     * Sets the column span private variable.
     *
     * @param colspan the specified column span value.
     */
    public void setColSpan(int colspan) {
        this.properties &= 0x00FF0000;
        this.properties |= (colspan & 0x0000FFFF);
    }


    /**
     * Returns the cell border object.
     *
     * @return the cell border object.
     */
    public boolean getBorder(int border) {
        return (this.properties & border) != 0;
    }


    /**
     * Returns the text alignment.
     */
    public int getTextAlignment() {
        return (this.properties & 0x00300000);
    }

}   // End of AbstractCell.java
