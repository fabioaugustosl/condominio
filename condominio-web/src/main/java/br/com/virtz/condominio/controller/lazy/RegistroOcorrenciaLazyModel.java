package br.com.virtz.condominio.controller.lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.virtz.condominio.entidades.RegistroOcorrencia;
import br.com.virtz.condominio.service.PaginacaoService;

public class RegistroOcorrenciaLazyModel extends LazyDataModel<RegistroOcorrencia> {

    private static final long serialVersionUID = 1L;

    private List<RegistroOcorrencia> ocorrencias;

    private PaginacaoService service;

    private Long idCondominio = null;

	public RegistroOcorrenciaLazyModel(List<RegistroOcorrencia> ocorrencias, Long idCondominio, PaginacaoService service) {
		this.ocorrencias = ocorrencias;
		this.idCondominio = idCondominio;
		this.service = service;
	}


	@Override
	public List<RegistroOcorrencia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

    	try {
           	ocorrencias = service.recuperarPorCondominioPaginado(idCondominio, first, pageSize);

            if (ocorrencias == null || ocorrencias.isEmpty()) {
            	ocorrencias = new ArrayList<RegistroOcorrencia>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set the total of players
        if(getRowCount() <= 0){
       		setRowCount(service.total(idCondominio));
        }

        // set the page dize
        setPageSize(pageSize);

        return ocorrencias;
	}

    @Override
    public Object getRowKey(RegistroOcorrencia player) {
        return player.getId();
    }

    @Override
    public RegistroOcorrencia getRowData(String playerId) {
        Integer id = Integer.valueOf(playerId);

        for (RegistroOcorrencia player : ocorrencias) {
            if(id.equals(player.getId())){
                return player;
            }
        }

        return null;
    }

}
