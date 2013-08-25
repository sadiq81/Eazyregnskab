/**
 *  TextCell.java
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
 * Used to create table cell objects.
 * See the Table class for more information.
 */
public class TextCell extends AbstractCell {

    protected Font font;
    protected String text;
    protected CompositeTextLine compositeTextLine;
    protected boolean wordwrap;


    /**
     * Creates a cell object and sets the font.
     *
     * @param font the font.
     */
    public TextCell(Font font) {
        this.font = font;
    }


    /**
     * Creates a cell object and sets the font and the cell text.
     *
     * @param font the font.
     * @param text the text.
     */
    public TextCell(Font font, String text) {
        this.font = font;
        this.text = text;
    }


    /**
     * Sets the font for this cell.
     *
     * @param font the font.
     */
    public void setFont(Font font) {
        this.font = font;
    }


    /**
     * Returns the font used by this cell.
     *
     * @return the font.
     */
    public Font getFont() {
        return this.font;
    }


    /**
     * Sets the cell text.
     *
     * @param text the cell text.
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * Returns the cell text.
     *
     * @return the cell text.
     */
    public String getText() {
        return this.text;
    }


    /**
     * Sets the composite text object.
     *
     * @param compositeTextLine the composite text object.
     */
    public void setCompositeTextLine(CompositeTextLine compositeTextLine) {
        this.compositeTextLine = compositeTextLine;
    }


    /**
     * Returns the composite text object.
     *
     * @return the composite text object.
     */
    public CompositeTextLine getCompositeTextLine() {
        return this.compositeTextLine;
    }


    @Override
    public void computeWidth() {
        if (text != null) {
            setWidth(font.stringWidth(text));
        }
    }


    /**
     * Sets the underline text parameter.
     * If the value of the underline variable is 'true' - the text is underlined.
     *
     * @param underline the underline text parameter.
     */
    public void setUnderline(boolean underline) {
        if (underline) {
            this.properties |= 0x00400000;
        } else {
            this.properties &= 0x00BFFFFF;
        }
    }


    /**
     * Returns the underline text parameter.
     *
     * @return the underline text parameter.
     */
    public boolean getUnderline() {
        return (properties & 0x00400000) != 0;
    }


    /**
     * Sets the strikeout text parameter.
     *
     * @param strikeout the strikeout text parameter.
     */
    public void setStrikeout(boolean strikeout) {
        if (strikeout) {
            this.properties |= 0x00800000;
        } else {
            this.properties &= 0x007FFFFF;
        }
    }


    /**
     * Returns the strikeout text parameter.
     *
     * @return the strikeout text parameter.
     */
    public boolean getStrikeout() {
        return (properties & 0x00800000) != 0;
    }


    /**
     * Draws the point, text and borders of this cell.
     */
    protected void paint(
            Page page,
            float x,
            float y,
            float w,
            float h,
            float margin) throws Exception {
        if (text != null && text.length() > 0) {
            drawBackground(page, x, y, w, h);
        }
        drawBorders(page, x, y, w, h);
        if (point != null) {
            point.x = (x + w) - (font.ascent / 2 + margin);
            point.y = y + (font.ascent / 2 + margin);
            point.r = font.ascent / 3;
            page.drawPoint(point);
        }
        if (text != null) {
            drawText(page, x, y, w, margin);
        }
    }


    private void drawBackground(
            Page page,
            float x,
            float y,
            float cell_w,
            float cell_h) throws Exception {
        page.setBrushColor(background);
        page.fillRect(x, y, cell_w, cell_h);
    }


