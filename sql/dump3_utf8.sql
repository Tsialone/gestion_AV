--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13
-- Dumped by pg_dump version 14.13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ajustement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ajustement (
    id_ajustement integer NOT NULL,
    date_ajurstement timestamp without time zone NOT NULL,
    id_mvt integer NOT NULL,
    id_inventaire integer NOT NULL
);


ALTER TABLE public.ajustement OWNER TO postgres;

--
-- Name: ajustement_id_ajustement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ajustement_id_ajustement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ajustement_id_ajustement_seq OWNER TO postgres;

--
-- Name: ajustement_id_ajustement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ajustement_id_ajustement_seq OWNED BY public.ajustement.id_ajustement;


--
-- Name: article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.article (
    id_article integer NOT NULL,
    libelle character varying(255),
    id_categorie integer NOT NULL
);


ALTER TABLE public.article OWNER TO postgres;

--
-- Name: article_id_article_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.article_id_article_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.article_id_article_seq OWNER TO postgres;

--
-- Name: article_id_article_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.article_id_article_seq OWNED BY public.article.id_article;


--
-- Name: caisse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caisse (
    id_caisse integer NOT NULL,
    lieu character varying(255)
);


ALTER TABLE public.caisse OWNER TO postgres;

--
-- Name: caisse_id_caisse_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.caisse_id_caisse_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.caisse_id_caisse_seq OWNER TO postgres;

--
-- Name: caisse_id_caisse_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.caisse_id_caisse_seq OWNED BY public.caisse.id_caisse;


--
-- Name: categorie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categorie (
    id_categorie integer NOT NULL,
    libelle character varying(255)
);


ALTER TABLE public.categorie OWNER TO postgres;

--
-- Name: categorie_id_categorie_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categorie_id_categorie_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categorie_id_categorie_seq OWNER TO postgres;

--
-- Name: categorie_id_categorie_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categorie_id_categorie_seq OWNED BY public.categorie.id_categorie;


--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    id_client integer NOT NULL,
    nom character varying(255)
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: client_id_client_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_id_client_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_id_client_seq OWNER TO postgres;

--
-- Name: client_id_client_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_id_client_seq OWNED BY public.client.id_client;


--
-- Name: commande; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commande (
    id_commande integer NOT NULL,
    date_ timestamp without time zone NOT NULL,
    remise numeric(15,2),
    id_proforma integer
);


ALTER TABLE public.commande OWNER TO postgres;

--
-- Name: commande_etat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.commande_etat (
    id_commande integer NOT NULL,
    id_etat integer NOT NULL,
    date_ timestamp without time zone NOT NULL
);


ALTER TABLE public.commande_etat OWNER TO postgres;

--
-- Name: commande_id_commande_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.commande_id_commande_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.commande_id_commande_seq OWNER TO postgres;

--
-- Name: commande_id_commande_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.commande_id_commande_seq OWNED BY public.commande.id_commande;


--
-- Name: conf_va; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.conf_va (
    id_conf_va integer NOT NULL,
    niveau_1 integer NOT NULL,
    niveau_2 integer NOT NULL,
    libelle character varying(50) NOT NULL
);


ALTER TABLE public.conf_va OWNER TO postgres;

--
-- Name: conf_va_id_conf_va_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.conf_va_id_conf_va_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.conf_va_id_conf_va_seq OWNER TO postgres;

--
-- Name: conf_va_id_conf_va_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.conf_va_id_conf_va_seq OWNED BY public.conf_va.id_conf_va;


--
-- Name: demande_achat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.demande_achat (
    id_da integer NOT NULL,
    date_demande date NOT NULL,
    id_client integer
);


ALTER TABLE public.demande_achat OWNER TO postgres;

--
-- Name: demande_achat_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.demande_achat_detail (
    id_article integer NOT NULL,
    id_da integer NOT NULL,
    quantite integer NOT NULL
);


ALTER TABLE public.demande_achat_detail OWNER TO postgres;

--
-- Name: demande_achat_id_da_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.demande_achat_id_da_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.demande_achat_id_da_seq OWNER TO postgres;

--
-- Name: demande_achat_id_da_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.demande_achat_id_da_seq OWNED BY public.demande_achat.id_da;


--
-- Name: demande_ajustement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.demande_ajustement (
    id_demande_ajustement integer NOT NULL,
    date_demande timestamp without time zone NOT NULL,
    id_utilisateur integer NOT NULL,
    id_inventaire integer
);


ALTER TABLE public.demande_ajustement OWNER TO postgres;

--
-- Name: demande_ajustement_id_demande_ajustement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.demande_ajustement_id_demande_ajustement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.demande_ajustement_id_demande_ajustement_seq OWNER TO postgres;

--
-- Name: demande_ajustement_id_demande_ajustement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.demande_ajustement_id_demande_ajustement_seq OWNED BY public.demande_ajustement.id_demande_ajustement;


--
-- Name: depot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.depot (
    id_depot integer NOT NULL,
    nom character varying(256),
    capacite integer DEFAULT 100 NOT NULL
);


ALTER TABLE public.depot OWNER TO postgres;

--
-- Name: depot_id_depot_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.depot_id_depot_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.depot_id_depot_seq OWNER TO postgres;

--
-- Name: depot_id_depot_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.depot_id_depot_seq OWNED BY public.depot.id_depot;


--
-- Name: dept; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dept (
    id_dept integer NOT NULL,
    nom character varying(255)
);


ALTER TABLE public.dept OWNER TO postgres;

--
-- Name: dept_id_dept_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dept_id_dept_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dept_id_dept_seq OWNER TO postgres;

--
-- Name: dept_id_dept_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dept_id_dept_seq OWNED BY public.dept.id_dept;


--
-- Name: etat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.etat (
    id_etat integer NOT NULL,
    libelle character varying(255)
);


ALTER TABLE public.etat OWNER TO postgres;

--
-- Name: etat_id_etat_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.etat_id_etat_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.etat_id_etat_seq OWNER TO postgres;

--
-- Name: etat_id_etat_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.etat_id_etat_seq OWNED BY public.etat.id_etat;


--
-- Name: fournisseur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fournisseur (
    id_fournisseur integer NOT NULL,
    nom character varying(255)
);


ALTER TABLE public.fournisseur OWNER TO postgres;

--
-- Name: fournisseur_id_fournisseur_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.fournisseur_id_fournisseur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.fournisseur_id_fournisseur_seq OWNER TO postgres;

--
-- Name: fournisseur_id_fournisseur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.fournisseur_id_fournisseur_seq OWNED BY public.fournisseur.id_fournisseur;


--
-- Name: historique_general; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.historique_general (
    id_hg integer NOT NULL,
    date_historique timestamp without time zone NOT NULL,
    nom_table character varying(50) NOT NULL,
    desc_ character varying(50) NOT NULL,
    id integer NOT NULL,
    id_utilisateur integer NOT NULL
);


ALTER TABLE public.historique_general OWNER TO postgres;

--
-- Name: historique_general_id_hg_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.historique_general_id_hg_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.historique_general_id_hg_seq OWNER TO postgres;

--
-- Name: historique_general_id_hg_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.historique_general_id_hg_seq OWNED BY public.historique_general.id_hg;


--
-- Name: inventaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventaire (
    id_inventaire integer NOT NULL,
    date_inventaire date NOT NULL
);


ALTER TABLE public.inventaire OWNER TO postgres;

--
-- Name: inventaire_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inventaire_detail (
    id_inventaire_detail integer NOT NULL,
    nombre integer NOT NULL,
    id_depot integer NOT NULL,
    id_article integer NOT NULL,
    id_inventaire integer NOT NULL
);


