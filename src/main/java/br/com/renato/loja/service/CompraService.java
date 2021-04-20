package br.com.renato.loja.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.renato.loja.client.FornecedorClient;
import br.com.renato.loja.client.TransportadorClient;
import br.com.renato.loja.model.dto.CompraDto;
import br.com.renato.loja.model.dto.InfoEntregaDto;
import br.com.renato.loja.model.dto.InfoFornecedorDto;
import br.com.renato.loja.model.dto.InfoPedidoDto;
import br.com.renato.loja.model.dto.VoucherDto;
import br.com.renato.loja.model.entity.Compra;
import br.com.renato.loja.model.entity.CompraEstado;
import br.com.renato.loja.repository.CompraRepository;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private RestTemplate clientHttp;

	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private TransportadorClient transportadorClient;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private org.springframework.cloud.client.discovery.DiscoveryClient discoveryClient;

	@HystrixCommand(fallbackMethod = "realizarCompraFallback", threadPoolKey = "realizarCompraThreadPool")
	public Compra realizarCompra(CompraDto compra) {

		Compra compraSalva = new Compra();

		compraRepository.save(compraSalva);

		compra.setCompraId(compraSalva.getId());

		InfoFornecedorDto info = buscarInformacoesEstado(compra);

		InfoPedidoDto infoPedido = realizarPedido(compra, compraSalva, info);

		realizarEntrega(compra, compraSalva, info, infoPedido);

		return compraSalva;
	}

	private InfoFornecedorDto buscarInformacoesEstado(CompraDto compra) {
		String estado = compra.getEndereco().getEstado();
		LOG.info("buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(estado);
		return info;
	}

	private InfoPedidoDto realizarPedido(CompraDto compra, Compra compraSalva, InfoFornecedorDto info) {

		LOG.info("realizando um pedido");
		InfoPedidoDto infoPedido = fornecedorClient.realizaPedido(compra.getItens());

		popularInformacaoPedido(compraSalva, info, infoPedido);
		return infoPedido;
	}

	private void realizarEntrega(CompraDto compra, Compra compraSalva, InfoFornecedorDto info,
			InfoPedidoDto infoPedido) {
		LOG.info("realizando Entrega");
		VoucherDto voucherDto = realizarEntrega(compra, info, infoPedido);

		popularInformacaoEntrega(compraSalva, voucherDto);
	}

	private VoucherDto realizarEntrega(CompraDto compra, InfoFornecedorDto info, InfoPedidoDto infoPedido) {
		InfoEntregaDto entregaDto = new InfoEntregaDto(infoPedido, info.getEndereco(), compra.getEndereco().toString());

		VoucherDto voucherDto = transportadorClient.reservaEntrega(entregaDto);
		return voucherDto;
	}

	private void popularInformacaoEntrega(Compra compraSalva, VoucherDto voucherDto) {
		compraSalva.setCompraEstado(CompraEstado.TRANSPORTE_EFETUADO);
		compraSalva.setVoucherId(voucherDto.getNumero());
		compraSalva.setPrevisaoParaEntrega(voucherDto.getPrevisaoParaEntrega());
	}

	private void popularInformacaoPedido(Compra compraSalva, InfoFornecedorDto info, InfoPedidoDto infoPedido) {
		compraSalva.setCompraEstado(CompraEstado.REALIZADO);
		compraSalva.setPedidoId(infoPedido.getId());
		compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());
		compraSalva.setEnderecoDestino(info.getEndereco());
	}

	@HystrixCommand(threadPoolKey = "recuperarCompraThreadPool")
	public Compra recuperarCompra(Long idCompra) {

		return compraRepository.findById(idCompra).orElse(new Compra());
	}

	public Compra realizarCompraFallback(CompraDto compra) {

		if (compra.getCompraId() > 0) {
			Optional<Compra> compraRecuperada = compraRepository.findById(compra.getCompraId());

			if (compraRecuperada.isPresent()) {
				return compraRecuperada.get();
			}
		}

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
}