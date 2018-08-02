package pl.jstk.webSecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
				.antMatchers("/", "/books", "/books/search", "/books/searchByParam", "/login", "/webjars/**",
						"/img/logo.png", "/css/main.css")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/")
				// .failureUrl("/403")
				.permitAll().and().exceptionHandling().accessDeniedPage("/403").and().logout().permitAll();

		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()
				.withUser(User.withUsername("admin").password("{noop}admin").roles("ADMIN").build())
				.withUser(User.withUsername("user").password("{noop}user").roles("USER").build());

	}

}
