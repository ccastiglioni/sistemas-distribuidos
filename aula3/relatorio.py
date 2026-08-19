import threading


# MODEL
class FilialModel:

    def calcular_total(self, lista_vendas):
        total = 0

        for venda in lista_vendas:
            total += venda

        return total


# VIEW
class FilialView:

    def mostrar_dados_entrada(self, quantidade_filiais, quantidade_registros):
        print("=== DADOS DAS FILIAIS ===")
        print("Quantidade de filiais:", quantidade_filiais)
        print("Quantidade de registros por filial:", quantidade_registros)
        print()

    def mostrar_resultado_filial(self, numero_filial, total):
        print(f"Filial {numero_filial}: R$ {total:.2f}")

    def mostrar_faturamento_total(self, total):
        print()
        print("=== FATURAMENTO TOTAL ===")
        print(f"Faturamento total: R$ {total:.2f}")


# THREAD
class ThreadFilial(threading.Thread):

    def __init__(self, lista_vendas, numero_filial):
        super().__init__(name=f"Filial-{numero_filial}")

        self.lista_vendas = lista_vendas
        self.numero_filial = numero_filial
        self.resultado = 0

    def run(self):

        model = FilialModel()

        self.resultado = model.calcular_total(
            self.lista_vendas
        )

    def retornar_resultado(self):
        return self.resultado


# CONTROLLER
class FilialController:

    def __init__(self, view):
        self.view = view

    def executar(self):

        quantidade_filiais = 4
        quantidade_registros = 10000

        # listas independentes
        lista_filial1 = [10] * quantidade_registros
        lista_filial2 = [20] * quantidade_registros
        lista_filial3 = [30] * quantidade_registros
        lista_filial4 = [40] * quantidade_registros

        self.view.mostrar_dados_entrada(
            quantidade_filiais,
            quantidade_registros
        )

        # cada thread recebe somente a lista da sua filial
        t1 = ThreadFilial(
            lista_filial1,
            1
        )

        t2 = ThreadFilial(
            lista_filial2,
            2
        )

        t3 = ThreadFilial(
            lista_filial3,
            3
        )

        t4 = ThreadFilial(
            lista_filial4,
            4
        )

        # FORK
        t1.start()
        t2.start()
        t3.start()
        t4.start()

        # JOIN
        t1.join()
        t2.join()
        t3.join()
        t4.join()

        # resultados individuais
        resultado1 = t1.retornar_resultado()
        resultado2 = t2.retornar_resultado()
        resultado3 = t3.retornar_resultado()
        resultado4 = t4.retornar_resultado()

        self.view.mostrar_resultado_filial(1, resultado1)
        self.view.mostrar_resultado_filial(2, resultado2)
        self.view.mostrar_resultado_filial(3, resultado3)
        self.view.mostrar_resultado_filial(4, resultado4)

        # thread principal junta os resultados
        faturamento_total = (
            resultado1
            + resultado2
            + resultado3
            + resultado4
        )

        self.view.mostrar_faturamento_total(
            faturamento_total
        )


# PROGRAMA PRINCIPAL
if __name__ == "__main__":

    view = FilialView()

    controller = FilialController(view)

    controller.executar()