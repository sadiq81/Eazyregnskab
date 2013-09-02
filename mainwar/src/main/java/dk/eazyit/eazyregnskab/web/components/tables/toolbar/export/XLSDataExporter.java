package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import com.google.common.collect.Lists;
import dk.eazyit.eazyregnskab.domain.ExportTableRow;
import dk.eazyit.eazyregnskab.util.ExcelWriter;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class XLSDataExporter extends SessionAwareDataExporter {

    private static final Logger LOG = LoggerFactory.getLogger(XLSDataExporter.class);

    public XLSDataExporter(Page page) {
        super(Model.of("XLS "), "application/vnd.ms-excel", "xls", page);
    }

    protected String getTitle() {
        return new StringResourceModel(page.getClass().getSimpleName() + ".report.title", new Model(getCurrentLegalEntity())).getObject();
    }

    @Override
    public <T> void exportData(IDataProvider<T> dataProvider, List<IExportableColumn<T, ?, ?>> columns, OutputStream outputStream) throws IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
        WritableSheet sheet = workbook.createSheet(new ResourceModel(page.getClass().getSimpleName() + ".datatable.export-file-name").getObject(), 0);

        try {
            ExcelWriter.addCaption(sheet, 0, 0, getTitle());

            for (int i = 0; i < columns.size(); i++) {
                ExcelWriter.addCaption(sheet, i, 1, columns.get(i).getDisplayModel().getObject());
                sheet.mergeCells(0, 0, columns.size() - 1, 0);
            }

            List<T> rows = Lists.newArrayList(dataProvider.iterator(0, dataProvider.size()));
            for (int row = 0; row < rows.size(); row++) {

                ExportTableRow<T> currentRow = (ExportTableRow<T>) rows.get(row);

                for (int column = 0; column < columns.size(); column++) {

                    Object o = columns.get(column).getDataModel(dataProvider.model((T) currentRow)).getObject();

                    if (o != null) {
                        if (o instanceof Number) {
                            ExcelWriter.addNumber(sheet, currentRow.getCellFormat(), column, row + 2, new Double(o.toString()));
                        } else if (o instanceof Date) {
                            ExcelWriter.addDate(sheet, currentRow.getCellFormat(), column, row + 2, (Date) o);
                        } else if (o instanceof Enum) {
                            ExcelWriter.addLabel(sheet, currentRow.getCellFormat(), column, row + 2, new ResourceModel(resourceKey((Enum) o)).getObject());
                        } else {
                            String s;
                            IConverter converter = Application.get().getConverterLocator().getConverter(o.getClass());
                            if (converter == null) {
                                s = o.toString();
                            } else {
                                s = converter.convertToString(o, Session.get().getLocale());
                            }
                            ExcelWriter.addLabel(sheet, currentRow.getCellFormat(), column, row + 2, s);

                        }
                    }
                }
            }
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            LOG.warn("Something went wrong with export to xls" + getTitle());
        } finally {
            outputStream.close();
        }
    }

}
