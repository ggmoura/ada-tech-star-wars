package tech.ada.star.wars.data.repository;

import org.springframework.data.repository.Repository;
import tech.ada.star.wars.data.entity.Rebelde;

import java.util.Optional;

public interface RebeldeRepository extends Repository<Rebelde, Long> {

    Rebelde save(Rebelde rebelde);

    Boolean existsByNome(String nome);

    Boolean existsById(Long id);

    Optional<Rebelde> findByNome(String nomeRebelde);
}
