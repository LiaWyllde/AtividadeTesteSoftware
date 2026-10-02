package com.fatec;

// A calculadora de fretes comtempla a regra de negócio de uma loja de roupas da zona leste que faz entregas em toda sao paulo

public class CalculadoraFreteSp {

    public static double calculafrete(double peso, String regiao, double valorCompra) {

        //Verifica se todos os valores são válidos.
        if (peso <= 0) {
            throw new IllegalArgumentException("Insira o valor das peças corretamente");
        }
        if (valorCompra <= 0) {
            throw new IllegalArgumentException("O valor de compra deve ser informado");
        }
        if (regiao.isBlank()) {
            throw new IllegalArgumentException("A regiao deve ser informada corretamente");
        }

        // Se a compra for maior que trezentos reais, o frete é grátis
        if (valorCompra >= 300) {
            return 0;
        }

        //Define valor por regiao
        double valorPorKg = switch (regiao.toUpperCase()) {
            case "LESTE" -> 7.0;
            case "SUL" -> 10.0;
            case "NORTE" -> 13.0;
            case "OESTE" -> 15.0;
            default -> throw new IllegalArgumentException("Informe a regiao corretamente");
        };

        //Se o peso em kg for menor que 1, o cálculo deve ser feito com o valor de 1kg
        if (peso/1000 < 1) {peso = 1;}
        //Verifica se o valor está em kg
        if (peso/1000 >= 1) {peso = peso/1000;}

        //Aplica a regra de negocios que arredonda as duas casa apos a virgula para cima
        double valor = peso * valorPorKg;
        valor = Math.ceil(valor * 100) / 100;

        //Retorna o valor do frete
        return valor;
    }
}