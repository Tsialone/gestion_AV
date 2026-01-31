\c gestion_db;
-- ========================================
-- INSERTION DES DONNÉES DE TEST
-- ========================================

-- 1. Données de base (Inchangées)
INSERT INTO categorie (libelle) VALUES ('Electronique'), ('Informatique'), ('Mobilier'), ('Fournitures');
INSERT INTO article (libelle, id_categorie) VALUES 
('Ordinateur Portable HP', 2), ('Souris Sans Fil', 2), ('Clavier Mecanique', 2), 
('Ecran 24 pouces', 1), ('Chaise de Bureau', 3), ('Imprimante Laser', 1), ('Stylos Boîte 50', 4);
INSERT INTO client (nom) VALUES ('Societe ABC SARL'), ('Entreprise XYZ SA'), ('Commerce 123'), ('StartUp Tech');
INSERT INTO fournisseur (nom) VALUES ('TechSupply SARL'), ('OfficeWorld SA'), ('ElectroDistrib'), ('FournituresPro');
INSERT INTO depot (nom) VALUES ('Depôt Central'), ('Depôt Secondaire'), ('Entrepôt Nord');
INSERT INTO caisse (id_caisse, lieu) VALUES (1, 'Siège Social'), (2, 'Succursale Est');
INSERT INTO etat (libelle) VALUES ('Cree'), ('Valide'), ('Commande'), ('Livree'), ('Annule');
INSERT INTO role (nom, niveau, seuil) VALUES ('Administrateur', 1, 1000000.00), ('Manager', 2, 500000.00), ('Employe', 3, 100000.00);
INSERT INTO dept (nom) VALUES ('Achats'), ('Logistique'), ('Ventes');
INSERT INTO utilisateur (nom, date_naissance, date_embauche, id_depot, id_role, id_dept) VALUES 
('Jean Dupont', '1985-03-15', '2020-01-10', 1, 1, 1),
('Marie Martin', '1990-07-22', '2021-05-15', 1, 2, 2),
('Pierre Durand', '1988-11-30', '2019-08-20', 2, 3, 2);

