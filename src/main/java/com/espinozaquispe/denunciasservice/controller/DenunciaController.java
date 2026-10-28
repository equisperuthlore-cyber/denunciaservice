package com.espinozaquispe.denunciasservice.controller;

import com.espinozaquispe.denunciasservice.entity.Denuncia;
import com.espinozaquispe.denunciasservice.repository.DenunciaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/denuncias")
@CrossOrigin(origins = "*")
public class DenunciaController {

    private final DenunciaRepository denunciaRepository;

    public DenunciaController(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    @GetMapping({"", "/"})
    public List<Denuncia> listarDenuncias(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) Integer limit) {
        if (offset < 0 || (limit != null && limit <= 0)) {
            throw new IllegalArgumentException("offset debe ser >= 0 y limit debe ser > 0");
        }
        if (limit == null) {
            return denunciaRepository.findAll();
        }
        return denunciaRepository.findAll(PageRequest.of(offset / limit, limit)).getContent();
    }

    @PostMapping({"", "/"})
    public Denuncia crearDenuncia(@RequestBody Denuncia denuncia) {
        denuncia.setEstado(1);
        return denunciaRepository.save(denuncia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> obtenerDenuncia(@PathVariable Integer id) {
        return denunciaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ciudadano/{dni}")
    public List<Denuncia> listarPorDni(@PathVariable String dni) {
        return denunciaRepository.findByDni(dni);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atenderDenuncia(@PathVariable Integer id) {
        return denunciaRepository.findById(id)
                .map(denuncia -> {
                    if (Integer.valueOf(1).equals(denuncia.getEstado())) {
                        denuncia.setEstado(0);
                        denunciaRepository.save(denuncia);
                    }
                    return ResponseEntity.ok(denuncia);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
