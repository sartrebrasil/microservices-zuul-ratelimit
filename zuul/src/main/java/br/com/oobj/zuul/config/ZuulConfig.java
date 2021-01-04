package br.com.oobj.zuul.config;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;

import br.com.oobj.zuul.ratelimit.RateLimitExceededListener;

@Configuration
@EnableZuulProxy
public class ZuulConfig {

	@Bean
	public RateLimiterErrorHandler rateLimitErrorHandler() {
		return new DefaultRateLimiterErrorHandler();
	}

	@Bean
	public RateLimitExceededListener rateLimitExceededListener() {
		return new RateLimitExceededListener();
	}

	@Bean
	public LocationRewriteFilter locationRewriteFilter() {
		return new LocationRewriteFilter();
	}

}
