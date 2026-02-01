-- ========================================
-- INSERTION DES DONNÉES DE TEST
-- ========================================

-- 1. Données de base
INSERT INTO categorie (libelle) VALUES 
('Electronique'),
('Informatique'),
('Mobilier'),
('Fournitures');

INSERT INTO article (libelle, id_categorie) VALUES 
('Ordinateur Portable HP', 2),
('Souris Sans Fil', 2),
('Clavier Mécanique', 2),
('Ecran 24 pouces', 1),
('Chaise de Bureau', 3),
('Imprimante Laser', 1),
('Stylos Boîte 50', 4);

INSERT INTO client (nom) VALUES 
('Société ABC SARL'),
('Entreprise XYZ SA'),
('Commerce 123'),
('StartUp Tech');

INSERT INTO fournisseur (nom) VALUES 
('TechSupply SARL'),
('OfficeWorld SA'),
('ElectroDistrib'),
('FournituresPro');

INSERT INTO depot (nom) VALUES 
('Dépôt Central'),
('Dépôt Secondaire'),
('Entrepôt Nord');

INSERT INTO caisse (id_caisse, lieu) VALUES 
('CAISSE_01', 'Siège Social'),
('CAISSE_02', 'Succursale Est');

INSERT INTO etat (libelle) VALUES 
('En attente'),
('Validé'),
('En cours'),
('Livré'),
('Annulé');

INSERT INTO role (nom, niveau, seuil) VALUES 
('Administrateur', 1, 1000000.00),
('Manager', 2, 500000.00),
('Employé', 3, 100000.00);

INSERT INTO dept (nom) VALUES 
('Achats'),
('Logistique'),
('Ventes');

INSERT INTO utilisateur (nom, date_naissance, date_embauche, id_depot, id_role, id_dept) VALUES 
('Jean Dupont', '1985-03-15', '2020-01-10', 1, 1, 1),
('Marie Martin', '1990-07-22', '2021-05-15', 1, 2, 2),
('Pierre Durand', '1988-11-30', '2019-08-20', 2, 3, 2);

-- ========================================
-- SCÉNARIO 1 : PREMIÈRE COMMANDE
-- ========================================

-- Demande d'achat 1
INSERT INTO demande_achat (date_demande, id_client) VALUES 
('2025-01-01', 1);

INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES 
(1, 1, 10), -- 10 Ordinateurs
(2, 1, 50), -- 50 Souris
(3, 1, 30); -- 30 Claviers

-- Proforma 1
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES 
('2025-01-01 09:00:00', '2025-01-15 18:00:00', 1, 1, 1);

INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES 
(1, 1, 850.00, 10),  -- Ordinateur à 850€
(2, 1, 15.00, 50),   -- Souris à 15€
(3, 1, 45.00, 30);   -- Clavier à 45€

INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES 
(1, 2, '2025-01-02 10:00:00'); -- Validé

-- Commande 1
INSERT INTO commande (date_, remise, id_proforma) VALUES 
('2025-01-03 14:00:00', 100.00, 1);

INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES 
(1, 2, '2025-01-03 14:30:00'); -- Validé

-- Livraison 1
INSERT INTO livraison (date_, id_commande) VALUES 
('2025-01-10 10:00:00', 1);

-- Paiement 1
INSERT INTO paiement (montant, date_, id_commande) VALUES 
(10000.00, '2025-01-10 11:00:00', 1);

INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES 
(10000.00, NULL, '2025-01-10 11:00:00', 1, 'CAISSE_01');

-- Mouvement de stock entrant 1
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-10 11:30:00', TRUE, 'Bon état', 'Réception commande 1 - TechSupply', 1, 1);

-- Création des lots pour l'entrée 1
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-001', 10, 10, 1),  -- 10 Ordinateurs
('LOT-SOU-001', 50, 50, 2),  -- 50 Souris
('LOT-CLA-001', 30, 30, 3);  -- 30 Claviers

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(1, 1),
(1, 2),
(1, 3);

