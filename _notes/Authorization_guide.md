
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