ALTER TABLE public.inventaire_detail OWNER TO postgres;

--
-- Name: inventaire_detail_id_inventaire_detail_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inventaire_detail_id_inventaire_detail_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.inventaire_detail_id_inventaire_detail_seq OWNER TO postgres;

--
-- Name: inventaire_detail_id_inventaire_detail_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inventaire_detail_id_inventaire_detail_seq OWNED BY public.inventaire_detail.id_inventaire_detail;


--
-- Name: inventaire_id_inventaire_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inventaire_id_inventaire_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.inventaire_id_inventaire_seq OWNER TO postgres;

--
-- Name: inventaire_id_inventaire_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inventaire_id_inventaire_seq OWNED BY public.inventaire.id_inventaire;


--
-- Name: livraison; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.livraison (
    id_livraison integer NOT NULL,
    date_ timestamp without time zone NOT NULL,
    id_commande integer NOT NULL
);


ALTER TABLE public.livraison OWNER TO postgres;

--
-- Name: livraison_id_livraison_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.livraison_id_livraison_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.livraison_id_livraison_seq OWNER TO postgres;

--
-- Name: livraison_id_livraison_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.livraison_id_livraison_seq OWNED BY public.livraison.id_livraison;


--
-- Name: lot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lot (
    id_lot integer NOT NULL,
    libelle character varying(255),
    qte integer,
    qte_initiale integer NOT NULL,
    id_article integer
);


ALTER TABLE public.lot OWNER TO postgres;

--
-- Name: lot_id_lot_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lot_id_lot_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lot_id_lot_seq OWNER TO postgres;

--
-- Name: lot_id_lot_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lot_id_lot_seq OWNED BY public.lot.id_lot;


--
-- Name: mvt_caisse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mvt_caisse (
    id_mvtc integer NOT NULL,
    debit numeric(15,2),
    credit numeric(15,2),
    date_ timestamp without time zone NOT NULL,
    id_paiement integer NOT NULL,
    id_caisse integer NOT NULL
);


ALTER TABLE public.mvt_caisse OWNER TO postgres;

--
-- Name: mvt_caisse_id_mvtc_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.mvt_caisse_id_mvtc_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mvt_caisse_id_mvtc_seq OWNER TO postgres;

--
-- Name: mvt_caisse_id_mvtc_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mvt_caisse_id_mvtc_seq OWNED BY public.mvt_caisse.id_mvtc;


--
-- Name: mvt_stock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mvt_stock (
    id_mvt integer NOT NULL,
    date_ timestamp without time zone NOT NULL,
    entrant boolean NOT NULL,
    description_qualite character varying(200),
    designation character varying(200),
    id_livraison integer,
    id_depot integer NOT NULL
);


ALTER TABLE public.mvt_stock OWNER TO postgres;

--
-- Name: mvt_stock_id_mvt_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.mvt_stock_id_mvt_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mvt_stock_id_mvt_seq OWNER TO postgres;

--
-- Name: mvt_stock_id_mvt_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mvt_stock_id_mvt_seq OWNED BY public.mvt_stock.id_mvt;


--
-- Name: mvt_stock_lot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mvt_stock_lot (
    id_mvt integer NOT NULL,
    id_lot integer NOT NULL,
    qte integer
);


ALTER TABLE public.mvt_stock_lot OWNER TO postgres;

--
-- Name: paiement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paiement (
    id_paiement integer NOT NULL,
    montant numeric(15,2) NOT NULL,
    date_ timestamp without time zone NOT NULL,
    id_commande integer NOT NULL
);


ALTER TABLE public.paiement OWNER TO postgres;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paiement_id_paiement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.paiement_id_paiement_seq OWNER TO postgres;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.paiement_id_paiement_seq OWNED BY public.paiement.id_paiement;


--
-- Name: proforma; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proforma (
    id_proforma integer NOT NULL,
    date_debut timestamp without time zone NOT NULL,
    date_fin timestamp without time zone NOT NULL,
    id_da integer NOT NULL,
    id_client integer,
    id_fournisseur integer
);


ALTER TABLE public.proforma OWNER TO postgres;

--
-- Name: proforma_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proforma_detail (
    id_article integer NOT NULL,
    id_proforma integer NOT NULL,
    prix numeric(15,2) NOT NULL,
    quantite integer NOT NULL
);


ALTER TABLE public.proforma_detail OWNER TO postgres;

--
-- Name: proforma_etat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proforma_etat (
    id_proforma integer NOT NULL,
    id_etat integer NOT NULL,
    date_ timestamp without time zone NOT NULL
);


ALTER TABLE public.proforma_etat OWNER TO postgres;

--
-- Name: proforma_id_proforma_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proforma_id_proforma_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.proforma_id_proforma_seq OWNER TO postgres;

--
-- Name: proforma_id_proforma_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proforma_id_proforma_seq OWNED BY public.proforma.id_proforma;


--
-- Name: restriction_categorie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.restriction_categorie (
    id_categorie integer NOT NULL,
    id_utilisateur integer NOT NULL
);


ALTER TABLE public.restriction_categorie OWNER TO postgres;

--
-- Name: restriction_fournisseur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.restriction_fournisseur (
    id_fournisseur integer NOT NULL,
    id_utilisateur integer NOT NULL
);


ALTER TABLE public.restriction_fournisseur OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id_role integer NOT NULL,
    nom character varying(255),
    niveau integer NOT NULL,
    seuil numeric(15,2) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: role_id_role_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_role_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_role_seq OWNER TO postgres;

--
-- Name: role_id_role_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_id_role_seq OWNED BY public.role.id_role;


--
-- Name: transfert; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transfert (
    id_transfert integer NOT NULL,
    date_transfert date,
    date_validation timestamp without time zone,
    id_utilisateur integer,
    mvt_cible integer NOT NULL,
    mvt_origine integer NOT NULL
);


ALTER TABLE public.transfert OWNER TO postgres;

--
-- Name: transfert_id_transfert_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transfert_id_transfert_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transfert_id_transfert_seq OWNER TO postgres;

--
-- Name: transfert_id_transfert_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transfert_id_transfert_seq OWNED BY public.transfert.id_transfert;


--
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilisateur (
    id_utilisateur integer NOT NULL,
    nom character varying(255),
    date_naissance date,
    date_embauche date,
    id_depot integer,
    id_role integer NOT NULL,
    id_dept integer NOT NULL
);


ALTER TABLE public.utilisateur OWNER TO postgres;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilisateur_id_utilisateur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.utilisateur_id_utilisateur_seq OWNER TO postgres;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilisateur_id_utilisateur_seq OWNED BY public.utilisateur.id_utilisateur;


--
-- Name: v_entree_stock_lot; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_entree_stock_lot AS
 SELECT msl.id_mvt,
    msl.id_lot,
    msl.qte AS quantite_mouvement,
    l.libelle AS libelle_lot,
    l.qte AS stock_actuel_lot,
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
    (pd.prix * (msl.qte)::numeric) AS prix_total_mouvement,
    f.id_fournisseur,
    f.nom AS nom_fournisseur,
    cl.id_client,
    cl.nom AS nom_client
   FROM (((((((((((public.mvt_stock_lot msl
     JOIN public.lot l ON ((msl.id_lot = l.id_lot)))
     JOIN public.article a ON ((l.id_article = a.id_article)))
     JOIN public.categorie c ON ((a.id_categorie = c.id_categorie)))
     JOIN public.mvt_stock ms ON ((msl.id_mvt = ms.id_mvt)))
     JOIN public.depot d ON ((ms.id_depot = d.id_depot)))
     LEFT JOIN public.livraison liv ON ((ms.id_livraison = liv.id_livraison)))
     LEFT JOIN public.commande cmd ON ((liv.id_commande = cmd.id_commande)))
     LEFT JOIN public.proforma p ON ((cmd.id_proforma = p.id_proforma)))
     LEFT JOIN public.proforma_detail pd ON (((p.id_proforma = pd.id_proforma) AND (a.id_article = pd.id_article))))
     LEFT JOIN public.fournisseur f ON ((p.id_fournisseur = f.id_fournisseur)))
     LEFT JOIN public.client cl ON ((p.id_client = cl.id_client)))
  WHERE (ms.entrant = true)
  ORDER BY ms.date_;


