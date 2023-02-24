# Ada Tech
Entrega do desafio técnico da Ada Tech, para a posição de professor, tecnologia Java!

# Como testar

Execute o metodo main da classe:
```java
package tech.ada.star.wars;

public class AdaTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdaTechApplication.class, args);
    }

}
```

# Utilize o Swagger, para testar a aplicação
### http://localhost:8080/swagger-ui/index.html#/

![Alt text](doc/swagger.png?raw=true "Swagger")


# Acesse o H2 para visualizar os dados
* JDBC URL: jdbc:h2:mem:ada_tech
* User Name: ada
* Password: 
#### http://localhost:8080/h2

![Alt text](doc/h2.png?raw=true "H2")


# Ao iniciar, o sistema irá incluir dois rebeldes, cada um com 10 intens de cada recurso, para facilitar os testes.
 * caso queira desabilitar a inclusão de Rebeldes automática, ao iniciar o sistema basta incluir a seguinte variável
```shell
ada.inicializa.dados: false
```



# Cenário de teste

[POST -> /rebeldes](http://localhost:8080/swagger-ui/index.html#/rebelde/adicionarRebelde)
```json
{
  "nome": "Maria Sophia",
  "idade": 10,
  "genero": "FEMININO",
  "localizacao": {
    "latitude": 10,
    "longitude": 20,
    "nomeBase": "Base Maria"
  },
  "inventario": [
    {
      "item": "COMIDA",
      "quantidade": 4
    }
  ]
}
```

[POST -> /rebeldes](http://localhost:8080/swagger-ui/index.html#/rebelde/adicionarRebelde)
```json
{
  "nome": "Marta Silva",
  "idade": 0,
  "genero": "FEMININO",
  "localizacao": {
    "latitude": 60,
    "longitude": 80,
    "nomeBase": "Base Marta"
  },
  "inventario": [
    {
      "item": "ARMA",
      "quantidade": 10
    },
    {
      "item": "COMIDA",
      "quantidade": 4
    }
  ]
}
```
### Tabela Recurso
Dois rebeldes cadastrados
* Marta Silva 4 COMIDA, 10 ARMA
* Maria Sophia 4 COMIDA

![Alt text](doc/cadastro_rebelde.png?raw=true "Cadastro Rebelde")
  

[POST -> /rebeldes/negociacoes](http://localhost:8080/swagger-ui/index.html#/rebelde/negociarItens)
```json
{
  "negociadorFonte": {
    "nome": "Maria Sophia",
    "recusrsosOferecidos": [
      {
        "item": "COMIDA",
        "quantidade": 4
      }
    ]
  },
  "negociadorAlvo": {
    "nome": "Marta Silva",
    "recusrsosOferecidos": [
      {
        "item": "ARMA",
        "quantidade": 1
      }
    ]
  }
}
```

A Marta Silva negociou 1 ARMA, com a Maria Sophia, que deu em troca 4 COMIDA
* Marta Silva 8 COMIDA, 9 ARMA
* Maria Sophia 0 COMIDA, 1 ARMA

![Alt text](doc/negociacao_recursos.png?raw=true "Cadastro Rebelde")

# Testes
Todos os métodos de negócio do serviço core da aplicaão foram testados

![Alt text](doc/code_coverage.png?raw=true "Configurando token JWT no swagger")

