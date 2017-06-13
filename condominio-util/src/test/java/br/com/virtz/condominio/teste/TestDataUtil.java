package br.com.virtz.condominio.teste;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import br.com.virtz.boleto.util.DataUtil;

public class TestDataUtil {
	
	
	DataUtil dataUtil = new DataUtil();

	@Test
	public void testDiferencaDiasNoMesmoMes() {
		Date d1 = new Date(2012, 8, 3);
		Date d2 = null;
		
		Assert.assertEquals(0, dataUtil.diasEntreDatas(d1, d2));
		
		d1 = new Date(2012, 8, 3);
		d2 = new Date(2012, 8, 8);
		
		Assert.assertEquals(5, dataUtil.diasEntreDatas(d1, d2));
	}
	
	
	@Test
	public void testDiferencaDiasEntreMeses() {
		Date d1 = new Date(2012, 8, 3);
		Date d2 = new Date(2012, 9, 8);
		
		Assert.assertEquals(35, dataUtil.diasEntreDatas(d1, d2));
		
		
		d1 = new Date(2012, 8, 3);
		d2 = new Date(2012, 10, 13);
		
		Assert.assertEquals(71, dataUtil.diasEntreDatas(d1, d2));
	}


	@Test
	public void testDiferencaDiasEntreAnos() {
		Date d1 = new Date(2012, 8, 3);
		Date d2 = new Date(2013, 9, 8);
		
		Assert.assertEquals(400, dataUtil.diasEntreDatas(d1, d2));
		
		
		d1 = new Date(2012, 8, 3);
		d2 = new Date(2013, 10, 13);
		
		Assert.assertEquals(435, dataUtil.diasEntreDatas(d1, d2));
	}
	
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
