# 🏥 Trabalho Prático – Sistema de Gerenciamento Hospitalar

### 🎯 Objetivo

Implementar um _Sistema de Gerenciamento Hospitalar_ em _Java, aplicando conceitos avançados de **Programação Orientada a Objetos (POO), com foco em **herança, polimorfismo, encapsulamento, persistência de dados_ e _regras de negócio mais complexas_.

---

## Descrição do Projeto

Desenvolvimento de um sistema de gerenciamento hospitalar utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

## Dados do Aluno

- **Nome completo:** Tiago Vieira Corrêa
- **Matrícula:** 251028761
- **Curso:** ENGENHARIAS/FCTE
- **Turma:** Turma 6

---

## Instruções para Compilação e Execução

1. **Compilação:**  
   A partir da pasta `sistema_hospital` utilizar o comando `mvn compile`

2. **Execução:**  
   A partir da pasta `sistema_hospital` utilizar o comando `mvn exec:java -Dexec.mainClass="br.hospital.app.Main"`

3. **Estrutura de Pastas:**

```
└── ep1-2025.2-TiagoCorrea
    ├── README.md
    └── sistema_hospital
        ├── data
        │   ├── dados_consultas.json
        │   ├── dados_internacoes.json
        │   ├── dados_medicos.json
        │   ├── dados_pacientes.json
        │   └── dados_pacientes_especiais.json
        ├── nbactions.xml
        ├── pom.xml
        ├── prints_execucao
        │   ├── print1.png
        │   ├── print2.png
        │   ├── print3.png
        │   ├── print5.png
        │   ├── print6.png
        │   └── video_sistema_hospital.mp4
        ├── src
        │   ├── main
        │   │   └── java
        │   │       └── br
        │   │           └── hospital
        │   │               ├── app
        │   │               │   └── Main.java
        │   │               ├── menu
        │   │               │   ├── Menu.java
        │   │               │   └── Tela.java
        │   │               ├── model
        │   │               │   ├── Consulta.java
        │   │               │   ├── Internacao.java
        │   │               │   ├── Medico.java
        │   │               │   ├── Paciente.java
        │   │               │   ├── PacienteEspecial.java
        │   │               │   ├── Pessoa.java
        │   │               │   └── Relatorio.java
        │   │               └── utils
        │   │                   ├── Inputs.java
        │   │                   ├── RepositorioJson.java
        │   │                   └── Verificador.java
        │   └── test
        │       └── java
        └── target
            ├── classes
            │   └── br
            │       └── hospital
            │           ├── app
            │           ├── menu
            │           ├── model
            │           └── utils
            ├── generated-sources
            │   └── annotations
            └── maven-status
                └── maven-compiler-plugin
                    └── compile
                        └── default-compile
                            ├── createdFiles.lst
                            └── inputFiles.lst
```

4. **Versão do JAVA utilizada:**  
   `java 21`

---

## Vídeo de Demonstração

- https://youtu.be/nFHYb43pirY?si=UrF4KHxRehHEQYLT

---

## Prints da Execução

1. Menus Principais:  
   <img width="277" height="104" alt="print1" src="https://github.com/user-attachments/assets/e21e6bf1-4fde-404b-b3a8-ec8c7b435b49" />
   
   <img width="239" height="157" alt="print2" src="https://github.com/user-attachments/assets/e5031c14-22f9-48eb-b8e4-c5691132d294" />
   
   <img width="289" height="158" alt="print5" src="https://github.com/user-attachments/assets/e30c9b98-b1dd-4508-a20a-5769c61efa4d" />


2. Cadastro de Médico:  
   <img width="411" height="135" alt="print3" src="https://github.com/user-attachments/assets/4d1c0f00-498d-4690-b2ff-01984e5584e0" />


3. Cadastro de Paciente:  
   <img width="448" height="105" alt="print6" src="https://github.com/user-attachments/assets/e25271b3-2107-4fc1-a83f-f0f715c62200" />

4. Relatório de médicos:  
   <img width="301" height="113" alt="print7" src="https://github.com/user-attachments/assets/7204d06c-3133-428b-9e99-ed1429e1a003" />

---

---

## Observações (Extras ou Dificuldades)

- Eu implementei um menu interativo dentro do sistema utilizando a biblioteca lanterna, do google e realizei a serialização por meio de arquivos json. Também houve problemas com a forma de salvar o vídeo e tive que fazê-lo pelo celular.

---

## Contato

- 251028761@aluno.unb.br

---

### 🖥️ Descrição do Sistema

