-- User accounts and authentication
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at DATE DEFAULT (CURDATE()),
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- Degree programs
CREATE TABLE programs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(15) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    total_units INTEGER NOT NULL,
    total_ge_elective_units INTEGER NOT NULL,
    INDEX idx_program_code (code)
);

-- Course catalog
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(15) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    units INTEGER NOT NULL,
    is_ge  BOOLEAN DEFAULT FALSE,
    semester_offered ENUM('FIRST', 'SECOND', 'SUMMER', 'BOTH') NOT NULL DEFAULT 'FIRST',
    INDEX idx_course_code (code),
    INDEX idx_semester_offered (semester_offered)
);

-- Course prerequisite relationships
CREATE TABLE prerequisites (
    course_id BIGINT NOT NULL,
    prerequisite_course_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, prerequisite_course_id),
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisite_course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_prerequisite_course (prerequisite_course_id)
);

-- User program enrollment (supports program shifts)
CREATE TABLE user_programs (
    user_id BIGINT NOT NULL,
    program_id BIGINT NOT NULL,
    is_current BOOLEAN DEFAULT TRUE,
    enrolled_at DATE DEFAULT (CURDATE()),
    PRIMARY KEY (user_id, program_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE CASCADE,
    INDEX idx_current_enrollment (user_id, is_current)
);

-- Program course requirements (core and electives)
CREATE TABLE program_courses (
    program_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    is_required BOOLEAN DEFAULT TRUE, -- TRUE = core, FALSE = elective
    PRIMARY KEY (program_id, course_id),
    FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_required_courses (program_id, is_required)
);

-- User completed courses with grades
CREATE TABLE user_courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    grade DECIMAL(3,2),
    semester_taken ENUM('FIRST', 'SECOND', 'SUMMER') NOT NULL DEFAULT 'FIRST',
    year_taken INTEGER NOT NULL,
    status ENUM('PASSED', 'FAILED', 'DROPPED', 'INCOMPLETE', 'IN_PROGRESS') DEFAULT 'PASSED',
    attempt_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    INDEX idx_user_courses (user_id, course_id),
    INDEX idx_semester_year (user_id, year_taken, semester_taken),
    INDEX idx_course_status (course_id, status)
);

-- Unit limits by program, year level, and semester
CREATE TABLE unit_limits (
    program_id BIGINT NOT NULL,
    year_level INTEGER NOT NULL,
    semester ENUM('FIRST', 'SECOND', 'SUMMER') NOT NULL,
    max_units INTEGER NOT NULL,
    PRIMARY KEY (program_id, year_level, semester),
    FOREIGN KEY (program_id) REFERENCES programs(id) ON DELETE CASCADE,
    INDEX idx_program_year (program_id, year_level)
);