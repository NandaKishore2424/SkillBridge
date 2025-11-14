-- Add new profile fields for admins
ALTER TABLE public.admins
    ADD COLUMN IF NOT EXISTS phone_number varchar(50);

ALTER TABLE public.admins
    ADD COLUMN IF NOT EXISTS role_title varchar(255);

-- Backfill existing admin data with defaults if needed
UPDATE public.admins
SET phone_number = COALESCE(phone_number, '+1-202-555-0199'),
    role_title = COALESCE(role_title, 'Program Director');

