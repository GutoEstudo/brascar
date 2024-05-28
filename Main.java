package br.com.brascar;

import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;
public class Main {

    private static Map<String, Map<String, String>> conta = new HashMap<>();
    private static Map<String, Map<String, String>> codigos = new HashMap<>();
    private static Map<String, List<String>> mensagensPendentes = new HashMap<>();
    private static String cpf;
    private static String carro;
    private static Double precoDiaria;
    private static List<String> cpfCadastrados = new ArrayList<>();
    private static String menu;
    private static Boolean isLoginNecessario = true;
    private static Scanner ler = new Scanner(System.in);
    private static Scanner lerComEspacos = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) {

        menu = "principal";

        while (!menu.equals("exit")) {
            if (isLoginNecessario) {
                System.out.println("Olá, bem vindo ao Brascar!");
                System.out.println("Para começar com essa conversa vocês precisa estar cadastrado no nosso sistema.");
                System.out.println("\nSe já estiver cadastrado, basta logar :).");
                System.out.println("1. Cadastrar");
                System.out.println("2. Logar");
                int escolha = ler.nextInt();
                switch (escolha) {
                    case 1:
                        realizarCadastro();
                        break;
                    case 2:
                        logar();
                        break;
                    default:
                        System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
                }
            } else {
                if (menu == "principal") {
                    mostrarMenuPrincipal();
                }

                if (menu == "aluguel de carro") {
                    alugarCarro();
                }

                if (menu == "seguro") {
                    realizarSeguroDeCarro();
                }

                if (menu == "envio de mensagem") {
                    enviarMensagem();
                }

                if (menu == "informacoes") {
                    manipularInformacoesConta();
                }

            }
        }
    }

    private static void realizarSeguroDeCarro() {
        System.out.println("Para realizar o seguro de um carro, você necessita de ter alugado um carro primeiro.");
        System.out.println("1. Já tenho um carro alugado.");
        System.out.println("2. Desejo alugar um carro");
        System.out.println("3. Voltar ao menu principal");
        System.out.println("4. Finalizar a conversa");

        int escolha = ler.nextInt();

        switch (escolha) {
            case 1:
                confirmarSeguro();
                break;
            case 2:
                menu = "aluguel de carro";
                break;
            case 3:
                menu = "principal";
                break;
            case 4:
                menu = "exit";
            default:
                System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
        }
    }

    private static void confirmarSeguro() {
        System.out.println("\n Por favor, digte o código de verificação fornecido no momento do aluguel do veículo.");
        String codigoDigitado = ler.next();
            if (Objects.equals(conta.get(cpf).get("codigos"), codigoDigitado)) {
                if( codigos.get(codigoDigitado).get("precoSeguro") != null) {
                    System.out.println("\n Você já tem um seguro. Verifique as informações na aba de informações da conta");
                    menu = "principal";
                    return;
                }

                double precoSeguro = Double.parseDouble(codigos.get(codigoDigitado).get("diaria"))*0.1*Double.parseDouble(codigos.get(codigoDigitado).get("dias"));
                System.out.println("\n O preço do seguro é 10% do valor do veículo adicionais ao preço da diaria.");
                System.out.println("\n O código é referente ao alugel de um " + codigos.get(codigoDigitado).get("carro") + " cujo " +
                        "preço da diária é " + codigos.get(codigoDigitado).get("diaria") + " R$, logo o preço do seguro será de: "
                        +  precoSeguro + " para os " + codigos.get(codigoDigitado).get("dias") + " dia(s) que você pretende ficar com o carro." +
                        " Deseja prosseguir com a aquisição do seguro? (s/n)");
                String simOuNao = ler.next();
                if(simOuNao.equalsIgnoreCase("s")) {
                    System.out.println("\n Seguro adquirido com sucesso!");
                    codigos.get(codigoDigitado).put("precoSeguro", String.valueOf(precoSeguro));
                }

            } else {
                System.out.println("\n O código de verificação não existe!");
            }
        menu = "principal";
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\nO que deseja fazer hoje?");
        System.out.println("1. Alugar um veículo;");
        System.out.println("2. Alugar um seguro;");
        System.out.println("3. Conversar com um de nossos funcionários;");
        System.out.println("4. Informações da conta;");
        System.out.println("5. Deslogar;");
        System.out.println("6. Finalizar conversa.");

        int escolha1 = ler.nextInt();

        switch (escolha1) {
            case 1:
                menu = "aluguel de carro";
                break;
            case 2:
                menu = "seguro";
                break;
            case 3:
                menu = "envio de mensagem";
                break;
            case 4:
                menu = "informacoes";
                break;
            case 5:
                isLoginNecessario = true;
                break;
            case 6:
                menu = "exit";
                break;
            default:
                System.out.println("Parece que não existe essa opção no menu. Por favor, digite novamente uma" +
                        "opção válida!");
                menu = "principal";
        }
    }

    private static void manipularInformacoesConta() {
        System.out.println("\n1. Visualizar as informações da minha conta;");
        System.out.println("2. Alterar informação da minha conta");
        System.out.println("3. Visualizar mensagens pendentes");
        System.out.println("4. Excluir minha conta");
        System.out.println("5. Voltar ao menu anterior");
        System.out.println("6. Finalizar conversa.");

        int escolha = ler.nextInt();

        switch (escolha) {
            case 1:
                visualizarInformacoesConta();
                break;
            case 2:
                alterarInformacoesConta();
                break;
            case 3:
                visualizarMensagensPendentes();
                break;
            case 4:
                excluirConta();
                break;
            case 5:
                menu = "principal";
                break;
            case 6:
                menu = "exit";
                break;
            default:
                System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
        }
    }


    private static void alugarCarro() {
        boolean erro = false;
        if ( Integer.parseInt(conta.get(cpf).get("idade")) < 18) {
            System.out.println("Você é menor de idade e não pode alugar um carro");
            menu = "principal";
            return;
        } else if (conta.get(cpf).get("codigos") != null) {
            System.out.println("\nVocê já alugou um carro, por favor verifique as informações na aba de informações da conta");
            menu = "principal";
            return;
        }

        System.out.println("\nEscolha o modelo de carro:");
        System.out.println("1. Onix");
        System.out.println("2. Ford Ka");
        System.out.println("3. Siena");
        System.out.println("4. Corolla");
        System.out.println("5. Civic");
        System.out.println("6. Cerato");
        System.out.println("7. Voltar ao menu anterior");
        System.out.println("8. Finaliar conversa");
        int escolhaB = ler.nextInt();
        switch (escolhaB) {
            case 1:
                carro = "Onix";
                precoDiaria = 80.00;
                break;
            case 2:
                carro = "Ford Ka";
                precoDiaria = 70.00;
                break;
            case 3:
                carro = "Siena";
                precoDiaria = 75.00;
                break;
            case 4:
                carro = "Corolla";
                precoDiaria = 150.00;
                break;
            case 5:
                carro = "Civic";
                precoDiaria = 140.00;
                break;
            case 6:
                carro = "Cerato";
                precoDiaria = 130.00;
            break;
            case 7:
                menu = "principal";
                break;
            case 8:
                menu = "exit";
                break;
            default:
                System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
                erro = true;


        }
        if (menu.equals("aluguel de carro") && !erro) {
            System.out.println("\nVocê escolheu o carro: " + carro);
            System.out.println("O valor da diária é de R$" + precoDiaria);
            System.out.println("Deseja prosseguir com este carro? (S/N)");
            String prosseguir = ler.next();

            if (prosseguir.equalsIgnoreCase("n")) {
                menu = "aluguel de carro";
            } else {
                confirmarAluguel();

                if (menu != "seguro") {
                    System.out.println("Obrigado por contratar nossos serviços");

                    System.out.println("Deseja mais alguma coisa? (S/N)");
                    String simOuNao = ler.next();

                    if (simOuNao.equalsIgnoreCase("s")) {
                        menu = "principal";
                    } else {
                        menu = "exit";
                    }
                }
            }
        }
    }

    private static void confirmarAluguel() {
        System.out.print("\nEntão vamos continuar com o alguel do " + carro + ". \nPor quantos dias deseja alugar o carro?");
        int dias = ler.nextInt();
        double precoTotal = precoDiaria * dias;

        System.out.println("O valor total do aluguel é de R$" + precoTotal);

        for (int tentativas = 0; tentativas < 5; tentativas++) {
            System.out.println("Digite seu cpf para confirmar o aluguel do carro.");
            String cpfDigitado = ler.next();
            if (cpf.equals(cpfDigitado)) {
                String codigoGerado = RandomStringUtils.randomAlphanumeric(5);

                System.out.println("Parabéns pelo aluguel do carro. Aqui está o seu código de verificação:"
                        + codigoGerado + ". Compareça a uma de nossas lojas físicas" +
                        " apresentando o seu nome, CPF e código de verificação para relizar o pagamento e retirar o seu" +
                        " veículo.");

                Map<String, String> infoCodigo = new HashMap<>();
                infoCodigo.put("carro", carro);
                infoCodigo.put("diaria", precoDiaria.toString());
                infoCodigo.put("dias", String.valueOf(dias));
                codigos.put(codigoGerado, infoCodigo);
                conta.get(cpf).put("codigos", codigoGerado);

                System.out.println("Deseja contratar um seguro para acompanhar o aluguel de seu novo carro? (s/n).");

                String simOuNao = ler.next();
                if (simOuNao.equalsIgnoreCase("s")) {
                    menu = "seguro";
                } else {
                    menu = "principal";
                }
                break;
            }
        }
    }

    private static void enviarMensagem() {
        List<String> mensagensEnviadas = mensagensPendentes.get(cpf);
        System.out.println("\nEscreva uma mensagem completa do que você necessita. Assim que" +
                " um de nossos funcionários estiver disponível ele irá te atender.");

        String mensagem = lerComEspacos.next();
        mensagensEnviadas.add(mensagem);
        mensagensPendentes.put(cpf, mensagensEnviadas);

        System.out.println("\nObrigado pela mensagem. Assim que possível retornaremos para o email: " +
                conta.get(cpf).get("email") +
                ". Enquanto isso o que deseja fazer?");

        System.out.println("1. Retornar ao menu principal");
        System.out.println("2. Enviar outra mensagem");
        System.out.println("3. Finalizar conversa");
        int escolha = ler.nextInt();
        switch (escolha) {
            case 1:
                menu = "principal";
                break;
            case 2:
                menu = "envio de mensagem";
                break;
            case 3:
                menu = "exit";
            default:
                System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
        }
    }

    private static void excluirConta() {
        menu = "e2";
        while (menu.equals("e2")) {
            System.out.println("Tem certeza que deseja excluir a sua conta? (S/N)");
            String escolha = ler.next();
            System.out.println("Informe sua senha: ");
            String senha = ler.next();
            if (conta.get(cpf).get("senha").equals(senha)) {
                if (escolha.equalsIgnoreCase("s")) {
                    conta.remove(cpf);
                    menu = "principal";
                    isLoginNecessario = true;
                }
            } else {
                System.out.println("Senha incorreta");
            }
        }
    }

    private static void visualizarMensagensPendentes() {

        if (!mensagensPendentes.get(cpf).isEmpty()) {
            System.out.println("\nAs seguintes mensagens foram enviadas pela sua conta: ");
            int i;
            for (i = 0; i < mensagensPendentes.get(cpf).size(); i++) {
                System.out.println(i+1 + "." + " " + mensagensPendentes.get(cpf).get(i));
            }
            System.out.println("\n");
            menu = "informacoes";
        } else {
            System.out.println("Você não possui mensagens pendentes!");
            menu = "informacoes";
        }
    }

    private static void alterarInformacoesConta() {
        menu = "alterar informacoes";

        while (menu.equals("alterar informacoes")) {
            boolean erro = false;
            System.out.println("Qual informação deseja alterar?");
            System.out.println("1. Nome");
            System.out.println("2. Email");
            System.out.println("3. Idade");
            System.out.println("4. Voltar ao menu anterior");
            System.out.println("5. Finalizar conversa");

            int escolha = ler.nextInt();

            switch (escolha) {
                case 1:
                    alterarNome();
                    break;
                case 2:
                    alterarEmail();
                    break;
                case 3:
                    alterarIdade();
                    break;
                case 4:
                    menu = "principal";
                    break;
                case 5:
                    menu = "exit";
                    break;
                default:
                    System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
                    erro = true;
            }
            if (menu.equals("alterar informacoes") && !erro) {
                System.out.println("\n Deseja alterar outra informação? (S/N)");
                String sOuN = ler.next();
                if (sOuN.equalsIgnoreCase("s")) {
                    menu = "alterar informacoes";
                } else {
                    menu = "informacoes";
                }
            }
        }
    }

    private static void alterarIdade() {
        System.out.println("Informe a nova idade: ");

        int idadeAntigo = Integer.parseInt(conta.get(cpf).get("idade"));
        int idadeNovo = ler.nextInt();
        conta.get(cpf).put("idade", Integer.toString(idadeNovo));

        System.out.println("Idade alterada de " + idadeAntigo + " para " + idadeNovo);
    }

    private static void alterarEmail() {
        System.out.println("Informe o novo email: ");

        String emailAntigo = (String) conta.get(cpf).get("email");
        String emailNovo = lerComEspacos.next();
        conta.get(cpf).put("email", emailNovo);

        System.out.println("Nome alterado de " + emailAntigo + " para " + emailNovo);
    }

    private static void alterarNome() {
        System.out.println("Informe o novo nome: ");

        String nomeAntigo = conta.get(cpf).get("nome");
        String nomeNovo = lerComEspacos.next();
        conta.get(cpf).put("nome", nomeNovo);

        System.out.println("Nome alterado de " + nomeAntigo + " para " + nomeNovo);
    }

    private static void visualizarInformacoesConta() {
        String codigoAtual = conta.get(cpf).get("codigos");
        System.out.println("\nNome da conta: " + conta.get(cpf).get("nome"));
        System.out.println("Email da conta: " + conta.get(cpf).get("email"));
        System.out.println("Idade do proprietário da conta: " + conta.get(cpf).get("idade"));
        System.out.println("CPF do proprietário da conta: " + cpf);
        if (codigoAtual != null) {
            System.out.println("Carro alugado: " + codigos.get(codigoAtual).get("carro") );
            System.out.println("Preço da diária do carro: " + codigos.get(codigoAtual).get("diaria") );
            System.out.println("Quantidade de dias em que o carro foi alugado: " + codigos.get(codigoAtual).get("dias") );
            if (codigos.get(codigoAtual).get("precoSeguro") != null) {
                System.out.println("Valor total pago do seguro: " + codigos.get(codigoAtual).get("precoSeguro") );
            }
        }

        System.out.println("\nO que deseja fazer?");
        System.out.println("1. Voltar ao menu anterior");
        System.out.println("2. Voltar ao menu principal");
        System.out.println("3. Finalizar conversa");

        int escolha = ler.nextInt();

        switch (escolha) {
            case 1:
                menu = "informacoes";
                break;
            case 2:
                menu = "principal";
                break;
            case 3:
                menu = "exit";
                break;
            default:
                System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
        }
    }


    private static void logar() {
        menu = "login";
        while (menu == "login") {
            System.out.println("\nPor favor, informe o CPF cadastrado");
            String cpf = ler.next();

            if (verificarExistenciaCpf(cpf)) {
                System.out.println("\nEncontramos seu CPF em nossa base!");
                Main.cpf = cpf;

                for (int tentativas = 0; tentativas < 5; tentativas++) {
                    System.out.println("\nPor favor, informe a senha");
                    String senha = ler.next();

                    if (conta.get(cpf).get("senha").equals(senha)) {
                        System.out.println("\nUsuario logado com sucesso!");
                        menu = "principal";
                        isLoginNecessario = false;
                        return;
                    } else {
                        System.out.println("\nSenha incorreta! Tente novamente. (" + (4 - tentativas) + "  tentativas restante(s))");
                    }
                }
                isLoginNecessario = true;
                return;
            }

            System.out.println("\nO CPF em questão não foi encontrado em nossa base. Como deseja prosseguir?");
            System.out.println("1. Reescrever o CPF;");
            System.out.println("2. Realizar cadastro;");
            System.out.println("3. Finalizar conversa.");

            int escolha = ler.nextInt();

            switch (escolha) {
                case 1:
                    menu = "login";
                    break;
                case 2:
                    realizarCadastro();
                    break;
                case 3:
                    menu = "exit";
                    break;
                default:
                    System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
            }
        }
    }


    private static void realizarCadastro() {
        menu = "realizacao de cadastro";

        while (menu == "realizacao de cadastro") {
            System.out.println("\nVamos começar com o seu processo de cadastro. Pirmeiramente informe o seu cpf:");
            cpf = ler.next();
            if (!verificarExistenciaCpf(cpf)) {
                Map<String, String> infos = new HashMap<>();
                cpfCadastrados.add(cpf);
                System.out.println("\nInforme o seu nome completo:");
                infos.put("nome", lerComEspacos.next());
                System.out.println("\nInforme a sua idade:");
                infos.put("idade", String.valueOf(ler.nextInt()));
                System.out.println("\nInforme o seu email:");
                infos.put("email", ler.next());
                System.out.println("\nInforme uma senha:");
                infos.put("senha", ler.next());

                mensagensPendentes.put(cpf, new ArrayList<>());
                conta.put(cpf, infos);
                isLoginNecessario = true;
                menu = "principal";
            } else {
                menu = "cpfCadastrado";
                while (menu.equals("cpfCadastrado")) {
                    System.out.println("\nO CPF em questão já está cadastrado!");
                    System.out.println("1. Tentar com outro CPF");
                    System.out.println("2. Fazer Login");
                    int escolha;
                    escolha = ler.nextInt();

                    switch (escolha) {
                        case 1:
                            menu = "realizacao de cadastro";
                        case 2:
                            menu = "login";
                            break;
                        default:
                            System.out.println("\nVocê não digitou uma das opções disponíveis. Tente novamente.");
                    }
                }
            }
        }
    }

    private static boolean verificarExistenciaCpf(String cpf) {
        for (String cpfCadastrado : cpfCadastrados) {
            if (cpfCadastrado.equals(cpf)) {
                return true;
            }
        }
        return false;
    }

}