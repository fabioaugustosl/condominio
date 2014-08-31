package br.com.virtz.condominio.dao;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DAO {
	
   @SuppressWarnings("unused")
   @Produces
   @PersistenceContext
   private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}
   
}
