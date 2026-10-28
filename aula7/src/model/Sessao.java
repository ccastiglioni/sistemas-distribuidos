package model;

import java.util.Objects;

// Classe mantida próxima à versão fornecida pelo professor.
public class Sessao {
    private String numeroCelular;
    private String dadosSessao;

    public Sessao(String numeroCelular, String dadosSessao) {
        this.numeroCelular = numeroCelular;
        this.dadosSessao = dadosSessao;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getDadosSessao() {
        return dadosSessao;
    }

    public void setDadosSessao(String dadosSessao) {
        this.dadosSessao = dadosSessao;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numeroCelular);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Sessao)) return false;
        Sessao other = (Sessao) obj;
        return Objects.equals(numeroCelular, other.numeroCelular);
    }

    @Override
    public String toString() {
        return "Sessao{numeroCelular=" + numeroCelular + ", dadosSessao=" + dadosSessao + "}";
    }
}
