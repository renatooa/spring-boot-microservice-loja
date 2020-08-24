package br.com.renato.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.renato.loja.model.Compra;
import br.com.renato.loja.model.dto.CompraDto;
import br.com.renato.loja.service.CompraService;

@RestController
@RequestMapping("/compra")
public class LojaController {

	@Autowired
	private CompraService compraService;

	@PostMapping
	public ResponseEntity<Compra> inserirCompra(@RequestBody CompraDto compra) {
		return ResponseEntity.ok(compraService.realizarCompra(compra));
	}

}