ALTER TABLE public.v_entree_stock_lot OWNER TO postgres;

--
-- Name: v_etat_stock; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_etat_stock AS
 SELECT ms.id_depot,
    d.nom AS depot,
    a.id_article,
    a.libelle AS article,
    ms.date_ AS date_mouvement,
        CASE
            WHEN ms.entrant THEN msl.qte
            ELSE (- msl.qte)
        END AS variation,
        CASE
            WHEN ms.entrant THEN msl.qte
            ELSE 0
        END AS entree,
        CASE
            WHEN (NOT ms.entrant) THEN msl.qte
            ELSE 0
        END AS sortie
   FROM ((((public.mvt_stock ms
     JOIN public.mvt_stock_lot msl ON ((msl.id_mvt = ms.id_mvt)))
     JOIN public.lot l ON ((l.id_lot = msl.id_lot)))
     JOIN public.article a ON ((a.id_article = l.id_article)))
     JOIN public.depot d ON ((d.id_depot = ms.id_depot)));


ALTER TABLE public.v_etat_stock OWNER TO postgres;

--
-- Name: v_journal_mvt_stock; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_journal_mvt_stock AS
 SELECT ms.id_mvt,
    ms.date_ AS date_mouvement,
    d.id_depot,
    d.nom AS depot,
    ms.entrant,
        CASE
            WHEN ms.entrant THEN 'ENTRÉE'::text
            ELSE 'SORTIE'::text
        END AS type_mouvement,
    ms.designation,
    ms.description_qualite,
    liv.id_livraison,
    liv.date_ AS date_livraison,
    a.id_article,
    a.libelle AS article,
    l.id_lot,
    l.libelle AS lot,
    msl.qte AS quantite
   FROM (((((public.mvt_stock ms
     JOIN public.depot d ON ((d.id_depot = ms.id_depot)))
     JOIN public.mvt_stock_lot msl ON ((msl.id_mvt = ms.id_mvt)))
     JOIN public.lot l ON ((l.id_lot = msl.id_lot)))
     JOIN public.article a ON ((a.id_article = l.id_article)))
     LEFT JOIN public.livraison liv ON ((liv.id_livraison = ms.id_livraison)));


ALTER TABLE public.v_journal_mvt_stock OWNER TO postgres;

--
-- Name: v_lot_cpl; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_lot_cpl AS
 SELECT l.id_lot,
    l.libelle AS lot,
    ms.date_ AS date_mouvement,
        CASE
            WHEN ms.entrant THEN msl.qte
            ELSE 0
        END AS entree,
        CASE
            WHEN (NOT ms.entrant) THEN msl.qte
            ELSE 0
        END AS sortie
   FROM ((public.mvt_stock_lot msl
     JOIN public.mvt_stock ms ON ((ms.id_mvt = msl.id_mvt)))
     JOIN public.lot l ON ((l.id_lot = msl.id_lot)));


ALTER TABLE public.v_lot_cpl OWNER TO postgres;

--
-- Name: v_sortie_stock_lot; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.v_sortie_stock_lot AS
 SELECT msl.id_mvt,
    msl.id_lot,
    msl.qte AS quantite_sortie,
    l.libelle AS libelle_lot,
    l.qte AS stock_restant_lot,
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
   FROM (((((public.mvt_stock_lot msl
     JOIN public.lot l ON ((msl.id_lot = l.id_lot)))
     JOIN public.article a ON ((l.id_article = a.id_article)))
     JOIN public.categorie c ON ((a.id_categorie = c.id_categorie)))
     JOIN public.mvt_stock ms ON ((msl.id_mvt = ms.id_mvt)))
     JOIN public.depot d ON ((ms.id_depot = d.id_depot)))
  WHERE (ms.entrant = false)
  ORDER BY ms.date_;


ALTER TABLE public.v_sortie_stock_lot OWNER TO postgres;

--
-- Name: validation_ajustement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.validation_ajustement (
    id_va integer NOT NULL,
    date_validation timestamp without time zone NOT NULL,
    id_utilisateur integer NOT NULL,
    id_ajustement integer NOT NULL
);


ALTER TABLE public.validation_ajustement OWNER TO postgres;

--
-- Name: validation_ajustement_id_va_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.validation_ajustement_id_va_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.validation_ajustement_id_va_seq OWNER TO postgres;

--
-- Name: validation_ajustement_id_va_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.validation_ajustement_id_va_seq OWNED BY public.validation_ajustement.id_va;


--
-- Name: validation_step; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.validation_step (
    id_validation_step integer NOT NULL,
    entity_type character varying(20) NOT NULL,
    entity_id integer NOT NULL,
    step_number integer NOT NULL,
    id_utilisateur integer NOT NULL,
    validated_at timestamp without time zone NOT NULL
);


ALTER TABLE public.validation_step OWNER TO postgres;

--
-- Name: validation_step_id_validation_step_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.validation_step_id_validation_step_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.validation_step_id_validation_step_seq OWNER TO postgres;

--
-- Name: validation_step_id_validation_step_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.validation_step_id_validation_step_seq OWNED BY public.validation_step.id_validation_step;


--
-- Name: ajustement id_ajustement; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ajustement ALTER COLUMN id_ajustement SET DEFAULT nextval('public.ajustement_id_ajustement_seq'::regclass);


--
-- Name: article id_article; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article ALTER COLUMN id_article SET DEFAULT nextval('public.article_id_article_seq'::regclass);


--
-- Name: caisse id_caisse; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caisse ALTER COLUMN id_caisse SET DEFAULT nextval('public.caisse_id_caisse_seq'::regclass);


--
-- Name: categorie id_categorie; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorie ALTER COLUMN id_categorie SET DEFAULT nextval('public.categorie_id_categorie_seq'::regclass);


--
-- Name: client id_client; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN id_client SET DEFAULT nextval('public.client_id_client_seq'::regclass);


--
-- Name: commande id_commande; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande ALTER COLUMN id_commande SET DEFAULT nextval('public.commande_id_commande_seq'::regclass);


--
-- Name: conf_va id_conf_va; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conf_va ALTER COLUMN id_conf_va SET DEFAULT nextval('public.conf_va_id_conf_va_seq'::regclass);


--
-- Name: demande_achat id_da; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat ALTER COLUMN id_da SET DEFAULT nextval('public.demande_achat_id_da_seq'::regclass);


--
-- Name: demande_ajustement id_demande_ajustement; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_ajustement ALTER COLUMN id_demande_ajustement SET DEFAULT nextval('public.demande_ajustement_id_demande_ajustement_seq'::regclass);


--
-- Name: depot id_depot; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.depot ALTER COLUMN id_depot SET DEFAULT nextval('public.depot_id_depot_seq'::regclass);


--
-- Name: dept id_dept; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dept ALTER COLUMN id_dept SET DEFAULT nextval('public.dept_id_dept_seq'::regclass);


