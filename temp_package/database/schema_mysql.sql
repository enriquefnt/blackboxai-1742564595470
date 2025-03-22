-- Tabla de usuarios
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    establecimiento VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de casos
CREATE TABLE cases (
    case_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    tipo_documento VARCHAR(20) NOT NULL,
    sexo INT NOT NULL, -- 1 = Masculino, 2 = Femenino
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    nombre_tutor VARCHAR(100),
    telefono_tutor VARCHAR(20),
    observaciones TEXT,
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de controles
CREATE TABLE controls (
    control_id INT AUTO_INCREMENT PRIMARY KEY,
    case_id INT,
    user_id INT,
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
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (case_id) REFERENCES cases(case_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices
CREATE INDEX idx_cases_user_id ON cases(user_id);
CREATE INDEX idx_cases_documento ON cases(numero_documento);
CREATE INDEX idx_controls_case_id ON controls(case_id);
CREATE INDEX idx_controls_fecha ON controls(fecha_control);

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