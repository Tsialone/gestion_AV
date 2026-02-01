CREATE VIEW v_entree_stock_lot AS
SELECT 
    msl.id_mvt,
    msl.id_lot,
    l.libelle AS libelle_lot,
    l.qte AS quantite_lot,
    l.qte_initiale AS quantite_initiale_lot,
    a.id_article,
    a.libelle AS libelle_article,
    c.id_categorie,
    c.libelle AS libelle_categorie,
    ms.date_ AS date_mouvement,
    ms.description_qualite,
    ms.designation,
    ms.id_depot,
    d.nom AS nom_depot,
    liv.id_livraison,
    liv.date_ AS date_livraison,
    cmd.id_commande,
    cmd.date_ AS date_commande,
    cmd.remise,
    p.id_proforma,
    p.date_debut AS date_debut_proforma,
    p.date_fin AS date_fin_proforma,
    pd.prix AS prix_unitaire,
    pd.quantite AS quantite_proforma,
    (pd.prix * l.qte) AS prix_total_lot,
    f.id_fournisseur,
    f.nom AS nom_fournisseur,
    cl.id_client,
    cl.nom AS nom_client
FROM 
    mvt_stock_lot msl
    INNER JOIN lot l ON msl.id_lot = l.id_lot
    INNER JOIN article a ON l.id_article = a.id_article
    INNER JOIN categorie c ON a.id_categorie = c.id_categorie
    INNER JOIN mvt_stock ms ON msl.id_mvt = ms.id_mvt
    INNER JOIN depot d ON ms.id_depot = d.id_depot
    LEFT JOIN livraison liv ON ms.id_livraison = liv.id_livraison
    LEFT JOIN commande cmd ON liv.id_commande = cmd.id_commande
    LEFT JOIN proforma p ON cmd.id_proforma = p.id_proforma
    LEFT JOIN proforma_detail pd ON (p.id_proforma = pd.id_proforma AND a.id_article = pd.id_article)
    LEFT JOIN fournisseur f ON p.id_fournisseur = f.id_fournisseur
    LEFT JOIN client cl ON p.id_client = cl.id_client
WHERE 
    ms.entrant = TRUE
ORDER BY 
    ms.date_ DESC, msl.id_mvt, msl.id_lot;




-- Vue pour les mouvements de stock SORTANTS
CREATE VIEW v_sortie_stock_lot AS
SELECT 
    msl.id_mvt,
    msl.id_lot,
    l.libelle AS libelle_lot,
    l.qte AS quantite_lot,
    l.qte_initiale AS quantite_initiale_lot,
    a.id_article,
    a.libelle AS libelle_article,
    c.id_categorie,
    c.libelle AS libelle_categorie,
    ms.date_ AS date_mouvement,
    ms.description_qualite,
    ms.designation,
    ms.id_depot,
    d.nom AS nom_depot
FROM 
    mvt_stock_lot msl
    INNER JOIN lot l ON msl.id_lot = l.id_lot
    INNER JOIN article a ON l.id_article = a.id_article
    INNER JOIN categorie c ON a.id_categorie = c.id_categorie
    INNER JOIN mvt_stock ms ON msl.id_mvt = ms.id_mvt
    INNER JOIN depot d ON ms.id_depot = d.id_depot
WHERE 
    ms.entrant = FALSE
ORDER BY 
    ms.date_ DESC, msl.id_mvt, msl.id_lot;














CREATE OR REPLACE VIEW v_journal_mvt_stock AS
SELECT
    ms.id_mvt,
    ms.date_                                AS date_mouvement,

    d.id_depot,
    d.nom                                   AS depot,

    ms.entrant,
    CASE 
        WHEN ms.entrant THEN 'ENTRÉE'
        ELSE 'SORTIE'
    END                                     AS type_mouvement,

    ms.designation,
    ms.description_qualite,

    -- livraison (optionnelle)
    liv.id_livraison,
    liv.date_                               AS date_livraison,

    a.id_article,
    a.libelle                               AS article,

    l.id_lot,
    l.libelle                               AS lot,

    msl.qte                                 AS quantite

FROM mvt_stock ms
JOIN depot d               ON d.id_depot = ms.id_depot
JOIN mvt_stock_lot msl     ON msl.id_mvt = ms.id_mvt
JOIN lot l                 ON l.id_lot = msl.id_lot
JOIN article a             ON a.id_article = l.id_article
LEFT JOIN livraison liv    ON liv.id_livraison = ms.id_livraison;


SELECT
    id_depot,
    depot,
    id_article,
    article,
    SUM(entree) AS total_entrees,
    SUM(sortie) AS total_sorties,
    SUM(variation) AS stock_a_date
FROM v_etat_stock
WHERE (:idDepot IS NULL OR id_depot = :idDepot)
  AND (:dateMax IS NULL OR date_mouvement <= :dateMax)
GROUP BY id_depot, depot, id_article, article
ORDER BY depot, article;



CREATE OR REPLACE VIEW v_etat_stock AS
select
    ms.id_depot,
    d.nom AS depot,
    a.id_article,
    a.libelle AS article,
    ms.date_ AS date_mouvement,
    CASE
        WHEN ms.entrant THEN msl.qte
        ELSE -msl.qte
    END AS variation,
    CASE WHEN ms.entrant THEN msl.qte ELSE 0 END AS entree,
    CASE WHEN NOT ms.entrant THEN msl.qte ELSE 0 END AS sortie
FROM mvt_stock ms
JOIN mvt_stock_lot msl ON msl.id_mvt = ms.id_mvt
JOIN lot l ON l.id_lot = msl.id_lot
JOIN article a ON a.id_article = l.id_article
JOIN depot d ON d.id_depot = ms.id_depot;


CREATE OR REPLACE VIEW v_lot_cpl AS
SELECT
    l.id_lot,
    l.libelle AS lot,
    ms.date_  AS date_mouvement,

    CASE WHEN ms.entrant THEN msl.qte ELSE 0 END AS entree,
    CASE WHEN NOT ms.entrant THEN msl.qte ELSE 0 END AS sortie
FROM mvt_stock_lot msl
JOIN mvt_stock ms ON ms.id_mvt = msl.id_mvt
JOIN lot l        ON l.id_lot = msl.id_lot;

