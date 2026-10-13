# Contexto

Mau es estudiante de ingenieria en sistemas computacionales. Este proyecto es
el primer avance de Lenguajes y Automatas, escrito en Java y Swing.

# Convenciones

- Conservar la separacion existente entre model, view y controller.
- Mantener nombres y comentarios en espanol, siguiendo el estilo del proyecto.
- Consultar InstruccionesPrimerAvance.txt antes de cambiar el alcance.
- Conservar las palabras reservadas, operadores, delimitadores y reglas de los
  AFD salvo que el equipo solicite cambiarlos.
- Separar tokens mediante recorridos manuales. No usar expresiones regulares,
  tablas hash ni bibliotecas de tokenizacion.
- Guardar cada clase en la carpeta correspondiente a su declaracion package.
- Reutilizar Lexer para el coloreado; mantener las posiciones de linea y columna.
- Mantener el orden de los simbolos, la exclusion de duplicados y el formato
  de registros de 92 bytes del archivo de acceso aleatorio.
- Hacer cambios pequenos relacionados con la tarea y explicar su proposito.

# Verificacion

Desde la raiz del proyecto, con un JDK 11 o superior:

```sh
javac -encoding UTF-8 --release 11 -d out Main.java PruebaAutomata.java PruebaNumeros.java PruebaIntegracion.java
java -Djava.awt.headless=true -cp out PruebaIntegracion
```

# Colaboracion

Trabajar en una rama por tarea. Antes de cambiar de rama, revisar git status.
Actualizar main antes de empezar cuando exista un remoto. No sobrescribir
cambios de otro colaborador. Si se solicita un pull request, describir los
cambios y la verificacion realizada para que el equipo pueda revisarlo.

