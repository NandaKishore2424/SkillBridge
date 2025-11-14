-- Student profile enhancements
ALTER TABLE public.students
    ADD COLUMN IF NOT EXISTS phone_number varchar(50),
    ADD COLUMN IF NOT EXISTS register_number varchar(100);

UPDATE public.students
SET phone_number = COALESCE(phone_number, '+1-202-555-0000'),
    register_number = COALESCE(register_number, 'AUTO-STU-' || substr(id::text, 1, 8));

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uq_students_register_number'
    ) THEN
        ALTER TABLE public.students
            ADD CONSTRAINT uq_students_register_number UNIQUE (register_number);
    END IF;
END $$;

-- Trainer profile enhancements
ALTER TABLE public.trainers
    ADD COLUMN IF NOT EXISTS department varchar(255),
    ADD COLUMN IF NOT EXISTS phone_number varchar(50),
    ADD COLUMN IF NOT EXISTS teacher_id varchar(100);

UPDATE public.trainers
SET department = COALESCE(department, 'General Studies'),
    phone_number = COALESCE(phone_number, '+1-202-555-1000'),
    teacher_id = COALESCE(teacher_id, 'AUTO-TRN-' || substr(id::text, 1, 8));

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uq_trainers_teacher_id'
    ) THEN
        ALTER TABLE public.trainers
            ADD CONSTRAINT uq_trainers_teacher_id UNIQUE (teacher_id);
    END IF;
END $$;