--
-- Name: etat id_etat; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.etat ALTER COLUMN id_etat SET DEFAULT nextval('public.etat_id_etat_seq'::regclass);


--
-- Name: fournisseur id_fournisseur; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fournisseur ALTER COLUMN id_fournisseur SET DEFAULT nextval('public.fournisseur_id_fournisseur_seq'::regclass);


--
-- Name: historique_general id_hg; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historique_general ALTER COLUMN id_hg SET DEFAULT nextval('public.historique_general_id_hg_seq'::regclass);


--
-- Name: inventaire id_inventaire; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire ALTER COLUMN id_inventaire SET DEFAULT nextval('public.inventaire_id_inventaire_seq'::regclass);


--
-- Name: inventaire_detail id_inventaire_detail; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire_detail ALTER COLUMN id_inventaire_detail SET DEFAULT nextval('public.inventaire_detail_id_inventaire_detail_seq'::regclass);


--
-- Name: livraison id_livraison; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livraison ALTER COLUMN id_livraison SET DEFAULT nextval('public.livraison_id_livraison_seq'::regclass);


--
-- Name: lot id_lot; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lot ALTER COLUMN id_lot SET DEFAULT nextval('public.lot_id_lot_seq'::regclass);


--
-- Name: mvt_caisse id_mvtc; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_caisse ALTER COLUMN id_mvtc SET DEFAULT nextval('public.mvt_caisse_id_mvtc_seq'::regclass);


--
-- Name: mvt_stock id_mvt; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock ALTER COLUMN id_mvt SET DEFAULT nextval('public.mvt_stock_id_mvt_seq'::regclass);


--
-- Name: paiement id_paiement; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement ALTER COLUMN id_paiement SET DEFAULT nextval('public.paiement_id_paiement_seq'::regclass);


--
-- Name: proforma id_proforma; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma ALTER COLUMN id_proforma SET DEFAULT nextval('public.proforma_id_proforma_seq'::regclass);


--
-- Name: role id_role; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id_role SET DEFAULT nextval('public.role_id_role_seq'::regclass);


--
-- Name: transfert id_transfert; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transfert ALTER COLUMN id_transfert SET DEFAULT nextval('public.transfert_id_transfert_seq'::regclass);


--
-- Name: utilisateur id_utilisateur; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur ALTER COLUMN id_utilisateur SET DEFAULT nextval('public.utilisateur_id_utilisateur_seq'::regclass);


--
-- Name: validation_ajustement id_va; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_ajustement ALTER COLUMN id_va SET DEFAULT nextval('public.validation_ajustement_id_va_seq'::regclass);


--
-- Name: validation_step id_validation_step; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_step ALTER COLUMN id_validation_step SET DEFAULT nextval('public.validation_step_id_validation_step_seq'::regclass);


--
-- Data for Name: ajustement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ajustement (id_ajustement, date_ajurstement, id_mvt, id_inventaire) FROM stdin;
\.


--
-- Data for Name: article; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.article (id_article, libelle, id_categorie) FROM stdin;
1	Laptop HP EliteBook	1
2	Laptop Dell XPS 15	1
3	Ecran Samsung 27"	1
4	Clavier Logitech MX	1
5	Souris Gaming Razer	1
6	iPhone 15 Pro	2
7	Samsung Galaxy S24	2
8	Tablette iPad Air	2
9	Huawei P60	2
10	TV LG OLED 55"	3
11	Casque Sony WH-1000XM5	3
12	Barre de son Bose	3
13	Enceinte JBL Flip	3
14	Refrigerateur Samsung	4
15	Machine a laver LG	4
16	Climatiseur Daikin	4
17	PlayStation 5	5
18	Xbox Series X	5
19	Nintendo Switch	5
20	Manette PS5 DualSense	5
21	Cable HDMI 2m	6
22	Chargeur USB-C	6
23	Coque iPhone	6
24	Support laptop	6
\.


--
-- Data for Name: caisse; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.caisse (id_caisse, lieu) FROM stdin;
1	Caisse Principale
2	Caisse Ankorondrano
3	Caisse Analakely
\.


--
-- Data for Name: categorie; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categorie (id_categorie, libelle) FROM stdin;
1	Informatique
2	Telephonie
3	Audio-Video
4	Electromenager
5	Gaming
6	Accessoires
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (id_client, nom) FROM stdin;
1	Entreprise Digitale SA
2	Tech Solutions SARL
3	Gaming House
4	Bureau Plus
5	Telecom Services
\.


--
-- Data for Name: commande; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commande (id_commande, date_, remise, id_proforma) FROM stdin;
1	2026-02-02 14:31:00	\N	1
2	2026-02-02 14:43:00	\N	2
3	2026-02-02 14:58:00	\N	3
4	2026-01-01 15:16:00	\N	4
5	2026-01-02 15:18:00	\N	5
\.


--
-- Data for Name: commande_etat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commande_etat (id_commande, id_etat, date_) FROM stdin;
1	1	2026-02-02 14:31:00
1	2	2026-02-02 14:32:20.418181
2	1	2026-02-02 14:43:00
2	2	2026-02-02 14:47:49.480834
3	1	2026-02-02 14:58:00
3	2	2026-02-02 14:59:19.683459
4	1	2026-01-01 15:16:00
4	2	2026-02-02 15:17:49.304398
5	1	2026-01-02 15:18:00
5	2	2026-02-02 15:20:10.927737
\.


--
-- Data for Name: conf_va; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.conf_va (id_conf_va, niveau_1, niveau_2, libelle) FROM stdin;
1	7	10	proforma
2	7	10	commande
\.


--
-- Data for Name: demande_achat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.demande_achat (id_da, date_demande, id_client) FROM stdin;
1	2026-02-01	\N
2	2026-02-02	2
3	2026-01-26	\N
4	2026-01-14	\N
\.


--
-- Data for Name: demande_achat_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.demande_achat_detail (id_article, id_da, quantite) FROM stdin;
1	1	100
2	1	50
6	1	60
6	2	70
8	3	500
6	3	100
7	3	550
10	4	7000000
11	4	1500000
\.


--
-- Data for Name: demande_ajustement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.demande_ajustement (id_demande_ajustement, date_demande, id_utilisateur, id_inventaire) FROM stdin;
\.


--
-- Data for Name: depot; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.depot (id_depot, nom, capacite) FROM stdin;
1	Depot Central Tana	100
2	Depot Ankorondrano	100
3	Depot Analakely	100
\.


--
-- Data for Name: dept; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dept (id_dept, nom) FROM stdin;
1	Ventes
2	Finance
3	Logistique
4	Direction
\.


--
-- Data for Name: etat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.etat (id_etat, libelle) FROM stdin;
1	Cree
2	Valide
3	Commande
4	Livre
5	Annule
\.


--
-- Data for Name: fournisseur; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.fournisseur (id_fournisseur, nom) FROM stdin;
1	TechImport SARL
2	ElectroDistrib SA
3	GameZone Distribution
4	PhonePro Madagascar
\.


