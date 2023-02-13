package tech.ada.star.wars.data.repository;

import org.springframework.data.repository.Repository;
import tech.ada.star.wars.data.entity.Localizacao;

import java.util.Optional;

public interface LocalizacaoRepository extends Repository<Localizacao, Long> {

    Localizacao save(Localizacao source);

    Optional<Localizacao> findByRebeldeNome(String nome);
}
