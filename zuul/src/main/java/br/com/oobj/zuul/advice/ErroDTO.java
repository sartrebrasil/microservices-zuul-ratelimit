package br.com.oobj.zuul.advice;

import org.springframework.http.HttpStatus;

public class ErroDTO {

	private Integer codigo;
	private String mensagem;

	public ErroDTO() {
	}

	public ErroDTO(Integer codigo, String mensagem) {
		this.codigo = codigo;
		this.mensagem = mensagem;
	}

	public ErroDTO(HttpStatus status, Exception ex) {
		this(status.value(), ex.getMessage());
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}