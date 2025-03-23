#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Creando recursos necesarios para CaseApp${NC}"
echo "----------------------------------------"

# Crear directorios necesarios
echo -e "\n${YELLOW}1. Creando directorios...${NC}"
mkdir -p app/src/main/res/mipmap-anydpi-v26
mkdir -p app/src/main/res/drawable
mkdir -p app/src/main/res/values

# Crear AndroidManifest.xml
echo -e "\n${YELLOW}2. Creando AndroidManifest.xml...${NC}"
cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".CaseApplication"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@drawable/ic_launcher_foreground"
        android:label="@string/app_name"
        android:roundIcon="@drawable/ic_launcher_foreground"
        android:supportsRtl="true"
        android:theme="@style/Theme.CaseApp"
        tools:targetApi="31">

        <activity
            android:name=".ui.login.LoginActivity"
            android:exported="true"
            android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ui.main.MainActivity"
            android:exported="false" />

        <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            tools:node="remove" />

    </application>
</manifest>
EOF

# Crear strings.xml
echo -e "\n${YELLOW}3. Creando strings.xml...${NC}"
cat > app/src/main/res/values/strings.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">CaseApp</string>
    <string name="login_prompt">Iniciar Sesión</string>
    <string name="username_hint">Usuario</string>
    <string name="password_hint">Contraseña</string>
    <string name="login_button">Entrar</string>
    <string name="login_failed">Error de inicio de sesión</string>
    <string name="welcome">Bienvenido</string>
    <string name="invalid_username">Usuario no válido</string>
    <string name="invalid_password">Contraseña debe tener >5 caracteres</string>
    <string name="sync_status">Estado de sincronización</string>
    <string name="sync_now">Sincronizar ahora</string>
    <string name="add_case">Agregar caso</string>
    <string name="search">Buscar</string>
    <string name="case_details">Detalles del caso</string>
    <string name="case_info">Información</string>
    <string name="case_controls">Controles</string>
    <string name="edit_case">Editar caso</string>
    <string name="delete_case">Eliminar caso</string>
    <string name="add_control">Agregar control</string>
    <string name="save">Guardar</string>
    <string name="cancel">Cancelar</string>
    <string name="empty_cases">No hay casos registrados</string>
    <string name="empty_controls">No hay controles registrados</string>
</resources>
EOF

# Crear ic_launcher_background.xml
echo -e "\n${YELLOW}4. Creando ic_launcher_background.xml...${NC}"
cat > app/src/main/res/values/ic_launcher_background.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="ic_launcher_background">#FFFFFF</color>
</resources>
EOF

# Crear ic_launcher_foreground.xml
echo -e "\n${YELLOW}5. Creando ic_launcher_foreground.xml...${NC}"
cat > app/src/main/res/drawable/ic_launcher_foreground.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108"
    android:tint="#000000">
    <group android:scaleX="2.61"
        android:scaleY="2.61"
        android:translateX="22.68"
        android:translateY="22.68">
        <path
            android:fillColor="@android:color/white"
            android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,5c1.66,0 3,1.34 3,3s-1.34,3 -3,3 -3,-1.34 -3,-3 1.34,-3 3,-3zM12,19.2c-2.5,0 -4.71,-1.28 -6,-3.22 0.03,-1.99 4,-3.08 6,-3.08 1.99,0 5.97,1.09 6,3.08 -1.29,1.94 -3.5,3.22 -6,3.22z"/>
    </group>
</vector>
EOF

# Crear ic_launcher.xml
echo -e "\n${YELLOW}6. Creando ic_launcher.xml...${NC}"
cat > app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@drawable/ic_launcher_foreground"/>
</adaptive-icon>
EOF

# Crear ic_launcher_round.xml
echo -e "\n${YELLOW}7. Creando ic_launcher_round.xml...${NC}"
cat > app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@drawable/ic_launcher_foreground"/>
</adaptive-icon>
EOF

echo -e "\n${GREEN}¡Recursos creados exitosamente!${NC}"
echo "----------------------------------------"
echo "Ahora puedes ejecutar:"
echo "./generar_apk_mac_v2.sh"