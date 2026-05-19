# Sistema de Gestion de Tareas Academicas

Proyecto en Java 21 con interfaz Swing, POO, arquitectura modular y un analisis heuristico de entropia de la informacion.

## Estructura

- `src/aplicacion`: punto de arranque
- `src/modelo`: clases del dominio
- `src/servicio`: logica del sistema
- `src/analisis`: entropia
- `src/funcional`: funciones puras, filtros, composicion funcional y calculo de indicadores
- `src/persistencia`: archivo JSON local
- `src/vista`: interfaz Swing
- `src/prueba`: prueba basica
- `docs`: material de entrega, UML, documento Overleaf y guion de sustentacion

## Compilar en PowerShell

```powershell
$archivos = Get-ChildItem -Recurse -Path .\src -Filter *.java | ForEach-Object { $_.FullName }
New-Item -ItemType Directory -Force -Path .\out | Out-Null
javac -d .\out $archivos
```

## Ejecutar la aplicacion

```powershell
java -cp .\out aplicacion.Main
```

## Ejecutar la prueba basica

```powershell
java -cp .\out prueba.PruebaBasica
```
