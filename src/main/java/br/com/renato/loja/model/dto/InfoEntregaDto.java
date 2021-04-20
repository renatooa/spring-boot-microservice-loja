package br.com.renato.loja.model.dto;

import java.time.LocalDate;

public class InfoEntregaDto {

	private Long pedidoId;

	private LocalDate dataParaEntrega;

	private String enderecoOrigem;

	private String enderecoDestino;

	public InfoEntregaDto() {

	}

	public InfoEntregaDto(InfoPedidoDto infoPedido, String enderecoOrigem, String enderecoDestino) {
		super();
		this.pedidoId = infoPedido.getId();
		this.dataParaEntrega = LocalDate.now().plusDays(infoPedido.getTempoDePreparo());
		this.enderecoOrigem = enderecoOrigem;
		this.enderecoDestino = enderecoDestino;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public LocalDate getDataParaEntrega() {
		return dataParaEntrega;
	}

	public void setDataParaEntrega(LocalDate dataParaEntrega) {
		this.dataParaEntrega = dataParaEntrega;
	}

	public String getEnderecoDestino() {
		return enderecoDestino;
	}

	public void setEnderecoDestino(String enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}

	public String getEnderecoOrigem() {
		return enderecoOrigem;
	}

	public void setEnderecoOrigem(String enderecoOrigem) {
		this.enderecoOrigem = enderecoOrigem;
	}

}
