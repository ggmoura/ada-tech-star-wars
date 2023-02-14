package tech.ada.star.wars.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import tech.ada.star.wars.data.entity.Item;
import tech.ada.star.wars.data.entity.Rebelde;
import tech.ada.star.wars.data.entity.Recurso;

import java.util.List;
import java.util.Optional;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    List<Recurso> findByRebelde(Rebelde rebeldeFonte);

    @Query("select sum(r.quantidade) from Recurso r where r.item = :item")
    Optional<Long> somarItems(Item item);

    @Query("select sum(r.quantidade) from Recurso r where r.item = :item and r.rebelde.contadorTraidor = 3")
    Optional<Long> somarItensTraidor(Item item);

}
