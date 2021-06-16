/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import entidades.Measurer;
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
public class MeasurerFacade extends AbstractFacade<Measurer> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MeasurerFacade() {
        super(Measurer.class);
    }

    public Measurer findByNumber(String number) {
        Query query = em.createNamedQuery("Measurer.findByNumber", Measurer.class);
        query.setParameter("number", number);
        List<Measurer> lista = query.getResultList();

        if (!lista.isEmpty()) {
            return lista.get(0);
        } else {
            return null;
        }
    }
}
