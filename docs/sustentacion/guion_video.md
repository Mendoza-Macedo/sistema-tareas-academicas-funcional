# Guion de sustentacion tecnica

Duracion maxima: 10 minutos.

## 0:00 - 0:40 Presentacion

Presentar el titulo del proyecto: Sistema de Gestion de Tareas Academicas con Programacion Funcional, POO y Entropia de la Informacion. Indicar que el objetivo es organizar actividades academicas y medir el desorden mediante entropia.

## 0:40 - 1:40 Problema

Explicar que los estudiantes suelen perder control de tareas, evaluaciones, prioridades y fechas. El sistema centraliza esa informacion y la transforma en indicadores.

## 1:40 - 3:00 Diseno POO

Mostrar el diagrama de clases. Explicar los cuatro pilares:

- Abstraccion: `ActividadAcademica`.
- Encapsulamiento: atributos privados y metodos de acceso.
- Herencia: `TareaAcademica` y `EvaluacionAcademica`.
- Polimorfismo: `getTipoActividad()` y `getResumenVisual()`.

## 3:00 - 4:20 Casos de uso

Mostrar el diagrama de casos de uso: registrar curso, registrar actividad, editar, cambiar estado, filtrar, visualizar tablero y calcular entropia.

## 4:20 - 6:20 Programacion funcional

Mostrar el paquete `funcional`. Explicar que `ActividadFunciones` y `EntropiaFuncional` trabajan con funciones puras, `Stream`, `filter`, `groupingBy`, conteos y composicion con `andThen`.

## 6:20 - 7:40 Entropia academica

Explicar los indicadores: inicio, proceso, vencidas, prioridad alta pendiente, fechas cercanas, sin fecha y concentracion por dia. Luego explicar los niveles baja, media y alta.

## 7:40 - 8:50 Demostracion

Ejecutar:

```powershell
javac -d out (Get-ChildItem -Recurse -Path .\src -Filter *.java | ForEach-Object { $_.FullName })
java -cp out prueba.PruebaBasica
java -cp out aplicacion.Main
```

Mostrar que aparece `Prueba basica OK` y luego abrir la interfaz.

## 8:50 - 10:00 Cierre

Concluir que el proyecto conserva POO, agrega procesamiento funcional estricto y usa entropia para apoyar la toma de decisiones academicas.
