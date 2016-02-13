package br.com.virtz.condominio.controller.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.service.PaginacaoService;

public class VisitantesLazyModel extends LazyDataModel<Visitante> {

    private static final long serialVersionUID = 1L;
 
    private List<Visitante> visitantes;
 
    private PaginacaoService service;
    
    private Long idCondominio = null;
	
	public VisitantesLazyModel(List<Visitante> visitantes, Long idCondominio, PaginacaoService service) {
		this.visitantes = visitantes;
		this.idCondominio = idCondominio;
		this.service = service;
	}
	
	
	@Override
	public List<Visitante> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

    	try {
                visitantes = service.recuperarPorCondominioPaginado(idCondominio, first, pageSize); 
 
                // If there is no player created yet, we will create 100!!
                if (visitantes == null || visitantes.isEmpty()) {
                                    // we will do the research again to get the created players
                	visitantes = new ArrayList<Visitante>();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // set the total of players
        if(getRowCount() <= 0){
            setRowCount(service.totalVisitantes(idCondominio));
        }
 
        // set the page dize
        setPageSize(pageSize);
 
        return visitantes;
	}
    
    @Override
    public Object getRowKey(Visitante player) {
        return player.getId();
    }
 
    @Override
    public Visitante getRowData(String playerId) {
        Integer id = Integer.valueOf(playerId);
 
        for (Visitante player : visitantes) {
            if(id.equals(player.getId())){
                return player;
            }
        }
 
        return null;
    }
    
}
