package com.capgemini.bedwards.almon.almondatastore.models.alert;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlertSpecification<T extends Alert<?>> implements Specification<T> {


    private final AlertFilterOptions ALERT_FILTER_OPTIONS;

    public AlertSpecification(AlertFilterOptions alertFilterOptions) {
        this.ALERT_FILTER_OPTIONS = alertFilterOptions;
    }

    @Override
    public Predicate toPredicate
            (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        if (ALERT_FILTER_OPTIONS.getStatus() != null && ALERT_FILTER_OPTIONS.getStatus().length > 0) {
            predicates.add(root.get("status").in(Arrays.asList(ALERT_FILTER_OPTIONS.getStatus())));
        }

        if (ALERT_FILTER_OPTIONS.getFrom() != null)
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), ALERT_FILTER_OPTIONS.getFrom()));
        if (ALERT_FILTER_OPTIONS.getTo() != null)
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), ALERT_FILTER_OPTIONS.getTo()));

        if (ALERT_FILTER_OPTIONS.getMonitors() != null && ALERT_FILTER_OPTIONS.getMonitors().length > 0) {
            predicates.add(root.get("monitor").in(Arrays.asList(ALERT_FILTER_OPTIONS.getMonitors())));
        }

        query.orderBy(builder.desc(root.get("createdAt")));


        return predicates.size() == 0 ? null : builder.and(predicates.toArray(new Predicate[0]));
    }
}
