package tech.ada.star.wars.listener;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.ada.star.wars.data.entity.*;
import tech.ada.star.wars.data.repository.RebeldeRepository;

import java.util.ArrayList;

@Component
public class InitialDataLoaderListener {

	@Autowired
	private RebeldeRepository rebeldeRepository;

	@Autowired
	private TaskExecutor taskExecutor;

	@Value(value = "${ada.inicializa.dados:true}")
	private boolean mustBeInitialize;

	@EventListener
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (this.mustBeInitialize) {
			taskExecutor.execute(() -> {
				rebeldeRepository.save(getRebelde("Gleidson", 42, Genero.MASCULINO));
				rebeldeRepository.save(getRebelde("Marta", 32, Genero.FEMININO));
			});
			this.mustBeInitialize = Boolean.FALSE;
		}
	}

	@NotNull
	private Rebelde getRebelde(String nome, Integer idade, Genero genero) {
		Rebelde rebelde = new Rebelde();
		rebelde.setNome(nome);
		rebelde.setIdade(idade);
		rebelde.setGenero(genero);
		Localizacao localizacao = new Localizacao();
		localizacao.setRebelde(rebelde);
		localizacao.setNomeBase("Localizacao ".concat(nome));
		localizacao.setLatitude(10L);
		localizacao.setLongitude(-10L);
		rebelde.setLocalizacao(localizacao);
		rebelde.setInventario(new ArrayList<>());
		rebelde.getInventario().add(new Recurso(Item.COMIDA, 10L, rebelde));
		rebelde.getInventario().add(new Recurso(Item.MUNICAO, 10L, rebelde));
		rebelde.getInventario().add(new Recurso(Item.AGUA, 10L, rebelde));
		rebelde.getInventario().add(new Recurso(Item.ARMA, 10L, rebelde));
		return rebelde;
	}


}