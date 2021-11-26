#Acceso a una BDR mediante ORM
Utilizando el esquema de ORACLE que se anexa, crear una aplicación Java que utilizando Hibernate gestione los préstamos que realiza la compañía X almacenados en dicho esquema.
Esta aplicación tendrá una pantalla similar a la siguiente en la que podremos:

1. Añadir, eliminar o actualizar los datos de un prestamo. Cada una de las acciones
anteriores tendrá su repercusión en los recibos asociados a dicho prestamo.
2. Cuando se añada un prestamo, todos los recibos generados tendran la fecha de pago a
nulo.
3. No se podrá eliminar ni actualizar los datos de un prestamo, si este ultimo tiene algun
recibo pagado.
4. Cuando se seleccione un prestamo de la tabla de prestamos, se mostrarán en la tabla
de recibos, sus recibos asociados.
5. Mediante el botón de Pagar se podrá dará por pagado el recibo seleccionado en la
tabla. Esta acción actualizará el importe pagado del prestamo asociado a dicho recibo.
6. Mediante el botón de Generar JSON se podrá generar un fichero JSON con los recibos
de la tabla de recibos ( siempre y cuando exista al menos 1 recibo ).
