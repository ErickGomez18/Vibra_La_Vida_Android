# Vibra la vida — estructura reorganizada

La reorganización conserva `MainActivity.kt` en el paquete raíz y separa las funcionalidades por módulo.

```text
com.example.vibralavida/
├── MainActivity.kt
├── pantallas_principales/
│   ├── HomeScreen.kt
│   ├── InitialProfileScreen.kt
│   ├── LoginScreen.kt
│   ├── ProfileScreen.kt
│   └── RegisterScreen.kt
├── habitos_saludables/
│   ├── CalculatorScreens.kt
│   ├── DassSurveyScreens.kt
│   ├── HealthyHabitsScreen.kt
│   ├── MoodSurveyMenuScreen.kt
│   ├── SleepModeScreen.kt
│   ├── SleepMonitoringService.kt
│   ├── SleepSurveyScreen.kt
│   └── SnoreAnalyzer.kt
├── trastornos_ritmo/
│   ├── HealthConnectManager.kt
│   ├── HealthConnectScreen.kt
│   └── PermissionsRationaleActivity.kt
├── agenda/
│   ├── MiAgendaScreen.kt
│   ├── medicamentos/
│   │   ├── AgregarMedicamentoScreen.kt
│   │   ├── Medicamento.kt
│   │   ├── MedicamentosScreen.kt
│   │   ├── NotificacionMedicamento.kt
│   │   ├── ProgramadorRecordatoriosMedicamento.kt
│   │   └── RecordatorioMedicamentoReceiver.kt
│   └── bitacora/
│       ├── AgregarBitacoraScreen.kt
│       ├── BitacoraSaludScreen.kt
│       ├── EstudioLaboratorio.kt
│       └── RegistroSalud.kt
└── ui/theme/
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## Cambios realizados

- Se movieron los archivos a carpetas funcionales.
- Se actualizaron las declaraciones `package` de todos los archivos movidos.
- Se actualizaron imports en `MainActivity.kt`.
- Se agregaron imports para `backgroundGradient()` donde fue necesario.
- Se agregaron imports de `R` en los subpaquetes que usan recursos de la aplicación.
- Se actualizó `AndroidManifest.xml` para las clases Android que cambiaron de paquete:
  - `PermissionsRationaleActivity`
  - `SleepMonitoringService`
  - `RecordatorioMedicamentoReceiver`

## Nota de compilación

No fue posible ejecutar la compilación dentro del entorno de preparación porque el Gradle Wrapper intenta descargar Gradle 9.3.1 desde Internet. Al abrir el proyecto en Android Studio con conexión, ejecutar **Sync Project with Gradle Files** y después **Build > Make Project**.
