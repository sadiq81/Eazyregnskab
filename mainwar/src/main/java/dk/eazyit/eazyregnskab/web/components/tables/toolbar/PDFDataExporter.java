package dk.eazyit.eazyregnskab.web.components.tables.toolbar;

import com.google.common.collect.Lists;
import com.pdfjet.*;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.IExportableColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
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
    float[] columnWidths;
    ExportableDataTable table;


    /**
     * Creates a new instance with the data format name model, content type and file name extensions provided.
     *
     * @param dataFormatNameModel The model of the exported data format name.
     * @param contentType         The MIME content type of the exported data type.
     * @param fileNameExtension   The file name extensions to use in the file name for the exported data.
     */
    public PDFDataExporter(float[] columnWidths, ExportableDataTable table) {
        super(Model.of("PDF "), "application/pdf", "pdf");
        float totalWidth = 0;
        for (float width : columnWidths) {
            totalWidth += width;
            if (totalWidth > 545) throw new NullPointerException("coloumns are wider than page");
        }
        this.columnWidths = columnWidths;
        this.table = table;
    }

    @Override
    public <T> void exportData(IDataProvider<T> dataProvider, List<IExportableColumn<T, ?, ?>> columns, OutputStream outputStream) throws IOException {

        try {
            PDF pdf = new PDF(outputStream);
            Page page = new Page(pdf, A4.PORTRAIT);


            Font headerFont = new Font(pdf, CoreFont.TIMES_BOLD);
            headerFont.setSize(10F);
            Font normalFont = new Font(pdf, CoreFont.TIMES_ROMAN);
            normalFont.setSize(8F);

            TextLine legalEntity = new TextLine(headerFont, table.getTitle());
            legalEntity.setLocation(25F, 15F);
            legalEntity.drawOn(page);

            Table table = new Table();
            List<List<Cell>> tableData = new ArrayList<List<Cell>>();

            List<Cell> headerDate = new ArrayList<Cell>();
            tableData.add(headerDate);
            for (int i = 0; i < columns.size(); i++) {
                headerDate.add(new Cell(headerFont, columns.get(i).getDisplayModel().getObject()));
            }
            List<T> rows = Lists.newArrayList(dataProvider.iterator(0, dataProvider.size()));
            for (int row = 0; row < rows.size(); row++) {
                T currentRow = rows.get(row);
                List<Cell> rowDate = new ArrayList<Cell>();
                for (int column = 0; column < columns.size(); column++) {

                    Object o = columns.get(column).getDataModel(dataProvider.model(currentRow)).getObject();

                    if (o != null) {

                        if (o instanceof Enum) {
                            rowDate.add(new Cell(normalFont, new ResourceModel(resourceKey((Enum) o)).getObject()));

                        } else if (o instanceof Date) {
                            IConverter converter = Application.get().getConverterLocator().getConverter(Date.class);
                            String s = converter.convertToString(o, Session.get().getLocale());
                            rowDate.add(new Cell(normalFont, s));
                        } else {
                            Class<?> c = o.getClass();
                            String s;
                            IConverter converter = Application.get().getConverterLocator().getConverter(c);
                            if (converter == null) {
                                s = o.toString();
                            } else {
                                s = converter.convertToString(o, Session.get().getLocale());
                            }
                            rowDate.add(new Cell(normalFont, s));
                        }
                    } else {
                        rowDate.add(new Cell(normalFont, ""));
                    }
                }
                tableData.add(rowDate);
            }

            table.setData(tableData, Table.DATA_HAS_1_HEADER_ROWS);

            for (int width = 0; width < columnWidths.length; width++) {
                table.setColumnWidth(width, columnWidths[width]);
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
                page = new Page(pdf, A4.PORTRAIT);
            }

            new ResourceModel("ChartOfAccountsPage.datatable.export-file-name").getObject();
            pdf.flush();

        } catch (Exception e) {
            LOG.warn("Something went wrong with export to pdf" + e.toString());
        } finally {
            outputStream.close();
        }
    }

    protected String resourceKey(Enum value) {
        return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
    }
}
