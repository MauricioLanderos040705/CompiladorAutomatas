# Compilador de Lenguajes y Automatas

Primer avance en Java y Swing. El lenguaje usa palabras reservadas con tema
de tacos y salsas. El programa realiza analisis lexico: separa tokens, reconoce
identificadores y numeros con AFD, guarda simbolos y muestra errores.

## Ejecutar

Se necesita un JDK 11 o superior. No hay dependencias externas.
Desde la carpeta raiz, en Terminal, PowerShell o una terminal de VS Code:

```sh
javac -encoding UTF-8 --release 11 -d out Main.java PruebaAutomata.java PruebaNumeros.java PruebaIntegracion.java
java -cp out Main
```

La carpeta `out` se genera al compilar. El programa crea su tabla en `data`
al iniciar. Estas carpetas generadas no se suben a Git.

## Verificar

```sh
java -cp out PruebaAutomata
java -cp out PruebaNumeros
java -Djava.awt.headless=true -cp out PruebaIntegracion
```

Las primeras dos pruebas imprimen ejemplos. `PruebaIntegracion` comprueba
resultados automaticamente y falla si encuentra una regresion. Usa un archivo
temporal propio y no cambia la tabla de simbolos de la aplicacion.

GitHub Actions ejecutara la compilacion y esta prueba en cada subida y pull
request cuando el proyecto se publique en GitHub con Actions habilitado.
Eso verifica el codigo y componentes del editor sin abrir la ventana completa.

## Estructura

| Ubicacion | Responsabilidad |
| --- | --- |
| `Main.java` | Iniciar la aplicacion en el hilo de Swing |
| `view/` | Editor, menus, tablas y panel de errores |
| `controller/` | Conectar acciones y leer archivos |
| `model/lexer/` | Separar tokens manualmente y guardar sus posiciones |
| `model/automata/` | Reconocer identificadores y numeros |
| `model/keywords/` | Palabras reservadas y coloreado |
| `model/symboltable/` | Simbolos y archivo de acceso aleatorio |
| `model/compiler/` | Coordinar el analisis |
| `model/errors/` | Registrar y describir errores |

## Reglas actuales

- Se distinguen mayusculas de minusculas.
- Un identificador inicia con letra ASCII o guion bajo. Despues admite letras
  ASCII, guion bajo y digitos. El limite es de 40 caracteres.
- Un numero admite enteros y decimales con signo negativo opcional. Debe tener
  digitos antes del punto y, si tiene punto, tambien despues. Su limite total
  es de 40 caracteres. `.5` y `5.` se rechazan.
- El lexer distingue un signo negativo de una resta segun el token anterior.
- Las 12 reservadas se cargan primero, en su orden de definicion. Despues se
  agregan identificadores y numeros validos en su primera aparicion.
- Un lexema repetido no crea otro registro. Cada compilacion reinicia la tabla.
- Cada registro del archivo ocupa 92 bytes. Se puede leer directamente por ID.

Las reglas y transiciones son las que ya implemento el equipo. Todavia no hay
analisis sintactico o semantico, ni soporte especial para cadenas o comentarios.
El reporte academico solicitado en la consigna se prepara aparte de esta guia.

## Correcciones de este avance

| Archivo | Cambio |
| --- | --- |
| `model/symboltable/SymbolTable.java` | Buscar en la lista en lugar de usar HashMap; conservar orden y duplicados |
| `model/keywords/ReservedWords.java` | Comparar secuencialmente las palabras, sin HashSet |
| `model/keywords/KeywordHighlighter.java` | Colorear los tokens reservados del mismo Lexer, sin expresiones regulares |
| `view/ErrorPanel.java` | Mover aqui la clase que antes estaba en model/errors, para coincidir con su package |

El coloreado usa linea y columna para ubicar cada token dentro del documento.
Por ejemplo, `2tacoBistec` se mantiene como candidato numerico invalido y no
colorea `tacoBistec` por separado.

## Subir por primera vez a GitHub

Git guarda el historial en tu computadora. GitHub aloja el repositorio para
compartirlo y revisar propuestas de cambios.

1. Crea en GitHub un repositorio llamado, por ejemplo, `CompiladorAutomatas`.
   Puede ser privado. Dejalo vacio, sin README, licencia ni gitignore, porque
   esta carpeta ya incluye archivos preparados.
2. Entra a esta carpeta desde la terminal. En la computadora de Mau:

   ```sh
   cd /Users/mauriciolanderos/Desktop/CompiladorAutomatas
   ```

