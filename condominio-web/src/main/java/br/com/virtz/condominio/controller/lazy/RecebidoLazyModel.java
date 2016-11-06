package br.com.virtz.condominio.controller.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.service.PaginacaoService;
import br.com.virtz.condominio.service.PaginacaoServiceRecebidos;

public class RecebidoLazyModel extends LazyDataModel<Recebido> {

    private static final long serialVersionUID = 1L;

    private List<Recebido> recebidos;

    private PaginacaoServiceRecebidos service;

    private Long idCondominio = null;
    private Long idApartamento = null;

	public RecebidoLazyModel(List<Recebido> recebidos, Long idCondominio, Long idApartamento, PaginacaoServiceRecebidos service) {
		this.recebidos = recebidos;
		this.idCondominio = idCondominio;
		this.service = service;
		this.idApartamento = idApartamento;
	}


	@Override
	public List<Recebido> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

    	try {
            if(idApartamento == null){
            	recebidos = service.recuperarPorCondominioPaginado(idCondominio, first, pageSize);
            } else {
            	recebidos = service.recuperarPorApartamentoPaginado(idApartamento, first, pageSize);
            }

                // If there is no player created yet, we will create 100!!
            if (recebidos == null || recebidos.isEmpty()) {
                                // we will do the research again to get the created players
            	recebidos = new ArrayList<Recebido>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set the total of players
        if(getRowCount() <= 0){
        	if(idApartamento == null){
        		setRowCount(service.total(idCondominio));
        	} else {
        		setRowCount(service.totalVisitantesApto(idApartamento));
        	}
        }

        // set the page dize
        setPageSize(pageSize);

        return recebidos;
	}

    @Override
    public Object getRowKey(Recebido player) {
        return player.getId();
    }

    @Override
    public Recebido getRowData(String playerId) {
        Integer id = Integer.valueOf(playerId);

        for (Recebido player : recebidos) {
            if(id.equals(player.getId())){
                return player;
            }
        }

        return null;
    }

}
