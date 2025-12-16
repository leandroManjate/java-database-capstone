# MySQL Database Schema Design

## doctors
| Field | Type | Description |
|------|------|------------|
| id | BIGINT | Primary key |
| name | VARCHAR(100) | Doctor name |
| email | VARCHAR(100) | Doctor email |
| specialty | VARCHAR(100) | Medical specialty |
| password | VARCHAR(255) | Login password |

## patients
| Field | Type | Description |
|------|------|------------|
| id | BIGINT | Primary key |
| name | VARCHAR(100) | Patient name |
| email | VARCHAR(100) | Patient email |
| phone | VARCHAR(20) | Phone number |
| password | VARCHAR(255) | Login password |

## appointments
| Field | Type | Description |
|------|------|------------|
| id | BIGINT | Primary key |
| appointment_time | DATETIME | Appointment date and time |
| doctor_id | BIGINT | Foreign key to doctors |
| patient_id | BIGINT | Foreign key to patients |

## prescriptions
| Field | Type | Description |
|------|------|------------|
| id | BIGINT | Primary key |
| medication | VARCHAR(255) | Medication name |
| instructions | TEXT | Usage instructions |
| appointment_id | BIGINT | Foreign key to appointments |

## Relationships
- appointments.doctor_id → doctors.id
- appointments.patient_id → patients.id
- prescriptions.appointment_id → appointments.id