--
-- Data for Name: historique_general; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.historique_general (id_hg, date_historique, nom_table, desc_, id, id_utilisateur) FROM stdin;
1	2026-02-02 14:31:15.432554	demande_achat	Création DA	1	12
2	2026-02-02 14:31:49.794516	proforma	Création proforma	1	12
3	2026-02-02 14:31:51.337819	validation_step	Validation proforma etape 1	1	12
4	2026-02-02 14:32:13.405421	validation_step	Validation proforma etape 2	1	6
5	2026-02-02 14:32:13.405421	proforma_etat	Proforma entierement valide	1	6
6	2026-02-02 14:31:00	commande	Création commande	1	6
7	2026-02-02 14:32:17.544407	validation_step	Validation commande etape 1	1	6
8	2026-02-02 14:32:20.418181	validation_step	Validation commande etape 2	1	12
9	2026-02-02 14:32:20.418181	commande_etat	Commande entierement validee	1	12
10	2026-02-02 14:32:24.722464	paiement	Paiement commande	1	12
11	2026-02-02 14:31:00	livraison	Livraison commande	1	12
12	2026-02-02 14:45:16.364557	proforma	Création proforma	2	12
13	2026-02-02 14:47:08.87637	validation_step	Validation proforma etape 1	2	12
14	2026-02-02 14:47:28.721119	validation_step	Validation proforma etape 2	2	6
15	2026-02-02 14:47:28.721119	proforma_etat	Proforma entierement valide	2	6
16	2026-02-02 14:43:00	commande	Création commande	2	6
17	2026-02-02 14:47:39.792574	paiement	Paiement commande	2	6
18	2026-02-02 14:47:42.720886	validation_step	Validation commande etape 1	2	6
19	2026-02-02 14:47:49.480834	validation_step	Validation commande etape 2	2	12
20	2026-02-02 14:47:49.480834	commande_etat	Commande entierement validee	2	12
21	2026-02-02 14:43:00	livraison	Livraison commande	2	6
22	2026-02-02 14:58:41.442873	demande_achat	Création DA	2	12
23	2026-02-02 14:59:05.492855	proforma	Création proforma	3	12
24	2026-02-02 14:59:07.009265	validation_step	Validation proforma etape 1	3	12
25	2026-02-02 14:59:12.54464	validation_step	Validation proforma etape 2	3	6
26	2026-02-02 14:59:12.54464	proforma_etat	Proforma entierement valide	3	6
27	2026-02-02 14:58:00	commande	Création commande	3	6
28	2026-02-02 14:59:16.301861	validation_step	Validation commande etape 1	3	6
29	2026-02-02 14:59:19.683459	validation_step	Validation commande etape 2	3	12
30	2026-02-02 14:59:19.683459	commande_etat	Commande entierement validee	3	12
31	2026-02-02 14:59:24.927427	paiement	Paiement commande	3	12
32	2026-02-02 14:58:00	livraison	Livraison commande	3	12
33	2026-02-02 15:08:49.00406	demande_achat	Création DA	3	12
34	2026-02-02 15:17:10.796699	proforma	Création proforma	4	12
35	2026-02-02 15:17:12.535306	validation_step	Validation proforma etape 1	4	12
36	2026-02-02 15:17:34.044535	validation_step	Validation proforma etape 2	4	6
37	2026-02-02 15:17:34.044535	proforma_etat	Proforma entierement valide	4	6
38	2026-01-01 15:16:00	commande	Création commande	4	6
39	2026-02-02 15:17:45.751219	validation_step	Validation commande etape 1	4	6
40	2026-02-02 15:17:49.304398	validation_step	Validation commande etape 2	4	12
41	2026-02-02 15:17:49.304398	commande_etat	Commande entierement validee	4	12
42	2026-02-02 15:17:55.32146	paiement	Paiement commande	4	12
43	2026-01-01 15:16:00	livraison	Livraison commande	4	6
44	2026-02-02 15:18:27.695962	demande_achat	Création DA	4	6
45	2026-02-02 15:19:08.091428	proforma	Création proforma	5	6
46	2026-02-02 15:19:10.377292	validation_step	Validation proforma etape 1	5	6
47	2026-02-02 15:19:50.952707	validation_step	Validation proforma etape 2	5	12
48	2026-02-02 15:19:50.952707	proforma_etat	Proforma entierement valide	5	12
49	2026-01-02 15:18:00	commande	Création commande	5	12
50	2026-02-02 15:19:54.832788	validation_step	Validation commande etape 1	5	12
51	2026-02-02 15:20:04.109741	paiement	Paiement commande	5	12
52	2026-02-02 15:20:10.927737	validation_step	Validation commande etape 2	5	6
53	2026-02-02 15:20:10.927737	commande_etat	Commande entierement validee	5	6
54	2026-01-02 15:18:00	livraison	Livraison commande	5	6
\.


--
-- Data for Name: inventaire; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventaire (id_inventaire, date_inventaire) FROM stdin;
\.


--
-- Data for Name: inventaire_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inventaire_detail (id_inventaire_detail, nombre, id_depot, id_article, id_inventaire) FROM stdin;
\.


--
-- Data for Name: livraison; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.livraison (id_livraison, date_, id_commande) FROM stdin;
1	2026-02-02 14:31:00	1
2	2026-02-02 14:43:00	2
3	2026-02-02 14:58:00	3
4	2026-01-01 15:16:00	4
5	2026-01-02 15:18:00	5
\.


--
-- Data for Name: lot; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lot (id_lot, libelle, qte, qte_initiale, id_article) FROM stdin;
1	Laptop HP EliteBook - Lot automatique	100	100	1
2	Laptop Dell XPS 15 - Lot automatique	50	50	2
4	Laptop HP EliteBook - Lot automatique	100	100	1
5	Laptop Dell XPS 15 - Lot automatique	50	50	2
3	iPhone 15 Pro - Lot automatique	0	60	6
6	iPhone 15 Pro - Lot automatique	10	20	6
7	iPhone 15 Pro - Lot automatique	100	100	6
8	Samsung Galaxy S24 - Lot automatique	550	550	7
9	Tablette iPad Air - Lot automatique	500	500	8
10	TV LG OLED 55" - Lot automatique	700	700	10
11	Casque Sony WH-1000XM5 - Lot automatique	150	150	11
\.


--
-- Data for Name: mvt_caisse; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mvt_caisse (id_mvtc, debit, credit, date_, id_paiement, id_caisse) FROM stdin;
1	775000000.00	0.00	2026-02-02 14:32:24.728346	1	1
2	335000000.00	0.00	2026-02-02 14:47:39.804973	2	1
3	0.00	385000000.00	2026-02-02 14:59:24.932811	3	1
4	7025000000.00	0.00	2026-02-02 15:17:55.328848	4	2
5	5087500000.00	0.00	2026-02-02 15:20:04.11298	5	2
\.


--
-- Data for Name: mvt_stock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mvt_stock (id_mvt, date_, entrant, description_qualite, designation, id_livraison, id_depot) FROM stdin;
1	2026-02-02 00:00:00	t	OK	\N	1	1
2	2026-02-02 00:00:00	t	OK	\N	2	1
3	2026-02-02 00:00:00	f	OK	\N	3	1
4	2026-01-01 00:00:00	t	OK	\N	4	2
5	2026-01-02 00:00:00	t	OK	\N	5	2
\.


--
-- Data for Name: mvt_stock_lot; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mvt_stock_lot (id_mvt, id_lot, qte) FROM stdin;
1	1	100
1	2	50
1	3	60
2	4	100
2	5	50
2	6	20
3	3	60
3	6	10
4	7	100
4	8	550
4	9	500
5	10	700
5	11	150
\.


--
-- Data for Name: paiement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.paiement (id_paiement, montant, date_, id_commande) FROM stdin;
1	775000000.00	2026-02-02 11:31:00	1
2	335000000.00	2026-02-02 11:43:00	2
3	385000000.00	2026-02-02 11:58:00	3
4	7025000000.00	2026-01-01 12:16:00	4
5	5087500000.00	2026-01-02 12:18:00	5
\.


