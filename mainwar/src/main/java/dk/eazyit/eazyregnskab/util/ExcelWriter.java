package dk.eazyit.eazyregnskab.util;

import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.util.Date;

/**
 * @author
 */
public class ExcelWriter {

    private static final WritableCellFormat header = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));


    public static void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, header);
        sheet.addCell(label);
    }

    public static void addNumber(WritableSheet sheet, WritableCellFormat format, int column, int row, Double amount) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, amount, format);
        sheet.addCell(number);
    }

    public static void addLabel(WritableSheet sheet, WritableCellFormat format, int column, int row, String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, format);
        sheet.addCell(label);
    }

    public static void addDate(WritableSheet sheet, WritableCellFormat format, int column, int row, Date date, String pattern) throws WriteException, RowsExceededException {
        DateTime dateTime;
        WritableFont font = new WritableFont(format.getFont());
        DateFormat customDateFormat = new DateFormat(pattern);
        WritableCellFormat cellFormat = new WritableCellFormat(font, customDateFormat);
        dateTime = new DateTime(column, row, date, cellFormat);
        sheet.addCell(dateTime);
    }
}
