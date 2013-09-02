package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.Font;
import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class PDFDataExporter extends SessionAwareDataExporter {

    private static final Logger LOG = LoggerFactory.getLogger(PDFDataExporter.class);

    public PDFDataExporter(Page page) {
        super(Model.of("PDF "), "application/pdf", "pdf", page);
    }

    /**
     * Max 545
     */
    protected abstract float[] getColumnWidths();

    protected List<Cell> getEmptyRow(Font font, int size) {
        List<Cell> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Cell cell = new Cell(font, "");
            cell.setBorder(Border.LEFT, false);
            cell.setBorder(Border.RIGHT, false);
            list.add(cell);
        }
        return list;
    }

    protected String resourceKey(Enum value) {
        return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
    }

    protected abstract String getTitle();

    protected Cell getNewCell(Font font, String text) {
        Cell cell = new Cell(font, text);
        cell.setTopPadding(0.5F);
        cell.setBottomPadding(0.5F);
        cell.setLeftPadding(1.5F);
        cell.setRightPadding(1.5F);
        return cell;
    }


}
