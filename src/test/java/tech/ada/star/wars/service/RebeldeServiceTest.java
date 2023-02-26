package tech.ada.star.wars.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import tech.ada.star.wars.data.entity.*;
import tech.ada.star.wars.data.repository.LocalizacaoRepository;
import tech.ada.star.wars.data.repository.RebeldeRepository;
import tech.ada.star.wars.data.repository.RecursoRepository;
import tech.ada.star.wars.exception.BusinessException;

import java.util.*;


@ExtendWith(MockitoExtension.class)
class RebeldeServiceTest {

    @InjectMocks
    private RebeldeService service;

    @Mock
    private RebeldeRepository repository;

    @Mock
    private LocalizacaoRepository localizacaoRepository;

    @Mock
    private RecursoRepository recursoRepository;

    @Test
    @DisplayName("Deve adicionar Rebelde")
    void deveAdicionarRebelde() {
        Rebelde rebelde = getRebelde("Rebelde");
        service.adicionarRebelde(rebelde);
        verify(repository, times(1)).existsByNome(rebelde.getNome());
        verify(repository, times(1)).save(rebelde);
    }

    @Test
    @DisplayName("Deve levantar exceção BusinessException quano o nome do Rebelde for repetido")
    void deveValidarNomeRebeldeRepetido() {
        Rebelde rebelde = getRebelde("Rebelde");
        when(repository.existsByNome(rebelde.getNome())).thenReturn(Boolean.TRUE);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.adicionarRebelde(rebelde);
        });
        verify(repository, times(1)).existsByNome(rebelde.getNome());
        verify(repository, times(0)).save(rebelde);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Nome {0} já está sendo utilizado", exception.getErrors().get(0).getText());
    }


    @Test
    @DisplayName("Deve agrupar os recursos iguais somando as quantidades")
    void deveAgruparRecursosDoRebelde() {
        Rebelde rebelde = getRebelde("Rebelde");
        Recurso recurso = new Recurso();
        recurso.setQuantidade(20L);
        recurso.setItem(Item.AGUA);
        rebelde.getInventario().add(recurso);
        when(repository.save(rebelde)).thenReturn(rebelde);
        Rebelde novoRebelde = service.adicionarRebelde(rebelde);
        assertThat(1, is(novoRebelde.getInventario().size()));
        assertThat(30L, is(novoRebelde.getInventario().get(0).getQuantidade()));
    }

    @Test
    @DisplayName("Deve atualizar a localização do rebelde")
    void deveAtualizarLocalizacaoRebelde() {
        Localizacao localizacao = getLocalizacao();
        localizacao.setRebelde(getRebeldeSingle("Rebelde"));
        Localizacao l = new Localizacao();
        when(localizacaoRepository.findByRebeldeNome(localizacao.getRebelde().getNome())).thenReturn(Optional.of(l));
        when(localizacaoRepository.save(l)).thenReturn(l);
        l = service.atualizarLocalizacaoRebelde(localizacao);
        assertEquals(localizacao.getNomeBase(), l.getNomeBase());
        assertEquals(localizacao.getLatitude(), l.getLatitude());
        assertEquals(localizacao.getLongitude(), l.getLongitude());
    }

    @Test
    @DisplayName("Deve levantar exceção BusinessException o Rebelde não estiver cadastrado")
    void deveLevantarExcecaoQuandoRebeldeNaoEstiverCadastrado() {
        Localizacao localizacao = getLocalizacao();
        localizacao.setRebelde(getRebeldeSingle("Rebelde"));
        Localizacao l = new Localizacao();
        String nome = localizacao.getRebelde().getNome();
        when(localizacaoRepository.findByRebeldeNome(nome)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.atualizarLocalizacaoRebelde(localizacao);
        });
        verify(localizacaoRepository, times(1)).findByRebeldeNome(nome);
        verify(localizacaoRepository, times(0)).save(l);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe localização para o Rebelde {0}", exception.getErrors().get(0).getText());
    }

    @Test
    void reportarRebeldeTraidor() {
        Rebelde rebelde = getRebelde("Rebelde");
        rebelde.setContadorTraidor(0);
        when(repository.findByNome(rebelde.getNome())).thenReturn(Optional.of(rebelde));
        service.reportarRebeldeTraidor(rebelde);
        verify(repository, times(1)).findByNome(rebelde.getNome());
        verify(repository, times(1)).save(rebelde);
        assertThat(1, is(rebelde.getContadorTraidor()));
    }

    @Test
    @DisplayName("Deve levantar exceção BusinessException quando passar nome não cadastrado")
    void deveLevantarExcecaoQuandoReportarRebeldeTraidorComNomeNaoCadastrado() {
        Rebelde rebelde = getRebelde("Rebelde");
        when(repository.findByNome(rebelde.getNome())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.reportarRebeldeTraidor(rebelde);
        });
        verify(repository, times(1)).findByNome(rebelde.getNome());
        verify(repository, times(0)).save(rebelde);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Rebelde {0} inexistente.", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve levantar exceção BusinessException quando ja possuir tres reports")
    void deveLevantarExcecaoQuandoReportarRebeldeTraidorComTresReportsAnteriores() {
        Rebelde rebelde = getRebelde("Rebelde");
        rebelde.setContadorTraidor(3);
        when(repository.findByNome(rebelde.getNome())).thenReturn(Optional.of(rebelde));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.reportarRebeldeTraidor(rebelde);
        });
        verify(repository, times(1)).findByNome(rebelde.getNome());
        verify(repository, times(0)).save(rebelde);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("O Rebelde {0} já foi definido como traidor.", exception.getErrors().get(0).getText());
    }

    @Test
    void deveNegociarRecursos() {
        Rebelde fonte = getRebelde("Rebelde Fonte");
        fonte.setContadorTraidor(0);
        Rebelde alvo = getRebelde("Rebelde Alvo");
        alvo.setNome("Rebelde Alvo");
        alvo.setContadorTraidor(0);
        fonte.setInventario(new ArrayList<>());
        Recurso recurso = new Recurso();
        recurso.setQuantidade(1L);
        recurso.setItem(Item.ARMA);
        fonte.getInventario().add(recurso);
        when(repository.findByNome(fonte.getNome())).thenReturn(Optional.of(fonte));
        when(repository.findByNome(alvo.getNome())).thenReturn(Optional.of(alvo));
        List<Recurso> listRecursosFonteNegociar = new ArrayList<>();
        Recurso rFonteNegociar = getRecurso(20L, Item.AGUA);
        Recurso r2FonteNegociar = getRecurso(2L, Item.MUNICAO);
        listRecursosFonteNegociar.add(rFonteNegociar);
        listRecursosFonteNegociar.add(r2FonteNegociar);
        List<Recurso> listRecursosFonteSalvo = new ArrayList<>();
        Recurso rFonteSalvo = getRecurso(10L, Item.MUNICAO);
        rFonteSalvo.setId(1L);
        Recurso r2FonteSalvo = getRecurso(20L, Item.AGUA);
        r2FonteSalvo.setId(2L);
        listRecursosFonteSalvo.add(rFonteSalvo);
        listRecursosFonteSalvo.add(r2FonteSalvo);
        List<Recurso> listRecursosAlvoNegociar = new ArrayList<>();
        Recurso rAlvoNegociar = getRecurso(46L, Item.COMIDA);
        listRecursosAlvoNegociar.add(rAlvoNegociar);
        List<Recurso> listRecursosAlvoSalvo = new ArrayList<>();
        Recurso rAlvo = getRecurso(50L, Item.COMIDA);
        rAlvo.setId(3L);
        listRecursosAlvoSalvo.add(rAlvo);
        when(recursoRepository.findByRebelde(alvo)).thenReturn(listRecursosFonteSalvo, listRecursosAlvoSalvo);
        service.negociarRecursos(fonte, listRecursosFonteNegociar, alvo, listRecursosAlvoNegociar);

    }

    @Test
    void deveLevantarExcecaoQuandoNegociarRecursosDeRebeldeInexistente() {
        Rebelde fonte = getRebelde("Rebelde Fonte");
        Rebelde alvo = getRebelde("Rebelde Alvo");
        when(repository.findByNome(fonte.getNome())).thenReturn(Optional.empty());
        when(repository.findByNome(alvo.getNome())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            List<Recurso> recursos = Collections.emptyList();
            service.negociarRecursos(fonte, recursos, alvo, recursos);
        });
        verify(repository, times(2)).findByNome(any());
        verify(recursoRepository, times(0)).findByRebelde(any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(2));
        assertEquals("Negociador Fonte {0} inexistente.", exception.getErrors().get(0).getText());
        assertEquals("Negociador Alvo {0} inexistente.", exception.getErrors().get(1).getText());
    }

    @Test
    void deveValidarQueRebeldeTraidorNaoPodeMovimentarItens() {
        Rebelde fonte = getRebelde("Rebelde Fonte");
        fonte.setContadorTraidor(3);
        Rebelde alvo = getRebelde("Rebelde Alvo");
        alvo.setContadorTraidor(3);
        when(repository.findByNome(fonte.getNome())).thenReturn(Optional.of(fonte));
        when(repository.findByNome(alvo.getNome())).thenReturn(Optional.of(alvo));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            List<Recurso> recursos = Collections.emptyList();
            service.negociarRecursos(fonte, recursos, alvo, recursos);
        });
        verify(repository, times(2)).findByNome(any());
        verify(recursoRepository, times(0)).findByRebelde(any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(2));
        assertEquals("Negociador {0} foi reportado como traidor, portanto, não pode negociar itens.",
                exception.getErrors().get(0).getText());
        assertEquals("Negociador {0} foi reportado como traidor, portanto, não pode negociar itens.",
                exception.getErrors().get(1).getText());
    }


    @Test
    void deveLevantarExcecaoQuandoAQuantidadeDePontosNaoForEquivalenteParaATroca() {
        Rebelde fonte = getRebelde("Rebelde Fonte");
        fonte.setContadorTraidor(0);
        Rebelde alvo = getRebelde("Rebelde Alvo");
        alvo.setContadorTraidor(0);
        fonte.setInventario(new ArrayList<>());
        Recurso recurso = new Recurso();
        recurso.setQuantidade(1L);
        recurso.setItem(Item.ARMA);
        fonte.getInventario().add(recurso);
        when(repository.findByNome(fonte.getNome())).thenReturn(Optional.of(fonte));
        when(repository.findByNome(alvo.getNome())).thenReturn(Optional.of(alvo));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            List<Recurso> recursos = Collections.emptyList();
            service.negociarRecursos(fonte, fonte.getInventario(), alvo, alvo.getInventario());
        });
        verify(repository, times(2)).findByNome(any());
        verify(recursoRepository, times(0)).findByRebelde(any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe equivalência nos itens a serem trocados, Fonte[{0}] Alvo[{1}]",
                exception.getErrors().get(0).getText());
    }
    
    @Test
    void deveLevantarExcecaoQuandoORebeldeNaoPossuirItensSuficientesParaTroca() {
        Rebelde fonte = getRebelde("Rebelde Fonte");
        fonte.setContadorTraidor(0);
        Rebelde alvo = getRebelde("Rebelde Alvo");
        alvo.setNome("Rebelde Alvo");
        alvo.setContadorTraidor(0);
        fonte.setInventario(new ArrayList<>());
        Recurso recurso = new Recurso();
        recurso.setQuantidade(1L);
        recurso.setItem(Item.ARMA);
        fonte.getInventario().add(recurso);
        when(repository.findByNome(fonte.getNome())).thenReturn(Optional.of(fonte));
        when(repository.findByNome(alvo.getNome())).thenReturn(Optional.of(alvo));
        List<Recurso> listRecursosFonteNegociar = new ArrayList<>();
        Recurso rFonteNegociar = getRecurso(20L, Item.AGUA);
        Recurso r2FonteNegociar = getRecurso(2L, Item.MUNICAO);
        listRecursosFonteNegociar.add(rFonteNegociar);
        listRecursosFonteNegociar.add(r2FonteNegociar);
        List<Recurso> listRecursosFonteSalvo = new ArrayList<>();
        Recurso rFonteSalvo = getRecurso(10L, Item.COMIDA);
        Recurso r2FonteSalvo = getRecurso(10L, Item.AGUA);
        listRecursosFonteSalvo.add(rFonteSalvo);
        listRecursosFonteSalvo.add(r2FonteSalvo);
        List<Recurso> listRecursosAlvoNegociar = new ArrayList<>();
        Recurso rAlvoNegociar = getRecurso(46L, Item.COMIDA);
        listRecursosAlvoNegociar.add(rAlvoNegociar);
        List<Recurso> listRecursosAlvoSalvo = new ArrayList<>();
        Recurso rAlvo = getRecurso(40L, Item.COMIDA);
        listRecursosAlvoSalvo.add(rAlvo);
        when(recursoRepository.findByRebelde(alvo)).thenReturn(listRecursosFonteSalvo, listRecursosAlvoSalvo);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            List<Recurso> recursos = Collections.emptyList();
            service.negociarRecursos(fonte, listRecursosFonteNegociar, alvo, listRecursosAlvoNegociar);
        });
        verify(repository, times(2)).findByNome(any());
        verify(recursoRepository, times(2)).findByRebelde(any());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(3));
        assertEquals("Negociador {0} não possui o item {1} para negociação.",
                exception.getErrors().get(0).getText());
        assertEquals("negociadorFonte", exception.getErrors().get(0).getParams().get("0"));
        assertEquals("A quantidade do item {0}, do negociador {1} é menor do que a disponível no inventário, " +
                "quantidade atual {2}.", exception.getErrors().get(1).getText());
    }

    @Test
    void pesquisarRebelde() {
        service.listarRebeldes();
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarRebeldes() {
        Rebelde rebelde = new Rebelde();
        when(repository.findByNome(anyString())).thenReturn(Optional.of(rebelde));
        service.pesquisarRebelde(anyString());
        verify(repository, times(1)).findByNome(anyString());
    }


    @Test
    void deveLancarExcecaoQuandoNaolistarRebeldes() {
        Rebelde rebelde = new Rebelde();
        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            List<Recurso> recursos = Collections.emptyList();
            service.pesquisarRebelde(anyString());
        });
        verify(repository, times(1)).findByNome(anyString());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Rebelde {0} inexistente.", exception.getErrors().get(0).getText());

    }

    private Rebelde getRebelde(String nome) {
        Rebelde rebelde = getRebeldeSingle(nome);
        Localizacao localizacao = getLocalizacao();
        rebelde.setLocalizacao(localizacao);
        rebelde.setInventario(new ArrayList<>());
        Recurso recurso = getRecurso(10L, Item.AGUA);
        rebelde.getInventario().add(recurso);
        return rebelde;
    }

    private Recurso getRecurso(Long quantidade, Item item) {
        Recurso recurso = new Recurso();
        recurso.setQuantidade(quantidade);
        recurso.setItem(item);
        return recurso;
    }

    private Rebelde getRebeldeSingle(String nome) {
        Rebelde rebelde = new Rebelde();
        rebelde.setGenero(Genero.FEMININO);
        rebelde.setNome(nome);
        rebelde.setIdade(42);
        return rebelde;
    }

    private Localizacao getLocalizacao() {
        Localizacao localizacao = new Localizacao();
        localizacao.setNomeBase("Nome Base Localizacao");
        localizacao.setLatitude(0L);
        localizacao.setLongitude(0L);
        return localizacao;
    }

}