--
-- Data for Name: proforma; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proforma (id_proforma, date_debut, date_fin, id_da, id_client, id_fournisseur) FROM stdin;
1	2026-02-02 14:31:00	2026-02-08 14:31:00	1	\N	1
2	2026-02-02 14:43:00	2026-02-08 14:43:00	1	\N	2
3	2026-02-02 14:58:00	2026-02-08 14:58:00	2	2	\N
4	2026-01-01 15:16:00	2026-03-21 15:16:00	3	\N	4
5	2026-01-02 15:18:00	2026-01-31 15:18:00	4	\N	2
\.


--
-- Data for Name: proforma_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proforma_detail (id_article, id_proforma, prix, quantite) FROM stdin;
1	1	3000000.00	100
2	1	3500000.00	50
6	1	5000000.00	60
1	2	1000000.00	100
2	2	1500000.00	50
6	2	8000000.00	20
6	3	5500000.00	70
6	4	6000000.00	100
7	4	7500000.00	550
8	4	4600000.00	500
10	5	7000000.00	700
11	5	1250000.00	150
\.


--
-- Data for Name: proforma_etat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proforma_etat (id_proforma, id_etat, date_) FROM stdin;
1	1	2026-02-02 14:31:49.794516
1	2	2026-02-02 14:32:13.405421
2	1	2026-02-02 14:45:16.364557
2	2	2026-02-02 14:47:28.721119
3	1	2026-02-02 14:59:05.492855
3	2	2026-02-02 14:59:12.54464
4	1	2026-02-02 15:17:10.796699
4	2	2026-02-02 15:17:34.044535
5	1	2026-02-02 15:19:08.091428
5	2	2026-02-02 15:19:50.952707
\.


--
-- Data for Name: restriction_categorie; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.restriction_categorie (id_categorie, id_utilisateur) FROM stdin;
1	13
2	14
5	15
6	15
\.


--
-- Data for Name: restriction_fournisseur; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.restriction_fournisseur (id_fournisseur, id_utilisateur) FROM stdin;
4	14
3	15
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id_role, nom, niveau, seuil) FROM stdin;
1	Stagiaire	1	0.00
2	Magasinier	3	50000.00
3	Vendeur	3	100000.00
4	Vendeur Senior	5	250000.00
5	Manager	7	500000.00
6	Chef Equipe	10	750000.00
7	Chef Dept	12	1000000.00
8	Directeur	15	5000000.00
\.


--
-- Data for Name: transfert; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transfert (id_transfert, date_transfert, date_validation, id_utilisateur, mvt_cible, mvt_origine) FROM stdin;
\.


--
-- Data for Name: utilisateur; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utilisateur (id_utilisateur, nom, date_naissance, date_embauche, id_depot, id_role, id_dept) FROM stdin;
1	stg_vt_rabe	2000-05-15	2025-01-01	1	1	1
2	vdr_vt_rakoto	1995-03-20	2023-06-01	1	3	1
3	vds_vt_rasoa	1990-08-10	2021-04-15	2	4	1
4	mgr_vt_andry	1985-12-25	2018-02-01	1	5	1
5	cde_vt_lova	1983-09-14	2016-05-20	1	6	1
6	cdp_vt_hery	1980-01-30	2015-01-10	\N	7	1
7	mag_fi_jean	1992-07-14	2022-03-01	1	2	2
8	mgr_fi_marie	1988-11-20	2019-08-15	2	5	2
9	cdp_fi_paul	1975-04-05	2010-05-20	\N	7	2
10	mag_lg_fidy	1993-09-12	2022-01-15	1	2	3
11	mgr_lg_tiana	1986-06-08	2017-11-01	2	5	3
12	dir_all_boss	1970-02-28	2005-01-01	\N	8	4
13	vdr_vt_info	1994-04-18	2023-08-01	1	3	1
14	vdr_vt_phone	1996-10-22	2024-01-15	2	3	1
15	mgr_vt_game	1991-12-01	2020-05-10	1	5	1
\.


--
-- Data for Name: validation_ajustement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.validation_ajustement (id_va, date_validation, id_utilisateur, id_ajustement) FROM stdin;
\.


--
-- Data for Name: validation_step; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.validation_step (id_validation_step, entity_type, entity_id, step_number, id_utilisateur, validated_at) FROM stdin;
1	proforma	1	1	12	2026-02-02 14:31:51.337819
2	proforma	1	2	6	2026-02-02 14:32:13.405421
3	commande	1	1	6	2026-02-02 14:32:17.544407
4	commande	1	2	12	2026-02-02 14:32:20.418181
5	proforma	2	1	12	2026-02-02 14:47:08.87637
6	proforma	2	2	6	2026-02-02 14:47:28.721119
7	commande	2	1	6	2026-02-02 14:47:42.720886
8	commande	2	2	12	2026-02-02 14:47:49.480834
9	proforma	3	1	12	2026-02-02 14:59:07.009265
10	proforma	3	2	6	2026-02-02 14:59:12.54464
11	commande	3	1	6	2026-02-02 14:59:16.301861
12	commande	3	2	12	2026-02-02 14:59:19.683459
13	proforma	4	1	12	2026-02-02 15:17:12.535306
14	proforma	4	2	6	2026-02-02 15:17:34.044535
15	commande	4	1	6	2026-02-02 15:17:45.751219
16	commande	4	2	12	2026-02-02 15:17:49.304398
17	proforma	5	1	6	2026-02-02 15:19:10.377292
18	proforma	5	2	12	2026-02-02 15:19:50.952707
19	commande	5	1	12	2026-02-02 15:19:54.832788
20	commande	5	2	6	2026-02-02 15:20:10.927737
\.


--
-- Name: ajustement_id_ajustement_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ajustement_id_ajustement_seq', 1, false);


--
-- Name: article_id_article_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.article_id_article_seq', 1, false);


--
-- Name: caisse_id_caisse_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.caisse_id_caisse_seq', 1, false);


--
-- Name: categorie_id_categorie_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categorie_id_categorie_seq', 1, false);


--
-- Name: client_id_client_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_id_client_seq', 1, false);


--
-- Name: commande_id_commande_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commande_id_commande_seq', 5, true);


--
-- Name: conf_va_id_conf_va_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.conf_va_id_conf_va_seq', 1, false);


--
-- Name: demande_achat_id_da_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.demande_achat_id_da_seq', 4, true);


--
-- Name: demande_ajustement_id_demande_ajustement_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.demande_ajustement_id_demande_ajustement_seq', 1, false);


--
-- Name: depot_id_depot_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.depot_id_depot_seq', 1, false);


--
-- Name: dept_id_dept_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.dept_id_dept_seq', 1, false);


--
-- Name: etat_id_etat_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.etat_id_etat_seq', 1, false);


--
-- Name: fournisseur_id_fournisseur_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.fournisseur_id_fournisseur_seq', 1, false);


--
-- Name: historique_general_id_hg_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.historique_general_id_hg_seq', 54, true);


--
-- Name: inventaire_detail_id_inventaire_detail_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.inventaire_detail_id_inventaire_detail_seq', 1, false);


--
-- Name: inventaire_id_inventaire_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.inventaire_id_inventaire_seq', 1, false);


--
-- Name: livraison_id_livraison_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.livraison_id_livraison_seq', 5, true);


--
-- Name: lot_id_lot_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.lot_id_lot_seq', 11, true);


--
-- Name: mvt_caisse_id_mvtc_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.mvt_caisse_id_mvtc_seq', 5, true);


--
-- Name: mvt_stock_id_mvt_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.mvt_stock_id_mvt_seq', 5, true);


--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paiement_id_paiement_seq', 5, true);


