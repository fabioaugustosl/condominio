package br.com.virtz.condominio.teste;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.CategoriaItemBalanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.geral.Demonstrativo;


public class TestBalancoPorCategoria {

	@Test
	public void testeAgruparCategorias() {
		
		Balanco balanco = getFixturesBalanco();
		Demonstrativo demo = new Demonstrativo(balanco);
		demo.setDespesas(balanco.getItens());
		demo.setReceitas(getReceitasFIxtures());
		
		
		Assert.assertEquals(4, balanco.getItens().size());
		Assert.assertEquals(4, demo.getDespesas().size());
		Assert.assertEquals(2, demo.getReceitas().size());
		
		
		Assert.assertEquals(3, demo.getCategoriasComItensDespesa().size());
		Assert.assertEquals(2, demo.getCategoriasComItensReceita().size());
		
		Assert.assertTrue(existeCategoriaComEsseNome(demo.getCategoriasComItensDespesa(), "Trabalhista"));
		Assert.assertTrue(existeCategoriaComEsseNome(demo.getCategoriasComItensDespesa(), "Dispendios gerais"));
		Assert.assertTrue(existeCategoriaComEsseNome(demo.getCategoriasComItensDespesa(), "OUTROS"));
		
		Assert.assertFalse(existeCategoriaComEsseNome(demo.getCategoriasComItensDespesa(), "Bunda"));
		
		Assert.assertTrue(existeCategoriaComEsseNome(demo.getCategoriasComItensReceita(), "INGRESSOS"));
		Assert.assertTrue(existeCategoriaComEsseNome(demo.getCategoriasComItensReceita(), "OUTROS"));
		Assert.assertFalse(existeCategoriaComEsseNome(demo.getCategoriasComItensReceita(), ""));
		
	}
	
	@Test
	public void testeSomatorios() {
		
		Balanco balanco = getFixturesBalanco();
		Demonstrativo demo = new Demonstrativo(balanco);
		demo.setDespesas(balanco.getItens());
		demo.setReceitas(getReceitasFIxtures());
		
		
		Assert.assertEquals(820.6d, demo.getTotalDespesas());
		Assert.assertEquals(1200.0d, demo.getTotalReceitas());
		
		CategoriaItemBalanco c1 = getCategoriaComEsseNome(demo.getCategoriasComItensDespesa(), "Trabalhista");
		Assert.assertEquals(260.6d, c1.getTotalCategoria());

	}
	
	
	

	private boolean existeCategoriaComEsseNome(List<CategoriaItemBalanco> categs, String nome) {
		for(CategoriaItemBalanco c :categs){
			if(nome.equals(c.getNome())){
				return true;
			}
		}
		return false;
	}
	
	private CategoriaItemBalanco getCategoriaComEsseNome(List<CategoriaItemBalanco> categs, String nome) {
		for(CategoriaItemBalanco c :categs){
			if(nome.equals(c.getNome())){
				return c;
			}
		}
		return null;
	}

	private Balanco getFixturesBalanco() {
		CategoriaItemBalanco c1 =new CategoriaItemBalanco();
		c1.setAtiva(true);
		c1.setId(1l);
		c1.setNome("Trabalhista");
		c1.setTipoBalanco(EnumTipoBalanco.DESPESA);
		
		CategoriaItemBalanco c2 =new CategoriaItemBalanco();
		c2.setAtiva(true);
		c2.setId(2l);
		c2.setNome("Dispendios gerais");
		c2.setTipoBalanco(EnumTipoBalanco.DESPESA);
		
		
		
		
		Balanco balanco = new Balanco();
		ItemBalanco item1 = new ItemBalanco();
		item1.setDescricao("CEMIG");
		item1.setCategoria(c1);
		item1.setData(new Date());
		item1.setTipoBalanco(EnumTipoBalanco.DESPESA);
		item1.setValor(110.50d);
		
		ItemBalanco item2 = new ItemBalanco();
		item2.setDescricao("AGUA");
		item2.setCategoria(c1);
		item2.setTipoBalanco(EnumTipoBalanco.DESPESA);
		item2.setData(new Date());
		item2.setValor(150.10d);
		
	
		ItemBalanco item3 = new ItemBalanco();
		item3.setDescricao("CESTA");
		item3.setData(new Date());
		item3.setTipoBalanco(EnumTipoBalanco.DESPESA);
		item3.setValor(300.00d);
		
		ItemBalanco item4 = new ItemBalanco();
		item4.setDescricao("ALUGUES MAQUINA");
		item4.setCategoria(c2);
		item4.setData(new Date());
		item4.setTipoBalanco(EnumTipoBalanco.DESPESA);
		item4.setValor(260.00d);
		
		
		balanco.setItens(new ArrayList<ItemBalanco>());
		balanco.getItens().add(item1);
		balanco.getItens().add(item2);
		balanco.getItens().add(item3);
		balanco.getItens().add(item4);
		//balanco.getItens().add(item5);
		return balanco;
	}

	
	private List<ItemBalanco> getReceitasFIxtures() {
		CategoriaItemBalanco c3 =new CategoriaItemBalanco();
		c3.setAtiva(true);
		c3.setId(3l);
		c3.setNome("INGRESSOS");
		c3.setTipoBalanco(EnumTipoBalanco.RECEITA);
		
		ItemBalanco item5 = new ItemBalanco();
		item5.setDescricao("Taxa Condominio");
		item5.setCategoria(c3);
		item5.setData(new Date());
		item5.setTipoBalanco(EnumTipoBalanco.RECEITA);
		item5.setValor(1100.00d);
		
		ItemBalanco item6 = new ItemBalanco();
		item6.setDescricao("Doacao");
		item6.setData(new Date());
		item6.setTipoBalanco(EnumTipoBalanco.RECEITA);
		item6.setValor(100.00d);
		
		List<ItemBalanco> itens = new ArrayList<ItemBalanco>();
		itens.add(item5);
		itens.add(item6);
		return itens;
	}
	
	

}
