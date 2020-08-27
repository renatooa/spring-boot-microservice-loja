package br.com.renato.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.renato.loja.model.dto.CompraDto;
import br.com.renato.loja.model.entity.Compra;
import br.com.renato.loja.service.CompraService;

@RestController
@RequestMapping("/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;

	@PostMapping
	@Transactional
	public ResponseEntity<Compra> inserirCompra(@RequestBody CompraDto compra) {
		return ResponseEntity.ok(compraService.realizarCompra(compra));
	}

	@GetMapping(path = "/{idCompra}")
	public ResponseEntity<Compra> getCompra(@PathVariable Long idCompra) {
		return ResponseEntity.ok(compraService.recuperarCompra(idCompra));
	}

}