--
-- Name: proforma_id_proforma_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.proforma_id_proforma_seq', 5, true);


--
-- Name: role_id_role_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_role_seq', 1, false);


--
-- Name: transfert_id_transfert_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transfert_id_transfert_seq', 1, false);


--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utilisateur_id_utilisateur_seq', 1, false);


--
-- Name: validation_ajustement_id_va_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.validation_ajustement_id_va_seq', 1, false);


--
-- Name: validation_step_id_validation_step_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.validation_step_id_validation_step_seq', 20, true);


--
-- Name: ajustement ajustement_id_mvt_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ajustement
    ADD CONSTRAINT ajustement_id_mvt_key UNIQUE (id_mvt);


--
-- Name: ajustement ajustement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ajustement
    ADD CONSTRAINT ajustement_pkey PRIMARY KEY (id_ajustement);


--
-- Name: article article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_pkey PRIMARY KEY (id_article);


--
-- Name: caisse caisse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caisse
    ADD CONSTRAINT caisse_pkey PRIMARY KEY (id_caisse);


--
-- Name: categorie categorie_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorie
    ADD CONSTRAINT categorie_pkey PRIMARY KEY (id_categorie);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id_client);


--
-- Name: commande_etat commande_etat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande_etat
    ADD CONSTRAINT commande_etat_pkey PRIMARY KEY (id_commande, id_etat);


--
-- Name: commande commande_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande
    ADD CONSTRAINT commande_pkey PRIMARY KEY (id_commande);


--
-- Name: conf_va conf_va_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.conf_va
    ADD CONSTRAINT conf_va_pkey PRIMARY KEY (id_conf_va);


--
-- Name: demande_achat_detail demande_achat_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat_detail
    ADD CONSTRAINT demande_achat_detail_pkey PRIMARY KEY (id_article, id_da);


--
-- Name: demande_achat demande_achat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat
    ADD CONSTRAINT demande_achat_pkey PRIMARY KEY (id_da);


--
-- Name: demande_ajustement demande_ajustement_id_inventaire_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_ajustement
    ADD CONSTRAINT demande_ajustement_id_inventaire_key UNIQUE (id_inventaire);


--
-- Name: demande_ajustement demande_ajustement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_ajustement
    ADD CONSTRAINT demande_ajustement_pkey PRIMARY KEY (id_demande_ajustement);


--
-- Name: depot depot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.depot
    ADD CONSTRAINT depot_pkey PRIMARY KEY (id_depot);


--
-- Name: dept dept_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dept
    ADD CONSTRAINT dept_pkey PRIMARY KEY (id_dept);


--
-- Name: etat etat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.etat
    ADD CONSTRAINT etat_pkey PRIMARY KEY (id_etat);


--
-- Name: fournisseur fournisseur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fournisseur
    ADD CONSTRAINT fournisseur_pkey PRIMARY KEY (id_fournisseur);


--
-- Name: historique_general historique_general_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historique_general
    ADD CONSTRAINT historique_general_pkey PRIMARY KEY (id_hg);


--
-- Name: inventaire_detail inventaire_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire_detail
    ADD CONSTRAINT inventaire_detail_pkey PRIMARY KEY (id_inventaire_detail);


--
-- Name: inventaire inventaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire
    ADD CONSTRAINT inventaire_pkey PRIMARY KEY (id_inventaire);


--
-- Name: livraison livraison_id_commande_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livraison
    ADD CONSTRAINT livraison_id_commande_key UNIQUE (id_commande);


--
-- Name: livraison livraison_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livraison
    ADD CONSTRAINT livraison_pkey PRIMARY KEY (id_livraison);


--
-- Name: lot lot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lot
    ADD CONSTRAINT lot_pkey PRIMARY KEY (id_lot);


--
-- Name: mvt_caisse mvt_caisse_id_paiement_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_caisse
    ADD CONSTRAINT mvt_caisse_id_paiement_key UNIQUE (id_paiement);


--
-- Name: mvt_caisse mvt_caisse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_caisse
    ADD CONSTRAINT mvt_caisse_pkey PRIMARY KEY (id_mvtc);


--
-- Name: mvt_stock mvt_stock_id_livraison_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock
    ADD CONSTRAINT mvt_stock_id_livraison_key UNIQUE (id_livraison);


--
-- Name: mvt_stock_lot mvt_stock_lot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock_lot
    ADD CONSTRAINT mvt_stock_lot_pkey PRIMARY KEY (id_mvt, id_lot);


--
-- Name: mvt_stock mvt_stock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock
    ADD CONSTRAINT mvt_stock_pkey PRIMARY KEY (id_mvt);


--
-- Name: paiement paiement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_pkey PRIMARY KEY (id_paiement);


--
-- Name: proforma_detail proforma_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_detail
    ADD CONSTRAINT proforma_detail_pkey PRIMARY KEY (id_article, id_proforma);


--
-- Name: proforma_etat proforma_etat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_etat
    ADD CONSTRAINT proforma_etat_pkey PRIMARY KEY (id_proforma, id_etat);


--
-- Name: proforma proforma_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma
    ADD CONSTRAINT proforma_pkey PRIMARY KEY (id_proforma);


--
-- Name: restriction_categorie restriction_categorie_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_categorie
    ADD CONSTRAINT restriction_categorie_pkey PRIMARY KEY (id_categorie, id_utilisateur);


--
-- Name: restriction_fournisseur restriction_fournisseur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_fournisseur
    ADD CONSTRAINT restriction_fournisseur_pkey PRIMARY KEY (id_fournisseur, id_utilisateur);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id_role);


--
-- Name: transfert transfert_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transfert
    ADD CONSTRAINT transfert_pkey PRIMARY KEY (id_transfert);


--
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id_utilisateur);


--
-- Name: validation_ajustement validation_ajustement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_ajustement
    ADD CONSTRAINT validation_ajustement_pkey PRIMARY KEY (id_va);


--
-- Name: validation_step validation_step_entity_type_entity_id_step_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_step
    ADD CONSTRAINT validation_step_entity_type_entity_id_step_number_key UNIQUE (entity_type, entity_id, step_number);


--
-- Name: validation_step validation_step_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_step
    ADD CONSTRAINT validation_step_pkey PRIMARY KEY (id_validation_step);


--
-- Name: idx_validation_step_entity; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_validation_step_entity ON public.validation_step USING btree (entity_type, entity_id);


--
-- Name: ajustement ajustement_id_inventaire_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ajustement
    ADD CONSTRAINT ajustement_id_inventaire_fkey FOREIGN KEY (id_inventaire) REFERENCES public.inventaire(id_inventaire);


--
-- Name: ajustement ajustement_id_mvt_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ajustement
    ADD CONSTRAINT ajustement_id_mvt_fkey FOREIGN KEY (id_mvt) REFERENCES public.mvt_stock(id_mvt);


--
-- Name: article article_id_categorie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_id_categorie_fkey FOREIGN KEY (id_categorie) REFERENCES public.categorie(id_categorie);


--
-- Name: commande_etat commande_etat_id_commande_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande_etat
    ADD CONSTRAINT commande_etat_id_commande_fkey FOREIGN KEY (id_commande) REFERENCES public.commande(id_commande);


--
-- Name: commande_etat commande_etat_id_etat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande_etat
    ADD CONSTRAINT commande_etat_id_etat_fkey FOREIGN KEY (id_etat) REFERENCES public.etat(id_etat);


--
-- Name: commande commande_id_proforma_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.commande
    ADD CONSTRAINT commande_id_proforma_fkey FOREIGN KEY (id_proforma) REFERENCES public.proforma(id_proforma);


