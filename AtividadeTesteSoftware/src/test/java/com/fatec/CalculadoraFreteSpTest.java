package com.fatec;

// metodos do JUnit 5.
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("ALL")
public class CalculadoraFreteSpTest {

    // TESTE 1: Verifica o frete gratis para compras a partir de 300
    @ParameterizedTest(name = "Compra de R${0} deve ter frete grátis (R$0.0)")
    @ValueSource(doubles = {300.0, 300.01, 500.0, 1500.0})
    void testFreteGratis(double valorCompra) {
        // Invoca o metodo e passa um peso e região válida, mas com valor de compra >= 300.
        double freteReal = CalculadoraFreteSp.calculafrete(2000, "LESTE", valorCompra);
        // O valor esperado é 0.0. O JUnit pede uma margem de segurança para comparar double, pos isso o delta
        assertEquals(0.0, freteReal, 0.001);
    }

    // TESTE 2: Verifica o calculo para diferentes regiões e pesos sem frete gratis.
    @ParameterizedTest(name = "Peso {0}g para região {1} = Frete de R${3}")
    @CsvSource({
            "800, LESTE, 150.0, 7.0",    // Menor que 1kg: cobra 1kg (1 * 7.0 = 7.0)
            "1500, SUL, 100.0, 15.0",    // 1.5kg * 10.0 = 15.0
            "2200, NORTE, 200.0, 28.6",  // 2.2kg * 13.0 = 28.6
            "1200, OESTE, 50.0, 18.0"    // 1.2kg * 15.0 = 18.0
    })
    void testCalculoFreteRegioes(double peso, String regiao, double valorCompra, double freteEsperado) {
        // Executa o método com os dados fornecidos pelo CSV.
        double freteReal = CalculadoraFreteSp.calculafrete(peso, regiao, valorCompra);
        // Verifica se o valor calculado é igual ao esperado.
        assertEquals(freteEsperado, freteReal, 0.001);
    }

    // TESTE 3: Verifica a regra de negocio do arredondamento para cima a partir da terceira casa decimal
    @ParameterizedTest(name = "Arredondamento de {0}g para região {1} = R${3}")
    @CsvSource({
            "1333, LESTE, 100.0, 9.34",  // 1.333kg * 7.0 = 9.331 -> Arredonda para 9.34
            "1234, NORTE, 100.0, 16.05"  // 1.234kg * 13.0 = 16.042 -> Arredonda para 16.05
    })
    void testArredondamento(double peso, String regiao, double valorCompra, double freteEsperado) {
        double freteReal = CalculadoraFreteSp.calculafrete(peso, regiao, valorCompra);
        assertEquals(freteEsperado, freteReal, 0.001);
    }

    // TESTE 4: Verifica se o sistema lança erro para regioes nao cadastradas ou em branco
    @ParameterizedTest(name = "Região inválida: ''{0}'' deve lançar exceção")
    @ValueSource(strings = {"CENTRO", "INTERIOR", "   "})
    void testRegioesInvalidas(String regiaoInvalida) {
        // assertThrows verifica se o bloco de código interno lança a exceção especificada.
        assertThrows(IllegalArgumentException.class, () -> {
            CalculadoraFreteSp.calculafrete(1500, regiaoInvalida, 100.0);
        });
    }

    // TESTE 5: Verifica se o sistema lança erro para pesos e valores de compra menores ou iguais a zero.
    @ParameterizedTest(name = "Valores inválidos: peso={0}g, compra=R${1} deve lançar exceção")
    @CsvSource({
            "0, 100.0",       // Peso zero
            "-500, 100.0",    // Peso negativo
            "1500, 0",        // Valor de compra zerado
            "1500, -10.0"     // Valor de compra negativo
    })
    void testValoresInvalidos(double peso, double valorCompra) {
        assertThrows(IllegalArgumentException.class, () -> {
            CalculadoraFreteSp.calculafrete(peso, "LESTE", valorCompra);
        });
    }
}