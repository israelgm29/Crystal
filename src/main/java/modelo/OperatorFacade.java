/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Operator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jhona
 */
@Stateless
public class OperatorFacade extends AbstractFacade<Operator> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OperatorFacade() {
        super(Operator.class);
    }
    
    public Operator findByLog(String username , String password){
        Query query = em.createNamedQuery("Operator.findByLogin",Operator.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<Operator> lista = query.getResultList();
        
        if (!lista.isEmpty()) {
            return lista.get(0);
        }else{
        return null;
        }
    }
    
}
