package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import com.google.common.collect.Lists;
import com.pdfjet.*;
import dk.eazyit.eazyregnskab.domain.ExportTableRow;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class PDFDataExporter extends SessionAwareDataExporter {

    private static final Logger LOG = LoggerFactory.getLogger(PDFDataExporter.class);

    public PDFDataExporter(Page page) {
        super(Model.of("PDF "), "application/download", "pdf", page);
    }

    protected float[] getColumnWidths() {
        String[] columns = new ResourceModel(page.getClass().getSimpleName() + ".columns.width").getObject().split(",");
        float[] widths = new float[columns.length];
        for (int i = 0; i < columns.length; i++) {
            widths[i] = new Float(columns[i]);
        }
        return widths;
    }

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

    protected String getTitle() {
        return new StringResourceModel(page.getClass().getSimpleName() + ".report.title", new Model(getCurrentLegalEntity())).getObject();
    }

    protected float[] getPageSize() {
        String[] sizes = new ResourceModel(page.getClass().getSimpleName() + ".page.size").getObject().split(",");
        return new float[]{new Float(sizes[0]), new Float(sizes[1])};
    }


    protected Cell getNewCell(Font font, String text) {
        Cell cell = new Cell(font, text);
        cell.setTopPadding(0.5F);
        cell.setBottomPadding(0.5F);
        cell.setLeftPadding(1.5F);
        cell.setRightPadding(1.5F);
        return cell;
    }

    @Override
    public <T> void exportData(IDataProvider<T> dataProvider, List<IExportableColumn<T, ?, ?>> columns, OutputStream outputStream) throws IOException {

        try {

            PDF pdf = new PDF(outputStream);
            com.pdfjet.Page page = new com.pdfjet.Page(pdf, getPageSize());

            Font headerFont = new Font(pdf, CoreFont.TIMES_BOLD);
            headerFont.setSize(14F);

            TextLine legalEntity = new TextLine(headerFont, getTitle());
            legalEntity.setLocation(25F, 15F);
            legalEntity.drawOn(page);

            Table table = new Table();

            List<List<Cell>> tableData = new ArrayList<>();

            List<Cell> headerDate = new ArrayList<>();
            tableData.add(headerDate);

            //Creating header line
            for (int i = 0; i < columns.size(); i++) {
                headerDate.add(new Cell(headerFont, columns.get(i).getDisplayModel().getObject()));
            }

            //Create rows
            List<T> rows = Lists.newArrayList(dataProvider.iterator(0, dataProvider.size()));
            for (int row = 0; row < rows.size(); row++) {

                ExportTableRow<T> currentRow = (ExportTableRow<T>) rows.get(row);
                Font font = currentRow.getFont(pdf);

                //Create columns in rows
                List<Cell> rowData = new ArrayList<>();
                for (int column = 0; column < columns.size(); column++) {

                    //Determine type of column for formatting
                    Object o = columns.get(column).getDataModel(dataProvider.model((T) currentRow)).getObject();

                    if (o == null) {
                        rowData.add(getNewCell(font, "")); //Headlines and Categories don't have sum
                    } else if (o instanceof Integer) {
                        rowData.add(getNewCell(font, o.toString()));
                    } else if (o instanceof Double) {
                        rowData.add(getNewCell(font, Application.get().getConverterLocator().getConverter(Double.class).convertToString((Double) o, Session.get().getLocale())));
                    } else if (o instanceof String) {
                        rowData.add(getNewCell(font, o.toString()));
                    } else if (o instanceof Date) {
                        rowData.add(getNewCell(font, Application.get().getConverterLocator().getConverter(Date.class).convertToString((Date) o, Session.get().getLocale())));
                    } else if (o instanceof Enum) {
                        rowData.add(getNewCell(font, new ResourceModel(resourceKey((Enum) o)).getObject()));
                    } else {
                        String s;
                        IConverter converter = Application.get().getConverterLocator().getConverter(o.getClass());
                        if (converter == null) {
                            s = o.toString();
                        } else {
                            s = converter.convertToString(o, Session.get().getLocale());
                        }
                        rowData.add(getNewCell(font, s));
                    }
                }

                tableData.add(rowData);

                //Some rows need space after them
                if (currentRow.insertSpaceAfterRowInTables()) {
                    tableData.add(getEmptyRow(headerFont, columns.size()));
                }
            }

            table.setData(tableData, Table.DATA_HAS_1_HEADER_ROWS);
            for (int width = 0; width < getColumnWidths().length; width++) {
                table.setColumnWidth(width, getColumnWidths()[width]);
            }

            table.setPosition(25.0f, 30.0f);
            table.rightAlignNumbers();

            while (true) {
                Point point = table.drawOn(page);
                // TO DO: Draw "Page 1 of N" here
                if (!table.hasMoreData()) {
                    // Allow the table to be drawn again later:
                    table.resetRenderedPagesCount();
                    break;
                }
                page = new com.pdfjet.Page(pdf, A4.PORTRAIT);
            }
            pdf.flush();
        } catch (Exception e) {
            LOG.warn("Something went wrong with export to pdf " + getTitle());
        } finally {
            outputStream.close();
        }
    }

}
