package com.capgemini.bedwards.almon.almondatastore.repository;

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
            value = "SELECT * FROM Service",
            countQuery = "SELECT count(*) FROM Service",
            nativeQuery = true)
    Page<Service> findServicesByUser(Pageable pageable);

}
