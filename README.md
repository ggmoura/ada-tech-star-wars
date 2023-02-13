# lets-code
Entrega do desafio técnico da Let's Code, para a posição de professor, tecnologia Java!


# Swagger
### http://localhost:8080/swagger-ui/index.html#/

# H2
#### http://localhost:8080/h2

# Como testar

Execute o metodo main da classe:
```java
package br.com.letscode;

public class MoviesBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesBattleApplication.class, args);
	}

}
```

Gere um token JWT utilizando o Postman ou Insomnia, existem dois perfis de usuário, o MANAGER E O PLAYER! 

```shell
curl --location --request POST 'localhost:8080/auth' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=sophia' \
--data-urlencode 'password=123456'
```

Clique em File -> import e selecione [ raw text ] e cole a curl...

![import_curl.png.png](doc/import_curl.png)

Copie o token para utilizar os serviços

![postman.png](doc/postman.png)

Usuários pré cadastrados!

```java
public class InitialDataLoaderListener {

    //...
    
	private void updateDefaultAccounts() {
		Privilege manager = Privilege.MANAGER;
		Privilege player = Privilege.PLAYER;
		String defaultPass = encoder.encode(DEFAULT_USEER_CREDENTIAL);
		taskExecutor.execute(() -> {
			this.userService.createUserIfNotFound(1L, "admin", defaultPass, "Gleidson", manager);
			this.userService.createUserIfNotFound(2L, "player", defaultPass, "Aline", player);
			this.userService.createUserIfNotFound(3L, "sophia", defaultPass, "Maria Sophia", player, manager);
			this.userService.createUserIfNotFound(4L, "marta", defaultPass, "Marta Silva De Oliveira", player);
			this.userService.createUserIfNotFound(5L, "davi", defaultPass, "Davi", player);
		});
	}

}
```

Copie o conteúdo do field token da resposta e utilize na interface do Swagger ```http://localhost:8080/swagger-ui/index.html#/``` para consumir os demais endpoints
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWEiLCJpYXQiOj...",
    "refresh-token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ..."
}
```

![Alt text](doc/swagger-auth.png?raw=true "Configurando token JWT no swagger")

# Fluxo padrão

 * GET /quizzes/start
 * GET /quizzes/next-step
 * POST /quizzes/response

Sempre que quiser iniciar um novo Quiz deve acionar o endpoint ```GET /quizzes/start```, e antes de responder uma step deve criá-la utilizando o endpoint ```GET /quizzes/next-step```

Um novo PLAYER pode ser cadastrado utilizando o endpoint ```POST /players``` de pleyer-controller

O sisstema já inicializa com 21 filmes, é possível cadastrar novos filmes utilizando o serviço do ```http://www.omdbapi.com``` utilizando dois enpoints,
 * ```POST /movies/title``` informando exatamente o título do filme
 * ```POST /movies/imdbid``` informando o imdbid da plataforma

![Alt text](doc/swagger.png?raw=true "Swagger")

# Testes
Todos os métodos de negócio foram testados

![Alt text](doc/code_coverage.png?raw=true "Configurando token JWT no swagger")

