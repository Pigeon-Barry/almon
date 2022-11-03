package com.capgemini.bedwards.almon.almondatastore.models.service;

import com.capgemini.bedwards.almon.almondatastore.models.monitor.Monitor;
import com.capgemini.bedwards.almon.almondatastore.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    @Pattern(regexp = Constants.SERVICE_REGEX, message = Constants.SERVICE_REGEX_INVALID_MESSAGE)
    @NotNull
    @Length(max = Constants.SERVICE_ID_MAX_LENGTH)
    @Column(length = Constants.SERVICE_ID_MAX_LENGTH)
    private String id;

    @NotBlank
    private String name;

    private String description;

    @OneToMany(mappedBy = "id.service", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Monitor> monitors;

    @Builder.Default
    private boolean enabled = true;


    public String toString() {
        return id + "-" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id);
    }
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
