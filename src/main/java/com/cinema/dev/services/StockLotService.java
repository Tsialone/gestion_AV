package com.cinema.dev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinema.dev.dtos.ValorisationStockRowDto;
import com.cinema.dev.models.EntreeStockLot;
import com.cinema.dev.models.SortieStockLot;
import com.cinema.dev.repositories.EntreeStockLotRepository;
import com.cinema.dev.repositories.SortieStockLotRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockLotService {
    
    @Autowired
    private EntreeStockLotRepository entreeRepository;
    
    @Autowired
    private SortieStockLotRepository sortieRepository;

     public List<ValorisationStockRowDto> getValorisationStockCUMP() throws Exception {
        List<ValorisationStockRowDto> tableau = new ArrayList<>();
        int indexEntree = 0;
        int indexEntreeLimit = 0;
        int indexSortie = 0;
        int indexSortieLimit = 0;
        float cump = 0;
        List<EntreeStockLot> entrees = entreeRepository.findAll();
        List<SortieStockLot> sorties = sortieRepository.findAll();

        System.out.println("taille entree = "   + entrees.size());
        System.out.println("taille sortie = "   + sorties.size());
        EntreeStockLot e = entrees.get(0);
        cump = e.getPrixUnitaire().floatValue();
        ValorisationStockRowDto curr = StockLotService.getValorisationRowFromEntreeInitCump(e, null);
        tableau.add(curr);
        indexEntree++;
        indexEntreeLimit++;
        while(indexSortie < sorties.size() || indexEntree < entrees.size()) {
            if(indexEntree >= entrees.size() ||(indexSortie < sorties.size() && sorties.get(indexSortie).getDateMouvement().isBefore(entrees.get(indexEntreeLimit).getDateMouvement()))) {
                // Il y a mvt sortie
                // Soustraire les qtt dans le stock
                SortieStockLot s = sorties.get(indexSortie);
                ValorisationStockRowDto lastMvtEntreeMemeArticle = null;
                for(int i = tableau.size() - 1; i >= 0; i --) {
                    System.out.println("etape i = " + i);
                    ValorisationStockRowDto row = tableau.get(i);
                    if (row.getTypeMouvement().equals("ENTREE") && row.getIdArticle().equals(s.getIdArticle())) {
                        lastMvtEntreeMemeArticle = row;
                        break;
                    }
                }
                if(lastMvtEntreeMemeArticle == null) {
                    throw new Exception("Erreur CUMP : Aucune entree trouvee pour l'article id " + s.getIdArticle() + " lors de la sortie id " + s.getIdMvt());
                }
                BigDecimal lastCump = lastMvtEntreeMemeArticle.getCump() != null ? lastMvtEntreeMemeArticle.getCump() : BigDecimal.ZERO;
                if(lastCump.equals(BigDecimal.ZERO)) System.out.println("last cump est zero");
                BigDecimal montantSortie = lastCump.multiply(BigDecimal.valueOf(s.getQuantiteSortie()));
                BigDecimal valeurGlobalePrecedente = tableau.isEmpty() ? BigDecimal.ZERO : tableau.get(tableau.size() - 1).getValeurStockLot();
                ValorisationStockRowDto sortie = getValorisationRowSortieCump(s, lastCump, montantSortie, valeurGlobalePrecedente);
                tableau.add(sortie);
                System.out.println("sortie = " + indexSortie);
                indexSortie ++;
                if(indexSortie < sorties.size() ) {
                    indexSortieLimit++;
                }
                System.out.println("sortie = " + indexSortie);
                System.out.println("--------------------------");

            } else if(indexEntree < entrees.size()) {
                EntreeStockLot entree = entrees.get(indexEntree);
                // Trouver le dernier mvt associe a cet article pour calculer le CUMP
                ValorisationStockRowDto lastMvtEntreeMemeArticle = null;
                for(int i = tableau.size() - 1; i >= 0; i --) {
                    System.out.println("etapee i = " + i);
                    ValorisationStockRowDto row = tableau.get(i);
                    if (row.getTypeMouvement().equals("ENTREE") && row.getIdArticle().equals(entree.getIdArticle())) {
                        lastMvtEntreeMemeArticle = row;
                        break;
                    }
                }
                BigDecimal currValorisation = tableau.getLast().getValeurStockLot().add(entree.getPrixUnitaire() != null ? 
                (entree.getPrixUnitaire().multiply(BigDecimal.valueOf(entree.getQuantiteInitialeLot())))  : BigDecimal.ZERO);
                ValorisationStockRowDto currEntree = null;
                if(lastMvtEntreeMemeArticle != null) {
                    BigDecimal lastCump = lastMvtEntreeMemeArticle.getCump() != null ? lastMvtEntreeMemeArticle.getCump() : BigDecimal.ZERO;
                    if(lastCump.equals(BigDecimal.ZERO)) System.out.println("last cump est zero");
                    Integer lastQttCumul = lastMvtEntreeMemeArticle.getQttCumul() != null ? lastMvtEntreeMemeArticle.getQttCumul() : 0;
                    if(lastQttCumul.equals(BigDecimal.ZERO)) System.out.println("last qtt cumul est zero");
                    currEntree = getValorisationRowEntreeCump(entree, currValorisation, lastCump, lastQttCumul.intValue());
                    System.out.println("lastCump = " + lastCump);
                    System.out.println("ancien qtt = " +  lastQttCumul.intValue());
                } else {
                    // Nouveaux
                    System.out.println("mickey");
                    currEntree = StockLotService.getValorisationRowFromEntreeInitCump(entree, currValorisation);
                }
                tableau.add(currEntree);
                  indexEntree ++;
                if(indexEntree < entrees.size() ) {
                    indexEntreeLimit++;
                }
                System.out.println("entree = " + indexEntree);
                System.out.println("--------------------------");
            }
        }
        return tableau;
    }

    public List<ValorisationStockRowDto> getValorisationStockLIFO() throws Exception {
        List<ValorisationStockRowDto> tableau = new ArrayList<>();
        int indexEntree = 0;
        int indexEntreeLimit = 0;
        int indexSortie = 0;
        int indexSortieLimit = 0;
        List<EntreeStockLot> entrees = entreeRepository.findAll();
        List<SortieStockLot> sorties = sortieRepository.findAll();

        System.out.println("taille entree = "   + entrees.size());
        System.out.println("taille sortie = "   + sorties.size());
        EntreeStockLot e = entrees.get(0);
        ValorisationStockRowDto curr = StockLotService.getValorisationRowFromEntreeInit(e);
        tableau.add(curr);
        indexEntree++;
        indexEntreeLimit++;
        while(indexSortie < sorties.size() || indexEntree < entrees.size()) {
            if(indexEntree >= entrees.size() ||(indexSortie < sorties.size() && sorties.get(indexSortie).getDateMouvement().isBefore(entrees.get(indexEntreeLimit).getDateMouvement()))) {
                // Il y a mvt sortie
                // Soustraire les qtt dans le stock
                SortieStockLot s = sorties.get(indexSortie);
                Integer qttASortir = s.getQuantiteSortie();
                System.out.println("taille tableau = " + tableau.size());
                System.out.println("qtt a sortir = " + qttASortir);
                BigDecimal montantSortie = BigDecimal.ZERO;
                // CORRECTION : Chercher dans 'tableau' les ENTREE qui ont encore du 'reste' POUR CE LOT PRECIS
                for(int i = tableau.size() - 1; i >= 0; i --) {
                    System.out.println("etape i = " + i);
                    ValorisationStockRowDto row = tableau.get(i);
                    if (qttASortir <= 0) {
                        System.out.println("banzai qtt a sortir < 0");
                        break;
                    } 

                    if (row.getTypeMouvement().equals("ENTREE") 
                        && row.getIdArticle().equals(s.getIdArticle()) 
                        && row.getReste() > 0) {
                        
                        int prelevable = Math.min(row.getReste(), qttASortir);
                        System.out.println("prelevable = " + prelevable + " pcq reste = " + row.getReste());
                        row.setReste(row.getReste() - prelevable);
                        qttASortir -= prelevable;
                        System.out.println("Update qtt a sortir = " + qttASortir);
                        montantSortie = montantSortie.add(row.getPrixUnitaire().multiply(BigDecimal.valueOf(prelevable)));
                        System.out.println("montant sorti = " + montantSortie);
                    }
                }
                System.err.println("END");
                // On sort de la boucle, toutes les quantites ont ete consommes
                BigDecimal valeurGlobalePrecedente = tableau.isEmpty() ? BigDecimal.ZERO : tableau.get(tableau.size() - 1).getValeurStockLot();
                ValorisationStockRowDto sortie = getValorisationRowSortie(s, montantSortie, valeurGlobalePrecedente);
                tableau.add(sortie);
                System.out.println("sortie = " + indexSortie);
                indexSortie ++;
                if(indexSortie < sorties.size() ) {
                    indexSortieLimit++;
                }
                System.out.println("sortie = " + indexSortie);
                System.out.println("--------------------------");

            } else if(indexEntree < entrees.size()) {
                EntreeStockLot entree = entrees.get(indexEntree);
                System.out.println("idMvt = " + entree.getIdMvt());
                System.out.println("valeurStockLot du dernier = " + tableau.getLast().getValeurStockLot());
                System.out.println("Add prix = " + (entree.getPrixUnitaire() != null ? 
                        (entree.getPrixUnitaire().multiply(BigDecimal.valueOf(entree.getQuantiteInitialeLot())))  : BigDecimal.ZERO));
                
                        BigDecimal currValorisation = tableau.getLast().getValeurStockLot().add(entree.getPrixUnitaire() != null ? 
                        (entree.getPrixUnitaire().multiply(BigDecimal.valueOf(entree.getQuantiteInitialeLot())))  : BigDecimal.ZERO);
                System.out.println("curr = " + currValorisation);
                ValorisationStockRowDto currEntree = getValorisationRowEntree(entree, currValorisation);
                tableau.add(currEntree);
                System.out.println("entree = " + indexEntree);
                indexEntree ++;
                if(indexEntree < entrees.size() ) {
                    indexEntreeLimit++;
                }
                System.out.println("entree = " + indexEntree);

                System.out.println("--------------------------");
            }
        }
        return tableau;
    }

    public List<ValorisationStockRowDto> getValorisationStockFIFO() throws Exception {
        List<ValorisationStockRowDto> tableau = new ArrayList<>();
        int indexEntree = 0;
        int indexEntreeLimit = 0;
        int indexSortie = 0;
        int indexSortieLimit = 0;
        List<EntreeStockLot> entrees = entreeRepository.findAll();
        List<SortieStockLot> sorties = sortieRepository.findAll();

        System.out.println("taille entree = "   + entrees.size());
        System.out.println("taille sortie = "   + sorties.size());
        EntreeStockLot e = entrees.get(0);
        ValorisationStockRowDto curr = StockLotService.getValorisationRowFromEntreeInit(e);
        tableau.add(curr);
        indexEntree++;
        indexEntreeLimit++;
        while(indexSortie < sorties.size() || indexEntree < entrees.size()) {
            if(indexEntree >= entrees.size() ||(indexSortie < sorties.size() && sorties.get(indexSortie).getDateMouvement().isBefore(entrees.get(indexEntreeLimit).getDateMouvement()))) {
                // Il y a mvt sortie
                // Soustraire les qtt dans le stock
                SortieStockLot s = sorties.get(indexSortie);
                Integer qttASortir = s.getQuantiteSortie();
                System.out.println("taille tableau = " + tableau.size());
                System.out.println("qtt a sortir = " + qttASortir);
                BigDecimal montantSortie = BigDecimal.ZERO;
                // CORRECTION : Chercher dans 'tableau' les ENTREE qui ont encore du 'reste' POUR CE LOT PRECIS
                for(int i = 0; i < tableau.size(); i ++) {
                    System.out.println("etape i = " + i);
                    ValorisationStockRowDto row = tableau.get(i);
                    if (qttASortir <= 0) {
                        System.out.println("banzai qtt a sortir < 0");
                        break;
                    } 

                    if (row.getTypeMouvement().equals("ENTREE") 
                        && row.getIdArticle().equals(s.getIdArticle()) 
                        && row.getReste() > 0) {
                        
                        int prelevable = Math.min(row.getReste(), qttASortir);
                        System.out.println("prelevable = " + prelevable + " pcq reste = " + row.getReste());
                        row.setReste(row.getReste() - prelevable);
                        qttASortir -= prelevable;
                        System.out.println("Update qtt a sortir = " + qttASortir);
                        montantSortie = montantSortie.add(row.getPrixUnitaire().multiply(BigDecimal.valueOf(prelevable)));
                        System.out.println("montant sorti = " + montantSortie);
                    }
                }
                System.err.println("END");
                // On sort de la boucle, toutes les quantites ont ete consommes
                BigDecimal valeurGlobalePrecedente = tableau.isEmpty() ? BigDecimal.ZERO : tableau.get(tableau.size() - 1).getValeurStockLot();
                ValorisationStockRowDto sortie = getValorisationRowSortie(s, montantSortie, valeurGlobalePrecedente);
                tableau.add(sortie);
                System.out.println("sortie = " + indexSortie);
                indexSortie ++;
                if(indexSortie < sorties.size() ) {
                    indexSortieLimit++;
                }
                System.out.println("sortie = " + indexSortie);
                System.out.println("--------------------------");

            } else if(indexEntree < entrees.size()) {
                EntreeStockLot entree = entrees.get(indexEntree);
                System.out.println("idMvt = " + entree.getIdMvt());
                System.out.println("valeurStockLot du dernier = " + tableau.getLast().getValeurStockLot());
                System.out.println("Add prix = " + (entree.getPrixUnitaire() != null ? 
                        (entree.getPrixUnitaire().multiply(BigDecimal.valueOf(entree.getQuantiteInitialeLot())))  : BigDecimal.ZERO));
                
                        BigDecimal currValorisation = tableau.getLast().getValeurStockLot().add(entree.getPrixUnitaire() != null ? 
                        (entree.getPrixUnitaire().multiply(BigDecimal.valueOf(entree.getQuantiteInitialeLot())))  : BigDecimal.ZERO);
                System.out.println("curr = " + currValorisation);
                ValorisationStockRowDto currEntree = getValorisationRowEntree(entree, currValorisation);
                tableau.add(currEntree);
                System.out.println("entree = " + indexEntree);
                indexEntree ++;
                if(indexEntree < entrees.size() ) {
                    indexEntreeLimit++;
                }
                System.out.println("entree = " + indexEntree);

                System.out.println("--------------------------");
            }
        }
        return tableau;
    }

    // CUMP
    private static ValorisationStockRowDto getValorisationRowFromEntreeInitCump(EntreeStockLot e, BigDecimal newValorisation) {
        BigDecimal valorisationToUse = newValorisation != null ? newValorisation : 
            (e.getPrixUnitaire() != null ? 
                e.getPrixUnitaire().multiply(BigDecimal.valueOf(e.getQuantiteInitialeLot())) : BigDecimal.ZERO);
        ValorisationStockRowDto curr = ValorisationStockRowDto.builder()
                    .dateMouvement(e.getDateMouvement())
                    .idMvt(e.getIdMvt())
                    .typeMouvement("ENTREE")
                    .designation(e.getDesignation())
                    .idArticle(e.getIdArticle())
                    .libelleArticle(e.getLibelleArticle())
                    .libelleCategorie(e.getLibelleCategorie())
                    .idLot(e.getIdLot())
                    .libelleLot(e.getLibelleLot())
                    .quantiteMouvement(e.getQuantiteMouvement())
                    .stockRestantLot(e.getStockActuelLot())
                    .quantiteInitialeLot(e.getQuantiteInitialeLot())
                    .prixUnitaire(e.getPrixUnitaire())
                    .valeurMouvement(e.getPrixTotalMouvement())
                    .valeurStockLot(valorisationToUse)
                    .nomTier(e.getNomFournisseur())
                    .nomDepot(e.getNomDepot())
                    .reste(e.getQuantiteMouvement())
                    .qttCumul(e.getQuantiteMouvement())
                    .cump(e.getPrixUnitaire())
                    .build();
        return curr;
    }

    private static ValorisationStockRowDto getValorisationRowEntreeCump(EntreeStockLot entree, BigDecimal currValorisation, BigDecimal ancienCump, Integer ancientQttCumul) {
         ValorisationStockRowDto currEntree = ValorisationStockRowDto.builder()
                    .dateMouvement(entree.getDateMouvement())
                    .idMvt(entree.getIdMvt())
                    .typeMouvement("ENTREE")
                    .designation(entree.getDesignation())
                    .idArticle(entree.getIdArticle())
                    .libelleArticle(entree.getLibelleArticle())
                    .libelleCategorie(entree.getLibelleCategorie())
                    .idLot(entree.getIdLot())
                    .libelleLot(entree.getLibelleLot())
                    .quantiteMouvement(entree.getQuantiteMouvement())
                    .stockRestantLot(entree.getStockActuelLot())
                    .quantiteInitialeLot(entree.getQuantiteInitialeLot())
                    .prixUnitaire(entree.getPrixUnitaire())
                    .valeurMouvement(entree.getPrixTotalMouvement())
                    .valeurStockLot(currValorisation)
                    .nomTier(entree.getNomFournisseur())
                    .nomDepot(entree.getNomDepot())
                    .reste(entree.getQuantiteMouvement())
                    .qttCumul(ancientQttCumul + entree.getQuantiteMouvement())
                    .cump(((ancienCump.multiply(BigDecimal.valueOf(ancientQttCumul))).add(entree.getPrixTotalMouvement())).divide(BigDecimal.valueOf(ancientQttCumul + entree.getQuantiteMouvement())))
                    .build();
        return currEntree;
    }

    private static ValorisationStockRowDto getValorisationRowSortieCump(SortieStockLot s, BigDecimal cump, BigDecimal montantSortie, BigDecimal valeurGlobalePrecedente) {
        ValorisationStockRowDto sortie = ValorisationStockRowDto.builder()
                        .dateMouvement(s.getDateMouvement())
                        .idMvt(s.getIdMvt())
                        .typeMouvement("SORTIE")
                        .designation(s.getDesignation())
                        .idArticle(s.getIdArticle())
                        .libelleArticle(s.getLibelleArticle())
                        .libelleCategorie(s.getLibelleCategorie())
                        .idLot(s.getIdLot())
                        .libelleLot(s.getLibelleLot())
                        .quantiteMouvement(s.getQuantiteSortie())
                        .stockRestantLot(0)
                        .quantiteInitialeLot(0)
                        .prixUnitaire(BigDecimal.ZERO)
                        .valeurMouvement(montantSortie)
                        .valeurStockLot(valeurGlobalePrecedente.subtract(montantSortie))
                        .nomTier("")
                        .nomDepot("")
                        .reste(0)   
                        .cump(cump)
                        .build();
        return sortie;
    }

    // FIFO ET LIFO
    private static ValorisationStockRowDto getValorisationRowFromEntreeInit(EntreeStockLot e) {
        ValorisationStockRowDto curr = ValorisationStockRowDto.builder()
                    .dateMouvement(e.getDateMouvement())
                    .idMvt(e.getIdMvt())
                    .typeMouvement("ENTREE")
                    .designation(e.getDesignation())
                    .idArticle(e.getIdArticle())
                    .libelleArticle(e.getLibelleArticle())
                    .libelleCategorie(e.getLibelleCategorie())
                    .idLot(e.getIdLot())
                    .libelleLot(e.getLibelleLot())
                    .quantiteMouvement(e.getQuantiteMouvement())
                    .stockRestantLot(e.getStockActuelLot())
                    .quantiteInitialeLot(e.getQuantiteInitialeLot())
                    .prixUnitaire(e.getPrixUnitaire())
                    .valeurMouvement(e.getPrixTotalMouvement())
                    .valeurStockLot(e.getPrixUnitaire() != null ? 
                        e.getPrixUnitaire().multiply(BigDecimal.valueOf(e.getQuantiteInitialeLot())) : BigDecimal.ZERO)
                    .nomTier(e.getNomFournisseur())
                    .nomDepot(e.getNomDepot())
                    .reste(e.getQuantiteMouvement())
                    .build();
        return curr;
    }

     private static ValorisationStockRowDto getValorisationRowEntree(EntreeStockLot entree, BigDecimal currValorisation) {
         ValorisationStockRowDto currEntree = ValorisationStockRowDto.builder()
                    .dateMouvement(entree.getDateMouvement())
                    .idMvt(entree.getIdMvt())
                    .typeMouvement("ENTREE")
                    .designation(entree.getDesignation())
                    .idArticle(entree.getIdArticle())
                    .libelleArticle(entree.getLibelleArticle())
                    .libelleCategorie(entree.getLibelleCategorie())
                    .idLot(entree.getIdLot())
                    .libelleLot(entree.getLibelleLot())
                    .quantiteMouvement(entree.getQuantiteMouvement())
                    .stockRestantLot(entree.getStockActuelLot())
                    .quantiteInitialeLot(entree.getQuantiteInitialeLot())
                    .prixUnitaire(entree.getPrixUnitaire())
                    .valeurMouvement(entree.getPrixTotalMouvement())
                    .valeurStockLot(currValorisation)
                    .nomTier(entree.getNomFournisseur())
                    .nomDepot(entree.getNomDepot())
                    .reste(entree.getQuantiteMouvement())
                    .build();
        return currEntree;
    }

    private static ValorisationStockRowDto getValorisationRowSortie(SortieStockLot s, BigDecimal montantSortie, BigDecimal valeurGlobalePrecedente) {
        ValorisationStockRowDto sortie = ValorisationStockRowDto.builder()
                        .dateMouvement(s.getDateMouvement())
                        .idMvt(s.getIdMvt())
                        .typeMouvement("SORTIE")
                        .designation(s.getDesignation())
                        .idArticle(s.getIdArticle())
                        .libelleArticle(s.getLibelleArticle())
                        .libelleCategorie(s.getLibelleCategorie())
                        .idLot(s.getIdLot())
                        .libelleLot(s.getLibelleLot())
                        .quantiteMouvement(s.getQuantiteSortie())
                        .stockRestantLot(0)
                        .quantiteInitialeLot(0)
                        .prixUnitaire(BigDecimal.ZERO)
                        .valeurMouvement(montantSortie)
                        .valeurStockLot(valeurGlobalePrecedente.subtract(montantSortie))
                        .nomTier("")
                        .nomDepot("")
                        .reste(0)   
                        .build();
        return sortie;
    }

    private BigDecimal trouverPrixAchatLot(Integer idLot) {
        return entreeRepository.findAll().stream()
                .filter(e -> e.getIdLot().equals(idLot))
                .map(EntreeStockLot::getPrixUnitaire)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    // Retourne directement les entités de la vue
    public List<EntreeStockLot> getAllEntrees() {
        return entreeRepository.findAllByOrderByDateMouvementDesc();
    }

    public List<EntreeStockLot> getEntreesByMouvement(Integer idMvt) {
        return entreeRepository.findByIdMvt(idMvt);
    }

    public List<SortieStockLot> getAllSorties() {
        return sortieRepository.findAllByOrderByDateMouvementDesc();
    }
}