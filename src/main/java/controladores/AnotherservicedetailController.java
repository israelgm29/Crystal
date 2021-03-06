package controladores;

import entidades.Anotherservicedetail;
import controladores.util.JsfUtil;
import controladores.util.PaginationHelper;
import modelo.AnotherservicedetailFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("anotherservicedetailController")
@SessionScoped
public class AnotherservicedetailController implements Serializable {

    private Anotherservicedetail current;
    private DataModel items = null;
    @EJB
    private modelo.AnotherservicedetailFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AnotherservicedetailController() {
    }

    public Anotherservicedetail getSelected() {
        if (current == null) {
            current = new Anotherservicedetail();
            current.setAnotherservicedetailPK(new entidades.AnotherservicedetailPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private AnotherservicedetailFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Anotherservicedetail) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Anotherservicedetail();
        current.setAnotherservicedetailPK(new entidades.AnotherservicedetailPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getAnotherservicedetailPK().setInvoiceid(current.getInvoice().getId());
            current.getAnotherservicedetailPK().setAnotherserviceid(current.getAnotherservice().getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AnotherservicedetailCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Anotherservicedetail) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getAnotherservicedetailPK().setInvoiceid(current.getInvoice().getId());
            current.getAnotherservicedetailPK().setAnotherserviceid(current.getAnotherservice().getId());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AnotherservicedetailUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Anotherservicedetail) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AnotherservicedetailDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Anotherservicedetail getAnotherservicedetail(entidades.AnotherservicedetailPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Anotherservicedetail.class)
    public static class AnotherservicedetailControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AnotherservicedetailController controller = (AnotherservicedetailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "anotherservicedetailController");
            return controller.getAnotherservicedetail(getKey(value));
        }

        entidades.AnotherservicedetailPK getKey(String value) {
            entidades.AnotherservicedetailPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entidades.AnotherservicedetailPK();
            key.setId(Integer.parseInt(values[0]));
            key.setInvoiceid(Integer.parseInt(values[1]));
            key.setAnotherserviceid(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(entidades.AnotherservicedetailPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getId());
            sb.append(SEPARATOR);
            sb.append(value.getInvoiceid());
            sb.append(SEPARATOR);
            sb.append(value.getAnotherserviceid());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Anotherservicedetail) {
                Anotherservicedetail o = (Anotherservicedetail) object;
                return getStringKey(o.getAnotherservicedetailPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Anotherservicedetail.class.getName());
            }
        }

    }

}
