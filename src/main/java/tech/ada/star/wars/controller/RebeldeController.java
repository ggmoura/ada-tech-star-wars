package tech.ada.star.wars.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.ada.star.wars.controller.commons.ResponseMessage;
import tech.ada.star.wars.controller.commons.ResponseObject;
import tech.ada.star.wars.controller.model.localizacao.LocalizacaoAtualizacaoRequestDTO;
import tech.ada.star.wars.controller.model.localizacao.LocalizacaoResponseDTO;
import tech.ada.star.wars.controller.model.negociacao.NegociacaoDTO;
import tech.ada.star.wars.controller.model.rebelde.RebeldeRequestDTO;
import tech.ada.star.wars.controller.model.rebelde.RebeldeResponseDTO;
import tech.ada.star.wars.controller.model.rebelde.RebeldeTraidorRequestDTO;
import tech.ada.star.wars.controller.relatorio.RebeldeListResponseDTO;
import tech.ada.star.wars.data.entity.Localizacao;
import tech.ada.star.wars.data.entity.Rebelde;
import tech.ada.star.wars.data.entity.Recurso;
import tech.ada.star.wars.service.RebeldeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "rebeldes")
@Tag(name = "rebelde", description = "API Rebeldes")
public class RebeldeController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RebeldeService service;

    @Operation(summary = "Recuperar Rebelde pelo nome")
    @GetMapping("/{nome}")
    public ResponseObject<RebeldeResponseDTO> consultarRebelde(@PathVariable(value = "nome") String nome) {
        Rebelde rebelde = service.pesquisarRebelde(nome);
        return ResponseObject.of(mapper.map(rebelde, RebeldeResponseDTO.class));
    }

    @Operation(summary = "Listar todos os Rebelde que não foram reportados como traidores")
    @GetMapping
    public ResponseObject<RebeldeListResponseDTO> listarRebeldes() {
        List<RebeldeListResponseDTO> itens = service.listarRebeldes().stream().map(
                rebelde -> mapper.map(rebelde, RebeldeListResponseDTO.class)).collect(Collectors.toList());
        return ResponseObject.page(itens);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Adicionar um rebelde, o nome do rebelde não pode ser repetido")
    @PostMapping
    public ResponseObject<RebeldeResponseDTO> adicionarRebelde(@Valid @RequestBody RebeldeRequestDTO request) {
        Rebelde rebelde = mapper.map(request, Rebelde.class);
        service.adicionarRebelde(rebelde);
        RebeldeResponseDTO response = mapper.map(rebelde, RebeldeResponseDTO.class);
        return ResponseObject.of(response, ResponseMessage.success("Rebelde {0} registrado", response.getNome()));
    }

    @Operation(summary = "Atualizar Localidade do Rebelde")
    @PutMapping("/localizacoes")
    public ResponseObject<LocalizacaoResponseDTO> atualizarLocalizacaoRebelde(
            @Valid @RequestBody LocalizacaoAtualizacaoRequestDTO request) {
        Localizacao source = mapper.map(request, Localizacao.class);
        Localizacao localizacao = service.atualizarLocalizacaoRebelde(source);
        LocalizacaoResponseDTO response = mapper.map(localizacao, LocalizacaoResponseDTO.class);
        return ResponseObject.of(response,
                ResponseMessage.success("Localização {0} atualizada com sucesso!", localizacao.getNomeBase())
        );
    }

    @Operation(summary = "Reportar Rebelde traidor")
    @PatchMapping("/traidores")
    public ResponseObject<Void> reportarRebeldeTraidor(@Valid @RequestBody RebeldeTraidorRequestDTO traidor) {
        Rebelde rebelde = mapper.map(traidor, Rebelde.class);
        service.reportarRebeldeTraidor(rebelde);
        return ResponseObject.of(
                ResponseMessage.success("Rebelde {0}, reportado como traidor!", traidor.getNome())
        );
    }

    @Operation(summary = "Negociar itens entre os rebeldes que não foram reportados como traidor")
    @PostMapping("/negociacoes")
    public ResponseObject<Void> negociarItens(@Valid @RequestBody NegociacaoDTO negociacao) {
        List<Recurso> recursosFonte = negociacao.getNegociadorFonte().getRecusrsosOferecidos().stream()
                .map(recursoDTO -> mapper.map(recursoDTO, Recurso.class)).collect(Collectors.toList());
        List<Recurso> recursosAlvo = negociacao.getNegociadorAlvo().getRecusrsosOferecidos().stream()
                .map(recursoDTO -> mapper.map(recursoDTO, Recurso.class)).collect(Collectors.toList());
        Rebelde negociadorFonte = new Rebelde(negociacao.getNegociadorFonte().getNome());
        Rebelde negociadorAlvo = new Rebelde(negociacao.getNegociadorAlvo().getNome());
        service.negociarRecursos(negociadorFonte, recursosFonte, negociadorAlvo, recursosAlvo);
        return ResponseObject.of(
                ResponseMessage.success("Negociação realizada com sucesso")
        );
    }

}
