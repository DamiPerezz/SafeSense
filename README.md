# SafeSense: Sistema de Seguridad dirigido a Plantas de Oficinas

![safesense-removebg-preview(1)](https://github.com/user-attachments/assets/76de8ea0-a8cb-4d94-8242-ed34f9a3d0f2)

## Resumen del Proyecto

**SafeSense** es un sistema diseñado para detectar presencias no autorizadas en edificios, oficinas y otros espacios de alta seguridad. Utiliza sensores y software especializado para identificar posibles intrusiones, alertar a los responsables y permitir una gestión efectiva de accesos y vigilancia.

El sistema combina hardware como sensores RFID, cámaras y detectores de movimiento, con un backend que integra bases de datos y notificaciones en tiempo real. Además, incluye funcionalidades para gestionar roles de usuarios y mantener un registro detallado de eventos.

---

## Funcionalidades Principales

- **Control de Accesos:** Tarjetas RFID únicas para cada empleado, con validación según zonas asignadas.
- **Alertas en Tiempo Real:** Notificaciones inmediatas ante movimientos fuera de horario o condiciones ambientales críticas.
- **Monitoreo Centralizado:** Cámaras en vivo y registro de eventos accesibles para los responsables de seguridad.
- **Gestión de Roles:** Funciones específicas para empleados, personal de seguridad y encargados de vigilancia.
- **Chat Interno:** Comunicación directa entre los responsables a través de un sistema de mensajería integrado.
- **Estadísticas y Reportes:** Análisis de zonas con mayor actividad o incidencias para mejorar la seguridad.

---

## Cómo Funciona SafeSense

1. **Detección:** Los sensores registran actividad, ya sea movimiento, temperatura, humedad o intentos de acceso.
2. **Validación:** El sistema verifica la información en la base de datos. Si el acceso está autorizado, se activa el mecanismo correspondiente (ej. abrir una puerta). Si no, se genera una alerta.
3. **Notificación:** Las incidencias son reportadas en tiempo real a los responsables, con opciones para responder rápidamente.
4. **Gestión Centralizada:** Todo el sistema puede ser supervisado desde una única interfaz accesible según el rol del usuario.

---

## Estructura del Proyecto

### Hardware
- **ESP32 WROOM:** Controlador principal para sensores y conexión con la base de datos.
- **ESP32CAM:** Cámaras para transmisión en vivo.
- **MFRC522:** Lector RFID para el control de accesos.
- **HC-SR04:** Sensor ultrasónico para detección de movimiento.
- **DHT11:** Sensor de temperatura y humedad para alertas ambientales.

### Software
- **Backend:** Desarrollado en Java, maneja la lógica del sistema y la conexión con la base de datos.
- **Base de Datos:** SQL Implementada con MariaDB, diseñada para gestionar usuarios, zonas, sensores y eventos.
- **Frontend:** Interfaz diseñada en Figma para facilitar su uso según los roles del sistema.

### Organización
- Metodología **SCRUM**, con herramientas como **Trello** y diagramas de Gantt para organizar y dar seguimiento al desarrollo.

---

## Cómo Usar SafeSense

1. **Clona este repositorio:** 
   ```bash
   git clone https://github.com/tu_usuario/safesense.git
   cd safesense
   ```

2. **Configura la Base de Datos:** 
   - Instala MariaDB.
   - Importa el archivo `safesense_db.sql` para crear las tablas necesarias.

3. **Ejecuta el Backend:** 
   - Asegúrate de tener Java JDK instalado.
   - Inicia el servidor con:
     ```bash
     java -jar SafeSenseBackend.jar
     ```

4. **Configura y Conecta el Hardware:** 
   - Sigue las instrucciones del manual de hardware en `docs/hardware_manual.md`.

5. **Inicia el Cliente:** 
   - Configura el archivo `client_config.json` y ejecuta el archivo `SafeSenseClient.jar`.

---

## Equipo de Trabajo

Este proyecto fue desarrollado por estudiantes del grado en Ingeniería Informática de la Escuela de Arquitectura, Ingeniería y Diseño (curso 2023-2024):

- **Damián Pérez Moreno:** Líder del proyecto y responsable de Hardware.
- **Sahar Aman Mohammadi:** Especialista en gestión de datos y bases de datos.
- **María Juárez Molera:** Diseñadora de interfaces y frontend.
- **Iker Álamo Gómez de Segura:** Responsable de backend.

El equipo cuenta con experiencia previa en proyectos como **Click & Eat** y **EventRut**, aplicando tecnologías similares y destacándose por su capacidad de organización, uso eficiente de herramientas como GitHub y Trello, y documentación clara.

---

## Contacto

Si tienes alguna duda o deseas colaborar, no dudes en escribirnos a: `safesense.contacto@ejemplo.com`.
