package com.hoaxify_1.hoaxify_1.user;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.transaction.TransactionScoped;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
public class User implements UserDetails{

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String userName;
	
	@NotNull
	private String displayName;
	 
	@NotNull
	
	private String password;

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return AuthorityUtils.createAuthorityList("Role User");
	}

	@Override
	@Transient
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
