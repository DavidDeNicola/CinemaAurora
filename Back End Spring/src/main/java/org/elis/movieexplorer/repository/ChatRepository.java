package org.elis.movieexplorer.repository;

import java.util.List;
import org.elis.movieexplorer.model.Chat;
import org.elis.movieexplorer.model.enums.StatoChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	List<Chat> findAllByOrderByCreatedAtAsc();

	List<Chat> findAllByUtenteIdOrderByCreatedAtAsc(Long utenteId);

	@Query("""
		    SELECT c
		    FROM Chat c
		    LEFT JOIN c.messaggi m
		    GROUP BY c
		    ORDER BY MAX(m.createdAt) DESC
		""")
	List<Chat> findForStaff();
	
	// trova le chat di un utente ordinate per ultimo messaggio 
	@Query("""
		    SELECT c
		    FROM Chat c
		    LEFT JOIN c.messaggi m
		    WHERE c.utente.id = :id
		    GROUP BY c
		    ORDER BY MAX(m.createdAt) DESC
		""")
	List<Chat> findForUser(@Param("id") Long id);
	
	List<Chat> findByStato(StatoChat stato);
	
	@Query("""
		    SELECT COUNT(DISTINCT c)
		    FROM Chat c
		    WHERE c.messaggiSospesoPerStaff IS TRUE
		""")
	Integer countNotReadedChatForStaff();
	
	@Query("""
		    SELECT c
		    FROM Chat c
		    JOIN c.messaggi m
		    WHERE  c.messaggiSospesoPerStaff IS TRUE
		    GROUP BY c
		    ORDER BY MAX(m.createdAt) DESC
		""")
		List<Chat> findNotReadedChatForStaff();
	
	@Query("""
		    SELECT COUNT(DISTINCT c)
		    FROM Chat c
		    WHERE c.utente.id = :id AND  c.messaggiSospesoPerCliente IS TRUE
		""")
	Integer countNotReadedChatForUser(@Param("id") Long id);
	
	@Query("""
		    SELECT c
		    FROM Chat c
		    JOIN c.messaggi m
		    WHERE c.utente.id = :id
		      AND c.messaggiSospesoPerCliente IS TRUE
		    GROUP BY c
		    ORDER BY MAX(m.createdAt) DESC
		""")
	List<Chat> findNotReadedChatForUser(@Param("id") Long id);
	
	List<Chat> findAllByUtenteId(Long utenteId);
}
