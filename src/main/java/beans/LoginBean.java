/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import entidades.Operator;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelo.OperatorFacade;

/**
 *
 * @author jhona
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    @EJB
    private OperatorFacade of;
    private Operator usuariologeado;

    public LoginBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Operator getUsuariologeado() {
        return usuariologeado;
    }

    public void setUsuariologeado(Operator usuariologeado) {
        this.usuariologeado = usuariologeado;
    }

    public String autenticar() {

        usuariologeado = of.findByLog(username, password);
        FacesContext context = FacesContext.getCurrentInstance();

        if (usuariologeado == null) {
            context.addMessage(null, new FacesMessage("El usuario no existe, registrese para tener acceso al sistema"));
            username = null;
            password = null;
            return null;
        } else {
            if (usuariologeado.getPassword().equals(password)) {
                context.getExternalContext().getSessionMap().put("user", usuariologeado);

                return "index?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Usuario o contraseña mal ", "Usuario o contrasela"));
            }
        }
        return null;

    }

    public boolean getHasMessage() {
        Iterator<FacesMessage> iterator = FacesContext.getCurrentInstance().getMessages();
        return (iterator != null) && (iterator.hasNext());
    }

    public String getErrorMessage() {
        Iterator<FacesMessage> iterator = FacesContext.getCurrentInstance().getMessages();
        return iterator.hasNext() ? iterator.next().getSummary() : "";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

}
