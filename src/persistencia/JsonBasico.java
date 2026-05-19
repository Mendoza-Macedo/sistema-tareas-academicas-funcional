package persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonBasico {
    public static String convertirAJson(Object valor) {
        if (valor == null) {
            return "null";
        }
        if (valor instanceof String texto) {
            return "\"" + escapar(texto) + "\"";
        }
        if (valor instanceof Number || valor instanceof Boolean) {
            return String.valueOf(valor);
        }
        if (valor instanceof Map<?, ?> mapa) {
            StringBuilder sb = new StringBuilder("{");
            boolean primero = true;
            for (Map.Entry<?, ?> entrada : mapa.entrySet()) {
                if (!primero) {
                    sb.append(",");
                }
                primero = false;
                sb.append(convertirAJson(String.valueOf(entrada.getKey())));
                sb.append(":");
                sb.append(convertirAJson(entrada.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (valor instanceof List<?> lista) {
            StringBuilder sb = new StringBuilder("[");
            boolean primero = true;
            for (Object item : lista) {
                if (!primero) {
                    sb.append(",");
                }
                primero = false;
                sb.append(convertirAJson(item));
            }
            sb.append("]");
            return sb.toString();
        }
        throw new IllegalArgumentException("Tipo no soportado en JSON");
    }

    private static String escapar(String texto) {
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public static Object parsear(String texto) {
        return new Parser(texto).parsearValor();
    }

    private static class Parser {
        private final String texto;
        private int indice;

        private Parser(String texto) {
            this.texto = texto;
        }

        private Object parsearValor() {
            saltarEspacios();
            if (indice >= texto.length()) {
                throw new IllegalArgumentException("JSON invalido");
            }
            char actual = texto.charAt(indice);
            return switch (actual) {
                case '{' -> parsearObjeto();
                case '[' -> parsearLista();
                case '"' -> parsearTexto();
                case 't' -> parsearLiteral("true", Boolean.TRUE);
                case 'f' -> parsearLiteral("false", Boolean.FALSE);
                case 'n' -> parsearLiteral("null", null);
                default -> parsearNumero();
            };
        }

        private Map<String, Object> parsearObjeto() {
            Map<String, Object> mapa = new LinkedHashMap<>();
            avanzar('{');
            saltarEspacios();
            if (ver('}')) {
                avanzar('}');
                return mapa;
            }
            while (true) {
                String llave = parsearTexto();
                avanzar(':');
                Object valor = parsearValor();
                mapa.put(llave, valor);
                saltarEspacios();
                if (ver('}')) {
                    avanzar('}');
                    break;
                }
                avanzar(',');
            }
            return mapa;
        }

        private List<Object> parsearLista() {
            List<Object> lista = new ArrayList<>();
            avanzar('[');
            saltarEspacios();
            if (ver(']')) {
                avanzar(']');
                return lista;
            }
            while (true) {
                lista.add(parsearValor());
                saltarEspacios();
                if (ver(']')) {
                    avanzar(']');
                    break;
                }
                avanzar(',');
            }
            return lista;
        }

        private String parsearTexto() {
            avanzar('"');
            StringBuilder sb = new StringBuilder();
            while (indice < texto.length()) {
                char actual = texto.charAt(indice++);
                if (actual == '"') {
                    return sb.toString();
                }
                if (actual == '\\') {
                    char escape = texto.charAt(indice++);
                    switch (escape) {
                        case '"' -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        default -> sb.append(escape);
                    }
                } else {
                    sb.append(actual);
                }
            }
            throw new IllegalArgumentException("Texto JSON sin cerrar");
        }

        private Object parsearLiteral(String esperado, Object valor) {
            if (!texto.startsWith(esperado, indice)) {
                throw new IllegalArgumentException("Literal JSON invalido");
            }
            indice += esperado.length();
            return valor;
        }

        private Number parsearNumero() {
            int inicio = indice;
            if (texto.charAt(indice) == '-') {
                indice++;
            }
            while (indice < texto.length() && Character.isDigit(texto.charAt(indice))) {
                indice++;
            }
            return Long.parseLong(texto.substring(inicio, indice));
        }

        private void avanzar(char esperado) {
            saltarEspacios();
            if (indice >= texto.length() || texto.charAt(indice) != esperado) {
                throw new IllegalArgumentException("JSON invalido");
            }
            indice++;
        }

        private boolean ver(char esperado) {
            saltarEspacios();
            return indice < texto.length() && texto.charAt(indice) == esperado;
        }

        private void saltarEspacios() {
            while (indice < texto.length() && Character.isWhitespace(texto.charAt(indice))) {
                indice++;
            }
        }
    }
}