--
-- Name: demande_achat_detail demande_achat_detail_id_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat_detail
    ADD CONSTRAINT demande_achat_detail_id_article_fkey FOREIGN KEY (id_article) REFERENCES public.article(id_article);


--
-- Name: demande_achat_detail demande_achat_detail_id_da_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat_detail
    ADD CONSTRAINT demande_achat_detail_id_da_fkey FOREIGN KEY (id_da) REFERENCES public.demande_achat(id_da);


--
-- Name: demande_achat demande_achat_id_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_achat
    ADD CONSTRAINT demande_achat_id_client_fkey FOREIGN KEY (id_client) REFERENCES public.client(id_client);


--
-- Name: demande_ajustement demande_ajustement_id_inventaire_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_ajustement
    ADD CONSTRAINT demande_ajustement_id_inventaire_fkey FOREIGN KEY (id_inventaire) REFERENCES public.inventaire(id_inventaire);


--
-- Name: demande_ajustement demande_ajustement_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.demande_ajustement
    ADD CONSTRAINT demande_ajustement_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: historique_general historique_general_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.historique_general
    ADD CONSTRAINT historique_general_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: inventaire_detail inventaire_detail_id_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire_detail
    ADD CONSTRAINT inventaire_detail_id_article_fkey FOREIGN KEY (id_article) REFERENCES public.article(id_article);


--
-- Name: inventaire_detail inventaire_detail_id_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire_detail
    ADD CONSTRAINT inventaire_detail_id_depot_fkey FOREIGN KEY (id_depot) REFERENCES public.depot(id_depot);


--
-- Name: inventaire_detail inventaire_detail_id_inventaire_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inventaire_detail
    ADD CONSTRAINT inventaire_detail_id_inventaire_fkey FOREIGN KEY (id_inventaire) REFERENCES public.inventaire(id_inventaire);


--
-- Name: livraison livraison_id_commande_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.livraison
    ADD CONSTRAINT livraison_id_commande_fkey FOREIGN KEY (id_commande) REFERENCES public.commande(id_commande);


--
-- Name: lot lot_id_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lot
    ADD CONSTRAINT lot_id_article_fkey FOREIGN KEY (id_article) REFERENCES public.article(id_article);


--
-- Name: mvt_caisse mvt_caisse_id_caisse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_caisse
    ADD CONSTRAINT mvt_caisse_id_caisse_fkey FOREIGN KEY (id_caisse) REFERENCES public.caisse(id_caisse);


--
-- Name: mvt_caisse mvt_caisse_id_paiement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_caisse
    ADD CONSTRAINT mvt_caisse_id_paiement_fkey FOREIGN KEY (id_paiement) REFERENCES public.paiement(id_paiement);


--
-- Name: mvt_stock mvt_stock_id_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock
    ADD CONSTRAINT mvt_stock_id_depot_fkey FOREIGN KEY (id_depot) REFERENCES public.depot(id_depot);


--
-- Name: mvt_stock mvt_stock_id_livraison_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock
    ADD CONSTRAINT mvt_stock_id_livraison_fkey FOREIGN KEY (id_livraison) REFERENCES public.livraison(id_livraison);


--
-- Name: mvt_stock_lot mvt_stock_lot_id_lot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock_lot
    ADD CONSTRAINT mvt_stock_lot_id_lot_fkey FOREIGN KEY (id_lot) REFERENCES public.lot(id_lot);


--
-- Name: mvt_stock_lot mvt_stock_lot_id_mvt_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mvt_stock_lot
    ADD CONSTRAINT mvt_stock_lot_id_mvt_fkey FOREIGN KEY (id_mvt) REFERENCES public.mvt_stock(id_mvt);


--
-- Name: paiement paiement_id_commande_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_id_commande_fkey FOREIGN KEY (id_commande) REFERENCES public.commande(id_commande);


--
-- Name: proforma_detail proforma_detail_id_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_detail
    ADD CONSTRAINT proforma_detail_id_article_fkey FOREIGN KEY (id_article) REFERENCES public.article(id_article);


--
-- Name: proforma_detail proforma_detail_id_proforma_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_detail
    ADD CONSTRAINT proforma_detail_id_proforma_fkey FOREIGN KEY (id_proforma) REFERENCES public.proforma(id_proforma);


--
-- Name: proforma_etat proforma_etat_id_etat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_etat
    ADD CONSTRAINT proforma_etat_id_etat_fkey FOREIGN KEY (id_etat) REFERENCES public.etat(id_etat);


--
-- Name: proforma_etat proforma_etat_id_proforma_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma_etat
    ADD CONSTRAINT proforma_etat_id_proforma_fkey FOREIGN KEY (id_proforma) REFERENCES public.proforma(id_proforma);


--
-- Name: proforma proforma_id_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma
    ADD CONSTRAINT proforma_id_client_fkey FOREIGN KEY (id_client) REFERENCES public.client(id_client);


--
-- Name: proforma proforma_id_da_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma
    ADD CONSTRAINT proforma_id_da_fkey FOREIGN KEY (id_da) REFERENCES public.demande_achat(id_da);


--
-- Name: proforma proforma_id_fournisseur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proforma
    ADD CONSTRAINT proforma_id_fournisseur_fkey FOREIGN KEY (id_fournisseur) REFERENCES public.fournisseur(id_fournisseur);


--
-- Name: restriction_categorie restriction_categorie_id_categorie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_categorie
    ADD CONSTRAINT restriction_categorie_id_categorie_fkey FOREIGN KEY (id_categorie) REFERENCES public.categorie(id_categorie);


--
-- Name: restriction_categorie restriction_categorie_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_categorie
    ADD CONSTRAINT restriction_categorie_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: restriction_fournisseur restriction_fournisseur_id_fournisseur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_fournisseur
    ADD CONSTRAINT restriction_fournisseur_id_fournisseur_fkey FOREIGN KEY (id_fournisseur) REFERENCES public.fournisseur(id_fournisseur);


--
-- Name: restriction_fournisseur restriction_fournisseur_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restriction_fournisseur
    ADD CONSTRAINT restriction_fournisseur_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: transfert transfert_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transfert
    ADD CONSTRAINT transfert_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: transfert transfert_mvt_cible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transfert
    ADD CONSTRAINT transfert_mvt_cible_fkey FOREIGN KEY (mvt_cible) REFERENCES public.mvt_stock(id_mvt);


--
-- Name: transfert transfert_mvt_origine_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transfert
    ADD CONSTRAINT transfert_mvt_origine_fkey FOREIGN KEY (mvt_origine) REFERENCES public.mvt_stock(id_mvt);


--
-- Name: utilisateur utilisateur_id_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_id_depot_fkey FOREIGN KEY (id_depot) REFERENCES public.depot(id_depot);


--
-- Name: utilisateur utilisateur_id_dept_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_id_dept_fkey FOREIGN KEY (id_dept) REFERENCES public.dept(id_dept);


--
-- Name: utilisateur utilisateur_id_role_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_id_role_fkey FOREIGN KEY (id_role) REFERENCES public.role(id_role);


--
-- Name: validation_ajustement validation_ajustement_id_ajustement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_ajustement
    ADD CONSTRAINT validation_ajustement_id_ajustement_fkey FOREIGN KEY (id_ajustement) REFERENCES public.ajustement(id_ajustement);


--
-- Name: validation_ajustement validation_ajustement_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_ajustement
    ADD CONSTRAINT validation_ajustement_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: validation_step validation_step_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.validation_step
    ADD CONSTRAINT validation_step_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- PostgreSQL database dump complete
--

