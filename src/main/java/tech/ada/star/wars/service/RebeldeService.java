package tech.ada.star.wars.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.star.wars.Constant;
import tech.ada.star.wars.controller.commons.ResponseMessage;
import tech.ada.star.wars.data.entity.Localizacao;
import tech.ada.star.wars.data.entity.Rebelde;
import tech.ada.star.wars.data.entity.Recurso;
import tech.ada.star.wars.data.repository.LocalizacaoRepository;
import tech.ada.star.wars.data.repository.RebeldeRepository;
import tech.ada.star.wars.data.repository.RecursoRepository;
import tech.ada.star.wars.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RebeldeService {

    @Autowired
    private RebeldeRepository repository;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private Logger logger;

    public void adicionarRebelde(Rebelde rebelde) {
        if (!repository.existsByNome(rebelde.getNome())) {
            List<Recurso> inventario = agruparRecursos(rebelde.getInventario());
            inventario.forEach(i -> i.setRebelde(rebelde));
            rebelde.setInventario(inventario);
            repository.save(rebelde);
        } else {
            throw new BusinessException(
                ResponseMessage.error("Nome {0} já está sendo utilizado", rebelde.getNome())
            );
        }
    }

    public Localizacao atualizarLocalizacaoRebelde(Localizacao source) {
        String nomeRebelde = source.getRebelde().getNome();
        Optional<Localizacao> target = localizacaoRepository.findByRebeldeNome(nomeRebelde);
        if (target.isPresent()) {
            Localizacao localizacao = target.get();
            localizacao.setLatitude(source.getLatitude());
            localizacao.setLongitude(source.getLongitude());
            localizacao.setNomeBase(source.getNomeBase());
            localizacaoRepository.save(localizacao);
            return localizacao;
        } else {
            throw new BusinessException(
                ResponseMessage.error("Não existe localização para o Rebelde {0}", nomeRebelde)
            );
        }
    }

    public void reportarRebeldeTraidor(Rebelde _rebelde) {
        String nomeRebelde = _rebelde.getNome();
        Optional<Rebelde> target = repository.findByNome(nomeRebelde);
        if (target.isPresent()) {
            Rebelde rebelde = target.get();
            if (rebelde.getContadorTraidor() < Constant.QUANTIDADE_REPORTS_TRAIDOR) {
                Integer contadorTraidor = rebelde.getContadorTraidor();
                rebelde.setContadorTraidor(++contadorTraidor);
                repository.save(rebelde);
            } else {
                throw new BusinessException(
                        ResponseMessage.error("O Rebelde {0} já foi definido como traidor.", nomeRebelde)
                );
            }
        } else  {
            throw new BusinessException(
                    ResponseMessage.error("Rebelde {0} inexistente.", nomeRebelde)
            );
        }
    }

    public void negociarRecursos(
            Rebelde negociadorFonte, List<Recurso> recursosFonte,
            Rebelde negociadorAlvo, List<Recurso> recursosAlvo) {
        List<ResponseMessage> erros = new ArrayList<>();
        Optional<Rebelde> rebeldeFonteOptional = repository.findByNome(negociadorFonte.getNome());
        Optional<Rebelde> rebeldeAlvoOptional = repository.findByNome(negociadorAlvo.getNome());
        if (rebeldeFonteOptional.isPresent() && rebeldeAlvoOptional.isPresent()) {
            Rebelde rebeldeFonte = rebeldeFonteOptional.get();
            Rebelde rebeldeAlvo = rebeldeAlvoOptional.get();
            recursosFonte = agruparRecursos(recursosFonte);
            recursosAlvo = agruparRecursos(recursosAlvo);
            validarEquivalencia(recursosFonte, recursosAlvo);
            List<Recurso> recursosInventarioFonte = recursoRepository.findByRebelde(rebeldeFonte);
            validarInventário(erros, recursosFonte, recursosInventarioFonte, "negociadorFonte");
            List<Recurso> recursosInventarioAlvo = recursoRepository.findByRebelde(rebeldeAlvo);
            validarInventário(erros, recursosAlvo, recursosInventarioAlvo, "negociadorAlvo");

            if (erros.isEmpty()) {
                recursosFonte.forEach(rf -> {
                    Recurso recursoFonte = recursosInventarioFonte.stream()
                            .filter(r -> r.getItem().equals(rf.getItem())).findFirst().get();
                    recursoFonte.setQuantidade(recursoFonte.getQuantidade() - rf.getQuantidade());
                    Recurso recursoAlvo = recursosInventarioAlvo.stream()
                            .filter(r -> r.getItem().equals(rf.getItem())).findFirst().get();
                    recursoAlvo.setQuantidade(recursoAlvo.getQuantidade() + rf.getQuantidade());
                });
                recursosAlvo.forEach(ra -> {
                    Recurso recursoAlvo = recursosInventarioAlvo.stream()
                            .filter(r -> r.getItem().equals(ra.getItem())).findFirst().get();
                    recursoAlvo.setQuantidade(recursoAlvo.getQuantidade() - ra.getQuantidade());
                    Recurso recursoFonte = recursosInventarioFonte.stream()
                            .filter(r -> r.getItem().equals(ra.getItem())).findFirst().get();
                    recursoFonte.setQuantidade(recursoFonte.getQuantidade() + ra.getQuantidade());
                });
                recursoRepository.saveAll(recursosInventarioFonte);
                recursoRepository.saveAll(recursosInventarioAlvo);
                System.out.println();
            } else {
                throw new BusinessException(erros);
            }
        } else {
            if (rebeldeFonteOptional.isEmpty()) {
                erros.add(ResponseMessage.error("Negociador Fonte {0} inexistente.", negociadorFonte.getNome()));
            }
            if (rebeldeAlvoOptional.isEmpty()) {
                erros.add(ResponseMessage.error("Negociador Alvo {0} inexistente.", negociadorAlvo.getNome()));
            }
            throw new BusinessException(erros);
        }
    }

    private void validarEquivalencia(List<Recurso> recursosFonte, List<Recurso> recursosAlvo) {
        Long totalPontosFonte =  recursosFonte.stream().map(r -> r.getItem().getPontuacao() * r.getQuantidade())
                .reduce(0L, Long::sum);
        Long totalPontosAlvo =  recursosAlvo.stream().map(r -> r.getItem().getPontuacao() * r.getQuantidade())
                .reduce(0L, Long::sum);
        if (!totalPontosFonte.equals(totalPontosAlvo)) {
            throw new BusinessException(
                    ResponseMessage.error("Não existe equivalência nos itens a serem trocados, Fonte[{0}] Alvo[{1}]",
                            totalPontosFonte.toString(), totalPontosAlvo.toString())
            );
        }

    }

    private void validarInventário(
            List<ResponseMessage> erros, List<Recurso> recursosNegociacao,
            List<Recurso> recursosInventario, String agente) {
        List<Recurso> indisponiveis =
                recursosInventario.stream().filter(r -> recursosNegociacao.contains(r)).collect(Collectors.toList());
        if (!indisponiveis.isEmpty()) {
            indisponiveis.forEach(recurso ->
                    erros.add(ResponseMessage.error(
                            "Negociador {0} não possui o item {1}.", agente, recurso.getItem().name())
                    )
            );
        }
        recursosNegociacao.forEach(recurso -> {
            Optional<Recurso> recursoInventario = recursosInventario.stream()
                    .filter(r -> r.getItem().equals(recurso.getItem())).findFirst();
            if (recursoInventario.isPresent() && recurso.getQuantidade() > recursoInventario.get().getQuantidade()) {
                erros.add(ResponseMessage.error(
                        "A quantidade do item {0}, do negociador {1} é menor do que a disponível no inventário, " +
                                "quantidade atual {2}.",
                        recurso.getItem().toString(), agente, recursoInventario.get().getQuantidade().toString())
                );
            }
        });

    }

    private List<Recurso> agruparRecursos(List<Recurso> recursos) {
        return recursos.stream().collect(Collectors.toMap(rec -> rec.getItem(), Function.identity(),
                (a, b) -> new Recurso(a.getItem(), a.getQuantidade() + b.getQuantidade())))
                .values().stream().collect(Collectors.toList());
    }

}
