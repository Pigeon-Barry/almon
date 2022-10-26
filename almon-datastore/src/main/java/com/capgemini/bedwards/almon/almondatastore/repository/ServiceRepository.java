package com.capgemini.bedwards.almon.almondatastore.repository;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    boolean existsById(String key);


    @Query(
            value = "SELECT * FROM service where id IN (select REPLACE(REPLACE(authority,\"SERVICE_\",\"\"),\"_CAN_VIEW\",\"\") from authority where \n" +
                    "(authority in (select authorities_authority FROM authority_roles where roles_name IN (SELECT roles_name FROM user_roles where users_id = \"ab7f370a-f66d-4118-8e0d-f44a3c96e80e\"))\n" +
                    "OR(authority in (select authorities_authority FROM authority_users where  users_id = \"ab7f370a-f66d-4118-8e0d-f44a3c96e80e\"))) \n" +
                    "and authority like \"SERVICE_%_CAN_VIEW\")",
            countQuery = "SELECT count(*) FROM service where id IN (select REPLACE(REPLACE(authority,\"SERVICE_\",\"\"),\"_CAN_VIEW\",\"\") from authority where \n" +
                    "(authority in (select authorities_authority FROM authority_roles where roles_name IN (SELECT roles_name FROM user_roles where users_id = \"ab7f370a-f66d-4118-8e0d-f44a3c96e80e\"))\n" +
                    "OR(authority in (select authorities_authority FROM authority_users where  users_id = \"ab7f370a-f66d-4118-8e0d-f44a3c96e80e\"))) \n" +
                    "and authority like \"SERVICE_%_CAN_VIEW\")",
            nativeQuery = true)
    Page<Service> findServicesByUser(Pageable pageable, User user);

}
