# lets-code
Entrega do desafio técnico da Let's Code, para a posição de professor, tecnologia Java!


# Swagger
### http://localhost:8080/swagger-ui/index.html#/

# H2
#### http://localhost:8080/h2

# Como testar
Gere um token JWT utilizando o Postman ou Insomnia, existem dois perfis de usuário, o MANAGER E O PLAYER! 

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
			this.userService.createUserIfNotFound(4L, "maite", defaultPass, "Maitê", player);
			this.userService.createUserIfNotFound(5L, "davi", defaultPass, "Davi", player);
		});
	}

}
```


```shell
curl --location --request POST 'localhost:8080/auth' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=sophia' \
--data-urlencode 'password=123456'
```
Copie o conteúdo do field token da resposta e utilize na interface do Swagger ```http://localhost:8080/swagger-ui/index.html#/``` para consumir os demais endpoints
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWEiLCJpYXQiOj...",
    "refresh-token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ..."
}
```
![Home_Swagger] (/data/swagger-auth.png)

# Fluxo padrão

 * GET /quizzes/start
 * GET /quizzes/next-step
 * POST /quizzes/response

Sempre que quiser iniciar um novo Quiz deve acionar o endpoint ```GET /quizzes/start```, e antes de responder uma step deve criá-la utilizando o endpoint ```GET /quizzes/next-step```