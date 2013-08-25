/**
 *  FlexTable.java
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

import java.util.ArrayList;
import java.util.List;


/**
 * Used to create table objects and draw them on a page.
 * <p/>
 * Please see Example_08.
 */
public class FlexTable {

    public static int DATA_HAS_0_HEADER_ROWS = 0;
    public static int DATA_HAS_1_HEADER_ROWS = 1;
    public static int DATA_HAS_2_HEADER_ROWS = 2;
    public static int DATA_HAS_3_HEADER_ROWS = 3;
    public static int DATA_HAS_4_HEADER_ROWS = 4;
    public static int DATA_HAS_5_HEADER_ROWS = 5;
    public static int DATA_HAS_6_HEADER_ROWS = 6;
    public static int DATA_HAS_7_HEADER_ROWS = 7;
    public static int DATA_HAS_8_HEADER_ROWS = 8;
    public static int DATA_HAS_9_HEADER_ROWS = 9;

    private int rendered = 0;
    private int numOfPages;

    private List<? extends List<? extends AbstractCell>> tableData = null;
    private int numOfHeaderRows = 0;

    private float x1;
    private float y1;

    private float margin = 1f;
    private float bottom_margin = 30f;


    /**
     * Create a table object.
     */
    public FlexTable() {
        tableData = new ArrayList<List<AbstractCell>>();
    }


    /**
     * Sets the position (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(double x, double y) {
        this.x1 = (float) x;
        this.y1 = (float) y;
    }


    /**
     * Sets the position (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     * Sets the location (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
    }


    /**
     * Sets the padding around the cells of this table.
     *
     * @param margin the padding.
     */
    public void setCellPadding(double margin) {
        this.margin = (float) margin;
    }


    /**
     * Sets the padding around the cells of this table.
     *
     * @param margin the padding.
     */
    public void setCellPadding(float margin) {
        this.margin = margin;
    }


    /**
     * Sets the margin around the cells of this table.
     *
     * @param margin the margin.
     */
    public void setCellMargin(double margin) {
        this.margin = (float) margin;
    }


    /**
     * Sets the margin around the cells of this table.
     *
     * @param margin the margin.
     */
    public void setCellMargin(float margin) {
        this.margin = margin;
    }


    /**
     * Sets the bottom margin for this table.
     *
     * @param bottom_margin the margin.
     */
    public void setBottomMargin(double bottom_margin) {
        this.bottom_margin = (float) bottom_margin;
    }


    /**
     * Sets the bottom margin for this table.
     *
     * @param bottom_margin the margin.
     */
    public void setBottomMargin(float bottom_margin) {
        this.bottom_margin = bottom_margin;
    }


    /**
     * Sets the table data.
     * <p/>
     * The table data is a perfect grid of cells.
     * All cell should be an unique object and you can not reuse blank cell objects.
     * Even if one or more cells have colspan bigger than zero the number of cells in the row will not change.
     *
     * @param tableData the table data.
     */
    public void setData(List<? extends List<? extends AbstractCell>> tableData) throws Exception {
        this.tableData = tableData;
        this.numOfHeaderRows = 0;
        this.rendered = numOfHeaderRows;
    }


    /**
     * Sets the table data and specifies the number of header rows in this data.
     *
     * @param tableData       the table data.
     * @param numOfHeaderRows the number of header rows in this data.
     */
    public void setData(
            List<? extends List<? extends AbstractCell>> tableData, int numOfHeaderRows) throws Exception {
        this.tableData = tableData;
        this.numOfHeaderRows = numOfHeaderRows;
        this.rendered = numOfHeaderRows;
    }


    /**
     * Auto adjusts the widths of all columns so that they are just wide enough to hold the text without truncation.
     */
    public void autoAdjustColumnWidths(float tableWidth) {
        // Find the maximum text width for each column
        float[] max_col_widths = new float[tableData.get(0).size()];
        for (List<? extends AbstractCell> row : tableData) {
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = row.get(j);
                if (cell.getColSpan() == 1) {
                    cell.computeWidth();
                    max_col_widths[j] = Math.max(max_col_widths[j], cell.getWidth());
                }
            }
        }

        if (tableWidth > 0) {
            // The sum of all columns must not be larger than tableWidth
            float sum = 0;
            for (int col = 0; col < max_col_widths.length; col++) {
                sum += max_col_widths[col];
                if (sum > tableWidth) {
                    max_col_widths[col] = Math.max(0, max_col_widths[col] - (sum - tableWidth));
                }
            }
        }

