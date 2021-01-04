package br.com.oobj.zuul.security.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Cacheable
@Table(name = "rest_app", indexes = { @Index(name = "idx_rest_app_token", columnList = "token") }, uniqueConstraints = {
		@UniqueConstraint(name = "uk_rest_app_token", columnNames = { "token" }) })
public class Aplicativo extends AbstractPersistable<Long> {

	@NotEmpty
	@Length(max = 120)
	@Column(nullable = false, length = 120)
	private String nome;

	@NotEmpty
	@Length(max = 120)
	@Column(nullable = false, length = 120)
	private String token;

	@NotEmpty
	@Length(max = 120)
	@Column(nullable = false, length = 120)
	private String secret;

	@Column
	private Boolean inativo;

	@Column(nullable = false)
	private Integer tipo;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Boolean getInativo() {
		return inativo;
	}

	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}

	/**
	 * @return the tipo
	 */
	public Integer getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
}