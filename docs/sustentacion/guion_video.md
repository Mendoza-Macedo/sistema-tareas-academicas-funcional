# Guion completo para sustentacion tecnica

Duracion objetivo: 9 minutos 30 segundos a 10 minutos.

Indicacion general: leer el texto con calma. En cada bloque, cambiar la pantalla segun la indicacion entre corchetes.

## 0:00 - 0:40 Presentacion

[Pantalla: mostrar la portada del documento en Overleaf.]

Buenos dias. Mi nombre es Mendoza Macedo Jose Miguel y en esta sustentacion presento mi proyecto titulado: Sistema de Gestion de Tareas Academicas con Programacion Funcional, Programacion Orientada a Objetos y Entropia de la Informacion.

El proyecto fue desarrollado para el curso Lenguajes de Programacion. La finalidad principal es construir una aplicacion en Java que permita organizar cursos y actividades academicas, pero aplicando tambien conceptos teoricos del curso, especialmente programacion funcional, programacion orientada a objetos, modularidad y analisis de entropia.

Durante la exposicion voy a explicar el problema, el diseno UML, la arquitectura, la parte orientada a objetos, la parte funcional, el calculo de entropia y finalmente una breve demostracion del sistema.

## 0:40 - 1:30 Problema y objetivo

[Pantalla: en Overleaf, bajar a la seccion Introduccion.]

El problema que se busca atender es la desorganizacion de actividades academicas. Un estudiante normalmente lleva varios cursos al mismo tiempo, con tareas, evaluaciones, prioridades y fechas de entrega diferentes. Si esta informacion esta dispersa, aumenta la incertidumbre sobre que actividad debe atender primero.

Por esa razon, el objetivo general del proyecto es implementar un sistema de gestion de tareas academicas que permita registrar cursos y actividades, controlar su estado de avance y calcular un indicador de entropia academica.

La idea central es trabajar con un diseno hibrido. Por un lado, se usa programacion orientada a objetos para representar entidades del dominio, como estudiante, curso y actividad academica. Por otro lado, se usa programacion funcional para procesar las listas de actividades mediante filtros, funciones puras, composicion y operaciones declarativas.

## 1:30 - 2:20 Diagrama de casos de uso

[Pantalla: en Overleaf, mostrar la figura "Diagrama de casos de uso".]

En este primer diagrama UML se muestran los casos de uso principales del sistema. El actor es el estudiante, quien puede registrar cursos, registrar actividades, editar actividades, cambiar el estado de una actividad, filtrar actividades, visualizar el tablero y calcular la entropia academica.

Tambien se observa que algunas acciones incluyen el guardado de informacion. Por ejemplo, cuando se registra un curso, se registra una actividad o se cambia un estado, el sistema debe persistir los datos en el archivo JSON.

Este diagrama permite justificar el alcance funcional del proyecto. No se trata solamente de una aplicacion visual, sino de un sistema que organiza informacion academica y la transforma en indicadores utiles para el estudiante.

## 2:20 - 3:20 Diagrama de clases

[Pantalla: en Overleaf, mostrar la figura "Diagrama de clases".]

Ahora se muestra el diagrama de clases. Aqui se puede observar la estructura orientada a objetos del sistema. La clase Estudiante contiene una lista de cursos. Cada Curso contiene una lista de actividades academicas. La clase ActividadAcademica es abstracta y concentra atributos comunes como titulo, descripcion, fecha de entrega, prioridad, estado, fecha de registro e identificador del curso.

De esta clase abstracta heredan TareaAcademica y EvaluacionAcademica. Esta parte es importante porque demuestra el uso de herencia y polimorfismo. Ambas clases son actividades academicas, pero pueden especializar su comportamiento mediante metodos como getTipoActividad y getResumenVisual.

Tambien se observan los servicios del sistema: SistemaAcademico, ServicioCursos, ServicioActividades y ServicioEntropia. Estos servicios coordinan la logica de registro, consulta, edicion y analisis. Finalmente aparece el paquete funcional, representado por ActividadFunciones y EntropiaFuncional, que se encarga de transformar y analizar las colecciones de datos.

