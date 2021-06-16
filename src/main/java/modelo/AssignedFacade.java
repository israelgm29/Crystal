/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Assigned;
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
public class AssignedFacade extends AbstractFacade<Assigned> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AssignedFacade() {
        super(Assigned.class);
    }
     public Assigned findByNumber(int id){
        Query query = em.createNamedQuery("Assigned.findByMeasurerid",Assigned.class);
        query.setParameter("measurerid", id);
        List<Assigned> lista = query.getResultList();
        
        if (!lista.isEmpty()) {
            return lista.get(0);
        }else{
        return null;
        }
    }
}
