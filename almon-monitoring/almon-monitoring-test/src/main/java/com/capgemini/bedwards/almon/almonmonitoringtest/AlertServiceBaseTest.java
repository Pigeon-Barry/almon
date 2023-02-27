package com.capgemini.bedwards.almon.almonmonitoringtest;

import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.notification.NotificationService;
import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.alert.AlertSpecification;
import com.capgemini.bedwards.almon.almondatastore.repository.alert.AlertRepository;
import com.capgemini.bedwards.almon.almonmonitoringcore.service.alert.AlertServiceBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public abstract class AlertServiceBaseTest<T extends Alert<?>> {

    @MockBean
    protected NotificationService notificationService;

    protected abstract Class<T> getAlertClass();

    protected abstract AlertRepository<T> getRepository();

    protected abstract AlertServiceBase<T> getAlertService();

    @Test
    public void positive_create() {
        T alert = mock(getAlertClass());
        when(getRepository().save(eq(alert))).thenReturn(alert);
        assertEquals(alert, getAlertService().create(alert));
        verify(getRepository(), times(1)).save(eq(alert));
        verify(notificationService, times(1)).send(eq(alert));
    }

    @Test
    public void positive_sendAlert() {
        T alert = mock(getAlertClass());
        getAlertService().sendAlert(alert);
        verify(notificationService, times(1)).send(eq(alert));
    }

    @Test
    public void positive_getAlertsPaginated_base() {
        int pageNumber = 1, pageSize = 5;
        AlertSpecification<T> specification = mock(AlertSpecification.class);
        Page<T> page = mock(Page.class);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        when(getRepository().findAll(eq(specification), eq(pageable))).thenReturn(page);

        assertEquals(page, getAlertService().getAlertsPaginated(specification, pageNumber, pageSize));
    }

    @Test
    public void positive_getAlertsPaginated_additional() {
        int pageNumber = 100, pageSize = 10;
        AlertSpecification<T> specification = mock(AlertSpecification.class);
        Page<T> page = mock(Page.class);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        when(getRepository().findAll(eq(specification), eq(pageable))).thenReturn(page);
        assertEquals(page, getAlertService().getAlertsPaginated(specification, pageNumber, pageSize));
    }

    @Test
    public void positive_getAlerts() {
        AlertSpecification<T> specification = mock(AlertSpecification.class);
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<T> list = new ArrayList<>();
        when(getRepository().findAll(eq(specification), eq(sort))).thenReturn(list);
        assertEquals(list, getAlertService().getAlerts(specification));
        verify(getRepository(), times(1)).findAll(eq(specification), eq(sort));
    }

    @Test
    public void positive_getAlerts_with_custom_sort() {
        AlertSpecification<T> specification = mock(AlertSpecification.class);
        Sort sort = Sort.by(Sort.Direction.DESC, "message");
        List<T> list = new ArrayList<>();
        when(getRepository().findAll(eq(specification), eq(sort))).thenReturn(list);
        assertEquals(list, getAlertService().getAlerts(specification, sort));
        verify(getRepository(), times(1)).findAll(eq(specification), eq(sort));
    }

    @Test
    public void positive_getAlertFromId() {
        UUID uuid = UUID.randomUUID();
        T alert = mock(getAlertClass());
        Optional<T> optional = Optional.of(alert);
        when(getRepository().findById(eq(uuid))).thenReturn(optional);
        assertEquals(alert, getAlertService().getAlertFromId(uuid));
    }

    @Test
    public void negative_getAlertFromId_not_found() {
        UUID uuid = UUID.randomUUID();
        Optional<T> optional = Optional.empty();
        when(getRepository().findById(eq(uuid))).thenReturn(optional);
        assertThrows(NotFoundException.class, () -> getAlertService().getAlertFromId(uuid), "Failed to find alert with id: '" + uuid + "'");
    }
}