## 3:20 - 4:10 Flujo funcional

[Pantalla: en Overleaf, mostrar la figura "Flujo de datos y composicion funcional".]

Este tercer diagrama representa el flujo funcional de datos. El proceso inicia con una lista de cursos. A partir de esa lista se obtiene una lista plana de actividades usando una transformacion equivalente a flatMap.

Luego las actividades se ordenan y se aplican filtros compuestos. Si existe un filtro por curso, se conserva solamente la actividad cuyo curso coincide. Si existe un filtro por prioridad, se conserva solamente la actividad con la prioridad seleccionada.

Despues se miden indicadores de manera funcional: actividades en inicio, actividades en proceso, actividades vencidas, actividades de prioridad alta pendientes y concentracion por fecha de entrega. Finalmente, esos indicadores se reducen a un puntaje y se interpreta el resultado como entropia baja, media o alta.

Este diagrama es importante porque adapta el UML al enfoque solicitado en la actividad: no solo muestra clases, sino tambien flujo de datos y composicion de funciones.

## 4:10 - 5:20 POO en el codigo

[Pantalla: abrir el proyecto en el editor. Mostrar `src/modelo/ActividadAcademica.java`.]

En el codigo, la programacion orientada a objetos se encuentra principalmente en el paquete modelo. Aqui se observa la clase abstracta ActividadAcademica. Esta clase representa la abstraccion general de una actividad academica. Contiene atributos comunes y metodos que son compartidos por las actividades concretas.

[Pantalla: mostrar `src/modelo/TareaAcademica.java` y luego `src/modelo/EvaluacionAcademica.java`.]

Ahora se muestran las clases TareaAcademica y EvaluacionAcademica. Estas clases heredan de ActividadAcademica. Con esto se aplica el pilar de herencia. Tambien se aplica polimorfismo porque ambas redefinen metodos como getTipoActividad y getResumenVisual.

[Pantalla: volver brevemente a `ActividadAcademica.java`, mostrando atributos privados y getters/setters.]

El encapsulamiento se evidencia porque los atributos se manejan dentro de la clase y se accede a ellos mediante metodos. De esta manera, el dominio academico queda modelado con los cuatro pilares: abstraccion, encapsulamiento, herencia y polimorfismo.

## 5:20 - 6:50 Programacion funcional en el codigo

[Pantalla: mostrar `src/funcional/ActividadFunciones.java`.]

La parte funcional esta concentrada en el paquete funcional. Primero se muestra la clase ActividadFunciones. Esta clase no representa una entidad con estado, sino un conjunto de funciones puras para procesar listas de actividades.

Aqui se observa el metodo actividadesDe, que recibe una lista de cursos y devuelve una lista de actividades. Para eso usa stream, flatMap, sorted y toList. Este estilo es declarativo porque se expresa que transformacion se quiere realizar, sin escribir ciclos manuales para acumular resultados.

[Pantalla: mostrar el metodo `filtrar`.]

En el metodo filtrar se observa la composicion funcional. Se crea un flujo de funciones usando porCurso y porPrioridad, unidos con andThen. Primero se aplica el filtro por curso y luego el filtro por prioridad. Esto demuestra composicion de funciones, uno de los conceptos importantes del paradigma funcional.

[Pantalla: mostrar los metodos `porCurso` y `porPrioridad`.]

Los metodos porCurso y porPrioridad devuelven funciones. Cada funcion recibe una lista de actividades y devuelve una nueva lista filtrada. Tambien se usan predicados, que son expresiones funcionales que evaluan si una actividad cumple una condicion.

Esta parte cumple con la idea de programacion funcional porque se evita modificar directamente la lista original y se trabaja con transformaciones sobre colecciones.

## 6:50 - 8:00 Entropia funcional

[Pantalla: mostrar `src/funcional/EntropiaFuncional.java`.]

