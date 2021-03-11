# avaliacao project

Desafio leitura / escrita em arquivo
=====================================

1 - Para rodar a aplicação no Intellij instale o plugin do quarkus/maven, vá até o edit configuration da IDE, clique em "Add New Configuration > Quarkus(Maven)", selecione a JRE11 no campo JRE > apply > ok.

2 - O file watcher observará a pasta "IN", que por sua vez acionará o FileStreamerService(resposável pelo IO de arquivos);

3 - Os Handlers do FileStreamer por sua vez poderão chamar services(neste projeto até o momento apenas o de Input precisa fazer isto).

4 - Os service aplicarão regras, e vao interagir com uma base mongo utilizando-se do padrão repository.

5 - O conteúdo do arquivo constará nos logs.
