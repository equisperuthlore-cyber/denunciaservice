package com.espinozaquispe.denunciasservice.repository;

import com.espinozaquispe.denunciasservice.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {

    List<Denuncia> findByDni(String dni);
}
