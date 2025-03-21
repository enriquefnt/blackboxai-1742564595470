# CaseApp - Aplicación de Seguimiento Antropométrico

Aplicación Android para el seguimiento antropométrico de pacientes pediátricos, implementando gráficos de crecimiento según los estándares de la OMS.

## Características

- Visualización de detalles del caso con información personal
- Gráficos antropométricos:
  - Peso para Edad
  - Talla para Edad
  - IMC para Edad
- Lista de controles con medidas y z-scores
- Sincronización offline-first con base de datos local
- Interfaz Material Design 3

## Tecnologías Utilizadas

- Kotlin
- MVVM Architecture
- Android Architecture Components
  - ViewModel
  - LiveData
  - Room
  - Navigation
  - WorkManager
- Retrofit para comunicación con API
- MPAndroidChart para gráficos
- Material Design Components

## Estructura del Proyecto

- `data/`: Modelos y acceso a datos (local y remoto)
- `ui/`: Componentes de la interfaz de usuario
- `util/`: Clases utilitarias
- `worker/`: Workers para tareas en segundo plano

## Configuración

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar el proyecto con Gradle
4. Ejecutar la aplicación

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
