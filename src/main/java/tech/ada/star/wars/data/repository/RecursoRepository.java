package tech.ada.star.wars.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import tech.ada.star.wars.data.entity.Rebelde;
import tech.ada.star.wars.data.entity.Recurso;

import java.util.List;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    List<Recurso> findByRebelde(Rebelde rebeldeFonte);

}
