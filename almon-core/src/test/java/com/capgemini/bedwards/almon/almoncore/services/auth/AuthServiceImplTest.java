package com.capgemini.bedwards.almon.almoncore.services.auth;

import com.capgemini.bedwards.almon.almondatastore.models.auth.Role;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.RoleRepository;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.DataCreationHelper.createRole;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String ROOT_ACCOUNT = "root.account@almon.com";

    private AuthServiceImpl getAuthServiceImpl() {
        AuthServiceImpl authService = new AuthServiceImpl(userRepository, roleRepository);
        ReflectionTestUtils.setField(authService, "passwordEncoder", passwordEncoder);
        ReflectionTestUtils.setField(authService, "rootAccount", ROOT_ACCOUNT);
        return authService;
    }


    public static Stream<Arguments> getAuthenticatedUserArgumentProvider() {
        return Stream.of(
                Arguments.of("testEmail@email.com", "password123"),
                Arguments.of("heyIAmAEmail@email.com", "a new password"),
                Arguments.of("I.do.not.like.being.called.a.generic.email@generic.co.uk", "this is a strange password for a user :/")
        );
    }

    @ParameterizedTest
    @MethodSource("getAuthenticatedUserArgumentProvider")
    public void positive_getAuthenticatedUser_validCredentials(String email, String password) {
        Optional<User> userOptional = Optional.of(User.builder().email(email).password(password).build());
        when(userRepository.findUserByEmail(eq(email))).thenReturn(userOptional);
        when(passwordEncoder.matches(eq(password), eq(password))).thenReturn(true);
        User returnedUser = getAuthServiceImpl().getAuthenticatedUser(email, password);
        assertEquals(userOptional.get(), returnedUser);
    }

    @Test
    public void negative_getAuthenticatedUser_userNotFound() {
        Optional<User> userOptional = Optional.empty();
        when(userRepository.findUserByEmail(eq("test@email.com"))).thenReturn(userOptional);
        User returnedUser = getAuthServiceImpl().getAuthenticatedUser("test@email.com", "invalidPassword");
        assertNull(returnedUser);
    }

    @Test
    public void negative_getAuthenticatedUser_invalidCredentialsIncorrectPassword() {
        Optional<User> userOptional = Optional.of(User.builder().email("test@email.com").password("password").build());
        when(userRepository.findUserByEmail(eq("test@email.com"))).thenReturn(userOptional);
        User returnedUser = getAuthServiceImpl().getAuthenticatedUser("test@email.com", "invalidPassword");
        assertNull(returnedUser);
    }

    public static Stream<Arguments> registerArgumentProvider() {
        return Stream.of(
                Arguments.of(ROOT_ACCOUNT, "rootAccount_firstname", "rootAccount_lastName", "securePassword"),
                Arguments.of("test@email.com", "standardAccount_firstname", "standardAccount_lastname", "password404"),
                Arguments.of("user1@email.com", "Ben", "Edwards", "passwordNotFound")
        );
    }

    @ParameterizedTest
    @MethodSource("registerArgumentProvider")
    public void positive_register_validRootAccount(String email, String firstname, String lastname, String password) {
        boolean isRootAccount = email.equals(ROOT_ACCOUNT);
        when(userRepository.existsByEmail(eq(email))).thenReturn(false);
        final Optional<Role> userRole = Optional.of(createRole("user"));
        when(roleRepository.findById(eq("USER"))).thenReturn(userRole);

        final Optional<Role> adminRole = Optional.of(createRole("admin"));
        if (isRootAccount) {
            when(roleRepository.findById(eq("ADMIN"))).thenReturn(adminRole);
        }
        when(passwordEncoder.encode(eq(password))).thenReturn("encoded" + password);


        when(userRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = getAuthServiceImpl().register(email, firstname, lastname, password);
        assertNotNull(createdUser);


        Set<Role> expectedRoles = new HashSet<Role>() {{
            add(userRole.get());
            if (isRootAccount)
                add(adminRole.get());
        }};
        assertEquals(email, createdUser.getEmail());
        if (isRootAccount)
            assertTrue(createdUser.isEnabled());
        else
            assertFalse(createdUser.isEnabled());
        assertEquals(firstname, createdUser.getFirstName());
        assertEquals(lastname, createdUser.getLastName());
        assertEquals("encoded" + password, createdUser.getPassword());
        assertIterableEquals(expectedRoles, createdUser.getRoles());

        if (isRootAccount)
            assertEquals(createdUser, createdUser.getApprovedBy());
        else
            assertNull(createdUser.getApprovedBy());
        assertNull(createdUser.getAuthorities());
        assertNull(createdUser.getServiceSubscriptions());
        assertNull(createdUser.getMonitorSubscriptions());

        verify(userRepository, times(isRootAccount ? 2 : 1)).saveAndFlush(createdUser);
    }

    @Test
    public void negative_register_userAlreadyExists() {
        when(userRepository.existsByEmail(eq("test@email.com"))).thenReturn(true);
        assertThrows(BadCredentialsException.class, () -> getAuthServiceImpl().register("test@email.com", "testF", "testL", "pwd"),
                "User already exists with this email");

    }


    @Test
    public void positive_checkUserExists_userFound() {
        when(userRepository.existsByEmail(eq("test@email.com"))).thenReturn(true);
        assertTrue(getAuthServiceImpl().checkUserExists("test@email.com"));
    }

    @Test
    public void negative_checkUserExists_userNotFound() {
        when(userRepository.existsByEmail(eq("test@email.com"))).thenReturn(false);
        assertFalse(getAuthServiceImpl().checkUserExists("test@email.com"));

    }
}
