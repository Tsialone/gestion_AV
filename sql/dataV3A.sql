\c gestion_db;

-- ========================================
-- PERMISSIONS DATA
-- Theme: Gestion Appareils Electroniques
-- Run after: table_27_janv.sql + views.sql
-- ========================================

-- 1. DEPARTMENTS
INSERT INTO dept (id_dept, nom) VALUES 
    (1, 'Ventes'),      -- can create DA, proforma, commande, livraison
    (2, 'Finance'),     -- can access valorisation stock
    (3, 'Logistique'),  -- stock management
    (4, 'Direction');   -- management

-- 2. ROLES (niveau >= 7 can validate, seuil = max validation amount)
INSERT INTO role (id_role, nom, niveau, seuil) VALUES 
    (1, 'Stagiaire', 1, 0.00),           -- no validation
    (2, 'Magasinier', 3, 50000.00),      -- basic
    (3, 'Vendeur', 3, 100000.00),        -- basic
    (4, 'Vendeur Senior', 5, 250000.00), -- intermediate
    (5, 'Manager', 7, 500000.00),        -- CAN VALIDATE
    (6, 'Chef Dept', 12, 1000000.00),    -- high authority
    (7, 'Directeur', 15, 5000000.00);    -- highest, bypasses restrictions

-- 3. CATEGORIES (Electronics)
INSERT INTO categorie (id_categorie, libelle) VALUES 
    (1, 'Informatique'),    -- computers, laptops
    (2, 'Telephonie'),      -- phones, tablets
    (3, 'Audio-Video'),     -- TVs, speakers
    (4, 'Electromenager'),  -- home appliances
    (5, 'Gaming'),          -- consoles
    (6, 'Accessoires');     -- cables, cases

-- 4. ARTICLES
INSERT INTO article (id_article, libelle, id_categorie) VALUES 
    -- PC
    (1, 'Laptop HP EliteBook', 1), 
    (2, 'Laptop Dell XPS 15', 1), 
    (3, 'Ecran Samsung 27"', 1), 
    (4, 'Clavier Logitech MX', 1), 
    (5, 'Souris Gaming Razer', 1),
    -- Phones
    (6, 'iPhone 15 Pro', 2), 
    (7, 'Samsung Galaxy S24', 2), 
    (8, 'Tablette iPad Air', 2), 
    (9, 'Huawei P60', 2),
    -- Audio-Video
    (10, 'TV LG OLED 55"', 3), 
    (11, 'Casque Sony WH-1000XM5', 3), 
    (12, 'Barre de son Bose', 3), 
    (13, 'Enceinte JBL Flip', 3),
    -- Electromenager
    (14, 'Refrigerateur Samsung', 4), 
    (15, 'Machine a laver LG', 4), 
    (16, 'Climatiseur Daikin', 4),
    -- Gaming
    (17, 'PlayStation 5', 5), 
    (18, 'Xbox Series X', 5), 
    (19, 'Nintendo Switch', 5), 
    (20, 'Manette PS5 DualSense', 5),
    -- Accessoires
    (21, 'Cable HDMI 2m', 6), 
    (22, 'Chargeur USB-C', 6), 
    (23, 'Coque iPhone', 6), 
    (24, 'Support laptop', 6);

-- 5. DEPOTS
INSERT INTO depot (id_depot, nom) VALUES 
    (1, 'Depot Central Tana'), (2, 'Depot Ankorondrano'), (3, 'Depot Analakely');

-- 6. CAISSES
INSERT INTO caisse (id_caisse, lieu) VALUES 
    (1, 'Caisse Principale'), (2, 'Caisse Ankorondrano'), (3, 'Caisse Analakely');

-- 7. ETATS
INSERT INTO etat (id_etat, libelle) VALUES 
    (1, 'Cree'), (2, 'Valide'), (3, 'Commande'), (4, 'Livre'), (5, 'Annule');

-- 8. FOURNISSEURS
INSERT INTO fournisseur (id_fournisseur, nom) VALUES 
    (1, 'TechImport SARL'), (2, 'ElectroDistrib SA'), (3, 'GameZone Distribution'), (4, 'PhonePro Madagascar');

-- 9. CLIENTS
INSERT INTO client (id_client, nom) VALUES 
    (1, 'Entreprise Digitale SA'), (2, 'Tech Solutions SARL'), (3, 'Gaming House'), (4, 'Bureau Plus'), (5, 'Telecom Services');

-- 10. UTILISATEURS (format: role_dept_name)
INSERT INTO utilisateur (id_utilisateur, nom, date_naissance, date_embauche, id_depot, id_role, id_dept) VALUES 
    (1, 'stg_vt_rabe', '2000-05-15', '2025-01-01', 1, 1, 1),     -- Stagiaire Ventes, niveau 1, cannot validate
    (2, 'vdr_vt_rakoto', '1995-03-20', '2023-06-01', 1, 3, 1),   -- Vendeur Ventes, niveau 3, no validation
    (3, 'vds_vt_rasoa', '1990-08-10', '2021-04-15', 2, 4, 1),    -- Vendeur Senior Ventes, niveau 5, no validation
    (4, 'mgr_vt_andry', '1985-12-25', '2018-02-01', 1, 5, 1),    -- Manager Ventes, niveau 7, CAN VALIDATE
    (5, 'cdp_vt_hery', '1980-01-30', '2015-01-10', NULL, 6, 1),  -- Chef Dept Ventes, niveau 12, full rights
    (6, 'mag_fi_jean', '1992-07-14', '2022-03-01', 1, 2, 2),     -- Magasinier Finance, can access valorisation
    (7, 'mgr_fi_marie', '1988-11-20', '2019-08-15', 2, 5, 2),    -- Manager Finance, validate + valorisation
    (8, 'cdp_fi_paul', '1975-04-05', '2010-05-20', NULL, 6, 2),  -- Chef Finance, full finance rights
    (9, 'mag_lg_fidy', '1993-09-12', '2022-01-15', 1, 2, 3),     -- Magasinier Logistique, stock only
    (10, 'mgr_lg_tiana', '1986-06-08', '2017-11-01', 2, 5, 3),   -- Manager Logistique, stock management
    (11, 'dir_all_boss', '1970-02-28', '2005-01-01', NULL, 7, 4),-- Directeur, niveau 15, CAN DO EVERYTHING
    (12, 'vdr_vt_info', '1994-04-18', '2023-08-01', 1, 3, 1),    -- Vendeur restricted to Informatique
    (13, 'vdr_vt_phone', '1996-10-22', '2024-01-15', 2, 3, 1),   -- Vendeur restricted to Telephonie + PhonePro
    (14, 'mgr_vt_game', '1991-12-01', '2020-05-10', 1, 5, 1);    -- Manager restricted to Gaming/Accessoires + GameZone

-- 11. RESTRICTION CATEGORIES (if user is here, can ONLY access these categories)
INSERT INTO restriction_categorie (id_categorie, id_utilisateur) VALUES 
    (1, 12),  -- vdr_vt_info -> Informatique only
    (2, 13),  -- vdr_vt_phone -> Telephonie only
    (5, 14),  -- mgr_vt_game -> Gaming
    (6, 14);  -- mgr_vt_game -> Accessoires

-- 12. RESTRICTION FOURNISSEURS (if user is here, can ONLY work with these suppliers)
INSERT INTO restriction_fournisseur (id_fournisseur, id_utilisateur) VALUES 
    (4, 13),  -- vdr_vt_phone -> PhonePro only
    (3, 14);  -- mgr_vt_game -> GameZone only

