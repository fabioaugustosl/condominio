package br.com.virtz.condominio.entidades;

public abstract class Entidade {
	public abstract Long getId();

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		
		if (!(obj instanceof Entidade))
			return false;

		if (obj == this)
			return true;

		
		return this.getId().equals(((Entidade)obj).getId());
	}
	
	@Override
	public int hashCode() {
	    int hash = 7;
	    hash = 23 * hash + (this.getId()!= null ? this.getId().hashCode() : 0);
	    return hash;
	}
}