        // Set all cells of a single column to the same width
        for (List<? extends AbstractCell> row : tableData) {
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = row.get(j);
                if (cell.getColSpan() <= 1) {
                    cell.setWidth(max_col_widths[j] + 3 * margin);
                } else {
                    float width = 0;
                    for (int col = j; col < row.size() && col < j + cell.getColSpan(); col++) {
                        width += max_col_widths[col];
                    }
                    cell.setWidth(width + 3 * margin);
                }
            }
        }
    }

    public void autoAdjustColumnWidths() {
        autoAdjustColumnWidths(-1);
    }

    /**
     * Sets the alignment of the numbers to the right.
     */
    public void rightAlignNumbers() {
        for (int i = numOfHeaderRows; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            for (AbstractCell cell : row) {
                if (cell instanceof TextCell) {
                    TextCell textCell = (TextCell) cell;

                    if (textCell.getText() != null) {
                        String str = textCell.text;
                        int len = str.length();
                        boolean isNumber = true;
                        int k = 0;
                        while (k < len) {
                            char ch = str.charAt(k++);
                            if (!Character.isDigit(ch)
                                    && ch != '('
                                    && ch != ')'
                                    && ch != '-'
                                    && ch != '.'
                                    && ch != ','
                                    && ch != '\'') {
                                isNumber = false;
                            }
                        }
                        if (isNumber) {
                            textCell.setTextAlignment(Align.RIGHT);
                        }
                    }
                }
            }
        }
    }


    /**
     * Removes the horizontal lines between the rows from index1 to index2.
     */
    public void removeLineBetweenRows(int index1, int index2) throws Exception {
        for (int j = index1; j < index2; j++) {
            List<? extends AbstractCell> row = tableData.get(j);
            for (AbstractCell cell : row) {
                cell.setBorder(Border.BOTTOM, false);
            }
            row = tableData.get(j + 1);
            for (AbstractCell cell : row) {
                cell.setBorder(Border.TOP, false);
            }
        }
    }


    /**
     * Sets the text alignment in the specified column.
     *
     * @param index     the index of the specified column.
     * @param alignment the specified alignment. Supported values: Align.LEFT, Align.RIGHT, Align.CENTER and Align.JUSTIFY.
     */
    public void setTextAlignInColumn(int index, int alignment) throws Exception {
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            if (index < row.size()) {
                row.get(index).setTextAlignment(alignment);
            }
        }
    }


    /**
     * Sets the color of the text in the specified column.
     *
     * @param index the index of the specified column.
     * @param color the color specified as an integer.
     */
    public void setTextColorInColumn(int index, int color) throws Exception {
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            if (index < row.size()) {
                row.get(index).setBrushColor(color);
            }
        }
    }


    /**
     * Sets the font and font size for the specified column.
     *
     * @param index the index of the specified column.
     * @param font  the font.
     * @param size  the font size.
     * @deprecated As of version 4.00, replaced by {@link #setFontInColumn(int, com.pdfjet.Font)}
     */
    @Deprecated
    public void setTextFontInColumn(int index, Font font, double size) throws Exception {
        for (List<? extends AbstractCell> row : tableData) {
            if (index < row.size()) {
                AbstractCell cell = row.get(index);
                if (cell instanceof TextCell) {
                    TextCell textCell = (TextCell) cell;
                    font.setSize(size);
                    textCell.setFont(font);
                }
            }
        }
    }


    /**
     * Sets the font and font size for the specified column.
     *
     * @param index the index of the specified column.
     * @param font  the font.
     */
    public void setFontInColumn(int index, Font font) throws Exception {
        for (List<? extends AbstractCell> row : tableData) {
            if (index < row.size()) {
                AbstractCell cell = row.get(index);
                if (cell instanceof TextCell) {
                    TextCell textCell = (TextCell) cell;
                    textCell.setFont(font);
                }
            }
        }
    }


    /**
     * Sets the color of the text in the specified row.
     *
     * @param index the index of the specified row.
     * @param color the color specified as an integer.
     */
    public void setTextColorInRow(int index, int color) throws Exception {
        List<? extends AbstractCell> row = tableData.get(index);
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setBrushColor(color);
        }
    }


    /**
     * Sets the font and font size for the text in the specified row.
     *
     * @param index the index of the specified row.
     * @param font  the font.
     * @param size  the font size.
     * @deprecated As of version 4.00, replaced by {@link #setFontInRow(int, com.pdfjet.Font)}
     */
    @Deprecated
    public void setTextFontInRow(int index, Font font, double size) throws Exception {
        List<? extends AbstractCell> row = tableData.get(index);
        for (AbstractCell cell : row) {
            if (cell instanceof TextCell) {
                TextCell textCell = (TextCell) cell;
                font.setSize(size);
                textCell.setFont(font);
            }
        }
    }


    /**
     * Sets the font and font size for the text in the specified row.
     *
     * @param index the index of the specified row.
     * @param font  the font.
     */
    public void setFontInRow(int index, Font font) throws Exception {
        List<? extends AbstractCell> row = tableData.get(index);
        for (AbstractCell cell : row) {
            if (cell instanceof TextCell) {
                TextCell textCell = (TextCell) cell;
                textCell.setFont(font);
            }
        }
    }


    /**
     * Sets the width of the column with the specified index.
     *
     * @param index the index of specified column.
     * @param width the specified width.
     */
    public void setColumnWidth(int index, double width) throws Exception {
        for (List<? extends AbstractCell> row : tableData) {
            if (index < row.size()) {
                row.get(index).setWidth((float) width);
            }
        }
    }


    /**
     * Returns the cell at the specified row and column.
     *
     * @param row the specified row.
     * @param col the specified column.
     * @return the cell at the specified row and column.
     * @deprecated use {@link #getCellAtRowColumn} instead, because this will throw an exception if your table contains other kinds of cells and you try to retrieve them
     */
    @Deprecated
    public TextCell getCellAt(int row, int col) throws Exception {
        if (row >= 0) {
            return (TextCell) tableData.get(row).get(col);
        }
        return (TextCell) tableData.get(tableData.size() + row).get(col);
    }


    /**
     * Returns the cell at the specified row and column.
     *
     * @param row the specified row.
     * @param col the specified column.
     * @return the cell at the specified row and column.
     */
    public AbstractCell getCellAtRowColumn(int row, int col) throws Exception {
        if (row >= 0) {
            return tableData.get(row).get(col);
        }
        return tableData.get(tableData.size() + row).get(col);
    }


    /**
     * Returns a list of cell for the specified row.
     *
     * @param index the index of the specified row.
     * @return the list of cells.
     * @deprecated use {@link #getRowAtIndex(int)} instead, because this will throw an Exception if you use e.g. ImageCell objects in your table
     */
    @Deprecated
    public List<? extends AbstractCell> getRow(int index) throws Exception {
        return (List<? extends AbstractCell>) tableData.get(index);
    }


    /**
     * Returns a list of cell for the specified row.
     *
     * @param index the index of the specified row.
     * @return the list of cells.
     */
    public List<? extends AbstractCell> getRowAtIndex(int index) throws Exception {
        return (List<? extends AbstractCell>) tableData.get(index);
    }


    /**
     * Returns a list of cell for the specified column.
     *
     * @param index the index of the specified column.
     * @return the list of cells.
     * @deprecated use {@link #getColumnAtIndex(int)} instead, because this will throw an Exception if you use e.g. ImageCell objects in your table
     */
    @Deprecated
    public List<TextCell> getColumn(int index) throws Exception {
        List<TextCell> column = new ArrayList<TextCell>();
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            if (index < row.size()) {
                column.add((TextCell) row.get(index));
            }
        }
        return column;
    }


    /**
     * Returns a list of cell for the specified column.
     *
     * @param index the index of the specified column.
     * @return the list of cells.
     */
    public List<AbstractCell> getColumnAtIndex(int index) throws Exception {
        List<AbstractCell> column = new ArrayList<AbstractCell>();
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            if (index < row.size()) {
                column.add(row.get(index));
            }
        }
        return column;
    }


    /**
     * Returns the total number of pages that are required to draw this table on.
     *
     * @param page the type of pages we are drawing this table on.
     * @return the number of pages.
     */
    public int getNumberOfPages(Page page) throws Exception {
        numOfPages = 1;
        while (hasMoreData()) {
            drawOn(page, false);
        }
        resetRenderedPagesCount();
        return numOfPages;
    }


    /**
     * Draws this table on the specified page.
     *
     * @param page the page to draw this table on.
     * @return Point the point on the page where to draw the next component.
     */
    public Point drawOn(Page page) throws Exception {
        return drawOn(page, true);
    }


    /**
     * Draws this table on the specified page.
     *
     * @param page the page to draw this table on.
     * @param draw if false - do not draw the table. Use to only find out where the table ends.
     * @return Point the point on the page where to draw the next component.
     */
    public Point drawOn(Page page, boolean draw) throws Exception {
        float x = x1;
        float y = y1;
        float cell_w;
        float cell_h = 0f;

        for (int i = 0; i < numOfHeaderRows; i++) {
            cell_h = 0f;
            List<? extends AbstractCell> dataRow = tableData.get(i);

            // Calculate the maximal height
            for (AbstractCell aDataRow : dataRow) {
                cell_h = Math.max(cell_h, aDataRow.getComputedHeight(2 * margin) + 2 * margin);
            }

            for (int j = 0; j < dataRow.size(); j++) {
                AbstractCell cell = dataRow.get(j);
                float cellHeight = cell.getComputedHeight(2 * margin) + 2 * margin;
                if (cellHeight > cell_h) {
                    cell_h = cellHeight;
                }
                cell_w = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int k = 1; k < colspan; k++) {
                    cell_w += dataRow.get(++j).getWidth();
                }

                if (draw) {
                    page.setBrushColor(cell.getBrushColor());
                    cell.paint(page, x, y, cell_w, cell_h, margin);
                }

                x += cell_w;
            }
            x = x1;
            y += cell_h;
        }

        for (int i = rendered; i < tableData.size(); i++) {
            cell_h = 0f;
            List<? extends AbstractCell> dataRow = tableData.get(i);

            // Calculate the maximal height
            for (AbstractCell aDataRow : dataRow) {
                cell_h = Math.max(cell_h, aDataRow.getComputedHeight(2 * margin) + 2 * margin);
            }

            for (int j = 0; j < dataRow.size(); j++) {
                AbstractCell cell = dataRow.get(j);
                cell_w = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int k = 1; k < colspan; k++) {
                    cell_w += dataRow.get(++j).getWidth();
                }

                if (draw) {
                    page.setBrushColor(cell.getBrushColor());
                    cell.paint(page, x, y, cell_w, cell_h, margin);
                }

                x += cell_w;
            }
            x = x1;
            y += cell_h;

            // Consider the height of the next row when checking if we must go to a new page
            if (i < (tableData.size() - 1)) {
                List<? extends AbstractCell> nextRow = tableData.get(i + 1);
                for (int j = 0; j < nextRow.size(); j++) {
                    AbstractCell cell = nextRow.get(j);
                    float cellHeight = cell.getComputedHeight(2 * margin) + 2 * margin;
                    if (cellHeight > cell_h) {
                        cell_h = cellHeight;
                    }
                }
            }

            if ((y + cell_h) > (page.height - bottom_margin)) {
                if (i == tableData.size() - 1) {
                    rendered = -1;
                } else {
                    rendered = i + 1;
                    numOfPages++;
                }
                return new Point(x, y);
            }
        }

        rendered = -1;

        return new Point(x, y);
    }


    /**
     * Returns true if the table contains more data that needs to be drawn on a page.
     */
    public boolean hasMoreData() {
        return rendered != -1;
    }


    /**
     * Returns the width of this table when drawn on a page.
     *
     * @return the widht of this table.
     */
    public float getWidth() {
        float table_width = 0.0f;
        List<? extends AbstractCell> row = tableData.get(0);
        for (int i = 0; i < row.size(); i++) {
            table_width += row.get(i).getWidth();
        }
        return table_width;
    }


    /**
     * Returns the number of data rows that have been rendered so far.
     *
     * @return the number of data rows that have been rendered so far.
     */
    public int getRowsRendered() {
        return rendered == -1 ? rendered : rendered - numOfHeaderRows;
    }


    /**
     * Wraps around the text in all cells so it fits the column width,
     * but do not allow rows to be split over two pages,
     * i.e. every cell is displayed completely, it isn't interrupted by a page break in the middle.
     */
    public void wrapAroundCellTextPreventPagebreaksInRows() {
        for (List<? extends AbstractCell> row : tableData) {
            for (AbstractCell tableCell : row) {
                if (tableCell instanceof TextCell) {
                    TextCell cell = (TextCell) tableCell;
                    cell.setWordwrap(true);
                }
            }
        }
    }


    /**
     * Wraps around the text in all cells so it fits the column width.
     */
    public void wrapAroundCellText() {
        List<List<AbstractCell>> tableData2 = new ArrayList<List<AbstractCell>>();

        for (List<? extends AbstractCell> row : tableData) {
            // Computes the number of lines necessary for this row of the table.
            boolean containsImage = false;
            int maxNumVerCells = 1;
            for (AbstractCell cell : row) {
                if (cell instanceof TextCell) {
                    TextCell textCell = (TextCell) cell;
                    maxNumVerCells = Math.max(maxNumVerCells, textCell.getNumVerCells(margin));
                } else if (cell instanceof ImageCell) {
                    containsImage = true;
                }
            }

            // If one of the cells is an image cell, then the row should not be split
            // into multiple rows. Otherwise the image would be split and might be displayed
            // on different pages (one part on one page and another part on the next page )
            if (containsImage) {
                // Instead set the wordwrap flag for these rows.
                List<AbstractCell> row2 = new ArrayList<AbstractCell>();
                for (AbstractCell cell : row) {
                    if (cell instanceof TextCell) {
                        TextCell textCell = (TextCell) cell;
                        textCell.setWordwrap(true);
                    }
                    row2.add(cell);
                }
                tableData2.add(row2);
            } else {
                // Runs through these lines and creates new cells for each line.
                // So that a single cell is divided into multiple cells in the output.
                // However the text is only set for the first line (j == 0).
                for (int j = 0; j < maxNumVerCells; j++) {
                    List<AbstractCell> row2 = new ArrayList<AbstractCell>();
                    for (AbstractCell cell : row) {
                        TextCell cell2;
                        if (cell instanceof TextCell) {
                            TextCell textCell = (TextCell) cell;

                            cell2 = new TextCell(textCell.getFont(), "");
                            if (j == 0) {
                                cell2.setText(textCell.getText());
                            }
                            cell2.setPoint(cell.getPoint());
                            cell2.setCompositeTextLine(textCell.getCompositeTextLine());

                            cell2.setWidth(cell.getWidth());
                            cell2.setHeight(cell.getHeight());
                            cell2.setLineWidth(cell.getLineWidth());

                            cell2.setBgColor(cell.getBgColor());
                            cell2.setPenColor(cell.getPenColor());
                            cell2.setBrushColor(cell.getBrushColor());

                            cell2.setProperties(cell.getProperties());
                        } else {
                            cell2 = new TextCell(null);
                        }
                        row2.add(cell2);
                    }
                    tableData2.add(row2);
                }
            }
        }

        // Processes all cells of the table and writes the text that does not fit
        // into a single cell into the consecutive cells that were generated in
        // the previous step.
        for (int i = 0; i < tableData2.size(); i++) {
            List<AbstractCell> row = tableData2.get(i);
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = row.get(j);
                if (cell instanceof TextCell) {
                    TextCell textCell = (TextCell) cell;

                    if (!textCell.isWordwrap() && textCell.text != null) {
                        String[] tokens = textCell.getText().split("\\s+");
                        int n = 0;
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < tokens.length; k++) {
                            String token = tokens[k];
                            if (textCell.font.stringWidth(sb.toString() + " " + token)
                                    > (cell.getWidth() - margin)) {
                                ((TextCell) tableData2.get(i + n).get(j)).setText(sb.toString());
                                sb = new StringBuilder(token);
                                n++;
                            } else {
                                if (k > 0) {
                                    sb.append(" ");
                                }
                                sb.append(token);
                            }
                        }
                        ((TextCell) tableData2.get(i + n).get(j)).setText(sb.toString());
                    }
                }
            }
        }

        tableData = tableData2;
    }


    /**
     * Sets all table cells borders to <strong>false</strong>.
     */
    public void setNoCellBorders() {
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = tableData.get(i).get(j);
                cell.setNoBorders();
            }
        }
    }


    /**
     * Sets the color of the cell border lines.
     *
     * @param color the color of the cell border lines.
     */
    public void setCellBordersColor(int color) {
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = tableData.get(i).get(j);
                cell.setPenColor(color);
            }
        }
    }


    /**
     * Sets the width of the cell border lines.
     *
     * @param width the width of the border lines.
     */
    public void setCellBordersWidth(float width) {
        for (int i = 0; i < tableData.size(); i++) {
            List<? extends AbstractCell> row = tableData.get(i);
            for (int j = 0; j < row.size(); j++) {
                AbstractCell cell = tableData.get(i).get(j);
                cell.setLineWidth(width);
            }
        }
    }


    /**
     * Resets the rendered pages count.
     * Call this method if you have to draw this table more than one time.
     */
    public void resetRenderedPagesCount() {
        this.rendered = numOfHeaderRows;
    }

}   // End of FlexTable.java
