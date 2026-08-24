package org.elis.movieexplorer.repository;

import org.elis.movieexplorer.model.Posto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostoRepository extends JpaRepository<Posto, Long> {

    @Query("SELECT COUNT(p) from Posto p where p.sala.id = :idSala")
    public Integer getNumeriPostiSala(Long idSala);

    public Optional<Posto> findById(Long id);

    public Optional<List<Posto>> getPostiBySalaId(Long idSala);

    @Query("SELECT p FROM Posto p WHERE p.sala.id = :idSala")
    public List<Posto> findBySala(Long idSala);

    @Query("SELECT b.posto.id FROM Biglietto b WHERE b.spettacolo.id = :idSpettacolo")
    public List<Long> findIdPostiOccupatiBySpettacolo(Long idSpettacolo);
}
