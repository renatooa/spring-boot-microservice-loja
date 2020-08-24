package br.com.renato.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.renato.loja.client.FornecedorClient;
import br.com.renato.loja.model.Compra;
import br.com.renato.loja.model.dto.CompraDto;
import br.com.renato.loja.model.dto.InfoFornecedorDto;
import br.com.renato.loja.model.dto.InfoPedidoDto;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private RestTemplate clientHttp;
	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private org.springframework.cloud.client.discovery.DiscoveryClient discoveryClient;

	public Compra realizarCompra(CompraDto compra) {

		final String estado = compra.getEndereco().getEstado();

		LOG.info("buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(estado);

		LOG.info("realizando um pedido");
		InfoPedidoDto infoPedido = fornecedorClient.realizaPedido(compra.getItens());

		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(infoPedido.getId());
		compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());
		compraSalva.setEnderecoDestino(info.getEndereco());

		System.out.println(info.getEndereco());

		return compraSalva;
	}

	@SuppressWarnings("unused")
	private InfoFornecedorDto getPorTemplate(CompraDto compraDto) {

		ResponseEntity<InfoFornecedorDto> resposta = clientHttp.exchange(
				"http://fornecedor/info/" + compraDto.getEndereco().getEstado(), HttpMethod.GET, null,
				InfoFornecedorDto.class);

		InfoFornecedorDto infoFornecedorDto = resposta.getBody();
		return infoFornecedorDto;
	}

	@SuppressWarnings("unused")
	private InfoFornecedorDto getPorFeign(CompraDto compraDto) {
		return fornecedorClient.getInfoPorEstado(compraDto.getEndereco().getEstado());
	}

	@SuppressWarnings("unused")
	private void printInstanciasFornecedor() {
		discoveryClient.getInstances("fornecedor").stream().forEach(ins -> {
			System.out.println(ins.getHost() + " : " + ins.getPort());
		});
	}
}