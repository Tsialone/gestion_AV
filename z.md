# Condition dans fifo tsy tokony selon id lot fa id article ihany

DROP DATABASE IF EXISTS gestion_db;
CREATE DATABASE gestion_db;
\c gestion_db;

CREATE TABLE article(
   id_article SERIAL,
   libelle VARCHAR(255) ,
   PRIMARY KEY(id_article)
);

CREATE TABLE categorie(
   id_categorie SERIAL,
   libelle VARCHAR(255) ,
   PRIMARY KEY(id_categorie)
);

CREATE TABLE client(
   id_client SERIAL,
   nom VARCHAR(255) ,
   PRIMARY KEY(id_client)
);

CREATE TABLE fournisseur(
   id_fournisseur SERIAL,
   nom VARCHAR(255) ,
   PRIMARY KEY(id_fournisseur)
);

CREATE TABLE depot(
   id_depot SERIAL,
   nom VARCHAR(256) ,
   PRIMARY KEY(id_depot)
);

CREATE TABLE caisse(
   id_caisse VARCHAR(50) ,
   lieu VARCHAR(255) ,
   PRIMARY KEY(id_caisse)
);

CREATE TABLE etat(
   id_etat SERIAL,
   libelle VARCHAR(255) ,
   PRIMARY KEY(id_etat)
);

CREATE TABLE lot(
   id_lot SERIAL,
   libelle VARCHAR(255) ,
   qte INTEGER,
   id_article INTEGER,
   PRIMARY KEY(id_lot),
   FOREIGN KEY(id_article) REFERENCES article(id_article)
);

CREATE TABLE demande_achat(
   id_da SERIAL,
   id_client INTEGER,
   PRIMARY KEY(id_da),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);

CREATE TABLE role(
   id_role SERIAL,
   nom VARCHAR(255) ,
   niveau INTEGER NOT NULL,
   seuil NUMERIC(15,2)   NOT NULL,
   PRIMARY KEY(id_role)
);

CREATE TABLE dept(
   id_dept SERIAL,
   nom VARCHAR(255) ,
   PRIMARY KEY(id_dept)
);

CREATE TABLE inventaire(
   id_inventaire SERIAL,
   date_inventaire DATE NOT NULL,
   PRIMARY KEY(id_inventaire)
);

CREATE TABLE inventaire_detail(
   id_inventaire_detail SERIAL,
   nombre INTEGER NOT NULL,
   id_depot INTEGER NOT NULL,
   id_article INTEGER NOT NULL,
   id_inventaire INTEGER NOT NULL,
   PRIMARY KEY(id_inventaire_detail),
   FOREIGN KEY(id_depot) REFERENCES depot(id_depot),
   FOREIGN KEY(id_article) REFERENCES article(id_article),
   FOREIGN KEY(id_inventaire) REFERENCES inventaire(id_inventaire)
);

CREATE TABLE conf_va(
   id_conf_va SERIAL,
   niveau_1 INTEGER NOT NULL,
   niveau_2 INTEGER NOT NULL,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_conf_va)
);

CREATE TABLE proforma(
   id_proforma SERIAL,
   date_debut TIMESTAMP NOT NULL,
   date_fin TIMESTAMP NOT NULL,
   id_da INTEGER NOT NULL,
   id_client INTEGER,
   id_fournisseur INTEGER,
   PRIMARY KEY(id_proforma),
   FOREIGN KEY(id_da) REFERENCES demande_achat(id_da),
   FOREIGN KEY(id_client) REFERENCES client(id_client),
   FOREIGN KEY(id_fournisseur) REFERENCES fournisseur(id_fournisseur)
);

CREATE TABLE commande(
   id_commande SERIAL,
   date_ TIMESTAMP NOT NULL,
   remise NUMERIC(15,2)  ,
   id_proforma INTEGER,
   PRIMARY KEY(id_commande),
   FOREIGN KEY(id_proforma) REFERENCES proforma(id_proforma)
);

CREATE TABLE livraison(
   id_livraison VARCHAR(50) ,
   date_ TIMESTAMP NOT NULL,
   id_commande INTEGER NOT NULL,
   PRIMARY KEY(id_livraison),
   UNIQUE(id_commande),
   FOREIGN KEY(id_commande) REFERENCES commande(id_commande)
);

CREATE TABLE utilisateur(
   id_utilisateur SERIAL,
   nom VARCHAR(255) ,
   date_naissance DATE,
   date_embauche DATE,
   id_depot INTEGER,
   id_role INTEGER NOT NULL,
   id_dept INTEGER NOT NULL,
   PRIMARY KEY(id_utilisateur),
   FOREIGN KEY(id_depot) REFERENCES depot(id_depot),
   FOREIGN KEY(id_role) REFERENCES role(id_role),
   FOREIGN KEY(id_dept) REFERENCES dept(id_dept)
);

CREATE TABLE historique_general(
   id_hg SERIAL,
   date_historique TIMESTAMP NOT NULL,
   nom_table VARCHAR(50)  NOT NULL,
   desc_ VARCHAR(50)  NOT NULL,
   id INTEGER NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   PRIMARY KEY(id_hg),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE demande_ajustement(
   id_demande_ajustement SERIAL,
   date_demande TIMESTAMP NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   id_inventaire INTEGER,
   PRIMARY KEY(id_demande_ajustement),
   UNIQUE(id_inventaire),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_inventaire) REFERENCES inventaire(id_inventaire)
);

