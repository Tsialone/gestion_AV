# About User - Role - Niveau - Restrictions

### 1. Department-Based Restrictions

| Departement | ID | What they CAN do | What they CANNOT do |
|-------------|-----|------------------|---------------------|
| Ventes | 1 | Create DA, Proforma, Commande, Paiement, Livraison | Access Valorisation Stock |
| Finance | 2 | Access Valorisation Stock (CUMP, FIFO, LIFO) | Create DA, Proforma, Commande, Livraison |
| Logistique | 3 | Stock management | Create DA, Proforma, Commande, Livraison, Valorisation |
| Direction | 4 | Everything (if niveau high enough) | Nothing restricted |

**Role/Niveau Reference:**

| Role | Niveau | Can Validate Step 1? | Can Validate Step 2? |
|------|--------|----------------------|----------------------|
| Stagiaire | 1 | No | No |
| Magasinier | 3 | No | No |
| Vendeur | 3 | No | No |
| Vendeur Senior | 5 | No | No |
| Manager | 7 | YES | No |
| Chef Equipe | 10 | YES | YES |
| Chef Departement | 12 | YES | YES |
| Directeur | 15 | YES | YES |

**To change validation requirements:**
```sql
-- Single-step validation (only Manager needed)
UPDATE conf_va SET niveau_1 = 7, niveau_2 = 0 WHERE libelle = 'proforma';

-- Two-step validation (Manager + Chef Dept)
UPDATE conf_va SET niveau_1 = 7, niveau_2 = 12 WHERE libelle = 'proforma';
```

### 3. Category Restrictions

If a user is in `restriction_categorie` table, they can ONLY work with those specific categories.
- If NOT in the table -> can access ALL categories
- If IN the table -> can ONLY access listed categories

Example users with category restrictions:

| User | ID | Restricted Categories |
|------|-----|----------------------|
| vdr_vt_info | 12 | Informatique only |
| vdr_vt_phone | 13 | Telephonie only |
| mgr_vt_game | 14 | Gaming + Accessoires |

### 4. Fournisseur Restrictions

Same logic as category restrictions. If in `restriction_fournisseur`, can only work with those suppliers.

| User | ID | Restricted Fournisseurs |
|------|-----|------------------------|
| vdr_vt_phone | 13 | PhonePro Madagascar only |
| mgr_vt_game | 14 | GameZone Distribution only |

---

## Test Users Reference

| ID | Username | Role | Dept | Niveau | Notes |
|----|----------|------|------|--------|-------|
| 1 | stg_vt_rabe | Stagiaire | Ventes | 1 | Can view, cannot validate |
| 2 | vdr_vt_rakoto | Vendeur | Ventes | 3 | Basic sales, no validation |
| 3 | vds_vt_rasoa | Vendeur Senior | Ventes | 5 | Still cannot validate |
| 4 | mgr_vt_andry | Manager | Ventes | 7 | CAN validate |
| 5 | cdp_vt_hery | Chef Dept | Ventes | 12 | Full validation rights |
| 6 | mag_fi_jean | Magasinier | Finance | 3 | Can access valorisation |
| 7 | mgr_fi_marie | Manager | Finance | 7 | Validate + Valorisation |
| 8 | cdp_fi_paul | Chef Dept | Finance | 12 | Full finance rights |
| 9 | mag_lg_fidy | Magasinier | Logistique | 3 | Stock only |
| 10 | mgr_lg_tiana | Manager | Logistique | 7 | Stock management |
| 11 | dir_all_boss | Directeur | Direction | 15 | CAN DO EVERYTHING |
| 12 | vdr_vt_info | Vendeur | Ventes | 3 | Informatique only |
| 13 | vdr_vt_phone | Vendeur | Ventes | 3 | Telephonie + PhonePro only |
| 14 | mgr_vt_game | Manager | Ventes | 7 | Gaming + GameZone only |
