package br.com.virtz.condominio.controller.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.service.IVisitanteService;
import br.com.virtz.condominio.session.SessaoUsuario;

public class VisitantesLazyModel extends LazyDataModel<Visitante> {

    private static final long serialVersionUID = 1L;
 
    private List<Visitante> visitantes;
 
    @Inject
    private IVisitanteService visitanteService;
    
	@Inject
	private SessaoUsuario sessao;
    
	
	
	@Override
	public List<Visitante> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		Long idCondominio = sessao.getUsuarioLogado().getCondominio().getId();
    	try {
                visitantes = visitanteService.recuperarPorCondominioPaginado(idCondominio, first, pageSize); 
 
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
            setRowCount(visitanteService.totalVisitantes(idCondominio));
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
