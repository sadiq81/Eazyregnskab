/**
 *  ImageCell.java
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
 * An image in a table cell.
 */
public class ImageCell extends AbstractCell {

    private Image image;

    private boolean keepAspect = true;
    private boolean increaseRowSize = true;
    private int maxWidth = 200;


    /**
     * Creates a new cell that contains an image.
     *
     * @param image the image to display
     * @throws Exception
     */
    public ImageCell(Image image) throws Exception {
        this.image = image;
    }


    /**
     * Creates a new cell that contains an image.
     *
     * @param image    the image to display
     * @param maxWidth the maximal width for this image if {@link Table#autoAdjustColumnWidths()} is called
     * @throws Exception
     */
    public ImageCell(Image image, int maxWidth) throws Exception {
        this.image = image;
        this.maxWidth = maxWidth;
    }


    /**
     * Creates a new cell that contains an image.
     *
     * @param image           the image to display
     * @param keepAspect      determines if the aspect ratio should be kept when scaling the image
     * @param increaseRowSize determines if this table row should increase its size to show the image with its correct height when scaled to the width of the column
     * @param maxWidth        the maximal width for this image if {@link Table#autoAdjustColumnWidths()} is called
     * @throws Exception
     */
    public ImageCell(Image image, boolean keepAspect, boolean increaseRowSize, int maxWidth)
            throws Exception {
        this.image = image;
        this.keepAspect = keepAspect;
        this.increaseRowSize = increaseRowSize;
        this.maxWidth = maxWidth;
    }


    @Override
    public void computeWidth() {
        if (increaseRowSize) {
            setWidth(Math.min(maxWidth, image.getWidth()));
        }
    }


    @Override
    protected void paint(Page page, float x, float y, float w, float h, float margin)
            throws Exception {
        image.setPosition(x + margin, y + margin);
        if (keepAspect) {
            float factor = Math.min(
                    (w - margin * 2) / image.getWidth(), (h - margin * 2) / image.getHeight());
            image.scaleBy(factor, factor);
        } else {
            image.w = w - margin * 2;
            image.h = h - margin * 2;
        }
        image.drawOn(page);
    }


    @Override
    public float getComputedHeight(float xmargin) {
        if (increaseRowSize) {
            return image.getHeight() * ((getWidth() - xmargin) / image.getWidth());
        } else {
            return 0;
        }
    }

}   // End of ImageCell.java
