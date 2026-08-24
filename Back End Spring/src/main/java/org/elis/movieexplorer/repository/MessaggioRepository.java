package org.elis.movieexplorer.repository;

import java.util.List;
import java.util.Optional;

import org.elis.movieexplorer.model.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessaggioRepository extends JpaRepository<Messaggio, Long>{

	List<Messaggio> findByChatIdOrderByCreatedAtAsc(Long id);

	Optional<Messaggio> findTopByChatIdOrderByCreatedAtAsc(Long chatId);
}
