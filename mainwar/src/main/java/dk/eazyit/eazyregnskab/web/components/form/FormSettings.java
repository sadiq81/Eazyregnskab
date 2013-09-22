package dk.eazyit.eazyregnskab.web.components.form;

import java.io.Serializable;

/**
 * @author
 */
public class FormSettings implements Serializable {

    private boolean confirmSave = false;
    private boolean confirmDelete = true;
    private boolean confirmNew = false;

    private boolean newVisible = false;
    private boolean deleteVisible = false;

    boolean isConfirmSave() {
        return confirmSave;
    }

    public FormSettings setConfirmSave(boolean confirmSave) {
        this.confirmSave = confirmSave;
        return this;
    }

    boolean isConfirmDelete() {
        return confirmDelete;
    }

    public FormSettings setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
        return this;
    }

    boolean isConfirmNew() {
        return confirmNew;
    }

    public FormSettings setConfirmNew(boolean confirmNew) {
        this.confirmNew = confirmNew;
        return this;
    }

    boolean isNewVisible() {
        return newVisible;
    }

    public FormSettings setNewVisible(boolean newVisible) {
        this.newVisible = newVisible;
        return this;
    }

    boolean isDeleteVisible() {
        return deleteVisible;
    }

    public FormSettings setDeleteVisible(boolean deleteVisible) {
        this.deleteVisible = deleteVisible;
        return this;
    }
}