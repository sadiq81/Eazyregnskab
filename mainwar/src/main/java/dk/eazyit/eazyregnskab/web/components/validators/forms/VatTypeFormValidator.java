package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.VatType;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author
 */
public class VatTypeFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public VatTypeFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = getDependentFormComponents()[0];
        VatType vatType = (VatType) formComponent1.getForm().getModelObject();

        if (vatType.getId() == 0 && vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_NAME_AND_LEGAL_ENTITY,
                formComponent1.getInput(),
                getCurrentLegalEntity()).size() > 0) {
            error(formComponent1, "name.all.ready.exists");
        }
        if (formComponent1.getInput().length() > 50) {
            error(formComponent1, "name.to.long");
        }
    }
}
