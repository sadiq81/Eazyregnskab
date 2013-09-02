package dk.eazyit.eazyregnskab.domain;

import com.pdfjet.Font;
import com.pdfjet.PDF;
import jxl.write.WritableCellFormat;

/**
 * @author
 */
public interface ExportTableRow<T> {

    public String getCssClassForDataTable();

    public Font getFont(PDF pdf) throws Exception;

    public WritableCellFormat getCellFormat();

    public boolean insertSpaceAfterRowInTables();

    public float[] getPageSize();
}
