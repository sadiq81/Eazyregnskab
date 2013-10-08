package dk.eazyit.eazyregnskab.web.components.modal.chartOfAccountsPage;

import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.services.importer.ImportParser;
import dk.eazyit.eazyregnskab.services.importer.financeaccount.FinanceAccountLine;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.form.FileUploadForm;
import dk.eazyit.eazyregnskab.web.components.lists.listeditor.ListEditor;
import dk.eazyit.eazyregnskab.web.components.lists.listeditor.ListItem;
import dk.eazyit.eazyregnskab.web.components.lists.listeditor.RemoveButton;
import dk.eazyit.eazyregnskab.web.components.modal.wizard.StepPanel;
import dk.eazyit.eazyregnskab.web.components.modal.wizard.WizardModal;
import dk.eazyit.eazyregnskab.web.components.modal.wizard.WizardPanel;
import dk.eazyit.eazyregnskab.web.components.models.file.FinanceAccountExample;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class ImportFinanceAccountsWizard extends WizardModal {

    File importedFile;
    List<FinanceAccountLine> lines = new ArrayList();

    public ImportFinanceAccountsWizard(String id) {
        super(id);
    }

    @Override
    public WizardPanel getPanel(String id) {
        return new ImportFinanceAccountsWizardPanel(id, this);
    }

    @Override
    public void reset() {
        super.reset();
        importedFile = null;
        lines = new ArrayList();
    }

    class ImportFinanceAccountsWizardPanel extends WizardPanel {

        ImportFinanceAccountsWizardPanel(String id, WizardModal modal) {
            super(id, modal);
            addPanel(new Step1(new ResourceModel("ChartOfAccountsPage.wizard.introduction"), new ResourceModel("ChartOfAccountsPage.how.to.import.finance.accounts")));
            addPanel(new Step2(new ResourceModel("ChartOfAccountsPage.wizard.fileupload"), Model.of("")));
            addPanel(new Step3(new ResourceModel("ChartOfAccountsPage.wizard.adjust"), Model.of("")));
            addPanel(new Step4(new ResourceModel("ChartOfAccountsPage.wizard.approve"), Model.of("")));
        }
    }

    class Step1 extends StepPanel {
        Step1(IModel<String> title, IModel<String> summary) {
            super(title, summary);
            add(new DownloadLink("example", new FinanceAccountExample()));
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);
            response.render(OnDomReadyHeaderItem.forScript("Wicket.Window.unloadConfirmation = false;"));
        }
    }

    class Step2 extends StepPanel {

        Step2(IModel<String> title, IModel<String> summary) {
            super(title, summary);
            add(new FileUploadForm("upload") {
                @Override
                protected void onFileUpload(AjaxRequestTarget target, File file) {
                    importedFile = file;
                    getPanel().getForm().buttonPressed("next", target);
                }
            });
        }

        @Override
        public boolean isNextAvailable() {
            return false;
        }
    }

    class Step3 extends StepPanel {

        @SpringBean
        ImportParser importParser;
        ListEditor<FinanceAccountLine> listEditor;

        Step3(IModel<String> title, IModel<String> summary) {
            super(title, summary);
            add(listEditor = new ListEditor<FinanceAccountLine>("financeAccounts", new FinanceAccountListModel()) {
                @Override
                protected void onPopulateItem(ListItem<FinanceAccountLine> item) {
                    item.add(new RemoveButton("remove") {
                        @Override
                        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            super.onSubmit(target, form);
                            target.add(getPanel());
                        }
                    });
                    item.add(new TextField<Integer>("accountNumber").setRequired(true));
                    item.add(new TextField<String>("accountName").setRequired(true));
                    item.add(new TextField<Integer>("accountFrom"));
                    item.add(new TextField<Integer>("accountTo"));
                    item.add(new EnumDropDownChoice<>("type", FinanceAccountType.getNonSystemAccounts()).setRequired(true));
                }
            });
        }

        @Override
        public void onNext(AjaxRequestTarget target) {
            lines = listEditor.getItems();
        }

        class FinanceAccountListModel implements IModel<List<FinanceAccountLine>> {
            @Override
            public List<FinanceAccountLine> getObject() {
                return importParser.parseImportedFinanceAccountsXls(importedFile);
            }

            @Override
            public void setObject(List<FinanceAccountLine> object) {
            }

            @Override
            public void detach() {
            }
        }
    }

    class Step4 extends StepPanel {

        Step4(IModel<String> title, IModel<String> description) {
            super(title, description);
        }
    }
}


