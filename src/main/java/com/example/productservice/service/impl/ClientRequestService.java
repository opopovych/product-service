package com.example.productservice.service.impl;

import com.example.productservice.model.ClientRequest;
import com.example.productservice.repository.ClientRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientRequestService {

    private final ClientRequestRepository repository;

    public ClientRequestService(ClientRequestRepository repository) {
        this.repository = repository;
    }

    public void save(ClientRequest request) {
        repository.save(request);
    }
}