3. Inicializa y registra la primera version:

   ```sh
   git init -b main
   git add .
   git commit -m "Primer avance con correcciones y pruebas"
   ```

   Si Git solicita tu identidad, configura `git config user.name "Tu nombre"`
   y `git config user.email "Tu correo de GitHub"` dentro de esta carpeta.
   Usa el correo privado de GitHub si esa es tu preferencia. Repite el commit.

4. Reemplaza `TU_USUARIO` por el propietario real del repositorio y conecta
   la direccion que GitHub muestra en su boton Code:

   ```sh
   git remote add origin https://github.com/TU_USUARIO/CompiladorAutomatas.git
   git push -u origin main
   ```

   Autenticate mediante GitHub Desktop, GitHub CLI o SSH; la contrasena normal
   de la cuenta no sirve para subir codigo por HTTPS. Con GitHub CLI instalado,
   usa `gh auth login` y `gh auth setup-git`. No compartan credenciales.

5. En GitHub, abre Settings > Collaborators, invita a tu companero y pidele
   que acepte. Cada quien usa su propia cuenta.

Estos pasos siguen la [guia oficial para subir codigo local](https://docs.github.com/en/migrations/importing-source-code/using-the-command-line-to-import-source-code/adding-locally-hosted-code-to-github).
La invitacion esta explicada en [acceso para colaboradores](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/repository-access-and-collaboration/inviting-collaborators-to-a-personal-repository).

## El companero descarga su copia una sola vez

Desde una carpeta donde quiera tener el proyecto, usando la direccion real:

```sh
git clone https://github.com/TU_USUARIO/CompiladorAutomatas.git
cd CompiladorAutomatas
```

Desde entonces ambos trabajan sobre sus copias del mismo repositorio.

## Una rama por tarea

Revisa `git status` antes de cambiar de rama. Guarda o termina el trabajo
pendiente antes de empezar otra tarea. Con la copia sin cambios pendientes:

```sh
git switch main
git pull --ff-only
git switch -c mejora/editor
```

Realiza el cambio, compila y ejecuta las pruebas. Revisa los archivos antes
de registrarlos y subirlos:

```sh
git diff
git status
git add .
git commit -m "Mejorar el editor"
git push -u origin mejora/editor
```

En GitHub, abre un pull request desde `mejora/editor` hacia `main`. El otro
integrante revisa el cambio y los resultados de Actions. Cuando este aprobado,
integrenlo con Merge pull request. Los nuevos commits en esa rama actualizan
el mismo pull request. [Referencia: GitHub flow](https://docs.github.com/en/get-started/using-github/github-flow).

Despues de integrar, ambos actualizan su copia:

```sh
git switch main
git pull --ff-only
```

Un conflicto aparece cuando los cambios de las ramas no se pueden combinar
automaticamente. Revisen ambos cambios y resuelvanlo en la rama de trabajo
antes de integrar. Eviten subir cambios a main directamente en el trabajo diario.

## Trabajar con Codex y Claude Code

Cada integrante abre su copia local del repositorio en su herramienta. La
historia permite ver que cambio, quien lo propuso y sobre que version trabajo.
Asignen tareas distintas y usen una rama por tarea, como `mejora/editor` y
`mejora/errores`. Si usan varios agentes en una sola computadora, utilicen
copias o worktrees separados.

`AGENTS.md` contiene las reglas del proyecto para Codex. `CLAUDE.md` importa
ese mismo archivo, para que Claude Code reciba las mismas convenciones.
El acceso de los agentes depende de las credenciales y permisos que tenga
cada integrante en su entorno.

Para pedir una tarea con pull request:

> Lee las instrucciones del proyecto. Crea una rama desde main actualizado,
> implementa [tarea concreta] conservando las reglas del lenguaje y el patron
> actual, ejecuta las pruebas, sube la rama y abre un pull request hacia main
> con la descripcion del cambio y los resultados. Dejalo para revision del equipo.

Los agentes pueden preparar cambios y abrir pull requests cuando tengan acceso
al repositorio. Para hacerlo desde la terminal necesitan GitHub CLI instalado
y autenticado; tambien pueden usar una integracion con GitHub disponible.
Si no tienen esa conexion, pueden dejar los cambios locales y ustedes abrir
el pull request desde GitHub.

Fuentes: [OpenAI Docs sobre worktrees y pull requests](https://learn.chatgpt.com/docs/environments/git-worktrees),
[Claude Code: flujos de trabajo](https://code.claude.com/docs/en/common-workflows),
[Claude Code: instrucciones compartidas](https://code.claude.com/docs/en/memory).
