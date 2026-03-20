Que tal praticar?
E aí, gostou do Spring? Ele pode parecer um pouco estranho no início, mas ele facilita muito a vida dos desenvolvedores Java! Vamos exercitar os conceitos que aprendemos de Spring?

Faça uma aplicação que realiza o cadastro de um usuário por uma API.

Na chamada da API, receba os atributos necessários e salve os dados desse usuário no banco de dados.

Após esse cadastramento, na primeira tentativa de login com um usuário cadastrado, a aplicação deve conceder acesso temporário a uma outra API que retorna uma mensagem de acesso garantido, com um tempo limite de 1 minuto na sessão.

A partir do momento em que esse usuário tem sua sessão finalizada após 1 minuto do cadastramento, ao tentar acessar a API que retorna a mensagem de acesso garantido, barre o usuário e envie uma mensagem dizendo que ele não fez login.
