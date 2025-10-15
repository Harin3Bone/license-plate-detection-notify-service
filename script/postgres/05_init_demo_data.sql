-- Init Users data
INSERT INTO public.users (user_id, username, password, first_name, last_name, email, phone_number, role, created_timestamp, created_by, last_updated_timestamp, last_updated_by) VALUES
('53e8b47a-0026-4b87-a873-527be7ed36d8', 'admin', '16972d79c7de5722d0e5b2c57806ef031a3a8425f85d09629c8849b9fc8c9efc', 'John', 'Doe', 'john.doe@gmail.com', '0801112234', 'Admin', '2025-10-15 14:24:28.582762 +00:00', '959b275b-350a-4c3a-b8d2-a317eb1f1611', '2025-10-15 14:24:28.582762 +00:00', '959b275b-350a-4c3a-b8d2-a317eb1f1611'),
('959b275b-350a-4c3a-b8d2-a317eb1f1611', 'SYSTEM', '630f2d694962acc3f1777ddeb89e5eb45650e407cb3fcb47f2c5faa17ad0fa3d', 'SYSTEM', 'SYSTEM', NULL, NULL, 'Admin', '2025-10-15 14:26:09.957525 +00:00', '959b275b-350a-4c3a-b8d2-a317eb1f1611', '2025-10-15 14:26:09.957525 +00:00', '959b275b-350a-4c3a-b8d2-a317eb1f1611')
;

-- Init Camera data
INSERT INTO public.cameras (camera_id, camera_name, province, district, sub_district, address, created_timestamp, created_by, last_updated_timestamp, last_updated_by) VALUES
('2ec15a48-c819-494c-a807-5c0f41ebaf36', 'สี่แยกอโศก', 'กรุงเทพมหานคร', 'วัฒนา', 'คลองเตยเหนือ', 'สี่แยกอโศกมนตรี ถนนสุขุมวิท สถานีรถไฟฟ้า BTS', '2025-10-15 15:04:48.926299 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8', '2025-10-15 15:04:48.926299 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8'),
('0e76998d-0590-4679-924a-049f92ab0b81', 'วงเวียนบางเขน', 'กรุงเทพมหานคร', 'อนุสาวรีย์', 'บางเขน', 'ไฟแดงอุโมงค์บางเขน โลตัสหลักสี่ สี่แยกวงเวียนบางเขน', '2025-10-15 15:04:48.926299 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8', '2025-10-15 15:04:48.926299 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8'),
('ad7de137-9287-402a-8b70-53684d96c88f', 'คลอง 1 ฟิวเจอร์รังสิต', 'ปทุมธานี', 'ธัญบุรี', 'ประชาธิปัตย์', 'ทางออกฟิวเจอร์พาร์ครังสิต คลอง 1', '2025-10-15 15:09:15.921163 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8', '2025-10-15 15:09:15.921163 +00:00', '53e8b47a-0026-4b87-a873-527be7ed36d8')
;