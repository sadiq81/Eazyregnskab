package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.extensions.javascript.jasny.FileUploadField;
import dk.eazyit.eazyregnskab.util.FileReader;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
public abstract class FileUploadForm extends BaseForm {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadForm.class);
    private FileUploadField fileUploadField;
    private static List<String> allowedTypes = new ArrayList<>(Arrays.asList("xls", "png"));
    @SpringBean
    FileReader fileReader;

    /**
     * Construct.
     *
     * @param name Component name
     */
    public FileUploadForm(String name) {
        super(name);
        setMultiPart(true);
        add(fileUploadField = (FileUploadField) new FileUploadField("fileInput").setRequired(true));
        setMaxSize(Bytes.megabytes(2));
        add(new IndicatingAjaxButton("submit", this) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                try {
                    FileUpload uploadedFile = fileUploadField.getFileUpload();
                    if (uploadedFile != null) {
                        LOG.info("uploaded file: " + uploadedFile.getClientFileName() + " - " + getCurrentLegalEntity().getName());
                        File file = new File(uploadedFile.getClientFileName());
                        file.createNewFile();
                        uploadedFile.writeTo(file);
                        if (allowedTypes.contains(fileReader.getExtensionFromFile(file))) {
                            onFileUpload(target, file);
                        } else {
                            uploadedFile.delete();
                        }
                    }
                } catch (Exception e) {
                    LOG.error("File upload error: ");
                }
            }
        });
    }

    protected abstract void onFileUpload(AjaxRequestTarget target, File file);

}