-- ========================================
-- SCÉNARIO 2 : DEUXIÈME COMMANDE
-- ========================================

-- Demande d'achat 2
INSERT INTO demande_achat (date_demande, id_client) VALUES 
('2025-01-12', 2);

INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES 
(1, 2, 5),  -- 5 Ordinateurs
(4, 2, 20), -- 20 Ecrans
(6, 2, 10); -- 10 Imprimantes

-- Proforma 2
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES 
('2025-01-12 09:00:00', '2025-01-20 18:00:00', 2, 2, 3);

INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES 
(1, 2, 820.00, 5),   -- Ordinateur à 820€ (prix différent)
(4, 2, 180.00, 20),  -- Ecran à 180€
(6, 2, 250.00, 10);  -- Imprimante à 250€

INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES 
(2, 2, '2025-01-13 10:00:00');

-- Commande 2
INSERT INTO commande (date_, remise, id_proforma) VALUES 
('2025-01-14 14:00:00', 50.00, 2);

INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES 
(2, 2, '2025-01-14 14:30:00');

-- Livraison 2
INSERT INTO livraison (date_, id_commande) VALUES 
('2025-01-18 15:00:00', 2);

-- Paiement 2
INSERT INTO paiement (montant, date_, id_commande) VALUES 
(7650.00, '2025-01-18 16:00:00', 2);

INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES 
(7650.00, NULL, '2025-01-18 16:00:00', 2, 'CAISSE_01');

-- Mouvement de stock entrant 2
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-18 16:30:00', TRUE, 'Excellent état', 'Réception commande 2 - ElectroDistrib', 2, 1);

-- Création des lots pour l'entrée 2
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-002', 5, 5, 1),     -- 5 Ordinateurs (prix différent)
('LOT-ECR-001', 20, 20, 4),   -- 20 Ecrans
('LOT-IMP-001', 10, 10, 6);   -- 10 Imprimantes

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(2, 4),
(2, 5),
(2, 6);

-- ========================================
-- SCÉNARIO 3 : TROISIÈME COMMANDE
-- ========================================

-- Demande d'achat 3
INSERT INTO demande_achat (date_demande, id_client) VALUES 
('2025-01-20', 3);

INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES 
(2, 3, 30), -- 30 Souris
(7, 3, 100); -- 100 Boîtes de stylos

-- Proforma 3
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES 
('2025-01-20 09:00:00', '2025-01-25 18:00:00', 3, 3, 4);

INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES 
(2, 3, 12.50, 30),   -- Souris à 12.50€ (prix différent du lot 1)
(7, 3, 8.00, 100);   -- Stylos à 8€

INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES 
(3, 2, '2025-01-21 10:00:00');

-- Commande 3
INSERT INTO commande (date_, remise, id_proforma) VALUES 
('2025-01-21 14:00:00', 25.00, 3);

INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES 
(3, 2, '2025-01-21 14:30:00');

-- Livraison 3
INSERT INTO livraison (date_, id_commande) VALUES 
('2025-01-24 10:00:00', 3);

-- Paiement 3
INSERT INTO paiement (montant, date_, id_commande) VALUES 
(1150.00, '2025-01-24 11:00:00', 3);

INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES 
(1150.00, NULL, '2025-01-24 11:00:00', 3, 'CAISSE_01');

-- Mouvement de stock entrant 3
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-24 11:30:00', TRUE, 'Bon état', 'Réception commande 3 - FournituresPro', 3, 1);

-- Création des lots pour l'entrée 3
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-SOU-002', 30, 30, 2),    -- 30 Souris (prix différent)
('LOT-STY-001', 100, 100, 7);  -- 100 Boîtes de stylos

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(3, 7),
(3, 8);

