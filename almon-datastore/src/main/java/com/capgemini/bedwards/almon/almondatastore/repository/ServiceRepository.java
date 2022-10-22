package com.capgemini.bedwards.almon.almondatastore.repository;

import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    boolean existsById(String key);

    Page<Service> findServicesByUsersIsOrOwnerIs(Pageable pageable, User user, User owner);
    
    default Page<Service> findServicesByUsersIsOrOwnerIs(Pageable pageable, User user){
        return findServicesByUsersIsOrOwnerIs(pageable,user,user);
    }
}
