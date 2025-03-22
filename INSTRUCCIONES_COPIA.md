# Instrucciones para Copiar los Archivos

## Archivos Principales a Copiar

1. **Archivos de Configuración:**
```bash
# En tu repositorio local
mkdir -p app/release gradle/wrapper database
```

2. **Contenido de los archivos:**

### install.sh
```bash
#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Instalador de CaseApp${NC}"
echo "----------------------------------------"

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java no está instalado${NC}"
    echo "Por favor, instala Java 17 o superior:"
    echo "sudo apt install openjdk-17-jdk  # Para Ubuntu/Debian"
    echo "sudo dnf install java-17-openjdk  # Para Fedora"
    exit 1
fi

# Verificar que estamos en el directorio correcto
if [ ! -f "generate-apk.sh" ]; then
    echo -e "${RED}Error: Debes ejecutar este script desde el directorio raíz del proyecto${NC}"
    echo "Ejecuta:"
    echo "git clone https://github.com/enriquefnt/blackboxai-1742564595470.git"
    echo "cd blackboxai-1742564595470"
    echo "./install.sh"
    exit 1
fi

# Dar permisos de ejecución
chmod +x generate-apk.sh
chmod +x gradlew

# Crear directorio para el APK
mkdir -p app/release

# Generar keystore
echo -e "${YELLOW}Generando keystore...${NC}"
keytool -genkey -v \
        -keystore app/release/keystore.jks \
        -alias caseapp \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass caseapp123 \
        -keypass caseapp123 \
        -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"

# Configurar keystore
echo -e "${YELLOW}Configurando keystore...${NC}"
cat > app/release/keystore.properties << EOF
storePassword=caseapp123
keyPassword=caseapp123
keyAlias=caseapp
storeFile=keystore.jks
EOF

# Descargar dependencias
echo -e "${YELLOW}Descargando dependencias...${NC}"
./gradlew --refresh-dependencies

# Compilar APK
echo -e "${YELLOW}Compilando APK...${NC}"
./gradlew clean assembleRelease

# Verificar si se generó el APK
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    # Copiar APK al directorio de release
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "${GREEN}¡APK generado exitosamente!${NC}"
    echo "Ubicación: app/release/CaseApp-v1.1.0.apk"
    
    # Instrucciones de instalación
    echo
    echo "Para instalar en tu dispositivo Android:"
    echo "1. Copia el archivo app/release/CaseApp-v1.1.0.apk a tu dispositivo"
    echo "2. En tu dispositivo Android:"
    echo "   - Ve a Ajustes > Seguridad"
    echo "   - Activa 'Orígenes desconocidos'"
    echo "3. Instala el APK"
    echo
    echo "Credenciales iniciales:"
    echo "Usuario: admin"
    echo "Contraseña: admin123"
else
    echo -e "${RED}Error: No se pudo generar el APK${NC}"
    echo "Por favor, revisa los errores anteriores"
    exit 1
fi
```

### app/build.gradle
```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs.kotlin'
}

def keystorePropertiesFile = rootProject.file("app/keystore.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

android {
    namespace 'com.example.caseapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.caseapp"
        minSdk 24
        targetSdk 34
        versionCode 2
        versionName "1.1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += ["room.schemaLocation": "$projectDir/schemas".toString()]
            }
        }
    }

    signingConfigs {
        release {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
            
            buildConfigField "String", "API_BASE_URL", "\"https://tu-servidor-produccion.com/api/\""
        }
        
        debug {
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
            debuggable true
            
            buildConfigField "String", "API_BASE_URL", "\"http://localhost:8000/api/\""
        }
    }

    buildFeatures {
        viewBinding true
        buildConfig true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
        freeCompilerArgs += [
            "-opt-in=kotlin.RequiresOptIn",
            "-Xjvm-default=all",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        ]
    }

    packagingOptions {
        resources {
            excludes += [
                'META-INF/DEPENDENCIES',
                'META-INF/LICENSE',
                'META-INF/LICENSE.txt',
                'META-INF/license.txt',
                'META-INF/NOTICE',
                'META-INF/NOTICE.txt',
                'META-INF/notice.txt',
                'META-INF/ASL2.0',
                'META-INF/*.kotlin_module'
            ]
        }
    }

    testOptions {
        unitTests.all {
            useJUnitPlatform()
        }
    }
}

dependencies {
    def lifecycle_version = "2.6.2"
    def room_version = "2.6.1"
    def nav_version = "2.7.5"
    def retrofit_version = "2.9.0"
    def okhttp_version = "4.12.0"
    def work_version = "2.9.0"
    def coroutines_version = "1.7.3"

    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    implementation "androidx.work:work-runtime-ktx:$work_version"

    implementation "androidx.security:security-crypto:1.1.0-alpha06"

    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutines_version"

    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.7.0'
    testImplementation 'org.mockito.kotlin:mockito-kotlin:5.1.0'
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version"
    testImplementation "androidx.arch.core:core-testing:2.2.0"
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

### database/schema_mysql.sql
```sql
-- Ver contenido en el archivo original
```

## Pasos para Copiar

1. Crea los directorios necesarios en tu repositorio local:
```bash
mkdir -p app/release gradle/wrapper database
```

2. Copia cada archivo en su ubicación correspondiente:
   - Copia el contenido de install.sh y guárdalo como `install.sh`
   - Copia el contenido de app/build.gradle y guárdalo como `app/build.gradle`
   - etc.

3. Haz commit de los cambios:
```bash
git add .
git commit -m "Actualización completa con scripts de instalación"
git push origin main
```

4. Ejecuta el script de instalación:
```bash
chmod +x install.sh
./install.sh
```

¿Necesitas ver el contenido de algún otro archivo específico?