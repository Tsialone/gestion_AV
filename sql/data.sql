-- 1. Données de base
INSERT INTO categorie (libelle) VALUES 
('Electronique'),
('Informatique'),
('Mobilier');

INSERT INTO article (libelle) VALUES 
('Ordinateur Portable HP'),
('Souris Sans Fil'),
('Clavier Mécanique'),
('Ecran 24 pouces'),
('Chaise de Bureau');

INSERT INTO article_categorie (id_article, id_categorie) VALUES 
(1, 2), -- Ordinateur -> Informatique
(2, 2), -- Souris -> Informatique
(3, 2), -- Clavier -> Informatique
(4, 1), -- Ecran -> Electronique
(5, 3); -- Chaise -> Mobilier

INSERT INTO client (nom) VALUES 
('Société ABC'),
('Entreprise XYZ'),
('Commerce 123');

INSERT INTO fournisseur (nom) VALUES 
('TechSupply SARL'),
('OfficeWorld SA'),
('ElectroDistrib');

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

-- 2. Scénario d'entrées de stock

-- Demande d'achat 1
INSERT INTO demande_achat (id_client) VALUES (1);

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
INSERT INTO livraison (id_livraison, date_, id_commande) VALUES 
('LIV-2025-001', '2025-01-10 10:00:00', 1);

-- Paiement 1
INSERT INTO paiement (montant, date_, id_commande) VALUES 
(10000.00, '2025-01-10 11:00:00', 1);

INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES 
(10000.00, NULL, '2025-01-10 11:00:00', 1, 'CAISSE_01');

-- Mouvement de stock entrant 1
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-10 11:30:00', TRUE, 'Bon état', 'Réception commande 1', 'LIV-2025-001', 1);

-- Création des lots pour l'entrée
INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ORD-001', 10, 1),  -- 10 Ordinateurs
('LOT-SOU-001', 50, 2),  -- 50 Souris
('LOT-CLA-001', 30, 3);  -- 30 Claviers

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(1, 1),
(1, 2),
(1, 3);

-- Demande d'achat 2 (pour avoir plus d'entrées)
INSERT INTO demande_achat (id_client) VALUES (2);

INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES 
(1, 2, 5),  -- 5 Ordinateurs
(4, 2, 20); -- 20 Ecrans

-- Proforma 2
INSERT INTO proforma (date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES 
('2025-01-12 09:00:00', '2025-01-20 18:00:00', 2, 2, 3);

INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES 
(1, 2, 820.00, 5),   -- Ordinateur à 820€
(4, 2, 180.00, 20);  -- Ecran à 180€

INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES 
(2, 2, '2025-01-13 10:00:00');

-- Commande 2
INSERT INTO commande (date_, remise, id_proforma) VALUES 
('2025-01-14 14:00:00', 50.00, 2);

INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES 
(2, 2, '2025-01-14 14:30:00');

-- Livraison 2
INSERT INTO livraison (id_livraison, date_, id_commande) VALUES 
('LIV-2025-002', '2025-01-18 15:00:00', 2);

-- Paiement 2
INSERT INTO paiement (montant, date_, id_commande) VALUES 
(7650.00, '2025-01-18 16:00:00', 2);

INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES 
(7650.00, NULL, '2025-01-18 16:00:00', 2, 'CAISSE_01');

-- Mouvement de stock entrant 2
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-18 16:30:00', TRUE, 'Excellent état', 'Réception commande 2', 'LIV-2025-002', 1);

-- Création des lots pour l'entrée 2
INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ORD-002', 5, 1),   -- 5 Ordinateurs
('LOT-ECR-001', 20, 4);  -- 20 Ecrans

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(2, 4),
(2, 5);

-- 3. Scénario de sorties de stock

-- Sortie 1 : Vente de 3 ordinateurs du premier lot
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-15 09:00:00', FALSE, NULL, 'Vente client ABC - 3 ordinateurs', NULL, 1);

-- On crée un nouveau lot pour la sortie (représentant les 3 ordinateurs sortis)
INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ORD-001-S1', 3, 1);  -- 3 Ordinateurs sortis

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(3, 6);

-- Sortie 2 : Vente de 20 souris
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-16 14:00:00', FALSE, NULL, 'Vente client XYZ - 20 souris', NULL, 1);

INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-SOU-001-S1', 20, 2);  -- 20 Souris sorties

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(4, 7);

-- Sortie 3 : Vente mixte (2 ordinateurs du lot 1, 10 claviers)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-20 10:30:00', FALSE, NULL, 'Vente client 123 - Pack bureautique', NULL, 1);

INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ORD-001-S2', 2, 1),   -- 2 Ordinateurs sortis
('LOT-CLA-001-S1', 10, 3);  -- 10 Claviers sortis

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(5, 8),
(5, 9);

-- Sortie 4 : Vente de 5 écrans
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-22 11:00:00', FALSE, NULL, 'Vente écrans - Entreprise XYZ', NULL, 1);

INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ECR-001-S1', 5, 4);  -- 5 Ecrans sortis

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(6, 10);

-- Sortie 5 : Vente de 2 ordinateurs du deuxième lot (prix différent)
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
('2025-01-23 15:00:00', FALSE, NULL, 'Vente ordinateurs lot 2', NULL, 1);

INSERT INTO lot (libelle, qte, id_article) VALUES 
('LOT-ORD-002-S1', 2, 1);  -- 2 Ordinateurs du lot 2 sortis

INSERT INTO mvt_stock_lot (id_mvt, id_lot) VALUES 
(7, 11);

-- Historique général (quelques exemples)
INSERT INTO historique_general (date_historique, nom_table, desc_, id, id_utilisateur) VALUES 
('2025-01-10 11:30:00', 'mvt_stock', 'Création mouvement entrant', 1, 2),
('2025-01-15 09:00:00', 'mvt_stock', 'Création mouvement sortant', 3, 3),
('2025-01-18 16:30:00', 'mvt_stock', 'Création mouvement entrant', 2, 2);