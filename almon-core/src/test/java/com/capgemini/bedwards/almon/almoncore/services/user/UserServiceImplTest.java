package com.capgemini.bedwards.almon.almoncore.services.user;


import com.capgemini.bedwards.almon.almoncore.exceptions.InvalidPermissionException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.repository.auth.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                UserServiceImpl.class
        }
)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;


    @Test
    public void positive_findPaginated_typical() {
        Page<User> page = mock(Page.class);
        Pageable pageable = PageRequest.of(0, 1);
        when(userRepository.findAll(eq(pageable))).thenReturn(page);
        assertEquals(page, userService.findPaginated(1, 1));
        verify(userRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    public void positive_findPaginated_typical_2() {
        Page<User> page = mock(Page.class);
        Pageable pageable = PageRequest.of(9, 100);
        when(userRepository.findAll(eq(pageable))).thenReturn(page);
        assertEquals(page, userService.findPaginated(10, 100));
        verify(userRepository, times(1)).findAll(eq(pageable));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void positive_findPaginatedWithFilter_typical(boolean enabled) {
        Page<User> page = mock(Page.class);
        Pageable pageable = PageRequest.of(0, 1);
        when(userRepository.findUsersByEnabledEquals(eq(pageable), eq(enabled))).thenReturn(page);
        assertEquals(page, userService.findPaginatedWithFilter(1, 1, enabled));
        verify(userRepository, times(1)).findUsersByEnabledEquals(eq(pageable), eq(enabled));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void positive_findPaginatedWithFilter_typical2(boolean enabled) {
        Page<User> page = mock(Page.class);
        Pageable pageable = PageRequest.of(9, 100);
        when(userRepository.findUsersByEnabledEquals(eq(pageable), eq(enabled))).thenReturn(page);
        assertEquals(page, userService.findPaginatedWithFilter(10, 100, enabled));
        verify(userRepository, times(1)).findUsersByEnabledEquals(eq(pageable), eq(enabled));
    }

    @Test
    public void positive_findPaginatedWithFilter_null() {
        Page<User> page = mock(Page.class);
        Pageable pageable = PageRequest.of(0, 1);
        when(userRepository.findAll(eq(pageable))).thenReturn(page);
        assertEquals(page, userService.findPaginatedWithFilter(1, 1, null));
        verify(userRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    public void positive_enable_account_typical() {
        User authorizer = User.builder().id(UUID.randomUUID()).build();
        User user = User.builder().id(UUID.randomUUID()).enabled(false).build();
        userService.enableAccount(authorizer, user);
        verify(userRepository, times(1)).saveAndFlush(eq(user));
        assertTrue(user.isEnabled());
        assertEquals(authorizer, user.getApprovedBy());
    }

    @Test
    public void negative_enable_account_own_account() {
        User authorizer = User.builder().id(UUID.randomUUID()).build();
        InvalidPermissionException invalidPermissionException = assertThrows(InvalidPermissionException.class,
                () -> userService.enableAccount(authorizer, authorizer));
        assertEquals("Can not disable/enable your own account", invalidPermissionException.getMessage());
    }

    @Test
    public void negative_enable_account_null_authorizer() {
        User user = User.builder().id(UUID.randomUUID()).enabled(false).build();
        userService.enableAccount(null, user);
        verify(userRepository, times(1)).saveAndFlush(eq(user));
        assertTrue(user.isEnabled());
        assertNull(user.getApprovedBy());
    }


    @Test
    public void positive_getUser_typical() {
        User user = User.builder().id(UUID.randomUUID()).build();
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUser(user.getId()));
    }

    @Test
    public void negative_getUser_not_found() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.getUser(id));
        assertEquals("User with id '" + id + "' could not be located", notFoundException.getMessage());
    }

    @Test
    public void positive_disable_account_typical() {
        User authorizer = User.builder().id(UUID.randomUUID()).build();
        User user = User.builder().id(UUID.randomUUID()).enabled(true).build();
        userService.disableAccount(authorizer, user);
        verify(userRepository, times(1)).saveAndFlush(eq(user));
        assertFalse(user.isEnabled());
        assertEquals(authorizer, user.getApprovedBy());
    }

    @Test
    public void negative_disable_account_own_account() {
        User authorizer = User.builder().id(UUID.randomUUID()).build();
        InvalidPermissionException invalidPermissionException = assertThrows(InvalidPermissionException.class,
                () -> userService.disableAccount(authorizer, authorizer));
        assertEquals("Can not disable/enable your own account", invalidPermissionException.getMessage());
    }

    @Test
    public void negative_disable_account_null_authorizer() {
        User user = User.builder().id(UUID.randomUUID()).enabled(true).build();
        userService.disableAccount(null, user);
        verify(userRepository, times(1)).saveAndFlush(eq(user));
        assertFalse(user.isEnabled());
        assertNull(user.getApprovedBy());
    }

    @Test
    public void positive_getUserByEmail_typical() {
        User user = User.builder().id(UUID.randomUUID()).email("email@myemail.com").build();
        when(userRepository.findUserByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserByEmail(user.getEmail()));
    }

    @Test
    public void negative_getUserByEmail_not_found() {
        String email = "email@myemail.com";
        when(userRepository.findUserByEmail(eq(email))).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.getUserByEmail(email));
        assertEquals("User with Email: " + email + " not found", notFoundException.getMessage());
    }


    @Test
    public void positive_convertEmailsToUsers_single() {
        User user = User.builder().id(UUID.randomUUID()).email("email@myemail.com").build();
        Set<String> emails = new HashSet<String>() {{
            add(user.getEmail());
        }};
        Set<User> users = new HashSet<User>() {{
            add(user);
        }};

        when(userRepository.findUserByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
        assertEquals(users, userService.convertEmailsToUsers(emails));
    }

    @Test
    public void positive_convertEmailsToUsers_multiple() {
        User user = User.builder().id(UUID.randomUUID()).email("email@myemail.com").build();
        User user2 = User.builder().id(UUID.randomUUID()).email("email2@myemail2.com").build();
        Set<String> emails = new HashSet<String>() {{
            add(user.getEmail());
            add(user2.getEmail());
        }};
        Set<User> users = new HashSet<User>() {{
            add(user);
            add(user2);
        }};

        when(userRepository.findUserByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));
        when(userRepository.findUserByEmail(eq(user2.getEmail()))).thenReturn(Optional.of(user2));
        assertEquals(users, userService.convertEmailsToUsers(emails));
    }

    @Test
    public void negative_convertEmailsToUsers_not_found() {
        Set<String> emails = new HashSet<String>() {{
            add("email@myemail.com");
        }};

        when(userRepository.findUserByEmail(eq("email@myemail.com"))).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.convertEmailsToUsers(emails));
        assertEquals("User with Email: email@myemail.com not found", notFoundException.getMessage());
    }


    @Test
    public void positive_findById() {
        UUID id = UUID.randomUUID();
        Optional<User> userOptional = Optional.of(User.builder().id(id).build());
        when(userRepository.findById(id)).thenReturn(userOptional);
        assertEquals(userOptional, userService.findById(id));
    }

    @Test
    public void positive_findById_not_found() {
        UUID id = UUID.randomUUID();
        Optional<User> userOptional = Optional.empty();
        when(userRepository.findById(id)).thenReturn(userOptional);
        assertEquals(userOptional, userService.findById(id));
        verify(userRepository, times(1)).findById(eq(id));
    }

    @Test
    public void positive_save() {
        User user = User.builder().id(UUID.randomUUID()).build();
        when(userRepository.saveAndFlush(eq(user))).thenReturn(user);
        assertEquals(user, userService.save(user));
        verify(userRepository, times(1)).saveAndFlush(eq(user));
    }

}
