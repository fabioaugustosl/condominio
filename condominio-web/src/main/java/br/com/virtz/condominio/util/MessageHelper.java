package br.com.virtz.condominio.util;

import java.io.Serializable;

public interface MessageHelper extends Serializable {

	public void addInfo(String msg);

	public void addWarn(String msg);

	public void addError(String msg);

	public void addFatal(String msg);

}
