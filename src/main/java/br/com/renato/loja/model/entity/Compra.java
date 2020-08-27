package br.com.renato.loja.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Compra {

	@Id
	private Long pedidoId;

	private Integer tempoDePreparo;

	private String enderecoDestino;

	public Compra() {
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public Integer getTempoDePreparo() {
		return tempoDePreparo;
	}

	public void setTempoDePreparo(Integer tempoDePreparo) {
		this.tempoDePreparo = tempoDePreparo;
	}

	public String getEnderecoDestino() {
		return enderecoDestino;
	}

	public void setEnderecoDestino(String enderecoDestino) {
		this.enderecoDestino = enderecoDestino;
	}
}