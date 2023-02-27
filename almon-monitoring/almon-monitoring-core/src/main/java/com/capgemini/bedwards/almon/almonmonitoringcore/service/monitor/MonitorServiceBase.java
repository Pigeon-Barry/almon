package com.capgemini.bedwards.almon.almonmonitoringcore.service.monitor;

import com.capgemini.bedwards.almon.almoncore.exceptions.BadRequestException;
import com.capgemini.bedwards.almon.almoncore.exceptions.NotFoundException;
import com.capgemini.bedwards.almon.almoncore.services.auth.AuthorityService;
import com.capgemini.bedwards.almon.almoncore.services.service.ServiceService;
import com.capgemini.bedwards.almon.almoncore.services.subscription.SubscriptionService;
import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import com.capgemini.bedwards.almon.almonmonitoringcore.repository.monitor.MonitorTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;

;

@Slf4j
public abstract class MonitorServiceBase<T extends Monitor> implements MonitorService<T> {


    protected final AuthorityService AUTHORITY_SERVICE;
    protected final SubscriptionService SUBSCRIPTION_SERVICE;
    protected final ServiceService SERVICE_SERVICE;


    @Autowired
    public MonitorServiceBase(AuthorityService authorityService, ServiceService serviceService, SubscriptionService subscriptionService) {
        this.AUTHORITY_SERVICE = authorityService;
        this.SERVICE_SERVICE = serviceService;
        this.SUBSCRIPTION_SERVICE = subscriptionService;
    }

    protected abstract MonitorTypeRepository<T> getRepository();

    @Override
    @Transactional
    public void enable(T monitor) {
        updateEnabledStatus(monitor, true);
    }

    @Override
    @Transactional
    public void disable(T monitor) {
        updateEnabledStatus(monitor, false);
    }

    protected void updateEnabledStatus(@NotNull T monitor, boolean enabled) {
        log.info((enabled ? "Enabling" : "Disabling") + " monitor: " + monitor);
        monitor.setEnabled(enabled);
        getRepository().save(monitor);
    }

    @Override
    @Transactional
    public void delete(T monitor) {
        log.info("Deleting monitor " + monitor.getId());
        this.AUTHORITY_SERVICE.deleteMonitorAuthorities(monitor);
        this.SUBSCRIPTION_SERVICE.clearSubscriptions(monitor);
        getRepository().delete(monitor);
    }

    @Override
    @Transactional
    public T create(T monitorType) {
        monitorType = save(monitorType);
        if (monitorType.isEnabled())
            enable(monitorType);
        AUTHORITY_SERVICE.addRole(
                AUTHORITY_SERVICE.createAuthority(
                        "SERVICE_" + monitorType.getId().getService().getId() + "_MONITOR_" + monitorType.getId() + "_CAN_ENABLE_DISABLE",
                        "Grants the ability to enable/disable this monitor"
                ),
                Collections.singleton(SERVICE_SERVICE.getOrCreateAdminRole(monitorType.getId().getService())));

        AUTHORITY_SERVICE.addRole(
                AUTHORITY_SERVICE.createAuthority(
                        "SERVICE_" + monitorType.getId().getService().getId() + "_MONITOR_" + monitorType.getId() + "_CAN_VIEW",
                        "Grants the ability to view this monitor"
                ),
                Collections.singleton(SERVICE_SERVICE.getOrCreateUserRole(monitorType.getId().getService()))
        );

        AUTHORITY_SERVICE.addRole(
                AUTHORITY_SERVICE.createAuthority(
                        "SERVICE_" + monitorType.getId().getService().getId() + "_MONITOR_" + monitorType.getId() + "_CAN_RUN",
                        "Grants the ability to run this monitor"
                ),
                Collections.singleton(SERVICE_SERVICE.getOrCreateUserRole(monitorType.getId().getService()))
        );
        AUTHORITY_SERVICE.addRole(
                AUTHORITY_SERVICE.createAuthority(
                        "SERVICE_" + monitorType.getId().getService().getId() + "_MONITOR_" + monitorType.getId() + "_CAN_DELETE",
                        "Grants the ability to Delete this monitor"
                ),
            Collections.singleton(
                SERVICE_SERVICE.getOrCreateAdminRole(monitorType.getId().getService()))
        );
      AUTHORITY_SERVICE.addRole(
          AUTHORITY_SERVICE.createAuthority(
              "SERVICE_" + monitorType.getId().getService().getId() + "_MONITOR_"
                  + monitorType.getId() + "_CAN_UPDATE",
              "Grants the ability to update  this monitor"
          ),
          Collections.singleton(
              SERVICE_SERVICE.getOrCreateAdminRole(monitorType.getId().getService()))
      );
      AUTHORITY_SERVICE.refreshRole(
          SERVICE_SERVICE.getOrCreateUserRole(monitorType.getId().getService()));
      AUTHORITY_SERVICE.refreshRole(
          SERVICE_SERVICE.getOrCreateAdminRole(monitorType.getId().getService()));
      return monitorType;
    }

    @Override
    @Transactional
    public T save(T monitorType) {
        //Reset the notification throttle timeout
        monitorType.setPreventNotificationUntil(Constants.DEFAULT_MONITOR_PREVENT_UNTIL);
        //Save monitor
        return getRepository().save(monitorType);
    }


    @Override
    public T getMonitorFromCombinedId(String source) {
        String[] parts = source.split("-", 2);
        if (parts.length != 2)
            throw new BadRequestException("Could not determine service and monitor id from '" + source + "'");

        Optional<T> MonitorOptional = findByIdSegments(parts[0], parts[1]);
        if (MonitorOptional.isPresent())
            return MonitorOptional.get();
        throw new NotFoundException("Failed to find monitor with id: '" + source + "'");
    }

    private Optional<T> findByIdSegments(String serviceId, String monitorId) {
        return getRepository().findById(
                Monitor.MonitorId.builder()
                        .id(monitorId)
                        .service(com.capgemini.bedwards.almon.almondatastore.models.service.Service.builder()
                                .id(serviceId).build())
                        .build()
        );
    }
}
