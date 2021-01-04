package br.com.oobj.zuul.security.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.oobj.zuul.security.model.Aplicativo;

public interface AplicativoRepository extends CrudRepository<Aplicativo, Long> {

	Optional<Aplicativo> findByToken(String token);
}
