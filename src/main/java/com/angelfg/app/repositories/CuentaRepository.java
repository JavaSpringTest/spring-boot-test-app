package com.angelfg.app.repositories;

import com.angelfg.app.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    //List<Cuenta> findAll();
    //Cuenta findById(Long id);
    //void update(Cuenta cuenta);

    @Query("select c from Cuenta c where c.persona = ?1")
    Optional<Cuenta> findByPersona(String persona);

//     Optional<Cuenta> findByPersona(String persona);

}
