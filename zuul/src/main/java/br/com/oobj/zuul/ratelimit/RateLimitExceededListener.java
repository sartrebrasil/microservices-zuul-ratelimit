package br.com.oobj.zuul.ratelimit;

import org.springframework.context.event.EventListener;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededEvent;

public class RateLimitExceededListener {

	@EventListener
	public void observe(RateLimitExceededEvent event) {
		System.out.println(event);
	}


}
