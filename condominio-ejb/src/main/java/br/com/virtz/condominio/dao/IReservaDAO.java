package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Reserva;

@Local
public interface IReservaDAO extends CrudDAO<Reserva> {
}
