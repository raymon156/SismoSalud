# SISMO-SALUD PE

Sistema en Java para apoyar la gestión de pacientes y recursos médicos del Hospital Nacional Edgardo Rebagliati Martins ante una emergencia sísmica.

Proyecto del curso **Lenguajes de Programación** – Universidad Privada del Norte (UPN).

> El sistema es una herramienta de apoyo administrativo y organizativo. No reemplaza las decisiones médicas.
> Todos los datos del programa son **ficticios**.

## Funcionalidades

- **Registro de pacientes** con nombre, DNI, edad y prioridad de triaje (ROJO, AMARILLO, VERDE).
- **Consulta de pacientes** ordenados del más urgente al menos urgente, con filtro por prioridad.
- **Camas y personal médico:** registrar, asignar un paciente y liberar.
- **Inventario de medicamentos:** registrar (con código, nombre, stock, mínimo y fecha de vencimiento), actualizar el stock y mostrar alertas cuando hay pocas existencias.
- **Protección del DNI:** en pantalla solo se muestran los dos últimos dígitos (por ejemplo, `******78`).
- Mensajes claros cuando un registro se completa o hay un error.

## Tecnologías

- Java
- Apache NetBeans (proyecto *Java with Ant → Java Application*)
- Java Swing (JFrame)
- UML con draw.io

## Estructura del proyecto

```
SismoSalud/
├── src/
│   ├── clases/                      Lógica y entidades
│   │   ├── Persona.java             (clase abstracta)
│   │   ├── Paciente.java
│   │   ├── PersonalMedico.java
│   │   ├── Cama.java
│   │   ├── Medicamento.java
│   │   ├── Prioridad.java           (enumeración)
│   │   └── Hospital.java            (guarda las listas y administra el sistema)
│   └── vista/                       Interfaz gráfica
│       ├── Principal.java           (ventana de inicio, contiene main)
│       ├── RegistroPacientes.java
│       ├── DisponibilidadCamasPersonal.java
│       └── InventarioMedicamentos.java
└── docs/
    └── UML_SismoSalud.drawio        Diagrama de clases (editable en draw.io)
```

## Paradigmas aplicados

| Paradigma | Dónde se ve |
|---|---|
| Orientado a objetos | Herencia `Persona → Paciente / PersonalMedico`, atributos privados, clase `Hospital` |
| Funcional | `filter`, `sorted` y `collect` en `Hospital` (por ejemplo `pacientesPorPrioridad()`) |
| Orientado a eventos | `ActionListener` en los botones de las ventanas |
| Imperativo | Validaciones secuenciales en constructores y métodos `registrar()` |

## Cómo ejecutar

1. Clona el repositorio:
   ```
   git clone <URL-DEL-REPOSITORIO>
   ```
2. Abre **Apache NetBeans** y ve a *File → Open Project*, luego selecciona la carpeta del proyecto.
   Si el repositorio solo contiene la carpeta `src`, crea un proyecto nuevo (*Java with Ant → Java Application*, sin crear clase main) y copia los paquetes `clases` y `vista` dentro de *Source Packages*.
3. Verifica que la clase principal sea `vista.Principal` (*Properties → Run → Main Class*).
4. Ejecuta con **F6** (*Run Project*).

## Uso

1. En la ventana **Sismo-Salud PE** elige una opción y presiona **Ingresar**.
2. **Registro de pacientes:** completa los campos y presiona *Registrar*. Usa el filtro para ver una prioridad.
3. **Camas y personal:** agrega una cama o un integrante del personal, selecciónalo en la tabla y usa *Asignar* o *Liberar*.
4. **Medicamentos:** agrega un medicamento (la fecha de vencimiento se escribe como `dd/mm/aaaa`) o selecciónalo y usa *Actualizar stock*. Las filas en rojo indican stock bajo.

Al abrir el programa se cargan datos de ejemplo ficticios (3 pacientes, 3 camas, 2 médicos y 3 medicamentos).

## Requerimientos cubiertos

| Código | Requerimiento | Dónde |
|---|---|---|
| RF01 | Registrar pacientes | `RegistroPacientes`, `Hospital.registrarPaciente()` |
| RF02 | Clasificar y priorizar | `Prioridad`, lista de prioridad al registrar |
| RF03 | Consultar pacientes y prioridad | `Hospital.pacientesPorPrioridad()` |
| RF04 | Registrar y consultar camas | `DisponibilidadCamasPersonal`, `Cama` |
| RF05 | Registrar y actualizar medicamentos | `InventarioMedicamentos`, `Medicamento` |
| RF06 | Alertas de stock bajo | `Medicamento.tieneStockBajo()` |
| RF07 | Interfaz con JFrame | Paquete `vista` |

## Integrantes – Grupo N.° 9

- Reimond Jose Anchirayco Pinado
- Jefferson Aymar Esquen Chunga
- Fernando Lenin Kenny Tineo Vicente
- Marquez Palacios Camila Mabel
- Marquez Palacios Malena Silvana

**Docente:** Williams Alfonso Chullo Villagaray
**Lima, noviembre de 2026**
