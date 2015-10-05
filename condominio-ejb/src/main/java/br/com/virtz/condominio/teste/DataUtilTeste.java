package br.com.virtz.condominio.teste;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import br.com.virtz.condominio.geral.DataUtil;

public class DataUtilTeste {

	@Test
	public void testeAdicionarDiasDentroDoMes() {
		DataUtil dt = new DataUtil();
		Calendar cUtil = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 10);
		c.set(Calendar.MONDAY, 8);
		c.set(Calendar.YEAR, 2013);
		
		Date dataMais2 = dt.adicionarDias(c.getTime(), 2);
		cUtil.setTime(dataMais2);
		Assert.assertEquals(12, cUtil.get(Calendar.DATE));
		Assert.assertEquals(8, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2013, cUtil.get(Calendar.YEAR));
		
		Date dataMais15 = dt.adicionarDias(c.getTime(), 15);
		cUtil.setTime(dataMais15);
		Assert.assertEquals(25, cUtil.get(Calendar.DATE));
		Assert.assertEquals(8, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2013, cUtil.get(Calendar.YEAR));
		
	}
	
	
	@Test
	public void testeAdicionarDiasDentroDoAno() {
		DataUtil dt = new DataUtil();
		Calendar cUtil = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 10);
		c.set(Calendar.MONDAY, 8);
		c.set(Calendar.YEAR, 2013);
		
		Date dataMais2 = dt.adicionarDias(c.getTime(), 60);
		cUtil.setTime(dataMais2);
		Assert.assertEquals(9, cUtil.get(Calendar.DATE));
		Assert.assertEquals(10, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2013, cUtil.get(Calendar.YEAR));
		
		Date dataMais15 = dt.adicionarDias(c.getTime(), 96);
		cUtil.setTime(dataMais15);
		Assert.assertEquals(15, cUtil.get(Calendar.DATE));
		Assert.assertEquals(11, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2013, cUtil.get(Calendar.YEAR));
		
	}
	
	
	@Test
	public void testeAdicionarDiasMudandoDeAno() {
		DataUtil dt = new DataUtil();
		Calendar cUtil = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 10);
		c.set(Calendar.MONDAY, 8);
		c.set(Calendar.YEAR, 2013);
		
		Date dataMais2 = dt.adicionarDias(c.getTime(), 180);
		cUtil.setTime(dataMais2);
		Assert.assertEquals(9, cUtil.get(Calendar.DATE));
		Assert.assertEquals(2, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2014, cUtil.get(Calendar.YEAR));
		
		Date dataMais15 = dt.adicionarDias(c.getTime(), 210);
		cUtil.setTime(dataMais15);
		Assert.assertEquals(8, cUtil.get(Calendar.DATE));
		Assert.assertEquals(3, cUtil.get(Calendar.MONTH));
		Assert.assertEquals(2014, cUtil.get(Calendar.YEAR));
		
	}

}
