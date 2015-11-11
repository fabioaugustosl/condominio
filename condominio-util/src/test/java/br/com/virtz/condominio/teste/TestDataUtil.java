package br.com.virtz.condominio.teste;

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
		
		Assert.assertEquals(70, dataUtil.diasEntreDatas(d1, d2));
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
}
