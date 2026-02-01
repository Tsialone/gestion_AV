\c gestion_db

--?========== Only for the Demande - Proforma - Commande - Paiement flow ==========?--
-- Categories
INSERT INTO categorie (libelle) VALUES
('Informatique'),
('Fournitures');

-- Articles
INSERT INTO article (libelle, id_categorie) VALUES
('Ordinateur Portable', 1),
('Souris Sans Fil', 1),
('Stylos Boîte 50', 2);

-- Clients
INSERT INTO client (nom) VALUES
('Client A'),
('Client B');

-- Fournisseurs
INSERT INTO fournisseur (nom) VALUES
('Fournisseur X'),
('Fournisseur Y');

-- Caisses (use Integer ids to match the model)
INSERT INTO caisse (id_caisse, lieu) VALUES
(1, 'Siege Social'),
(2, 'Succursale');

-- Etats (basic ones used by proforma/commande flows)
INSERT INTO etat (libelle) VALUES
('Cree'),
('Valide'),
('Commandee'),
('Payee');
