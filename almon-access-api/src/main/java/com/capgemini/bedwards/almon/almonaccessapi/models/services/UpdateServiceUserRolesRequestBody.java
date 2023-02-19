package com.capgemini.bedwards.almon.almonaccessapi.models.services;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString

public class UpdateServiceUserRolesRequestBody {

    private Set<String> standard;
    private Set<String> admin;
}
