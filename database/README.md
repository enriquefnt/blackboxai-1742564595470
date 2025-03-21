# Estructura de la Base de Datos

Este directorio contiene los scripts SQL necesarios para configurar la base de datos del servidor.

## Tablas Principales

### Users (Usuarios)
- `user_id`: ID único del usuario (PK)
- `username`: Nombre de usuario único
- `nombre`: Nombre real
- `apellido`: Apellido
- `email`: Correo electrónico único
- `password_hash`: Hash de la contraseña (bcrypt)
- `rol`: Rol del usuario (ADMIN, MEDICO, ENFERMERO)
- `establecimiento`: Lugar de trabajo
- `activo`: Estado del usuario
- `fecha_creacion`: Timestamp de creación
- `fecha_modificacion`: Timestamp de última modificación

### Cases (Casos)
- `case_id`: ID único del caso (PK)
- `user_id`: ID del usuario que creó el caso (FK)
- `nombre`: Nombre del paciente
- `apellido`: Apellido del paciente
- `numero_documento`: Número de documento
- `tipo_documento`: Tipo de documento
- `sexo`: Género (1=Masculino, 2=Femenino)
- `fecha_nacimiento`: Fecha de nacimiento
- Campos adicionales para contacto y observaciones

### Controls (Controles)
- `control_id`: ID único del control (PK)
- `case_id`: ID del caso asociado (FK)
- `user_id`: ID del usuario que realizó el control (FK)
- `fecha_control`: Fecha del control
- Medidas antropométricas:
  - `peso`: en kilogramos
  - `talla`: en centímetros
  - `perimetro_cefalico`: en centímetros
- Z-scores calculados
- Campos para diagnóstico y seguimiento

## Implementación

1. Asegúrate de tener PostgreSQL instalado
2. Crea una base de datos:
   ```sql
   CREATE DATABASE caseapp;
   ```
3. Ejecuta el script schema.sql:
   ```bash
   psql -d caseapp -f schema.sql
   ```

## Credenciales por Defecto

Usuario administrador inicial:
- Username: admin
- Password: admin123
- Email: admin@example.com

## Notas Importantes

- Las contraseñas se almacenan usando bcrypt
- Todas las tablas tienen campos de auditoría (fecha_creacion, fecha_modificacion)
- Los triggers actualizan automáticamente fecha_modificacion
- Las eliminaciones son lógicas (campo activo)
- Las relaciones cases->controls usan DELETE CASCADE