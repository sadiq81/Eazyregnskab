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
    private static final WritableCellFormat normal = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12));

    public static void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, header);
        sheet.addCell(label);
    }

    public static void addNumber(WritableSheet sheet, int column, int row, Double amount) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, amount, normal);
        sheet.addCell(number);
    }

    public static void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, normal);
        sheet.addCell(label);
    }

    public static void addDate(WritableSheet sheet, int column, int row, Date date) throws WriteException, RowsExceededException {
        DateTime dateTime;
        dateTime = new DateTime(column, row, date);
        sheet.addCell(dateTime);
    }
}