-- ========================================
-- SORTIES DE STOCK
-- ========================================

-- Sortie 1 : Vente de 3 ordinateurs du premier lot (15/01/2025)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-15 09:00:00', FALSE, NULL, 'Vente client ABC - 3 ordinateurs', NULL, 1);

-- Lot de sortie référençant le lot d'origine
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-001-S1', 3, 3, 1);

-- Mise à jour de la quantité du lot d'origine
UPDATE lot SET qte = qte - 3 WHERE id_lot = 1;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(4, 9);

-- Sortie 2 : Vente de 20 souris du premier lot (16/01/2025)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-16 14:00:00', FALSE, NULL, 'Vente client XYZ - 20 souris', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-SOU-001-S1', 20, 20, 2);

UPDATE lot SET qte = qte - 20 WHERE id_lot = 2;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(5, 10);

-- Sortie 3 : Vente mixte (20/01/2025)
-- 2 ordinateurs du lot 1 + 10 claviers
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-20 10:30:00', FALSE, NULL, 'Vente client 123 - Pack bureautique', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-001-S2', 2, 2, 1),
('LOT-CLA-001-S1', 10, 10, 3);

UPDATE lot SET qte = qte - 2 WHERE id_lot = 1;
UPDATE lot SET qte = qte - 10 WHERE id_lot = 3;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(6, 11),
(6, 12);

-- Sortie 4 : Vente de 5 écrans (22/01/2025)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-22 11:00:00', FALSE, NULL, 'Vente écrans - Entreprise XYZ', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ECR-001-S1', 5, 5, 4);

UPDATE lot SET qte = qte - 5 WHERE id_lot = 5;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(7, 13);

-- Sortie 5 : Vente de 2 ordinateurs du deuxième lot (23/01/2025)
-- Important : prix d'achat différent (820€ vs 850€)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-23 15:00:00', FALSE, NULL, 'Vente ordinateurs lot 2', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-002-S1', 2, 2, 1);

UPDATE lot SET qte = qte - 2 WHERE id_lot = 4;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(8, 14);

-- Sortie 6 : Vente de 15 souris du deuxième lot (25/01/2025)
-- Important : prix d'achat différent (12.50€ vs 15€)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-25 16:00:00', FALSE, NULL, 'Vente souris lot 2 - StartUp Tech', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-SOU-002-S1', 15, 15, 2);

UPDATE lot SET qte = qte - 15 WHERE id_lot = 7;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(9, 15);

-- Sortie 7 : Vente mixte (26/01/2025)
-- 3 imprimantes + 20 boîtes de stylos + 5 claviers
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-26 10:00:00', FALSE, NULL, 'Vente fournitures bureau - Commerce 123', NULL, 1);

INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-IMP-001-S1', 3, 3, 6),
('LOT-STY-001-S1', 20, 20, 7),
('LOT-CLA-001-S2', 5, 5, 3);

UPDATE lot SET qte = qte - 3 WHERE id_lot = 6;
UPDATE lot SET qte = qte - 20 WHERE id_lot = 8;
UPDATE lot SET qte = qte - 5 WHERE id_lot = 3;

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(10, 16),
(10, 17),
(10, 18);

-- Historique général
INSERT INTO historique_general (date_historique, nom_table, desc_, id, id_utilisateur) VALUES 
('2025-01-10 11:30:00', 'mvt_stock', 'Création mouvement entrant', 1, 2),
('2025-01-15 09:00:00', 'mvt_stock', 'Création mouvement sortant', 4, 3),
('2025-01-18 16:30:00', 'mvt_stock', 'Création mouvement entrant', 2, 2),
('2025-01-20 10:30:00', 'mvt_stock', 'Création mouvement sortant', 6, 3),
('2025-01-24 11:30:00', 'mvt_stock', 'Création mouvement entrant', 3, 2),
('2025-01-26 10:00:00', 'mvt_stock', 'Création mouvement sortant', 10, 3);