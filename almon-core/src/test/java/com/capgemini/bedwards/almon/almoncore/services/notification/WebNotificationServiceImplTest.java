package com.capgemini.bedwards.almon.almoncore.services.notification;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.notification.WebNotification;
import com.capgemini.bedwards.almon.almondatastore.repository.notification.WebNotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.capgemini.bedwards.almon.almontest.helpers.DataCreationHelper.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebNotificationServiceImplTest {


    @Mock
    private WebNotificationRepository webNotificationRepository;


    private WebNotificationServiceImpl getWebNotificationServiceImpl() {
        return new WebNotificationServiceImpl(webNotificationRepository);
    }


    @Test
    public void positive_save_valid() {
        WebNotification webNotification = WebNotification.builder().id(UUID.randomUUID()).build();
        getWebNotificationServiceImpl().save(webNotification);
        verify(webNotificationRepository, times(1)).save(webNotification);
    }

    private void positive_getNotifications_validHelper(List<WebNotification> returnList) {
        User user = createUser("test@email.com");
        when(webNotificationRepository.findAllByUser(eq(user))).thenReturn(returnList);
        List<WebNotification> webNotifications = getWebNotificationServiceImpl().getNotifications(user);
        assertEquals(returnList, webNotifications);
    }

    @Test
    public void positive_getNotifications_validSingle() {
        positive_getNotifications_validHelper(new ArrayList<WebNotification>() {{
            add(WebNotification.builder().id(UUID.randomUUID()).build());
        }});
    }

    @Test
    public void positive_getNotifications_validMultiple() {
        positive_getNotifications_validHelper(new ArrayList<WebNotification>() {{
            add(WebNotification.builder().id(UUID.randomUUID()).build());
            add(WebNotification.builder().id(UUID.randomUUID()).build());
            add(WebNotification.builder().id(UUID.randomUUID()).build());
        }});
    }

    public static Stream<Arguments> getNotificationsPaginatedArgumentProvider() {
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 1),
                Arguments.of(10, 2),
                Arguments.of(20, 200)
        );
    }

    @ParameterizedTest
    @MethodSource("getNotificationsPaginatedArgumentProvider")
    public void positive_getNotificationsPaginated_valid(int pageNo, int pageSize) {
        User user = createUser("test@email.com");
        TestPage expectedPage = new TestPage();
        when(webNotificationRepository.findAllByUser(eq(user), eq(PageRequest.of(pageNo - 1, pageSize)))).thenReturn(expectedPage);
        Page<WebNotification> page = getWebNotificationServiceImpl().getNotifications(user, pageNo, pageSize);
        assertEquals(expectedPage, page);
        verify(webNotificationRepository, times(1)).findAllByUser(eq(user), eq(PageRequest.of(pageNo - 1, pageSize)));
    }

    @Test
    public void negative_getNotificationsPaginated_negativePageNumber() {
        User user = createUser("test@email.com");
        assertThrows(IllegalArgumentException.class,
                () -> getWebNotificationServiceImpl().getNotifications(user, -1, 2),
                "Page index must not be less than zero");
    }

    @Test
    public void negative_getNotificationsPaginated_zeroPageNumber() {
        User user = createUser("test@email.com");
        assertThrows(IllegalArgumentException.class,
                () -> getWebNotificationServiceImpl().getNotifications(user, 0, 2),
                "Page index must not be less than zero");
    }

    @Test
    public void positive_read_valid() {
        User user = createUser("test@email.com");
        WebNotification webNotification = WebNotification.builder().id(UUID.randomUUID())
                .sentTO(new HashMap<User, Boolean>() {{
                    put(user, false);
                }}).build();

        getWebNotificationServiceImpl().read(user, webNotification);
        assertTrue(webNotification.getSentTO().get(user));
        verify(webNotificationRepository, times(1)).save(eq(webNotification));
    }

    @Test
    public void negative_read_webNotificationNotSentToUser() {
        User user = createUser("test@email.com");
        WebNotification webNotification = WebNotification.builder().id(UUID.randomUUID()).build();

        assertThrows(NotFoundException.class,
                (() -> getWebNotificationServiceImpl().read(user, webNotification)),
                "Web Notification was not sent to user: " + user.getId());
        verify(webNotificationRepository, times(0)).save(eq(webNotification));
    }


    @Test
    public void positive_findById_valid() {
        Optional<WebNotification> webNotification = Optional.of(WebNotification.builder().id(UUID.randomUUID()).build());
        when(webNotificationRepository.findById(eq((webNotification.get().getId())))).thenReturn(webNotification);
        Optional<WebNotification> foundWebNotification = getWebNotificationServiceImpl().findById(webNotification.get().getId());
        assertTrue(foundWebNotification.isPresent());
        assertEquals(webNotification, foundWebNotification);
    }

    @Test
    public void negative_findById_notFound() {
        Optional<WebNotification> webNotification = Optional.empty();
        when(webNotificationRepository.findById(any())).thenReturn(webNotification);
        Optional<WebNotification> foundWebNotification = getWebNotificationServiceImpl().findById(UUID.randomUUID());
        assertFalse(foundWebNotification.isPresent());
        assertEquals(webNotification, foundWebNotification);
    }

    @Test
    public void positive_findByWebNotificationId_valid() {
        Optional<WebNotification> webNotification = Optional.of(WebNotification.builder().id(UUID.randomUUID()).build());
        when(webNotificationRepository.findById(eq((webNotification.get().getId())))).thenReturn(webNotification);
        WebNotification foundWebNotification = getWebNotificationServiceImpl().findByWebNotificationId(webNotification.get().getId());
        assertEquals(webNotification.get(), foundWebNotification);
    }

    @Test
    public void negative_findByWebNotificationId_notFound() {
        Optional<WebNotification> webNotification = Optional.empty();
        when(webNotificationRepository.findById(any())).thenReturn(webNotification);
        UUID uuid = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> getWebNotificationServiceImpl().findByWebNotificationId(uuid),
                "Web Notification with id: " + uuid + " not found");
    }

    private class TestPage implements Page {

        @Override
        public int getTotalPages() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getTotalElements() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Page map(Function converter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumber() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfElements() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List getContent() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasContent() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Sort getSort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isFirst() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isLast() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Pageable nextPageable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Pageable previousPageable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator iterator() {
            throw new UnsupportedOperationException();
        }
    }

}