    private void drawBorders(
            Page page,
            float x,
            float y,
            float cell_w,
            float cell_h) throws Exception {

        page.setPenColor(pen);
        page.setPenWidth(lineWidth);

        if (getBorder(Border.TOP) &&
                getBorder(Border.BOTTOM) &&
                getBorder(Border.LEFT) &&
                getBorder(Border.RIGHT)) {
            page.drawRect(x, y, cell_w, cell_h);
        } else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + cell_w, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + cell_h);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + cell_h);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + cell_w, y);
                page.lineTo(x + cell_w, y + cell_h);
                page.strokePath();
            }
        }

    }


    private void drawText(
            final Page page,
            final float x,
            float y,
            final float cell_w,
            final float margin) throws Exception {

        final float y_text = y + font.ascent + margin;
        page.setPenColor(pen);
        page.setBrushColor(brush);

        if (!wordwrap || getColSpan() > 1) {
            String textline = this.text;
            drawSingleTextLine(page, x, cell_w, margin, y_text, textline);
        } else {
            final Exception exceptions[] = new Exception[1];
            final float ypos[] = new float[1];
            ypos[0] = y_text;

            iterateLines(this.text, margin, new IterationListener() {
                public void line(String textline) {
                    if (exceptions[0] == null) {
                        try {
                            drawSingleTextLine(page, x, cell_w, margin, ypos[0], textline);
                            ypos[0] += font.body_height;
                        } catch (Exception e) {
                            exceptions[0] = e;
                        }
                    }
                }
            });
            if (exceptions[0] != null) throw exceptions[0];
        }
    }


    private void drawSingleTextLine(
            Page page, float x, float cell_w, float margin, float y_text, String text)
            throws Exception {
        if (getTextAlignment() == Align.RIGHT) {
            if (compositeTextLine == null) {
                float x_text = (x + cell_w) - (font.stringWidth(text) + margin);
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                compositeTextLine.setPosition(
                        (x + cell_w) - (compositeTextLine.getWidth() + margin),
                        y_text);
                compositeTextLine.drawOn(page);
            }
        } else if (getTextAlignment() == Align.CENTER) {
            if (compositeTextLine == null) {
                float x_text = x + (cell_w - font.stringWidth(text)) / 2;
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                compositeTextLine.setPosition(
                        x + (cell_w - compositeTextLine.getWidth()) / 2,
                        y_text);
                compositeTextLine.drawOn(page);
            }
        } else {
            // Use the default - Align.LEFT
            float x_text = x + margin;
            if (compositeTextLine == null) {
                page.drawString(font, text, x_text, y_text);
                if (getUnderline()) {
                    underlineText(page, font, text, x_text, y_text);
                }
                if (getStrikeout()) {
                    strikeoutText(page, font, text, x_text, y_text);
                }
            } else {
                compositeTextLine.setPosition(x_text, y_text);
                compositeTextLine.drawOn(page);
            }
        }
    }


    private void underlineText(
            Page page, Font font, String text, float x, float y)
            throws Exception {
        float descent = font.getDescent();
        page.setPenWidth(font.underlineThickness);
        page.moveTo(x, y + descent);
        page.lineTo(x + font.stringWidth(text), y + descent);
        page.strokePath();
    }


    private void strikeoutText(
            Page page, Font font, String text, float x, float y)
            throws Exception {
        page.setPenWidth(font.underlineThickness);
        page.moveTo(x, y - font.getAscent() / 3.0);
        page.lineTo(x + font.stringWidth(text), y - font.getAscent() / 3.0);
        page.strokePath();
    }


    /**
     * Returns the number of lines that are necessary to display the current text using the
     * current width.
     *
     * @param margin the x margin (left and right side added) which reduces the available width
     * @return the number of lines
     */
    protected int getNumVerCells(float margin) {
        final int n[] = new int[1];
        n[0] = 0;
        IterationListener iterationListener = new IterationListener() {
            public void line(String text) {
                n[0]++;
            }
        };

        iterateLines(getText(), margin, iterationListener);

        return n[0];
    }


    private void iterateLines(String text, float margin, IterationListener iterationListener) {
        String[] tokens = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (font.stringWidth((sb.length() > 0 ? (sb.toString() + " ") : "") + token)
                    > (getWidth() - margin)) {
                iterationListener.line(sb.toString());
                sb = new StringBuilder(token);
            } else {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(token);
            }
        }
        if (sb.toString().length() > 0) {
            iterationListener.line(sb.toString());
        }
    }


    protected interface IterationListener {
        void line(String text);
    }


    public float getComputedHeight(float xmargin) {
        if (wordwrap && getColSpan() <= 1) {
            return getNumVerCells(xmargin) * font.body_height;
        } else {
            return font.body_height;
        }
    }


    public boolean isWordwrap() {
        return wordwrap;
    }


    public void setWordwrap(boolean wordwrap) {
        this.wordwrap = wordwrap;
    }

}   // End of TextCell.java
