# Entrega de la actividad

## Tema

Sistema de Gestion de Tareas Academicas con Programacion Funcional, POO y Entropia de la Informacion.

## Entregable 1: Documento Overleaf

Archivo base:

- `docs/overleaf/main.tex`

Pendiente antes de entregar:

- Reemplazar `AGREGAR_ENLACE_GITHUB` por el enlace real del repositorio.
- Exportar los diagramas PlantUML como imagenes si se desea insertarlos directamente en Overleaf.

## Entregable 2: Codigo fuente

Paquetes principales:

- `src/modelo`: entidades POO.
- `src/servicio`: casos de uso y logica del sistema.
- `src/funcional`: funciones puras, composicion funcional y medicion de entropia.
- `src/analisis`: servicio que expone la entropia al sistema.
- `src/vista`: interfaz Swing.
- `src/persistencia`: guardado JSON.

Compilacion en PowerShell:

```powershell
$archivos = Get-ChildItem -Recurse -Path .\src -Filter *.java | ForEach-Object { $_.FullName }
New-Item -ItemType Directory -Force -Path .\out | Out-Null
javac -d .\out $archivos
```

Prueba:

```powershell
java -cp .\out prueba.PruebaBasica
```

Ejecucion:

```powershell
java -cp .\out aplicacion.Main
```

## Entregable 3: Sustentacion tecnica

Guion:

- `docs/sustentacion/guion_video.md`

Duracion maxima sugerida: 10 minutos.

## Diagramas UML

Editables en PlantUML:

- `docs/uml/casos_uso.puml`
- `docs/uml/clases.puml`
- `docs/uml/flujo_funcional.puml`
