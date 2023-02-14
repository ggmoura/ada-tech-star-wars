package tech.ada.star.wars.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.star.wars.controller.relatorio.RelatorioMediaItemDTO;
import tech.ada.star.wars.controller.relatorio.RelatorioPontosPerdidosDTO;
import tech.ada.star.wars.controller.relatorio.RelatorioPorcentagemDTO;
import tech.ada.star.wars.data.entity.Item;
import tech.ada.star.wars.data.repository.RebeldeRepository;
import tech.ada.star.wars.data.repository.RecursoRepository;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RelatorioService {

    private DecimalFormat df;

    public RelatorioService() {
        df = new DecimalFormat("0.##");
    }

    @Autowired
    private RebeldeRepository rebeldeRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    public RelatorioPorcentagemDTO gerarRelatorioPorcentagemTraidores() {
        Long qtdRebeldes = rebeldeRepository.countRebeldes();
        Long qtdTraidores = rebeldeRepository.countTraidors();
        RelatorioPorcentagemDTO relatorio = new RelatorioPorcentagemDTO();
        relatorio.setNomeRelatorio("Porcentagem de traidores.");
        relatorio.setQuantidadeRebelde(qtdRebeldes);
        relatorio.setQuantidadeTraidor(qtdTraidores);
        if (!qtdRebeldes.equals(0L) && !qtdTraidores.equals(0L)) {
            relatorio.setPorcentagem(df.format((float) qtdTraidores / (qtdRebeldes + qtdTraidores)));
        } else if (qtdRebeldes.equals(0L) && qtdTraidores > 0L) {
            relatorio.setPorcentagem("100.0");
        } else {
            relatorio.setPorcentagem("0.0");
        }
        return relatorio;
    }

    public RelatorioPorcentagemDTO gerarRelatorioPorcentagemRebeldes() {
        Long qtdRebeldes = rebeldeRepository.countRebeldes();
        Long qtdTraidores = rebeldeRepository.countTraidors();
        RelatorioPorcentagemDTO relatorio = new RelatorioPorcentagemDTO();
        relatorio.setNomeRelatorio("Porcentagem de rebeldes.");
        relatorio.setQuantidadeRebelde(qtdRebeldes);
        relatorio.setQuantidadeTraidor(qtdTraidores);
        if (!qtdTraidores.equals(0L) && !qtdRebeldes.equals(0L)) {
            relatorio.setPorcentagem(df.format((float) qtdRebeldes / (qtdRebeldes + qtdTraidores)));
        } else if (qtdTraidores.equals(0L) && qtdRebeldes > 0L) {
            relatorio.setPorcentagem("100.0");
        } else {
            relatorio.setPorcentagem("0.0");
        }
        return relatorio;
    }

    public RelatorioMediaItemDTO gerarRelatorioMediaRecursoPorRebelde() {
        Long qtdRebelde = rebeldeRepository.countRebeldes();
        Map<Item, String> mediaPorItem = new HashMap<>();
        Arrays.stream(Item.values()).forEach(item -> {
           Long qdtPorItem = recursoRepository.somarItems(item).orElse(0L);
           mediaPorItem.put(item, df.format(qtdRebelde > 0 ? qdtPorItem / qtdRebelde : 0));
        });
        RelatorioMediaItemDTO relatorio = new RelatorioMediaItemDTO();
        relatorio.setNomeRelatorio("Quantidade média de cada tipo de recurso por rebelde.");
        relatorio.setMediaRecursos(mediaPorItem);
        return relatorio;
    }

    public RelatorioPontosPerdidosDTO gerarRelatorioPontosPerdidosTraidores() {
        AtomicReference<Long> pontosPerdidos = new AtomicReference<>(0L);
        RelatorioPontosPerdidosDTO relatorio = new RelatorioPontosPerdidosDTO();
        Arrays.stream(Item.values()).forEach(item -> {
            Long qtdItens = recursoRepository.somarItensTraidor(item).orElse(0L);
            pontosPerdidos.updateAndGet(v -> v + qtdItens * item.getPontuacao());
        });
        relatorio.setNomeRelatorio("Pontos perdidos devido a traidores.");
        relatorio.setQuantidadePontos(pontosPerdidos.get());
        return relatorio;
    }



}
