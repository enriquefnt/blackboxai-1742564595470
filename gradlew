# 8. Configurar Gradle
echo -e "\n${YELLOW}8. Configurando Gradle...${NC}"
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
if [ -f gradlew ]; then
    chmod +x gradlew
else
    echo -e "\n${RED}Error: El archivo gradlew no se encontró.${NC}"
    exit 1
fi