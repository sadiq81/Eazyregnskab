package dk.eazyit.eazyregnskab.web.components.tables.toolbar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author
 */
public class CustomExportToolbar extends ExportToolbar {

    public CustomExportToolbar(DataTable<?, ?> table, IModel<String> messageModel, IModel<String> fileNameModel, float[] columnWidths) {
        super(table, messageModel, fileNameModel);
        addDataExporter(new CSVDataExporter().setCharacterSet("UTF-8").setDataFormatNameModel(Model.of("CSV ")));
        addDataExporter(new XLSDataExporter());
        addDataExporter(new PDFDataExporter(columnWidths));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer td = (WebMarkupContainer) get("td");
        td.add(AttributeModifier.append("style", "text-align: right;"));
    }
}
