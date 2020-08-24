package br.com.renato.loja.model.dto;

import java.util.List;

public class CompraDto {

	private EnderecoDto endereco;

	private List<CompraItemDto> itens;

	public CompraDto() {
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