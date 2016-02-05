package br.com.virtz.condominio.geral;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.CategoriaItemBalanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.exception.AppException;

public class Demonstrativo {
	
	private Balanco balanco = null;
	private List<CategoriaItemBalanco> categoriasComItensDespesa = null;
	private List<CategoriaItemBalanco> categoriasComItensReceita = null;
	
	private List<ItemBalanco> receitas = null;
	private List<ItemBalanco> despesas = null;
	
	private Double totalReceitas = null;
	private Double totalDespesas = null;
	
	
	public Demonstrativo(Balanco balanco) {
		super();
		this.balanco = balanco;

		zerarTotais();
		
		categoriasComItensDespesa = new ArrayList<CategoriaItemBalanco>();
		categoriasComItensReceita = new ArrayList<CategoriaItemBalanco>();
		
	}
	
	
	public void setReceitas(List<ItemBalanco> receitas){
		this.receitas = receitas;
		agruparItensPorCategoria(receitas, EnumTipoBalanco.RECEITA);
		try {
			this.totalReceitas = somarItens(receitas);
		} catch (AppException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setDespesas(List<ItemBalanco> despesas){
		this.despesas = despesas;
		agruparItensPorCategoria(despesas, EnumTipoBalanco.DESPESA);
		try {
			this.totalDespesas = somarItens(despesas);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void zerarTotais(){
		this.totalDespesas = 0d;
		this.totalReceitas = 0d;
	}
	
	public void agruparItensPorCategoria(List<ItemBalanco> itens, EnumTipoBalanco tipoBalanco) {
		if(itens == null || itens.isEmpty()){
			return;
		}
		
		List<CategoriaItemBalanco> categoriasItensBalanco = new ArrayList<CategoriaItemBalanco>();
		Map<CategoriaItemBalanco, Set<ItemBalanco>> map = new HashMap<CategoriaItemBalanco, Set<ItemBalanco>>();
		for(ItemBalanco item : itens){
			CategoriaItemBalanco categ = item.getCategoria();
			if(categ == null){
				categ = new CategoriaItemBalanco();
				categ.setAtiva(true);
				categ.setNome("OUTROS");
				categ.setTipoBalanco(item.getTipoBalanco());
			}
			
			Set<ItemBalanco> lista = map.get(categ);
			if(lista == null){
				lista = new HashSet<ItemBalanco>();
			}
						
			lista.add(item);
			map.put(categ, lista);
			
		}
		
		if(map != null){
			for(CategoriaItemBalanco cat : map.keySet()){
				cat.setItens(new ArrayList<ItemBalanco>( map.get(cat) ));
				try {
					cat.setTotalCategoria(somarItens(cat.getItens()));
				} catch (AppException e) {
					e.printStackTrace();
				}
				categoriasItensBalanco.add(cat);
			}
		}
		
		Collections.sort(categoriasItensBalanco);
		
		if(EnumTipoBalanco.RECEITA.equals(tipoBalanco)){
			this.categoriasComItensReceita = categoriasItensBalanco;
		} else {
			this.categoriasComItensDespesa = categoriasItensBalanco;
		}
		
	}
	
	public Double somarItens(List<ItemBalanco> itens) throws AppException {
		if(itens == null || itens.isEmpty()){
			return 0d;
		}
		BigDecimal v = new BigDecimal(0d);
		
		for(ItemBalanco i : itens){
			v = v.add(new BigDecimal(i.getValor()));
		}
		
		return v.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}


	public Balanco getBalanco() {
		return balanco;
	}


	public void setBalanco(Balanco balanco) {
		this.balanco = balanco;
	}


	public List<CategoriaItemBalanco> getCategoriasComItensDespesa() {
		return categoriasComItensDespesa;
	}


	public void setCategoriasComItensDespesa(
			List<CategoriaItemBalanco> categoriasComItensDespesa) {
		this.categoriasComItensDespesa = categoriasComItensDespesa;
	}


	public List<CategoriaItemBalanco> getCategoriasComItensReceita() {
		return categoriasComItensReceita;
	}


	public void setCategoriasComItensReceita(
			List<CategoriaItemBalanco> categoriasComItensReceita) {
		this.categoriasComItensReceita = categoriasComItensReceita;
	}


	public Double getTotalReceitas() {
		return totalReceitas;
	}


	public void setTotalReceitas(Double totalReceitas) {
		this.totalReceitas = totalReceitas;
	}


	public Double getTotalDespesas() {
		return totalDespesas;
	}


	public void setTotalDespesas(Double totalDespesas) {
		this.totalDespesas = totalDespesas;
	}


	public List<ItemBalanco> getReceitas() {
		return receitas;
	}


	public List<ItemBalanco> getDespesas() {
		return despesas;
	}
	
	
	
	
}