-- ========================================
-- SCÉNARIO 1 : PREMIÈRE COMMANDE (Entrée)
-- ========================================
INSERT INTO demande_achat (date_demande, id_client) VALUES ('2025-01-01', 1);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (1, 1, 10), (2, 1, 50), (3, 1, 30);
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES ('2025-01-01 09:00:00', '2025-01-15 18:00:00', 1, 1, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (1, 1, 850.00, 10), (2, 1, 15.00, 50), (3, 1, 45.00, 30);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (1, 2, '2025-01-02 10:00:00');
INSERT INTO commande (date_, remise, id_proforma) VALUES ('2025-01-03 14:00:00', 100.00, 1);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (1, 2, '2025-01-03 14:30:00');
-- Commande not fully paid yet (needs 600 Ar more), so NO livraison
INSERT INTO paiement (montant, date_, id_commande) VALUES (10000.00, '2025-01-10 11:00:00', 1);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (10000.00, NULL, '2025-01-10 11:00:00', 1, 1);

-- No stock movement yet since not delivered

-- ========================================
-- SCÉNARIO 2 : DEUXIÈME COMMANDE (Entrée)
-- ========================================
INSERT INTO demande_achat (date_demande, id_client) VALUES ('2025-01-12', 2);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (1, 2, 5), (4, 2, 20), (6, 2, 10);
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES ('2025-01-12 09:00:00', '2025-01-20 18:00:00', 2, 2, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (1, 2, 820.00, 5), (4, 2, 180.00, 20), (6, 2, 250.00, 10);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (2, 2, '2025-01-13 10:00:00');
INSERT INTO commande (date_, remise, id_proforma) VALUES ('2025-01-14 14:00:00', 50.00, 2);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (2, 2, '2025-01-14 14:30:00');
-- Commande not fully paid yet (needs 2550 Ar more), so NO livraison
INSERT INTO paiement (montant, date_, id_commande) VALUES (7650.00, '2025-01-18 16:00:00', 2);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (7650.00, NULL, '2025-01-18 16:00:00', 2, 1);

-- No stock movement yet since not delivered
-- ========================================
-- Note: Stock movements (mvt_stock, lot, mvt_stock_lot) would be created
-- after commandes are fully paid and delivered
-- ========================================

-- ========================================
-- SCÉNARIO 3 : THIRD COMMANDE - FULLY PAID AND DELIVERED (for stock testing)
-- ========================================
INSERT INTO demande_achat (date_demande, id_client) VALUES ('2025-01-05', 3);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (5, 3, 15), (7, 3, 100);
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES ('2025-01-05 10:00:00', '2025-01-25 18:00:00', 3, 3, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (5, 3, 120.00, 15), (7, 3, 5.00, 100);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (3, 2, '2025-01-06 09:00:00');
INSERT INTO commande (date_, remise, id_proforma) VALUES ('2025-01-07 10:00:00', 0.00, 3);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (3, 2, '2025-01-07 10:30:00');
-- FULLY PAID
INSERT INTO paiement (montant, date_, id_commande) VALUES (2300.00, '2025-01-08 14:00:00', 3);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (2300.00, NULL, '2025-01-08 14:00:00', 3, 1);
-- DELIVERED
INSERT INTO livraison (date_, id_commande) VALUES ('2025-01-09 11:00:00', 3);

-- Stock entry from this delivery
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-09 11:30:00', TRUE, 'Bon etat', 'Reception commande 3', 1, 1);

-- Create lots for this stock entry
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-CHAISE-001', 15, 15, 5), 
('LOT-STYLO-001', 100, 100, 7);

-- Link lots to stock movement
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (1, 1, 15), (1, 2, 100);

-- ========================================
-- SCÉNARIO 4 : FOURTH COMMANDE - FULLY PAID AND DELIVERED (for more variety)
-- ========================================
INSERT INTO demande_achat (date_demande, id_client) VALUES ('2025-01-08', 4);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (1, 4, 8), (4, 4, 12);
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES ('2025-01-08 09:00:00', '2025-01-28 18:00:00', 4, 4, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (1, 4, 880.00, 8), (4, 4, 190.00, 12);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (4, 2, '2025-01-09 10:00:00');
INSERT INTO commande (date_, remise, id_proforma) VALUES ('2025-01-10 11:00:00', 200.00, 4);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (4, 2, '2025-01-10 11:30:00');
INSERT INTO paiement (montant, date_, id_commande) VALUES (9320.00, '2025-01-11 15:00:00', 4);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (9120.00, NULL, '2025-01-11 15:00:00', 4, 2);
INSERT INTO livraison (date_, id_commande) VALUES ('2025-01-12 10:00:00', 4);

-- Stock entry from this delivery
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-12 10:30:00', TRUE, 'Excellent etat', 'Reception commande 4', 2, 2);

-- Create lots for this stock entry
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-001', 8, 8, 1), 
('LOT-ECRAN-001', 12, 12, 4);

-- Link lots to stock movement
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (2, 3, 8), (2, 4, 12);

-- ========================================
-- SORTIES DE STOCK (Some items sold from inventory)
-- ========================================

-- Sortie 1: Vente de 5 chaises (from LOT-CHAISE-001)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-15 14:00:00', FALSE, NULL, 'Vente 5 chaises - Client local', NULL, 1);
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (3, 1, 5);
UPDATE lot SET qte = qte - 5 WHERE id_lot = 1;

-- Sortie 2: Vente de 30 stylos (from LOT-STYLO-001)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-16 09:30:00', FALSE, NULL, 'Vente 30 stylos - Bureau interne', NULL, 1);
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (4, 2, 30);
UPDATE lot SET qte = qte - 30 WHERE id_lot = 2;

-- Sortie 3: Vente de 3 ordinateurs (from LOT-ORD-001)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-17 11:00:00', FALSE, NULL, 'Vente 3 ordinateurs - Startup Tech', NULL, 2);
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (5, 3, 3);
UPDATE lot SET qte = qte - 3 WHERE id_lot = 3;

-- Sortie 4: Vente de 7 écrans (from LOT-ECRAN-001)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-19 13:30:00', FALSE, NULL, 'Vente 7 ecrans - Commerce 123', NULL, 2);
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (6, 4, 7);
UPDATE lot SET qte = qte - 7 WHERE id_lot = 4;

