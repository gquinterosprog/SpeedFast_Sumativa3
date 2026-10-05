# SpeedFast — Sumativa 3 de POO II

Aplicación de escritorio para gestionar repartidores, pedidos y entregas con Java Swing, JDBC y MySQL. Conserva las clases de polimorfismo y concurrencia del proyecto anterior. La aplicación se inicia desde `speedfast.app.Main`.

## Requisitos

- JDK 17 o superior.
- IntelliJ IDEA con soporte Maven.
- MySQL Server local.

## Preparar la base de datos

1. Iniciar MySQL Server.
2. Ejecutar la aplicación. Con la clave correcta, esta crea `speedfast_db` y las tablas al iniciar por primera vez.
3. Si prefieres preparar la base manualmente, ejecuta `database/speedfast_db.sql` en MySQL Workbench o en la consola. El script contiene el mismo esquema con sus claves foráneas.

El ID de cada registro lo genera MySQL. No hay datos de ejemplo obligatorios; pueden agregarse desde la interfaz.

## Abrir y ejecutar en IntelliJ

1. Abrir esta carpeta como proyecto en IntelliJ IDEA.
2. Esperar a que Maven cargue `mysql-connector-j`.
3. Ejecutar `speedfast.app.Main`. Al abrir, la aplicación solicitará la contraseña local de MySQL en un cuadro de diálogo. No la guarda en el proyecto.
4. El programa usa `root` y `jdbc:mysql://localhost:3306/speedfast_db` por defecto. Si tu configuración es diferente, agrega `SPEEDFAST_DB_USER` y/o `SPEEDFAST_DB_URL` en **Run > Edit Configurations > Environment variables**.

No publiques la contraseña ni la incluyas en el código o en GitHub.

## Uso

- **Repartidores:** registrar, listar, editar y eliminar. Selecciona una fila para editar o eliminar.
- **Pedidos:** registrar dirección, tipo y estado; listar, editar y eliminar. Solo se ofrecen los tres tipos y los tres estados del esquema de esta sumativa.
- **Entregas:** seleccionar un pedido y repartidor ya registrados, ingresar fecha `AAAA-MM-DD` y hora `HH:mm`, y registrar, listar, editar o eliminar. Los filtros permiten mostrar entregas de un pedido, de un repartidor o de ambos.
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
