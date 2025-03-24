#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Creando iconos para CaseApp${NC}"
echo "----------------------------------------"

# Crear directorios para los iconos
echo -e "\n${YELLOW}1. Creando directorios...${NC}"
mkdir -p app/src/main/res/mipmap-hdpi
mkdir -p app/src/main/res/mipmap-mdpi
mkdir -p app/src/main/res/mipmap-xhdpi
mkdir -p app/src/main/res/mipmap-xxhdpi
mkdir -p app/src/main/res/mipmap-xxxhdpi
mkdir -p app/src/main/res/drawable

# Modificar AndroidManifest.xml para usar drawable en lugar de mipmap
echo -e "\n${YELLOW}2. Actualizando AndroidManifest.xml...${NC}"
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
        android:icon="@drawable/app_icon"
        android:label="@string/app_name"
        android:roundIcon="@drawable/app_icon"
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

    </application>
</manifest>
EOF

# Crear icono simple en drawable
echo -e "\n${YELLOW}3. Creando icono simple...${NC}"
cat > app/src/main/res/drawable/app_icon.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108"
    android:tint="#000000">
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M54,28C40.19,28 29,39.19 29,53C29,66.81 40.19,78 54,78C67.81,78 79,66.81 79,53C79,39.19 67.81,28 54,28ZM54,72C43.52,72 35,63.48 35,53C35,42.52 43.52,34 54,34C64.48,34 73,42.52 73,53C73,63.48 64.48,72 54,72ZM54,38C45.73,38 39,44.73 39,53C39,61.27 45.73,68 54,68C62.27,68 69,61.27 69,53C69,44.73 62.27,38 54,38Z"/>
</vector>
EOF

# Crear colores
echo -e "\n${YELLOW}4. Creando colores...${NC}"
cat > app/src/main/res/values/colors.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="ic_launcher_background">#FFFFFF</color>
</resources>
EOF

echo -e "\n${GREEN}¡Iconos creados exitosamente!${NC}"
echo "----------------------------------------"
echo "Ahora ejecuta:"
echo "./generar_apk_final.sh"