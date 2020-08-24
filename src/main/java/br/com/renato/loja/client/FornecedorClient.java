package br.com.renato.loja.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.renato.loja.model.dto.CompraItemDto;
import br.com.renato.loja.model.dto.InfoFornecedorDto;
import br.com.renato.loja.model.dto.InfoPedidoDto;

@FeignClient("fornecedor")
public interface FornecedorClient {

	@RequestMapping("/info/{estado}")
	InfoFornecedorDto getInfoPorEstado(@PathVariable String estado);

	@RequestMapping(method=RequestMethod.POST, value="/pedido")
	InfoPedidoDto realizaPedido(List<CompraItemDto> itens);
}
