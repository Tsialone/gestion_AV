# Authorization System Documentation

## SQL Insertion Order

```
1. table_27_janv.sql    -- Creates all tables
2. views.sql            -- Creates views
3. dataV3A.sql          -- Permissions data (users, roles, depts, categories, restrictions)
4. dataV3B.sql          -- Transaction data (demandes, proformas, commandes, stock)
```

---

## Authorization Rules Summary

### 1. Department-Based Restrictions

| Departement | ID | What they CAN do | What they CANNOT do |
|-------------|-----|------------------|---------------------|
| Ventes | 1 | Create DA, Proforma, Commande, Paiement, Livraison | Access Valorisation Stock |
| Finance | 2 | Access Valorisation Stock (CUMP, FIFO, LIFO) | Create DA, Proforma, Commande, Livraison |
| Logistique | 3 | Stock management | Create DA, Proforma, Commande, Livraison, Valorisation |
| Direction | 4 | Everything (if niveau high enough) | Nothing restricted |

### 2. Role/Niveau Restrictions for VALIDATION

Only users with **niveau >= 7** can validate Proformas and Commandes.

| Role | Niveau | Can Validate? |
|------|--------|---------------|
| Stagiaire | 1 | No |
| Magasinier | 3 | No |
| Vendeur | 3 | No |
| Vendeur Senior | 5 | No |
| Manager | 7 | YES |
| Chef Departement | 12 | Yes |
| Directeur | 15 | Yes (can do everything) |

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

---

## Test Scenarios

### Scenario 1: Department Restriction
- Login as `mag_fi_jean` (Finance) -> Try to create a Demande d'Achat -> Should get ERROR
- Login as `vdr_vt_rakoto` (Ventes) -> Create Demande d'Achat -> Should WORK

### Scenario 2: Validation Rights
- Login as `vdr_vt_rakoto` (niveau 3) -> Try to validate Proforma -> Should get ERROR
- Login as `mgr_vt_andry` (niveau 7) -> Validate Proforma -> Should WORK

### Scenario 3: Valorisation Access
- Login as `vdr_vt_rakoto` (Ventes) -> Go to Valorisation menu -> Should get ERROR
- Login as `mag_fi_jean` (Finance) -> Go to Valorisation -> Should WORK

### Scenario 4: Category Restriction
- Login as `vdr_vt_info` -> Try to sell iPhone (Telephonie) -> Should get ERROR
- Login as `vdr_vt_info` -> Sell Laptop (Informatique) -> Should WORK

### Scenario 5: Director Override
- Login as `dir_all_boss` -> Do anything -> Should ALWAYS WORK

---

## How to Add New Authorization for Your Functionalities

### Step 1: Identify What Needs Restriction

Ask yourself:
1. Which department(s) should access this feature?
2. What niveau minimum is required?
3. Are there category/fournisseur restrictions to check?

### Step 2: Add Authorization Check in Service Layer

In your service class, inject `AuthorizationService` and call the appropriate method:

```java
@Service
public class YourNewService {
    
    @Autowired
    private AuthorizationService authorizationService;
    
    public void yourNewFunction(Integer idUtilisateur, ...) {
        // Check if user can do this
        authorizationService.requireVentesDept(idUtilisateur, "Your Action Name");
        // OR
        authorizationService.requireFinanceDept(idUtilisateur, "Your Action Name");
        // OR
        authorizationService.requireValidationRights(idUtilisateur, "Your Action Name");
        
        // For article category checks:
        List<Integer> articleIds = Arrays.asList(1, 2, 3); // articles involved
        authorizationService.requireArticleAccess(idUtilisateur, articleIds, "Your Action");
        
        // Your business logic here...
        
        // Log the action to historique
        authorizationService.logAction(idUtilisateur, "table_name", "Description", entityId);
    }
}
```

### Step 3: Available Authorization Methods

