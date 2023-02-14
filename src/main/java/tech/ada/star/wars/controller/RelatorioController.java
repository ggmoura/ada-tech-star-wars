package tech.ada.star.wars.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ada.star.wars.controller.commons.ResponseObject;
import tech.ada.star.wars.controller.relatorio.RelatorioMediaItemDTO;
import tech.ada.star.wars.controller.relatorio.RelatorioPontosPerdidosDTO;
import tech.ada.star.wars.controller.relatorio.RelatorioPorcentagemDTO;
import tech.ada.star.wars.service.RelatorioService;

@RestController
@RequestMapping(value = "relatorios")
@Tag(name = "relatorio", description = "API Relatórios")
public class RelatorioController {

    @Autowired
    private RelatorioService service;

    @Operation(summary = "Porcentagem de traidores.")
    @GetMapping("/porcentagem-traidores")
    public ResponseObject<RelatorioPorcentagemDTO> gerarRelatorioPorcentagemTraidores() {
        return ResponseObject.of(service.gerarRelatorioPorcentagemTraidores());
    }

    @Operation(summary = "Porcentagem de rebeldes.")
    @GetMapping("/porcentagem-rebeldes")
    public ResponseObject<RelatorioPorcentagemDTO> gerarRelatorioPorcentagemRebeldes() {
        return ResponseObject.of(service.gerarRelatorioPorcentagemRebeldes());
    }

    @Operation(summary = "Quantidade média de cada tipo de recurso por rebelde (Ex: 2 armas por rebelde).")
    @GetMapping("/media-recursos")
    public ResponseObject<RelatorioMediaItemDTO> gerarRelatorioMediaRecursoPorRebelde() {
        return ResponseObject.of(service.gerarRelatorioMediaRecursoPorRebelde());
    }

    @Operation(summary = "Pontos perdidos devido a traidores.")
    @GetMapping("/pontos-perdidos")
    public ResponseObject<RelatorioPontosPerdidosDTO> gerarRelatorioPontosPerdidosTraidores() {
        return ResponseObject.of(service.gerarRelatorioPontosPerdidosTraidores());
    }

}
