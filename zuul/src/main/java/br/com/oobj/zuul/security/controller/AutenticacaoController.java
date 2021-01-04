package br.com.oobj.zuul.security.controller;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.com.oobj.auth.jwt.JwtAbstractAuthenticationEndpoint;
import br.com.oobj.auth.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

//@RestController
//@Api(tags = "Autenticação")
public class AutenticacaoController extends JwtAbstractAuthenticationEndpoint {

	private static final Logger LOG = LoggerFactory.getLogger(AutenticacaoController.class);

	@Autowired
	public AutenticacaoController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager, jwtTokenUtil, userDetailsService);
	}

	/**
	 * Autentica um usuário usando padrão BASIC de autenticação
	 * 
	 * @param authenticationToken
	 * @return
	 * @deprecated prefira {@link #authenticate(Authentication)}
	 */
	@PostMapping(path = "/session")
	@ApiOperation(value = "Nova Sessão", 
			notes = "Cria um token de autorização para utilização da API associado a conta informada. "
			+ "As credenciais devem ser informadas com um cabeçalho Basic Authentication. "
			+ "Mantido por questões de compatibilidade com autenticação Basic. "
			+ "Prefira \"/authenticate\"", 
			authorizations = { @Authorization("basic") })
	@Deprecated
	public ResponseEntity<String> createSession(
			@RequestHeader(value = "Authorization") String authorization) {

		try {
			String jwtToken = super.authenticate(getAuthenticationRequest(authorization));
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set(getTokenHeader(), jwtToken);
			return ResponseEntity.ok().headers(responseHeaders).body(getTokenHeader() + ": " + jwtToken);

		} catch (AuthenticationException e) {
			LOG.warn(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}

	/**
	 * Decodes the header into a username and password.
	 *
	 * @throws BadCredentialsException if the Basic header is not present or is not
	 *                                 valid Base64
	 */
	protected Authentication getAuthenticationRequest(String authorization) {
		
		if (!authorization.toLowerCase().startsWith("basic ")) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}

		byte[] decoded;
		try {
			byte[] base64Token = authorization.substring(6).getBytes(StandardCharsets.UTF_8);
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, StandardCharsets.UTF_8);
		int delim = token.indexOf(':');
		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		
		String[] tokens = new String[] { token.substring(0, delim), token.substring(delim + 1) };
		return new UsernamePasswordAuthenticationToken(tokens[0], tokens[1]);
	}
	
}
