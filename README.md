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

# H2
#### http://localhost:8080/h2

![Alt text](doc/swagger.png?raw=true "Swagger")

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
      "item": "ARMA",
      "quantidade": 5
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
      "quantidade": 100
    }
  ]
}
```



# Testes
Todos os métodos de negócio do serviço core da aplicaão foram testados

![Alt text](doc/code_coverage.png?raw=true "Configurando token JWT no swagger")

