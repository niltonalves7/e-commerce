package com.project.ecomerce.client.controller;

import com.project.ecomerce.client.dto.request.RegisterClientRequestDTO;
import com.project.ecomerce.client.dto.response.RegisterClientResponseDTO;
import com.project.ecomerce.client.service.ClientService;
import com.project.ecomerce.client.service.ClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<RegisterClientResponseDTO> createClient(@RequestBody @Valid RegisterClientRequestDTO clientDto) {

        RegisterClientResponseDTO response = clientService.createClient(clientDto);
        return ResponseEntity
                .created(URI.create("/clients/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RegisterClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterClientResponseDTO> getClientById(@PathVariable UUID id) {
                return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterClientResponseDTO> updateClient(@PathVariable UUID id, @RequestBody @Valid RegisterClientRequestDTO clientDto) {
        return ResponseEntity.ok(clientService.updateClient(id, clientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}