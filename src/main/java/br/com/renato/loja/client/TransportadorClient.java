package br.com.renato.loja.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.renato.loja.model.dto.InfoEntregaDto;
import br.com.renato.loja.model.dto.VoucherDto;

@FeignClient("transportador")
public interface TransportadorClient {

	@PostMapping(value = "/entrega")
	public VoucherDto reservaEntrega(@RequestBody InfoEntregaDto entregaDTO);
}
