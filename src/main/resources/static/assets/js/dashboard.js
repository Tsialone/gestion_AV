console.log('=== Dashboard.js chargé ===');

function initCharts() {
    console.log('=== INIT CHARTS START ===');
    console.log('ApexCharts disponible?', typeof ApexCharts);
    
    // Attendre un petit délai pour s'assurer que tout est prêt
    setTimeout(function() {
        // Graphique Ventes par Catégorie (Histogramme)
        var elementCategorie = document.querySelector("#chart-ventes-categorie");
        console.log('Element chart-ventes-categorie trouvé?', !!elementCategorie);
        
        var optionsCategorie = {
            series: [{
                name: 'Ventes',
                data: [450, 380, 320, 250, 180, 120]
            }],
            chart: {
                type: 'bar',
                height: 350
            },
            plotOptions: {
                bar: {
                    horizontal: false,
                    columnWidth: '55%',
                    endingShape: 'rounded'
                },
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                show: true,
                width: 2,
                colors: ['transparent']
            },
            xaxis: {
                categories: ['Électronique', 'Alimentaire', 'Textile', 'Mobilier', 'Cosmétique', 'Jouets'],
            },
            yaxis: {
                title: {
                    text: 'Nombre de ventes'
                }
            },
            fill: {
                opacity: 1
            },
            tooltip: {
                y: {
                    formatter: function (val) {
                        return val + " ventes"
                    }
                }
            },
            colors: ['#435ebe']
        };

        if (elementCategorie && typeof ApexCharts !== 'undefined') {
            console.log('Création chart catégorie...');
            try {
                var chartCategorie = new ApexCharts(elementCategorie, optionsCategorie);
                chartCategorie.render();
                console.log('✓ Chart catégorie rendu avec succès');
            } catch(e) {
                console.error('✗ Erreur création chart catégorie:', e);
            }
        } else {
            console.error('✗ Impossible de créer chart catégorie', {
                element: !!elementCategorie, 
                ApexCharts: typeof ApexCharts
            });
        }

        // Graphique Évolution des Ventes (Courbe)
        var elementEvolution = document.querySelector("#chart-ventes-evolution");
        console.log('Element chart-ventes-evolution trouvé?', !!elementEvolution);
        
        var optionsEvolution = {
            series: [{
                name: "Ventes",
                data: [120, 140, 170, 150, 190, 210, 240, 260, 230, 280, 310, 340]
            }],
            chart: {
                height: 350,
                type: 'line',
                zoom: {
                    enabled: false
                }
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'smooth',
                width: 3
            },
            grid: {
                row: {
                    colors: ['#f3f3f3', 'transparent'],
                    opacity: 0.5
                },
            },
            xaxis: {
                categories: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Aoû', 'Sep', 'Oct', 'Nov', 'Déc'],
            },
            yaxis: {
                title: {
                    text: 'Nombre de ventes'
                }
            },
            colors: ['#28c76f']
        };

        if (elementEvolution && typeof ApexCharts !== 'undefined') {
            console.log('Création chart évolution...');
            try {
                var chartEvolution = new ApexCharts(elementEvolution, optionsEvolution);
                chartEvolution.render();
                console.log('✓ Chart évolution rendu avec succès');
            } catch(e) {
                console.error('✗ Erreur création chart évolution:', e);
            }
        } else {
            console.error('✗ Impossible de créer chart évolution', {
                element: !!elementEvolution, 
                ApexCharts: typeof ApexCharts
            });
        }
        console.log('=== INIT CHARTS END ===');
    }, 500);  // Délai pour s'assurer que tout est prêt
}

// Initialiser quand le DOM est prêt
if (document.readyState === 'loading') {
    console.log('DOM pas encore chargé, attente...');
    document.addEventListener('DOMContentLoaded', initCharts);
} else {
    console.log('DOM déjà chargé, initialisation des charts');
    initCharts();
}

// Aussi au chargement complet de la page
window.addEventListener('load', function() {
    console.log('Page chargée complètement');
    initCharts();
});
