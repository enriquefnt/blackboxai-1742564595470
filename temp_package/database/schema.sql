-- Tabla de usuarios
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    establecimiento VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de casos
CREATE TABLE cases (
    case_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    sexo INTEGER NOT NULL, -- 1 = Masculino, 2 = Femenino
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    nombre_tutor VARCHAR(100),
    telefono_tutor VARCHAR(20),
    observaciones TEXT,
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de controles
CREATE TABLE controls (
    control_id SERIAL PRIMARY KEY,
    case_id INTEGER REFERENCES cases(case_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id),
    fecha_control DATE NOT NULL,
    peso DECIMAL(5,2) NOT NULL, -- en kilogramos
    talla DECIMAL(5,2) NOT NULL, -- en centímetros
    perimetro_cefalico DECIMAL(5,2), -- en centímetros
    z_score_peso DECIMAL(4,2),
    z_score_talla DECIMAL(4,2),
    z_score_imc DECIMAL(4,2),
    z_score_pc DECIMAL(4,2),
    observaciones TEXT,
    diagnostico VARCHAR(100),
    tratamiento TEXT,
    proximo_cita_recomendada DATE,
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_cases_user_id ON cases(user_id);
CREATE INDEX idx_cases_documento ON cases(numero_documento);
CREATE INDEX idx_controls_case_id ON controls(case_id);
CREATE INDEX idx_controls_fecha ON controls(fecha_control);

-- Trigger para actualizar fecha_modificacion
CREATE OR REPLACE FUNCTION update_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_modificacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_fecha_modificacion
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_fecha_modificacion();

CREATE TRIGGER update_cases_fecha_modificacion
    BEFORE UPDATE ON cases
    FOR EACH ROW
    EXECUTE FUNCTION update_fecha_modificacion();

CREATE TRIGGER update_controls_fecha_modificacion
    BEFORE UPDATE ON controls
    FOR EACH ROW
    EXECUTE FUNCTION update_fecha_modificacion();

-- Usuario administrador por defecto
INSERT INTO users (
    username,
    nombre,
    apellido,
    email,
    password_hash,
    rol,
    establecimiento
) VALUES (
    'admin',
    'Administrador',
    'Sistema',
    'admin@example.com',
    '$2a$10$kIqR/PTloYan/MRNiEsy6uYO6OCHVmAKR4kflVKQkJ345nqTiuGeO', -- contraseña: admin123
    'ADMIN',
    'Hospital Central'
);