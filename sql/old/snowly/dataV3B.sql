\c gestion_db;

-- ========================================
-- TRANSACTION DATA (coherent test scenarios)
-- Run after: table_27_janv.sql + views.sql + dataV3A.sql
-- ========================================

-- SCENARIO 1: Commande client - Informatique (fully paid + delivered)
INSERT INTO demande_achat (id_da, date_demande, id_client) VALUES (1, '2025-01-05', 1);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (1, 1, 10), (2, 1, 5), (3, 1, 8);
INSERT INTO proforma (id_proforma, date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES (1, '2025-01-05 09:00', '2025-01-20 18:00', 1, 1, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (1, 1, 1200000.00, 10), (2, 1, 1500000.00, 5), (3, 1, 450000.00, 8);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (1, 1, '2025-01-05 09:00'), (1, 2, '2025-01-06 10:00');
INSERT INTO commande (id_commande, date_, remise, id_proforma) VALUES (1, '2025-01-07 14:00', 500000.00, 1);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (1, 1, '2025-01-07 14:00'), (1, 2, '2025-01-07 15:00');
INSERT INTO paiement (id_paiement, montant, date_, id_commande) VALUES (1, 23100000.00, '2025-01-08 10:00', 1); -- total: 12M+7.5M+3.6M-0.5M = 22.6M
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (23100000.00, NULL, '2025-01-08 10:00', 1, 1);
INSERT INTO livraison (id_livraison, date_, id_commande) VALUES (1, '2025-01-09 11:00', 1);
INSERT INTO mvt_stock (id_mvt, date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES (1, '2025-01-09 11:30', TRUE, 'Bon etat', 'Reception cmd 1', 1, 1);
INSERT INTO lot (id_lot, libelle, qte, qte_initiale, id_article) VALUES (1, 'LOT-HP-001', 10, 10, 1), (2, 'LOT-DELL-001', 5, 5, 2), (3, 'LOT-ECRAN-001', 8, 8, 3);
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES (1, 1, 10), (1, 2, 5), (1, 3, 8);

-- SCENARIO 2: Commande fournisseur - Gaming (partially paid, not delivered)
INSERT INTO demande_achat (id_da, date_demande, id_client) VALUES (2, '2025-01-10', NULL);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (17, 2, 20), (18, 2, 15), (20, 2, 50);
INSERT INTO proforma (id_proforma, date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES (2, '2025-01-10 10:00', '2025-01-25 18:00', 2, NULL, 3);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (17, 2, 800000.00, 20), (18, 2, 750000.00, 15), (20, 2, 80000.00, 50);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (2, 1, '2025-01-10 10:00'), (2, 2, '2025-01-11 09:00');
INSERT INTO commande (id_commande, date_, remise, id_proforma) VALUES (2, '2025-01-12 11:00', 0.00, 2);
INSERT INTO commande_etat (id_commande, id_etat, date_) VALUES (2, 1, '2025-01-12 11:00'), (2, 2, '2025-01-12 14:00');
INSERT INTO paiement (id_paiement, montant, date_, id_commande) VALUES (2, 15000000.00, '2025-01-13 10:00', 2); -- partial, total needed: 16M+11.25M+4M = 31.25M
INSERT INTO mvt_caisse (debit, credit, date_, id_paiement, id_caisse) VALUES (NULL, 15000000.00, '2025-01-13 10:00', 2, 1);
-- No livraison yet

-- SCENARIO 3: Commande client - Telephonie (validated proforma, no commande yet)
INSERT INTO demande_achat (id_da, date_demande, id_client) VALUES (3, '2025-01-15', 5);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (6, 3, 30), (7, 3, 25), (8, 3, 10);
INSERT INTO proforma (id_proforma, date_debut, date_fin, id_da, id_client, id_fournisseur) VALUES (3, '2025-01-15 09:00', '2025-01-30 18:00', 3, 5, NULL);
INSERT INTO proforma_detail (id_article, id_proforma, prix, quantite) VALUES (6, 3, 2000000.00, 30), (7, 3, 1500000.00, 25), (8, 3, 1200000.00, 10);
INSERT INTO proforma_etat (id_proforma, id_etat, date_) VALUES (3, 1, '2025-01-15 09:00'); 
-- Not validated yet
-- No commande yet

-- SCENARIO 4: Demande achat only (not yet proforma)
INSERT INTO demande_achat (id_da, date_demande, id_client) VALUES (4, '2025-01-20', 3);
INSERT INTO demande_achat_detail (id_article, id_da, quantite) VALUES (17, 4, 5), (19, 4, 10), (20, 4, 20);
-- No proforma yet

-- STOCK MOVEMENTS: Some sales from Scenario 1 stock
INSERT INTO mvt_stock (id_mvt, date_, entrant, description_qualite, designation, id_livraison, id_depot) VALUES 
    (2, '2025-01-15 14:00', FALSE, NULL, 'Vente 3 HP laptops', NULL, 1),
    (3, '2025-01-18 10:00', FALSE, NULL, 'Vente 2 Dell laptops', NULL, 1);
    
INSERT INTO mvt_stock_lot (id_mvt, id_lot, qte) VALUES 
    (2, 1, 3), 
    (3, 2, 2);

UPDATE lot SET qte = 7 WHERE id_lot = 1;  -- 10-3 = 7
UPDATE lot SET qte = 3 WHERE id_lot = 2;  -- 5-2 = 3