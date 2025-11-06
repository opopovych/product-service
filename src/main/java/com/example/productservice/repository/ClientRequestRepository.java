package com.example.productservice.repository;

import com.example.productservice.model.ClientRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRequestRepository extends JpaRepository<ClientRequest, Long> {
}
