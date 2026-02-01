-- ========================================
-- INSERTION DES DONNÉES DE TEST
-- ========================================

-- 1. Données de base (Inchangées)
INSERT INTO categorie (libelle) VALUES ('Electronique'), ('Informatique'), ('Mobilier'), ('Fournitures');
INSERT INTO article (libelle, id_categorie) VALUES 
('Ordinateur Portable HP', 2), ('Souris Sans Fil', 2), ('Clavier Mécanique', 2), 
('Ecran 24 pouces', 1), ('Chaise de Bureau', 3), ('Imprimante Laser', 1), ('Stylos Boîte 50', 4);
INSERT INTO client (nom) VALUES ('Société ABC SARL'), ('Entreprise XYZ SA'), ('Commerce 123'), ('StartUp Tech');
INSERT INTO fournisseur (nom) VALUES ('TechSupply SARL'), ('OfficeWorld SA'), ('ElectroDistrib'), ('FournituresPro');
INSERT INTO depot (nom) VALUES ('Dépôt Central'), ('Dépôt Secondaire'), ('Entrepôt Nord');
INSERT INTO caisse (id_caisse, lieu) VALUES (1, 'Siège Social'), (2, 'Succursale Est');
INSERT INTO etat (libelle) VALUES ('En attente'), ('Validé'), ('En cours'), ('Livré'), ('Annulé');
INSERT INTO role (nom, niveau, seuil) VALUES ('Administrateur', 1, 1000000.00), ('Manager', 2, 500000.00), ('Employé', 3, 100000.00);
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
INSERT INTO livraison (date_, id_commande) VALUES ('2025-01-10 10:00:00', 1);
INSERT INTO paiement (montant, date_, id_commande) VALUES (10000.00, '2025-01-10 11:00:00', 1);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (10000.00, NULL, '2025-01-10 11:00:00', 1, 1);

INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-10 11:30:00', TRUE, 'Bon état', 'Réception commande 1', 1, 1);

-- Entrée : qte = qte_initiale
-- lot 1 2 3
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-001', 10, 10, 1), ('LOT-SOU-001', 50, 50, 2), ('LOT-CLA-001', 30, 30, 3);

INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (1, 1, 10), (1, 2, 50), (1, 3, 30);

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
INSERT INTO livraison (date_, id_commande) VALUES ('2025-01-18 15:00:00', 2);
INSERT INTO paiement (montant, date_, id_commande) VALUES (7650.00, '2025-01-18 16:00:00', 2);
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (7650.00, NULL, '2025-01-18 16:00:00', 2, 1);

INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-18 16:30:00', TRUE, 'Excellent état', 'Réception commande 2', 2, 1);

-- lot 4 5 6
INSERT INTO lot (libelle, qte, qte_initiale, id_article) VALUES 
('LOT-ORD-002', 5, 5, 1), ('LOT-ECR-001', 20, 20, 4), ('LOT-IMP-001', 10, 10, 6);

INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (2, 4, 5), (2, 5, 20), (2, 6, 10);
-- ========================================
-- SORTIES DE STOCK (Mise à jour des lots existants)
-- ========================================

-- Sortie 1 : Vente de 3 ordinateurs (On pioche dans le LOT 1)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-15 09:00:00', FALSE, NULL, 'Vente client ABC - 3 ordinateurs', NULL, 1);

-- On pointe vers le lot original (id_lot = 1) et on enregistre la quantité sortie
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (3, 1, 3);

-- Mise à jour du stock réel dans le lot
UPDATE lot SET qte = qte - 3 WHERE id_lot = 1;


-- Sortie 2 : Vente de 20 souris (On pioche dans le LOT 2)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-20 14:00:00', FALSE, NULL, 'Vente client XYZ - 20 souris', NULL, 1);

INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (4, 2, 20);
UPDATE lot SET qte = qte - 20 WHERE id_lot = 2;


-- Sortie 3 : Vente mixte (2 ordinateurs du lot 1 + 10 claviers du lot 3)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-21 10:30:00', FALSE, NULL, 'Vente client 123 - Pack bureautique', NULL, 1);

-- On lie le même mouvement (id_mvt 6) à deux lots différents
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES 
(5, 1, 10),  -- 2 ordi du lot 1
(5, 3, 10); -- 10 claviers du lot 3

UPDATE lot SET qte = qte - 2 WHERE id_lot = 1;
UPDATE lot SET qte = qte - 10 WHERE id_lot = 3;


-- Sortie 4 : Vente de 5 écrans (On pioche dans le LOT 5)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-22 11:00:00', FALSE, NULL, 'Vente écrans - Entreprise XYZ', NULL, 1);

INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (6, 5, 5);
UPDATE lot SET qte = qte - 5 WHERE id_lot = 5;


-- Sortie 5 : Vente de 2 ordinateurs du deuxième lot (id_lot = 4)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-23 15:00:00', FALSE, NULL, 'Vente ordinateurs lot 2', NULL, 1);

INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (7, 4, 2);
UPDATE lot SET qte = qte - 2 WHERE id_lot = 4;