package tech.ada.star.wars.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static tech.ada.star.wars.Constant.QUANTIDADE_REPORTS_TRAIDOR;
import tech.ada.star.wars.controller.commons.ResponseMessage;
import tech.ada.star.wars.data.entity.Item;
import tech.ada.star.wars.data.entity.Localizacao;
import tech.ada.star.wars.data.entity.Rebelde;
import tech.ada.star.wars.data.entity.Recurso;
import tech.ada.star.wars.data.repository.LocalizacaoRepository;
import tech.ada.star.wars.data.repository.RebeldeRepository;
import tech.ada.star.wars.data.repository.RecursoRepository;
import tech.ada.star.wars.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Rebelde adicionarRebelde(Rebelde rebelde) {
        if (!repository.existsByNome(rebelde.getNome())) {
            List<Recurso> inventario = agruparRecursos(rebelde.getInventario());
            inventario.forEach(i -> i.setRebelde(rebelde));
            rebelde.setInventario(inventario);
            return repository.save(rebelde);
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
            return localizacaoRepository.save(localizacao);
        } else {
            throw new BusinessException(
                ResponseMessage.error("Não existe localização para o Rebelde {0}", nomeRebelde)
            );
        }
    }

    public Rebelde reportarRebeldeTraidor(Rebelde _rebelde) {
        String nomeRebelde = _rebelde.getNome();
        Optional<Rebelde> target = repository.findByNome(nomeRebelde);
        if (target.isPresent()) {
            Rebelde rebelde = target.get();
            if (rebelde.getContadorTraidor() < QUANTIDADE_REPORTS_TRAIDOR) {
                Integer contadorTraidor = rebelde.getContadorTraidor();
                rebelde.setContadorTraidor(++contadorTraidor);
                return repository.save(rebelde);
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
            validarTraidor(rebeldeFonte, rebeldeAlvo);
            recursosFonte = agruparRecursos(recursosFonte);
            recursosAlvo = agruparRecursos(recursosAlvo);
            validarEquivalencia(recursosFonte, recursosAlvo);
            List<Recurso> recursosInventarioFonte = recursoRepository.findByRebelde(rebeldeFonte);
            validarInventario(erros, recursosFonte, recursosInventarioFonte, "negociadorFonte");
            List<Recurso> recursosInventarioAlvo = recursoRepository.findByRebelde(rebeldeAlvo);
            validarInventario(erros, recursosAlvo, recursosInventarioAlvo, "negociadorAlvo");

            if (erros.isEmpty()) {
                recursosFonte.forEach(rf -> {
                    Recurso recursoFonte = recursosInventarioFonte.stream()
                            .filter(r -> r.getItem().equals(rf.getItem())).findFirst().orElseThrow();
                    recursoFonte.setQuantidade(recursoFonte.getQuantidade() - rf.getQuantidade());
                    Recurso recursoAlvo = recursosInventarioAlvo.stream()
                            .filter(r -> r.getItem().equals(rf.getItem())).findFirst()
                            .orElse(new Recurso(rf.getItem(), rf.getQuantidade()));
                    if (Objects.isNull(recursoAlvo.getId())) {
                        recursoAlvo.setRebelde(rebeldeAlvo);
                        recursosInventarioAlvo.add(recursoAlvo);
                    } else {
                        recursoAlvo.setQuantidade(recursoAlvo.getQuantidade() + rf.getQuantidade());
                    }
                });
                recursosAlvo.forEach(ra -> {
                    Recurso recursoAlvo = recursosInventarioAlvo.stream()
                            .filter(r -> r.getItem().equals(ra.getItem())).findFirst().orElseThrow();
                    recursoAlvo.setQuantidade(recursoAlvo.getQuantidade() - ra.getQuantidade());
                    Recurso recursoFonte = recursosInventarioFonte.stream()
                            .filter(r -> r.getItem().equals(ra.getItem())).findFirst()
                            .orElse(new Recurso(ra.getItem(), ra.getQuantidade()));
                    if (Objects.isNull(recursoFonte.getId())) {
                        recursoFonte.setRebelde(rebeldeFonte);
                        recursosInventarioFonte.add(recursoFonte);
                    } else {
                        recursoFonte.setQuantidade(recursoFonte.getQuantidade() + ra.getQuantidade());
                    }
                });
                recursoRepository.saveAll(recursosInventarioFonte);
                recursoRepository.saveAll(recursosInventarioAlvo);
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

    private void validarTraidor(Rebelde negociadorFonte, Rebelde negociadorAlvo) {
        List<ResponseMessage> erros = new ArrayList<>();
        if (negociadorFonte.getContadorTraidor().equals(QUANTIDADE_REPORTS_TRAIDOR)) {
            erros.add(ResponseMessage.error(
                    "Negociador {0} foi reportado como traidor, portanto, não pode negociar itens.",
                    negociadorFonte.getNome())
            );
        }
        if (negociadorAlvo.getContadorTraidor().equals(QUANTIDADE_REPORTS_TRAIDOR)) {
            erros.add(ResponseMessage.error(
                    "Negociador {0} foi reportado como traidor, portanto, não pode negociar itens.",
                    negociadorAlvo.getNome())
            );
        }
        if (!erros.isEmpty()) {
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

    private void validarInventario(
            List<ResponseMessage> erros, List<Recurso> recursosNegociacao,
            List<Recurso> recursosInventario, String agente) {

        List<Item> itensNegociacao =
                recursosNegociacao.stream().map(Recurso::getItem).collect(Collectors.toList());
        List<Item> itensInventario =
                recursosInventario.stream().map(Recurso::getItem).collect(Collectors.toList());
        List<Item> indisponiveis = itensNegociacao.stream()
                .filter(item -> !itensInventario.contains(item)).collect(Collectors.toList());
        if (!indisponiveis.isEmpty()) {
            indisponiveis.forEach(item ->
                    erros.add(ResponseMessage.error("Negociador {0} não possui o item {1} para negociação.",
                            agente, item.name())
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
        return new ArrayList<>(recursos.stream().collect(Collectors.toMap(Recurso::getItem, Function.identity(),
                (a, b) -> new Recurso(a.getItem(), a.getQuantidade() + b.getQuantidade()))).values());
    }

    public Rebelde pesquisarRebelde(String nomeRebelde) {
        Optional<Rebelde> rebeldeOptional = repository.findByNome(nomeRebelde);
        if (rebeldeOptional.isPresent()) {
            return rebeldeOptional.get();
        }
        throw new BusinessException(ResponseMessage.error("Rebelde {0} inexistente.", nomeRebelde));
    }

    public List<Rebelde> listarRebeldes() {
        return repository.findAll();
    }
}
