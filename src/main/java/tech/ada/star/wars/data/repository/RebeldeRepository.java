package tech.ada.star.wars.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import tech.ada.star.wars.data.entity.Rebelde;

import java.util.List;
import java.util.Optional;

public interface RebeldeRepository extends Repository<Rebelde, Long> {

    Rebelde save(Rebelde rebelde);

    Boolean existsByNome(String nome);

    Boolean existsById(Long id);

    Optional<Rebelde> findByNome(String nomeRebelde);

    @Query("select count(r) from Rebelde r where r.contadorTraidor < 3")
    Long countRebeldes();

    @Query("select count(r) from Rebelde r where r.contadorTraidor = 3")
    Long countTraidors();

    List<Rebelde> findAll();
}
