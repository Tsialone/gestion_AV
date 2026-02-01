-- =========================
-- DONNÉES MINIMALES COHÉRENTES
-- 1 catégorie
-- 1 article
-- 1 dépôt
-- 1 lot
-- mouvements entrée / sortie
-- =========================

-- Catégorie
INSERT INTO categorie (libelle)
VALUES ('Informatique');

-- Article
INSERT INTO article (libelle, id_categorie)
VALUES ('Ordinateur portable', 1);

-- Dépôt
INSERT INTO depot (nom)
VALUES ('Dépôt principal');

-- Lot (un seul)
INSERT INTO lot (libelle, qte, qte_initiale, id_article)
VALUES ('Lot ORD-001', 0, 20, 1);

-- Mouvements de stock
INSERT INTO mvt_stock (date_, entrant, description_qualite, designation, id_depot)
VALUES
('2024-01-10 09:00:00', true,  'Neuf', 'Réception initiale', 1),  -- id_mvt = 1
('2024-01-15 14:00:00', false, 'Bon état', 'Sortie vente', 1),   -- id_mvt = 2
('2024-01-20 10:00:00', true,  'Neuf', 'Réapprovisionnement', 1); -- id_mvt = 3

-- Liaison mouvements ↔ lot
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte)
VALUES
(1, 1, 20),
(2, 1, 5),
(3, 1, 10);
