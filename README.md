For quick insertion, use the python script in `sql/`
```
py execute_sql.py
```

## Script Configuration
Check the script, in the main function
Just replace the port/password with yours   

## SQL Insertion Order

```
1. table_27_janv.sql    -- Creates all tables
2. views.sql            -- Creates views
3. dataV3A.sql          -- Permissions data (users, roles, depts, categories, restrictions)
4. dataV3B.sql          -- Transaction data (demandes, proformas, commandes, stock)
5. dataV3C.sql          -- Validation data
```