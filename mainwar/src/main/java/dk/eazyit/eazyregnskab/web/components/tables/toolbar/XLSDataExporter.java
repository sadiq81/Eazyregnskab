package dk.eazyit.eazyregnskab.web.components.tables.toolbar;

import com.google.common.collect.Lists;
import dk.eazyit.eazyregnskab.util.ExcelWriter;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class XLSDataExporter extends SessionAwareDataExporter {


    private static final Logger LOG = LoggerFactory.getLogger(XLSDataExporter.class);


    /**
     * Creates a new instance with the data format name model, content type and file name extensions provided.
     *
     * @param dataFormatNameModel The model of the exported data format name.
     * @param contentType         The MIME content type of the exported data type.
     * @param fileNameExtension   The file name extensions to use in the file name for the exported data.
     */
    public XLSDataExporter() {
        super(Model.of("XLS "), "application/vnd.ms-excel", "xls");
    }

    @Override
    public <T> void exportData(IDataProvider<T> dataProvider, List<IExportableColumn<T, ?, ?>> columns, OutputStream outputStream) throws IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
        WritableSheet sheet = workbook.createSheet(new ResourceModel("ChartOfAccountsPage.datatable.export-file-name").getObject(), 0);


        try {

            for (int i = 0; i < columns.size(); i++) {
                ExcelWriter.addCaption(sheet, i, 0, columns.get(i).getDisplayModel().getObject());
            }

            List<T> rows = Lists.newArrayList(dataProvider.iterator(0, dataProvider.size()));
            for (int row = 0; row < rows.size(); row++) {
                T currentRow = rows.get(row);
                for (int column = 0; column < columns.size(); column++) {

                    Object o = columns.get(column).getDataModel(dataProvider.model(currentRow)).getObject();

                    if (o != null) {

                        if (o instanceof Number) {
                            ExcelWriter.addNumber(sheet, column, row + 1, new Integer(o.toString()));
                        } else if (o instanceof Date) {
                            ExcelWriter.addDate(sheet, column, row + 1, (Date) o);
                        } else if (o instanceof Enum) {
                            ExcelWriter.addLabel(sheet, column, row + 1, new ResourceModel(resourceKey((Enum) o)).getObject());
                        } else {
                            Class<?> c = o.getClass();
                            String s;
                            IConverter converter = Application.get().getConverterLocator().getConverter(c);
                            if (converter == null) {
                                s = o.toString();
                            } else {
                                s = converter.convertToString(o, Session.get().getLocale());
                            }
                            ExcelWriter.addLabel(sheet, column, row + 1, s);

                        }
                    }
                }
            }
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            LOG.warn("Something went wrong with export to xls" + e.toString());
        } finally {
            outputStream.close();
        }
    }

    protected String resourceKey(Enum value) {
        return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
    }
}
