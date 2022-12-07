package com.capgemini.bedwards.almon.almonaccessweb.model.services;

import java.util.Set;
import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class UpdateServiceUserRolesRequestBody {

    private Set<String> standard;
    private Set<String> admin;
}
