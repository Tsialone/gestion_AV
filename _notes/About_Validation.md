# About deptVt 
- All users in that dept can create DA, Profo, Comm, Paiement
- Only users w/ niveau >= 7 can validate Profo/Comm 
    ex: `mgr_vt_andry(7)`, `cdp_vt_hery(12)`


# About deptFinance
- Similar principle as deptVt, all users in that dept 
can consult the valorisation
- Only users w/ niveau >= 7 can validate + do valorisation
    ex: `mgr_fi_marie(7)`, `cdp_fi_paul(12)`


# About deptLogistique:
- Similar, all users in that dept can consult stock
- Only niveau >= 7  can do the stock management
    ex: `mgr_lg_tiana(7)`


# About Restrictions: 
The following users have restictions about product Category + fournisseur
ex: `vdr_vt_info(3)` = article info
    `vdr_vt_phone(3)` = telephone categ & fournisseur PhonePro
    `mgr_vt_game(7)` = gaming + access & fournisseur GameZone

# About deptDirection
- The only user `dir_all_boss(15)` can do anything

# About validation:
- In `conf_va`, we establish the "hierarchy"/nb of validation for one entity:
    - entity name
    - minRequiredNiveauForVA1
    - minRequiredNiveauForVA2
- If niveau2 is set to 0, only niveau1 is requiered for validation
- Same user cannot validate the same proforma/commande twice
- Validation is sequential <=> step1 must be done before step2 can do anything
- When a user validate, it writes into `validation_step`:
    - entity_type, 
    - entity_id, (id_proforma/id_commande)
    - step_number, 
    - id_utilisateur, 
    - createdAt
- The etat of proforma/commande changes to 'Valide' after fully validated:
    - If niveau2 > 0: both step 1 and step 2 are complete
    - If niveau2 = 0: only step 1 is required
