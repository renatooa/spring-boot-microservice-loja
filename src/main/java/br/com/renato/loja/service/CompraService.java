package br.com.renato.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.renato.loja.client.FornecedorClient;
import br.com.renato.loja.model.dto.CompraDto;
import br.com.renato.loja.model.dto.InfoFornecedorDto;
import br.com.renato.loja.model.dto.InfoPedidoDto;
import br.com.renato.loja.model.entity.Compra;
import br.com.renato.loja.repository.CompraRepository;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private RestTemplate clientHttp;

	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private org.springframework.cloud.client.discovery.DiscoveryClient discoveryClient;

	@HystrixCommand(fallbackMethod = "realizarCompraFallback", threadPoolKey = "realizarCompraThreadPool")
	public Compra realizarCompra(CompraDto compra) {

		final String estado = compra.getEndereco().getEstado();

		LOG.info("buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(estado);

		LOG.info("realizando um pedido");

		InfoPedidoDto infoPedido = fornecedorClient.realizaPedido(compra.getItens());

		Compra compraSalva = criarCompra(infoPedido);
		compraSalva.setEnderecoDestino(info.getEndereco());

		System.out.println(info.getEndereco());

		compraRepository.save(compraSalva);

		return compraSalva;
	}
	@HystrixCommand(threadPoolKey = "recuperarCompraThreadPool")
	public Compra recuperarCompra(Long idCompra) {

		return compraRepository.findById(idCompra).orElse(new Compra());
	}

	public Compra realizarCompraFallback(CompraDto compra) {

		Compra compraEmFilaMensagem = new Compra();
		LOG.info("realizando um pedido em Fallback");
		return compraEmFilaMensagem;
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

	private Compra criarCompra(InfoPedidoDto infoPedido) {
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(infoPedido.getId());
		compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());

		return compraSalva;
	}
}