```java
// DEPARTMENT CHECKS
authorizationService.requireVentesDept(idUtilisateur, actionName);   // Must be in Ventes (id=1)
authorizationService.requireFinanceDept(idUtilisateur, actionName);  // Must be in Finance (id=2)

// NIVEAU/ROLE CHECKS  
authorizationService.requireValidationRights(idUtilisateur, actionName);  // niveau >= 7
authorizationService.requireDirectorLevel(idUtilisateur, actionName);     // niveau >= 15

// COMBINED CHECKS (Dept + Niveau)
authorizationService.requireVentesAndValidation(idUtilisateur, actionName);  // Ventes + niveau >= 7

// ARTICLE/CATEGORY CHECKS
authorizationService.requireArticleAccess(idUtilisateur, articleIds, actionName);

// FOURNISSEUR CHECKS
authorizationService.requireFournisseurAccess(idUtilisateur, idFournisseur, actionName);

// GET USER INFO
Utilisateur user = authorizationService.getUtilisateur(idUtilisateur);
Role role = authorizationService.getRole(idUtilisateur);

// LOG ACTION
authorizationService.logAction(idUtilisateur, "table_name", "description", entityId);
```

### Step 4: Handle in Controller

The controller gets the user ID from session automatically:

```java
@PostMapping("/your-action")
public String yourAction(HttpSession session, 
                         @RequestParam ..., 
                         RedirectAttributes redirectAttributes) {
    Integer idUtilisateur = sessionService.getCurrentUserId(session);
    
    try {
        yourService.yourNewFunction(idUtilisateur, ...);
        redirectAttributes.addFlashAttribute("toastMessage", "Success!");
        redirectAttributes.addFlashAttribute("toastType", "success");
    } catch (SecurityException e) {
        // Authorization failed - show error to user
        redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
        redirectAttributes.addFlashAttribute("toastType", "error");
    }
    return "redirect:/your-page";
}
```

### Step 5: Add New Authorization Method (if needed)

If you need a new type of check, add it to `AuthorizationService.java`:

```java
// Example: Only Logistique department
public void requireLogistiqueDept(Integer idUtilisateur, String action) {
    Utilisateur user = getUtilisateur(idUtilisateur);
    if (user.getIdDept() != DEPT_LOGISTIQUE) {  // Add constant: private static final int DEPT_LOGISTIQUE = 3;
        throw new SecurityException("Action '" + action + "' reservee au departement Logistique");
    }
}

// Example: Custom niveau requirement
public void requireMinimumNiveau(Integer idUtilisateur, int minimumNiveau, String action) {
    Role role = getRole(idUtilisateur);
    if (role.getNiveau() < minimumNiveau) {
        throw new SecurityException("Action '" + action + "' requiert niveau " + minimumNiveau + " minimum");
    }
}
```

---

## Key Files

| File | Purpose |
|------|---------|
| AuthorizationService.java | All authorization logic |
| SessionService.java | Manages logged-in user session |
| GlobalControllerAdvice.java | Injects currentUser to all pages |
| LoginInterceptor.java | Redirects to login if not authenticated |
| UtilisateurSessionDTO.java | User session data structure |

---

## Database Tables for Permissions

| Table | Purpose |
|-------|---------|
| utilisateur | Users with id_role and id_dept |
| role | Roles with niveau (1-15) and seuil |
| dept | Departments (Ventes=1, Finance=2, etc.) |
| restriction_categorie | Which categories a user is limited to |
| restriction_fournisseur | Which suppliers a user is limited to |
| historique_general | Logs all actions with user info |

---

## Important Constants

In `AuthorizationService.java`:
```java
private static final int DEPT_VENTES = 1;
private static final int DEPT_FINANCE = 2;
private static final int NIVEAU_VALIDATION = 7;    // Minimum level to validate
private static final int NIVEAU_DIRECTEUR = 15;    // Director level (bypass restrictions)
```
