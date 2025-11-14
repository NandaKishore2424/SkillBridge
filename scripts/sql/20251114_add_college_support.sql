-- Enable UUID generation if not already available
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Core college catalog
CREATE TABLE IF NOT EXISTS public.colleges (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL UNIQUE,
    domain varchar(255) NOT NULL UNIQUE,
    website_url text,
    contact_email varchar(255),
    contact_phone varchar(50),
    address text,
    created_at timestamptz NOT NULL DEFAULT now()
);

-- 2. Link authentication tables to colleges
ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS college_id uuid;

ALTER TABLE public.users
    ADD CONSTRAINT fk_users_college
    FOREIGN KEY (college_id) REFERENCES public.colleges(id);

ALTER TABLE public.admins
    ADD COLUMN IF NOT EXISTS college_id uuid;

ALTER TABLE public.admins
    ADD CONSTRAINT fk_admins_college
    FOREIGN KEY (college_id) REFERENCES public.colleges(id);

ALTER TABLE public.admins
    ADD CONSTRAINT uq_admin_college UNIQUE (college_id);

ALTER TABLE public.students
    ADD COLUMN IF NOT EXISTS college_id uuid;

ALTER TABLE public.students
    ADD CONSTRAINT fk_students_college
    FOREIGN KEY (college_id) REFERENCES public.colleges(id);

ALTER TABLE public.trainers
    ADD COLUMN IF NOT EXISTS college_id uuid;

ALTER TABLE public.trainers
    ADD CONSTRAINT fk_trainers_college
    FOREIGN KEY (college_id) REFERENCES public.colleges(id);

-- 3. Seed a default college and backfill existing users
INSERT INTO public.colleges (name, domain, website_url, contact_email, contact_phone, address)
VALUES (
    'SkillBridge University',
    'skillbridge.edu',
    'https://skillbridge.edu',
    'contact@skillbridge.edu',
    '+1-202-555-0100',
    '123 Learning Ave, Knowledge City'
)
ON CONFLICT (domain) DO UPDATE
SET name = EXCLUDED.name,
    website_url = EXCLUDED.website_url,
    contact_email = EXCLUDED.contact_email,
    contact_phone = EXCLUDED.contact_phone,
    address = EXCLUDED.address;

WITH default_college AS (
    SELECT id FROM public.colleges WHERE domain = 'skillbridge.edu'
)
UPDATE public.users
SET college_id = (SELECT id FROM default_college)
WHERE college_id IS NULL;

WITH default_college AS (
    SELECT id FROM public.colleges WHERE domain = 'skillbridge.edu'
)
UPDATE public.admins
SET college_id = (SELECT id FROM default_college)
WHERE college_id IS NULL;

WITH default_college AS (
    SELECT id FROM public.colleges WHERE domain = 'skillbridge.edu'
)
UPDATE public.students
SET college_id = (SELECT id FROM default_college)
WHERE college_id IS NULL;

WITH default_college AS (
    SELECT id FROM public.colleges WHERE domain = 'skillbridge.edu'
)
UPDATE public.trainers
SET college_id = (SELECT id FROM default_college)
WHERE college_id IS NULL;

