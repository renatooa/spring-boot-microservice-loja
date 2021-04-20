package br.com.renato.loja.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompraDto {

	@JsonIgnore
	private Long compraId;

	private EnderecoDto endereco;

	private List<CompraItemDto> itens;

	public CompraDto() {
	}

	public Long getCompraId() {
		return compraId;
	}

	public void setCompraId(Long compraId) {
		this.compraId = compraId;
	}

	public EnderecoDto getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDto endereco) {
		this.endereco = endereco;
	}

	public List<CompraItemDto> getItens() {
		return itens;
	}

	public void setItens(List<CompraItemDto> itens) {
		this.itens = itens;
	}
}