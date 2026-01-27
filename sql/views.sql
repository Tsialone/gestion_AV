CREATE VIEW v_entree_stock_lot AS
SELECT 
    msl.id_mvt,
    msl.id_lot,
    l.libelle AS libelle_lot,
    l.qte AS quantite_lot,
    a.id_article,
    a.libelle AS libelle_article,
    ms.date_ AS date_mouvement,
    ms.description_qualite,
    ms.designation,
    ms.id_depot,
    d.nom AS nom_depot,
    liv.id_livraison,
    liv.date_ AS date_livraison,
    c.id_commande,
    c.date_ AS date_commande,
    c.remise,
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
    INNER JOIN mvt_stock ms ON msl.id_mvt = ms.id_mvt
    INNER JOIN depot d ON ms.id_depot = d.id_depot
    LEFT JOIN livraison liv ON ms.id_livraison = liv.id_livraison
    LEFT JOIN commande c ON liv.id_commande = c.id_commande
    LEFT JOIN proforma p ON c.id_proforma = p.id_proforma
    LEFT JOIN proforma_detail pd ON (p.id_proforma = pd.id_proforma AND a.id_article = pd.id_article)
    LEFT JOIN fournisseur f ON p.id_fournisseur = f.id_fournisseur
    LEFT JOIN client cl ON p.id_client = cl.id_client
WHERE 
    ms.entrant = TRUE
ORDER BY 
    ms.date_ DESC, msl.id_mvt, msl.id_lot;

    
CREATE VIEW v_sortie_stock_lot AS
SELECT 
    msl.id_mvt,
    msl.id_lot,
    l.libelle AS libelle_lot,
    l.qte AS quantite_lot,
    a.id_article,
    a.libelle AS libelle_article,
    ms.date_ AS date_mouvement,
    ms.description_qualite,
    ms.designation,
    ms.id_depot,
    d.nom AS nom_depot
FROM 
    mvt_stock_lot msl
    INNER JOIN lot l ON msl.id_lot = l.id_lot
    INNER JOIN article a ON l.id_article = a.id_article
    INNER JOIN mvt_stock ms ON msl.id_mvt = ms.id_mvt
    INNER JOIN depot d ON ms.id_depot = d.id_depot
WHERE 
    ms.entrant = FALSE
ORDER BY 
    ms.date_ DESC, msl.id_mvt, msl.id_lot;
