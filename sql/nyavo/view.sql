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

