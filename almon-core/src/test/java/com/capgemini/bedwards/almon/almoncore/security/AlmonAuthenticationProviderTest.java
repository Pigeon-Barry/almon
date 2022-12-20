package com.capgemini.bedwards.almon.almoncore.security;

import com.capgemini.bedwards.almon.almoncore.services.auth.AuthService;
import com.capgemini.bedwards.almon.almoncore.util.SecurityUtil;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlmonAuthenticationProviderTest {

  @InjectMocks
  private AlmonAuthenticationProvider authenticationProvider;

  @Mock
  private AuthService authService;

  public static Stream<Arguments> basicUserProvider() {
    return Stream.of(
        Arguments.of(
            User.builder()
                .email("test@email.com")
                .password("mypassword")
                .build()),
        Arguments.of(
            User.builder()
                .email("user2@email.com")
                .password("a new password")
                .build()),
        Arguments.of(
            User.builder()
                .email("a_third_user@email.com")
                .password("a more new password")
                .build())
    );
  }

  @ParameterizedTest
  @MethodSource("basicUserProvider")
  public void positive_authenticate_validCredentials(User user) {
    when(authService.getAuthenticatedUser(eq(user.getEmail()), eq(user.getPassword()))).thenReturn(
        user);

    Authentication returnedAuthentication = authenticationProvider.authenticate(
        new TestAuthentication(user.getEmail(), user.getPassword()));

    assertEquals(SecurityUtil.getNewUserAuthenticationToken(user, user.getEmail()),
        returnedAuthentication);
  }

  @Test
  public void negative_authenticate_invalidCredentials() {
    User user = User.builder()
        .email("test@email.com")
        .password("mypassword")
        .build();
    when(authService.getAuthenticatedUser(eq(user.getEmail()), eq(user.getPassword()))).thenReturn(
        null);

    assertThrows(BadCredentialsException.class,
        () -> authenticationProvider.authenticate(
            new TestAuthentication(user.getEmail(), user.getPassword())),
        "Invalid Credentials"
    );
  }


  @Test
  public void positive_supports_UsingUsernamePasswordAuthenticationToken() {
    assertTrue(authenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
  }

  @ParameterizedTest
  @ValueSource(classes = {String.class, Object.class, Integer.class})
  public void negative_supports_InvalidClass(Class<?> _class) {
    assertFalse(authenticationProvider.supports(_class));
  }


  private class TestAuthentication implements Authentication {

    public final String NAME;
    public final String PASSWORD;

    public TestAuthentication(String name, String password) {
      this.NAME = name;
      this.PASSWORD = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object getCredentials() {
      return this.PASSWORD;
    }

    @Override
    public Object getDetails() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object getPrincipal() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAuthenticated() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
      return this.NAME;
    }
  }

}
