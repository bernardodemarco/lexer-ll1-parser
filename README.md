# Implementação Analisador Léxico - Parte A

Bernardo De Marco Gonçalves - 22102557

## Instruções para execução

Para compilação e exeução do analisador léxico implementado, o _script bash_ `run.sh` foi criado. Ele realiza a compilação de todos arquivos Java para o diretório `out` e, em seguida, realiza a execução da classe `Main`. Portanto, basta navegar até a _root_ do projeto, conceder permissão de execução ao _script_ e executá-lo:

```bash
ls -l
# total 20
# -rw-rw-r-- 1 bernardodemarco bernardodemarco  423 Oct 14 11:02 lexer.iml
# drwxrwxr-x 6 bernardodemarco bernardodemarco 4096 Oct 17 14:34 out
# -rw-rw-r-- 1 bernardodemarco bernardodemarco  441 Oct 17 14:40 README.md
# -rwxrwxr-x 1 bernardodemarco bernardodemarco   77 Oct 17 14:34 run.sh
# drwxrwxr-x 7 bernardodemarco bernardodemarco 4096 Oct 17 14:22 src

# Fornecer permissões de execução ao _script_
chmod +x ./run.sh

# Executar o _script_
./run.sh
```

Os arquivos de código-fonte a serem analisados pelo lexer.Lexer encontram-se no _folder_ `/src/resources.lsi`. Ao executar o lexer.Lexer, é solicitado ao usuário qual arquivo ele deseja que o lexer.Lexer analise. Dois arquivos estão disponíveis, um com erros léxicos e outro sem.
