package br.com.renato.loja.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.renato.loja.model.entity.Compra;

@Repository
public interface CompraRepository extends CrudRepository<Compra, Long> {

}
