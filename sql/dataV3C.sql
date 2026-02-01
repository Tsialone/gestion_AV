\c gestion_db;

-- ============================================
-- VALIDATION STEP TABLE
-- For multi-step hierarchical validation
-- ============================================

-- Table to track each validation step for proforma/commande
CREATE TABLE IF NOT EXISTS validation_step (
    id_validation_step SERIAL PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL,              -- 'proforma' or 'commande'
    entity_id INTEGER NOT NULL,                     -- id_proforma or id_commande
    step_number INTEGER NOT NULL,                   -- 1 or 2
    id_utilisateur INTEGER NOT NULL,                -- who validated
    validated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur),
    UNIQUE (entity_type, entity_id, step_number)    -- one validation per step per entity
);

-- Index for faster lookups
CREATE INDEX IF NOT EXISTS idx_validation_step_entity ON validation_step(entity_type, entity_id);

-- ============================================
-- CONFIGURATION DATA
-- Insert validation config (if not exists)
-- ============================================


-- 13. VALIDATION CONFIGURATION (multi-step hierarchical validation)
INSERT INTO conf_va (id_conf_va, libelle, niveau_1, niveau_2) VALUES 
    (1, 'proforma', 7, 10),   -- proforma: step 1 = niveau 7+, step 2 = niveau 10+
    (2, 'commande', 7, 10);   -- commande: step 1 = niveau 7+, step 2 = niveau 10+

-- Configuration for proforma validation: niveau_1=7, niveau_2=10
INSERT INTO conf_va (libelle, niveau_1, niveau_2)
SELECT 'proforma', 7, 10
WHERE NOT EXISTS (SELECT 1 FROM conf_va WHERE libelle = 'proforma');

-- Configuration for commande validation: niveau_1=7, niveau_2=10  
INSERT INTO conf_va (libelle, niveau_1, niveau_2)
SELECT 'commande', 7, 10
WHERE NOT EXISTS (SELECT 1 FROM conf_va WHERE libelle = 'commande');

-- ============================================
-- HOW IT WORKS:
-- ============================================
-- 1. conf_va stores the required niveaux for each entity type
--    - niveau_1: minimum level for step 1 validation
--    - niveau_2: minimum level for step 2 validation (0 = no step 2 required)
--
-- 2. When a user validates:
--    - Check if they have required niveau for the next step
--    - Check they haven't already validated a step for this entity
--    - Insert into validation_step
--
-- 3. Entity is fully validated when:
--    - If niveau_2 > 0: both step 1 and step 2 are complete
--    - If niveau_2 = 0: only step 1 is required
--
-- 4. The etat of proforma/commande changes to 'Valide' only when fully validated
