# Compilador de Lenguajes y Autómatas

En este proyecto hicimos una app en Java con una interfaz en Swing para ver cómo se analiza el código de un lenguaje. Usamos palabras relacionadas con tacos y salsas como palabras reservadas. La idea es escribir código, analizarlo y revisar qué elementos reconoce la app y dónde encuentra errores.

## Cómo se usa la app

Al abrirla, tenemos un editor a la izquierda para escribir el código. También podemos cargar un archivo de texto desde **Archivo > Abrir archivo...**. Mientras escribimos, las palabras reservadas se resaltan en naranja y negritas. En la parte de abajo se muestra la línea y la columna donde está el cursor.

Para analizar el texto, presionamos el botón **Compilar**, usamos **F5** o elegimos **Análisis > Compilar**. Los resultados aparecen en las pestañas de la derecha y los errores se muestran en el panel de abajo. Si corregimos el código, hay que volver a compilar para actualizar esos resultados.

## Qué pasa al presionar Compilar

Primero, la app separa el texto en partes llamadas **tokens**. Por ejemplo, en `cantidad = 3;` reconoce `cantidad`, `=`, `3` y `;` por separado. Cada token tiene un tipo, una línea y una columna para saber dónde aparece en el código.

Los tokens se clasifican como palabras reservadas, identificadores, números, operadores o delimitadores. Los caracteres que la app no reconoce se marcan como desconocidos. Los espacios y saltos de línea no aparecen como tokens, pero sí se toman en cuenta para ubicar cada elemento.

Después se revisan los identificadores y los números con **autómatas finitos deterministas (AFD)**. Estos recorren cada texto carácter por carácter para comprobar si cumple las reglas. Si un identificador o un número no es válido, se registra un error.

En la pestaña **Tokens** podemos ver todas las apariciones de cada elemento, aunque se repita. La **Tabla de símbolos**, en cambio, guarda las palabras reservadas y los identificadores y números válidos sin repetirlos. Al iniciar la app ya aparecen las 12 palabras reservadas; al compilar se agregan los demás símbolos en el orden en que se encuentran.

La tabla también se guarda en `data/Tabla de símbolos.dat`. Cada compilación vuelve a crearla con los datos del código que estamos analizando.

El panel **Errores** muestra el tipo de error, una descripción y su línea y columna. Por ejemplo, podemos ver errores por números mal escritos, identificadores demasiado largos o símbolos como `@`. También aparece un resumen con la cantidad de errores, o el mensaje **Sin errores** si no se encontró ninguno.

## Palabras reservadas del lenguaje

Elegimos estos nombres para representar los tipos de datos y las estructuras de control:

| Palabra | Equivalente |
| --- | --- |
| `tacoBistec` | `int` |
| `tacoChorizo` | `float` |
| `tacoPastor` | `double` |
| `tacoSuadero` | `char` |
| `tacoCabeza` | `string` |
| `tacoPapa` | `boolean` |
| `tacoAdobado` | `void` |
| `guacamole` | `if` |
| `picoGallo` | `else` |
| `salsaVerde` | `while` |
| `salsaRoja` | `for` |
| `salsaAmarilla` | `do` |

Por ahora, la app reconoce estas palabras y las resalta. Todavía no comprueba que los tipos de datos o las estructuras de control se usen correctamente.

## Un ejemplo

Podemos escribir esto en el editor y presionar **Compilar**:

```text
tacoBistec cantidad = 3;
tacoChorizo precio = 12.5;
cantidad = cantidad - 1;
```

En la primera línea se reconocen estos tokens:

| Texto | Tipo |
| --- | --- |
| `tacoBistec` | Palabra reservada |
| `cantidad` | Identificador |
| `=` | Operador |
| `3` | Número |
| `;` | Delimitador |

En la tabla de símbolos se agregan `cantidad`, `3`, `precio`, `12.5` y `1`, además de las palabras reservadas que ya estaban cargadas. Aunque `cantidad` aparece varias veces, solo tiene un registro en esa tabla. El `-` de la última línea se reconoce como una resta.

Si cambiamos `12.5` por `12.`, la app muestra un error porque falta un dígito después del punto decimal.

## Reglas que se revisan en esta 

- Se distinguen mayúsculas y minúsculas: `tacoBistec` es una palabra reservada, pero `TacoBistec` se reconoce como identificador.
- Los identificadores empiezan con una letra de `A` a `Z`, de `a` a `z` o con `_`. Después pueden llevar esas mismas letras, guion bajo y dígitos. Su límite es de 40 caracteres.
- Se aceptan números enteros y decimales, incluidos negativos como `-8` y `-2.5`. El signo debe estar junto al número y la app usa el token anterior para distinguirlo de una resta.
- Los decimales deben tener dígitos antes y después del punto. Por eso `.5` y `5.` se rechazan. Los números también tienen un límite de 40 caracteres, contando el signo y el punto.

Esta versión realiza **análisis léxico**. Aunque el botón se llama Compilar, todavía no ejecuta el código ni revisa la sintaxis o el significado de las instrucciones. Tampoco reconoce cadenas entre comillas ni comentarios como elementos propios del lenguaje. Que aparezca **Sin errores** significa que pasó la revisión léxica, pero no asegura que sea un programa correcto.
