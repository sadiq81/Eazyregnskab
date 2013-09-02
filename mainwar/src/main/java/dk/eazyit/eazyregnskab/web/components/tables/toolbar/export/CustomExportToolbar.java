package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import dk.eazyit.eazyregnskab.domain.ExportTableRow;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author
 */
public abstract class CustomExportToolbar<E extends ExportTableRow> extends ExportToolbar {


    public CustomExportToolbar(ExportableDataTable<E, ?> table, IModel<String> messageModel, IModel<String> fileNameModel) {
        super(table, messageModel, fileNameModel);
        addDataExporter(new CSVDataExporter().setCharacterSet("UTF-8").setDataFormatNameModel(Model.of("CSV ")));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer td = (WebMarkupContainer) get("td");
        td.add(AttributeModifier.append("style", "text-align: right;"));
    }
}
