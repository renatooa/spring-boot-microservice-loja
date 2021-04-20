package br.com.renato.loja.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.renato.loja.model.dto.CompraItemDto;
import br.com.renato.loja.model.dto.InfoFornecedorDto;
import br.com.renato.loja.model.dto.InfoPedidoDto;

@FeignClient("fornecedor")
public interface FornecedorClient {

	@GetMapping(value = "/info/{estado}")
	InfoFornecedorDto getInfoPorEstado(@PathVariable String estado);

	@PostMapping(value = "/pedido")
	InfoPedidoDto realizaPedido(List<CompraItemDto> itens);

	@GetMapping(value = "/pedido/{idPedido}")
	InfoPedidoDto getPedido(@PathVariable Long idPedido);
}