Ahora se muestra la clase EntropiaFuncional. Esta clase calcula el nivel de desorden academico a partir de las actividades registradas.

Primero se observa el record IndicadoresEntropia. Este record agrupa los valores que se van a medir: total de actividades, actividades en inicio, actividades en proceso, vencidas, prioridad alta pendiente, completadas, cercanas a vencer, sin fecha y maxima concentracion de actividades en una fecha.

[Pantalla: mostrar el metodo `medir`.]

El metodo medir recibe una lista de actividades y la fecha actual. A partir de esa informacion calcula los indicadores. Por ejemplo, usa groupingBy para agrupar actividades por fecha de entrega. Tambien usa funciones de conteo con predicados para medir actividades vencidas, actividades completadas o actividades con prioridad alta pendiente.

[Pantalla: mostrar el metodo `puntaje` y luego `interpretar`.]

Luego, el metodo puntaje combina los indicadores. Las actividades en inicio y en proceso suman al desorden. Las vencidas tienen mayor peso. Las actividades de prioridad alta pendientes tambien incrementan el puntaje. Ademas, se consideran penalizaciones si hay muchas actividades sin fecha, muchas actividades cercanas o mucha concentracion en una misma fecha.

Finalmente, el metodo interpretar convierte el puntaje en un resultado cualitativo. Si el puntaje es bajo, la entropia es baja. Si el puntaje es intermedio, la entropia es media. Si el puntaje supera el limite, se interpreta como entropia alta.

De esta manera, el sistema usa la entropia de la informacion como una idea adaptada al contexto academico: mientras mas tareas pendientes, vencidas, urgentes o concentradas existan, mayor sera la incertidumbre del estudiante.

## 8:00 - 8:50 Integracion con servicios

[Pantalla: mostrar `src/analisis/ServicioEntropia.java`.]

La clase ServicioEntropia sirve como puente entre el sistema orientado a objetos y el modulo funcional. Primero convierte la lista de cursos en una lista de actividades usando ActividadFunciones.actividadesDe. Luego envia esa lista a EntropiaFuncional.evaluar.

[Pantalla: mostrar `src/servicio/ServicioActividades.java`, metodo `listarActividades` y metodo `filtrar`.]

En ServicioActividades tambien se reutiliza el modulo funcional. El metodo listarActividades obtiene las actividades ordenadas mediante ActividadFunciones. El metodo filtrar delega el filtrado a ActividadFunciones.filtrar. Esto significa que la logica funcional no esta aislada, sino integrada en el comportamiento real del sistema.

## 8:50 - 9:30 Prueba basica

[Pantalla: mostrar terminal en la carpeta del proyecto.]

Para verificar el funcionamiento, se ejecuta la prueba basica del sistema. Primero se compila el proyecto y luego se ejecuta la clase PruebaBasica.

[Pantalla: ejecutar o mostrar el comando.]

El comando de ejecucion es:

```powershell
java -cp out prueba.PruebaBasica
```

[Pantalla: mostrar la salida `Prueba basica OK`.]

La salida esperada es Prueba basica OK. Esta prueba valida que se registre correctamente un curso, que se registren dos actividades, que el filtro funcional produzca el resultado esperado y que el calculo de entropia genere un puntaje valido.

## 9:30 - 10:00 Cierre

[Pantalla: volver al Overleaf, seccion Conclusiones o Enlaces de entrega.]

En conclusion, el proyecto cumple con el objetivo de aplicar programacion funcional desde la teoria hasta la practica. El sistema conserva los cuatro pilares de la programacion orientada a objetos para modelar el dominio academico, pero utiliza programacion funcional para procesar los datos de forma declarativa.

Ademas, el calculo de entropia permite transformar las actividades registradas en un indicador de desorden academico, lo cual aporta valor practico para la organizacion del estudiante.

Finalmente, el proyecto cuenta con documento en Overleaf, diagramas UML, codigo fuente publicado en GitHub y esta sustentacion tecnica como evidencia del desarrollo realizado. Muchas gracias.