CREATE TABLE mvt_stock(
   id_mvt SERIAL,
   date_ TIMESTAMP NOT NULL,
   entrant BOOLEAN NOT NULL,
   description_qualite VARCHAR(200) ,
   designation VARCHAR(200) ,
   id_livraison VARCHAR(50) ,
   id_depot INTEGER NOT NULL,
   PRIMARY KEY(id_mvt),
   UNIQUE(id_livraison),
   FOREIGN KEY(id_livraison) REFERENCES livraison(id_livraison),
   FOREIGN KEY(id_depot) REFERENCES depot(id_depot)
);

CREATE TABLE paiement(
   id_paiement SERIAL,
   montant NUMERIC(15,2)   NOT NULL,
   date_ TIMESTAMP NOT NULL,
   id_commande INTEGER NOT NULL,
   PRIMARY KEY(id_paiement),
   FOREIGN KEY(id_commande) REFERENCES commande(id_commande)
);

CREATE TABLE ajustement(
   id_ajustement SERIAL,
   date_ajurstement TIMESTAMP NOT NULL,
   id_mvt INTEGER NOT NULL,
   id_inventaire INTEGER NOT NULL,
   PRIMARY KEY(id_ajustement),
   UNIQUE(id_mvt),
   FOREIGN KEY(id_mvt) REFERENCES mvt_stock(id_mvt),
   FOREIGN KEY(id_inventaire) REFERENCES inventaire(id_inventaire)
);

CREATE TABLE validation_ajustement(
   id_va SERIAL,
   date_validation TIMESTAMP NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   id_ajustement INTEGER NOT NULL,
   PRIMARY KEY(id_va),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(id_ajustement) REFERENCES ajustement(id_ajustement)
);

CREATE TABLE transfert(
   id_transfert SERIAL,
   date_transfert DATE,
   date_validation TIMESTAMP,
   id_utilisateur INTEGER,
   mvt_cible INTEGER NOT NULL,
   mvt_origine INTEGER NOT NULL,
   PRIMARY KEY(id_transfert),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   FOREIGN KEY(mvt_cible) REFERENCES mvt_stock(id_mvt),
   FOREIGN KEY(mvt_origine) REFERENCES mvt_stock(id_mvt)
);

CREATE TABLE mvt_caisse(
   id_mvtc SERIAL,
   debit NUMERIC(15,2)  ,
   credit NUMERIC(15,2)  ,
   date_ TIMESTAMP NOT NULL,
   id_paiement INTEGER NOT NULL,
   id_caisse VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_mvtc),
   UNIQUE(id_paiement),
   FOREIGN KEY(id_paiement) REFERENCES paiement(id_paiement),
   FOREIGN KEY(id_caisse) REFERENCES caisse(id_caisse)
);

CREATE TABLE article_categorie(
   id_article INTEGER,
   id_categorie INTEGER,
   PRIMARY KEY(id_article, id_categorie),
   FOREIGN KEY(id_article) REFERENCES article(id_article),
   FOREIGN KEY(id_categorie) REFERENCES categorie(id_categorie)
);

CREATE TABLE proforma_detail(
   id_article INTEGER,
   id_proforma INTEGER,
   prix NUMERIC(15,2)   NOT NULL,
   quantite INTEGER NOT NULL,
   PRIMARY KEY(id_article, id_proforma),
   FOREIGN KEY(id_article) REFERENCES article(id_article),
   FOREIGN KEY(id_proforma) REFERENCES proforma(id_proforma)
);

CREATE TABLE commande_etat(
   id_commande INTEGER,
   id_etat INTEGER,
   date_ TIMESTAMP NOT NULL,
   PRIMARY KEY(id_commande, id_etat),
   FOREIGN KEY(id_commande) REFERENCES commande(id_commande),
   FOREIGN KEY(id_etat) REFERENCES etat(id_etat)
);

CREATE TABLE proforma_etat(
   id_proforma INTEGER,
   id_etat INTEGER,
   date_ TIMESTAMP NOT NULL,
   PRIMARY KEY(id_proforma, id_etat),
   FOREIGN KEY(id_proforma) REFERENCES proforma(id_proforma),
   FOREIGN KEY(id_etat) REFERENCES etat(id_etat)
);

CREATE TABLE demande_achat_detail(
   id_article INTEGER,
   id_da INTEGER,
   quantite INTEGER NOT NULL,
   PRIMARY KEY(id_article, id_da),
   FOREIGN KEY(id_article) REFERENCES article(id_article),
   FOREIGN KEY(id_da) REFERENCES demande_achat(id_da)
);

CREATE TABLE mvt_stock_lot(
   id_mvt INTEGER,
   id_lot INTEGER,
   PRIMARY KEY(id_mvt, id_lot),
   FOREIGN KEY(id_mvt) REFERENCES mvt_stock(id_mvt),
   FOREIGN KEY(id_lot) REFERENCES lot(id_lot)
);

CREATE TABLE restriction_categorie(
   id_categorie INTEGER,
   id_utilisateur INTEGER,
   PRIMARY KEY(id_categorie, id_utilisateur),
   FOREIGN KEY(id_categorie) REFERENCES categorie(id_categorie),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

CREATE TABLE restriction_fournisseur(
   id_fournisseur INTEGER,
   id_utilisateur INTEGER,
   PRIMARY KEY(id_fournisseur, id_utilisateur),
   FOREIGN KEY(id_fournisseur) REFERENCES fournisseur(id_fournisseur),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);

Creer une vue qui va lister les mvt_stock_lot join la table lot, join article, join mvtstock et la tu va prendre uniquement ce qui sont rentrant avec le booleen rentrant, join livraison, join commande, join proforma pour obtnir le prix de chaque lot, c'est a dire le prix de chaque ligne dans la table mvt_stock_lot, en effet, un mvt_stock_lot d'entree fait creer a chaque fois un lot, creer moi cette vue.