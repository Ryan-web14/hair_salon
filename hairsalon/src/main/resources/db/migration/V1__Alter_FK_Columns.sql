ALTER TABLE schedule ALTER COLUMN barber_id DROP NOT NULL; -- V1__Alter_FK_Columns.sql
ALTER TABLE availability ALTER COLUMN barber_id DROP NOT NULL;
ALTER TABLE appointment ALTER COLUMN barber_id DROP NOT NULL;
ALTER TABLE appointment ALTER COLUMN haircut_id DROP NOT NULL;