O sistema deve simular o funcionamento de um hospital com cadastro de _pacientes, médicos, especialidades, consultas e internações_.

1. _Cadastro de Pacientes_

   - Pacientes comuns e pacientes especiais (ex: com plano de saúde).
   - Cada paciente deve ter: nome, CPF, idade, histórico de consultas e internações.

2. _Cadastro de Médicos_

   - Médicos podem ter especialidades (ex: cardiologia, pediatria, ortopedia).
   - Cada médico deve ter: nome, CRM, especialidade, custo da consulta e agenda de horários.

3. _Agendamento de Consultas_

   - Um paciente pode agendar uma consulta com um médico disponível.
   - Consultas devem registrar: paciente, médico, data/hora, local, status (agendada, concluída, cancelada).
   - Pacientes especiais (plano de saúde) podem ter _vantagens_, como desconto.
   - Duas consultas não podem estar agendadas com o mesmo médico na mesma hora, ou no mesmo local e hora

4. _Consultas e Diagnósticos_

   - Ao concluir uma consulta, o médico pode registrar _diagnóstico_ e/ou _prescrição de medicamentos_.
   - Cada consulta deve ser registrada no _histórico do paciente_.

5. _Internações_

   - Pacientes podem ser internados.
   - Registrar: paciente, médico responsável, data de entrada, data de saída (se já liberado), quarto e custo da internação.
   - Deve existir controle de _ocupação dos quartos_ (não permitir duas internações no mesmo quarto simultaneamente).
   - Internações devem poder ser canceladas, quando isso ocorrer, o sistema deve ser atualizado automaticamente.

6. _Planos de saúde_

   - Planos de saude podem ser cadastrados.
   - Cada plano pode oferecer _descontos_ para _especializações_ diferentes, com possibilidade de descontos variados.
   - Um paciente que tenha o plano de saúde deve ter o desconto aplicado.
   - Deve existir a possibilidade de um plano _especial_ que torna internação de menos de uma semana de duração gratuita.
   - Pacientes com 60+ anos de idade devem ter descontos diferentes.

7. _Relatórios_
   - Pacientes cadastrados (com histórico de consultas e internações).
   - Médicos cadastrados (com agenda e número de consultas realizadas).
   - Consultas futuras e passadas (com filtros por paciente, médico ou especialidade).
   - Pacientes internados no momento (com tempo de internação).
   - Estatísticas gerais (ex: médico que mais atendeu, especialidade mais procurada).
   - Quantidade de pessoas em um determinado plano de saúde e quanto aquele plano _economizou_ das pessoas que o usam.

---

### ⚙️ Requisitos Técnicos

- O sistema deve ser implementado em _Java_.
- Interface via _terminal (linha de comando)_.
- Os dados devem ser persistidos em _arquivos_ (.txt ou .csv).
- Deve existir _menu interativo_, permitindo navegar entre as opções principais.

---

### 📊 Critérios de Avaliação

1. _Modos da Aplicação (1,5)_ → Cadastro de pacientes, médicos, planos de saúde, consultas e internações.
2. _Armazenamento em arquivo (1,0)_ → Dados persistidos corretamente, leitura e escrita funcional.
3. _Herança (1,0)_ → Ex.: Paciente e PacienteEspecial, Consulta e ConsultaEspecial, Médico e subclasses por especialidade.
4. _Polimorfismo (1,0)_ → Ex.: regras diferentes para agendamento, preços de consultas.
5. _Encapsulamento (1,0)_ → Atributos privados, getters e setters adequados.
6. _Modelagem (1,0)_ → Estrutura de classes clara, bem planejada e com relacionamentos consistentes.
7. _Execução (0,5)_ → Sistema compila, roda sem erros e possui menus funcionais.
8. _Qualidade do Código (1,0)_ → Código limpo, organizado, nomes adequados e boas práticas.
9. _Repositório (1,0)_ → Uso adequado de versionamento, commits frequentes com mensagens claras.
10. _README (1,0)_ → Vídeo curto (máx. 5 min) demonstrando as funcionalidades + prints de execução + explicação da modelagem.

🔹 _Total = 10 pontos_  
🔹 _Pontuação extra (até 1,5)_ → Melhorias relevantes, como:

- Sistema de triagem automática com fila de prioridade.
- Estatísticas avançadas (tempo médio de internação, taxa de ocupação por especialidade).
- Exportação de relatórios em formato .csv ou .pdf.
- Implementação de testes unitários para classes principais.
- Menu visual.
