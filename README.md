# SpeedFast — Sumativa 3 de POO II

Aplicación de escritorio para gestionar repartidores, pedidos y entregas con Java Swing, JDBC y MySQL. Parte del proyecto de la semana 5: las clases previas de polimorfismo y concurrencia se conservan, y `MainSemana5` permite seguir ejecutando esa simulación por consola. Para la sumativa 3 se ejecuta `speedfast.app.Main`.

## Requisitos

- JDK 17 o superior.
- IntelliJ IDEA con soporte Maven.
- MySQL Server local.

## Preparar la base de datos

1. Iniciar MySQL Server.
2. Abrir MySQL Workbench o la consola de MySQL.
3. Ejecutar `database/speedfast_db.sql`. El script crea `speedfast_db` y las tablas `repartidores`, `pedidos` y `entregas` con sus claves foráneas.

El ID de cada registro lo genera MySQL. No hay datos de ejemplo obligatorios; pueden agregarse desde la interfaz.

## Abrir y ejecutar en IntelliJ

1. Abrir esta carpeta como proyecto en IntelliJ IDEA.
2. Esperar a que Maven cargue `mysql-connector-j`.
3. Configurar la ejecución de `speedfast.app.Main`.
4. En **Run > Edit Configurations > Environment variables**, agregar `SPEEDFAST_DB_PASSWORD` con la clave local de MySQL. El programa usa `root` y `jdbc:mysql://localhost:3306/speedfast_db` por defecto. Si tu configuración es diferente, agrega `SPEEDFAST_DB_USER` y/o `SPEEDFAST_DB_URL`.
5. Ejecutar `Main`.

No publiques la contraseña ni la incluyas en el código o en GitHub.

## Uso

- **Repartidores:** registrar, listar, editar y eliminar. Selecciona una fila para editar o eliminar.
- **Pedidos:** registrar dirección, tipo y estado; listar, editar y eliminar. Solo se ofrecen los tres tipos y los tres estados del esquema de esta sumativa.
- **Entregas:** seleccionar un pedido y repartidor ya registrados, ingresar fecha `AAAA-MM-DD` y hora `HH:mm`, y registrar, listar, editar o eliminar.
- Cada operación actualiza las tablas y los desplegables con datos consultados desde MySQL. Si un pedido o repartidor tiene entregas asociadas, la clave foránea impide eliminarlo hasta quitar primero esas entregas.

## Organización

- `speedfast.modelo`: objetos del dominio y registros persistentes.
- `speedfast.dao`: conexión JDBC y CRUD de cada entidad.
- `speedfast.vista`: ventanas, formularios, tablas y mensajes.
- `speedfast.app.Main`: inicio de la aplicación Swing.
- `speedfast.servicio` y `speedfast.contrato`: lógica de las semanas anteriores.

Los formularios validan los datos antes de llamar a los DAO. Los DAO usan `PreparedStatement`, recorren consultas con `ResultSet` y cierran conexiones mediante `try-with-resources`.

## Comprobación rápida

1. Registrar un repartidor y un pedido.
2. Crear una entrega que relacione ambos.
3. Editar cada registro y comprobar el cambio en la tabla.
4. Cerrar la aplicación, volver a abrirla y comprobar que los datos siguen presentes.
5. Eliminar primero la entrega y luego el pedido y el repartidor.
