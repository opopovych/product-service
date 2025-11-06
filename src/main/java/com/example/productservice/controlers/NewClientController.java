package com.example.productservice.controlers;

import com.example.productservice.model.ClientRequest;
import com.example.productservice.repository.ClientRequestRepository;
import com.example.productservice.service.ProductRangeService;
import com.example.productservice.service.impl.ClientRequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/new-client")
public class NewClientController {
    @Autowired
    private ClientRequestRepository repository;
    @Autowired
    private ProductRangeService productRangeService;

    private final ClientRequestService service;

    @Autowired
    public NewClientController(ClientRequestService service) {
        this.service = service;
    }

    // Головна сторінка нового клієнта
    @GetMapping
    public String showNewClientHome() {
        return "new-client"; // new-client.html (інформація, умови, кнопка)
    }

    // Відображення форми
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("clientRequest", new ClientRequest()); // для прив'язки форми
        return "new-client-form"; // new-client-form.html
    }
    @GetMapping("/all-clients")
    public String showAllClients(Model model) {
        List<ClientRequest> clients = repository.findAll();
        model.addAttribute("clients", clients); // для перегляду клієнтів
        return "clients";
    }

    // Обробка запиту
    @PostMapping("/request")
    public ResponseEntity<String> handleRequest(@ModelAttribute("clientRequest") ClientRequest clientRequest) {
        try {
            service.save(clientRequest);

            String html = """
        <!DOCTYPE html>
        <html lang="uk">
        <head>
            <meta charset="UTF-8">
            <title>Дякуємо</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <style>
                .custom-modal {
                    border-radius: 15px;
                    border: none;
                    overflow: hidden;
                    box-shadow: 0 0 15px rgba(0,0,0,0.3);
                }
                .custom-modal .modal-header {
                    background: linear-gradient(to right, #6F4E37, #3E2723);
                    color: white;
                    border: none;
                }
                .custom-modal .modal-body {
                    font-size: 1.1em;
                    color: #3E2723;
                    padding: 20px;
                    text-align: center;
                }
                .btn-coffee {
                    background-color: #6F4E37;
                    color: #fff;
                    font-weight: bold;
                    border-radius: 25px;
                    padding: 10px 25px;
                    border: none;
                    transition: 0.3s;
                }
                .btn-coffee:hover {
                    background-color: #4B3621;
                    transform: scale(1.05);
                }
            </style>
        </head>
        <body>
        <div class="modal fade show" id="thankYouModal" tabindex="-1" style="display:block;" aria-modal="true" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content custom-modal">
                    <div class="modal-header">
                        <h5 class="modal-title">🙏 Дякуємо!</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Ми зв'яжемося з вами через Viber найближчим часом.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-coffee" data-bs-dismiss="modal">Добре ☕</button>
                    </div>
                </div>
            </div>
        </div>
        
        <script>
            const modalEl = document.getElementById('thankYouModal');
            const modal = new bootstrap.Modal(modalEl);
            modal.show();

            // у будь-якому випадку закриття модалки -> редірект на /new-client
            modalEl.addEventListener('hidden.bs.modal', function () {
                window.location.href = '/new-client';
            });
        </script>
        </body>
        </html>
        """;

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.TEXT_HTML)
                    .body(html);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Помилка при збереженні клієнта: " + e.getMessage());
        }
    }

    @GetMapping("/range")
    public ResponseEntity<String> viewExcel() {
        return productRangeService.viewExcelFromDB("M");
    }
    // Видалення клієнта по id
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/new-client/all-clients";
    }


}


