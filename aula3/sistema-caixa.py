import threading


# VARIÁVEL GLOBAL / COMPARTILHADA
saldo_central = 0
lock = threading.Lock()


# MODEL
class SaldoModel:

    def __init__(self):
        self.valor_ficha = 10.00

    def adicionar_venda(self):
        global saldo_central

        with lock:
            saldo_central += self.valor_ficha

    def retornar_saldo(self):
        global saldo_central

        with lock:
            return saldo_central


# VIEW
class CaixaView:

    def mostrar_dados_entrada(self, quantidade_caixas, quantidade_fichas, valor_ficha):
        print("=== DADOS DO EVENTO ===")
        print("Quantidade de caixas:", quantidade_caixas)
        print("Quantidade de fichas por caixa:", quantidade_fichas)
        print(f"Valor de cada ficha: R$ {valor_ficha:.2f}")
        print()

    def mostrar_inicio_caixa(self, numero_caixa):
        print(f"Caixa {numero_caixa} iniciou as vendas")

    def mostrar_fim_caixa(self, numero_caixa):
        print(f"Caixa {numero_caixa} terminou as vendas")

    def mostrar_saldo_final(self, saldo):
        print()
        print("=== RESULTADO FINAL ===")
        print(f"Saldo central: R$ {saldo:.2f}")


# CONTROLLER
class CaixaController:

    def __init__(self, model, view):
        self.model = model
        self.view = view

    def operacao_caixa(self, quantidade_fichas, numero_caixa):

        self.view.mostrar_inicio_caixa(numero_caixa)

        for i in range(quantidade_fichas):
            self.model.adicionar_venda()

        self.view.mostrar_fim_caixa(numero_caixa)

    def executar(self):

        quantidade_caixas = 5
        quantidade_fichas = 1000

        self.view.mostrar_dados_entrada(
            quantidade_caixas,
            quantidade_fichas,
            self.model.valor_ficha
        )

        t1 = threading.Thread(
            target=self.operacao_caixa,
            args=(quantidade_fichas, 1),
            name="Caixa-1"
        )

        t2 = threading.Thread(
            target=self.operacao_caixa,
            args=(quantidade_fichas, 2),
            name="Caixa-2"
        )

        t3 = threading.Thread(
            target=self.operacao_caixa,
            args=(quantidade_fichas, 3),
            name="Caixa-3"
        )

        t4 = threading.Thread(
            target=self.operacao_caixa,
            args=(quantidade_fichas, 4),
            name="Caixa-4"
        )

        t5 = threading.Thread(
            target=self.operacao_caixa,
            args=(quantidade_fichas, 5),
            name="Caixa-5"
        )

        t1.start()
        t2.start()
        t3.start()
        t4.start()
        t5.start()

        t1.join()
        t2.join()
        t3.join()
        t4.join()
        t5.join()

        self.view.mostrar_saldo_final(
            self.model.retornar_saldo()
        )


# PROGRAMA PRINCIPAL
if __name__ == "__main__":

    model = SaldoModel()
    view = CaixaView()

    controller = CaixaController(model, view)

    controller.executar()