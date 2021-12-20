
#### 1. Exigences Fonctionnalités à tester

| Identifiants des <br> fonctionnalités | Description des fonctionnalités |
| ------------------------------------- | ------------------------------- |
| **EF - 001** | Respecter les champs valides dans les déclarations          |
| **EF - 002** | Respecter le nombre d'heures requis pour les catégories     |
| **EF - 003** | Respecter les dates supportées pour les cycles et activités |
| **EF - 004** | Faire la mise à jour du fichier de statistiques             |

<br>

#### 2. Suites et cas de tests

| Identifiants des <br> fonctionnalités | Identifiants des <br> suites de tests | Description des fonctionnalités | Nombre de <br> cas de tests |
| ------------------------------------- | ------------------------------------- | ------------------------------- | --------------------------- |
| **EF - 001** | **ST - 001** | Respecter les champs valides <br> dans les déclarations générales        | 12 |
|              | **ST - 002** | Respecter les champs valides <br> dans les déclarations des architectes  | 2  |
|              | **ST - 003** | Respecter les champs valides <br> dans les déclarations des psychologues | 2  |
|              | **ST - 004** | Respecter les champs valides <br> dans les déclarations des géologues    | 4  |
|              | **ST - 005** | Respecter les champs valides <br> dans les déclarations des podiatres    | 2  |
| **EF - 002** | **ST - 006** | Respect du nombre d'heures dans les <br> déclarations générales          | 8  |
|              | **ST - 007** | Respect du nombre d'heures dans les <br> déclarations des architectes    | 1  |
|              | **ST - 008** | Respect du nombre d'heures dans les <br> déclarations des psychologues   | 3  |
|              | **ST - 009** | Respect du nombre d'heures dans les <br> déclarations des géologues      | 4  |
|              | **ST - 010** | Respect du nombre d'heures dans les <br> déclarations des podiatres      | 1  |
| **EF - 003** | **ST - 011** | Respect des dates supportées <br> pour les déclarations générales        | 2  | 
|              | **ST - 012** | Respect des dates supportées <br> pour les déclarations des architectes  | 4  | 
|              | **ST - 013** | Respect des dates supportées <br> pour les déclarations des psychologues | 2  |
|              | **ST - 014** | Respect des dates supportées <br> pour les déclarations des géologues    | 2  |
| **EF - 004** | **ST - 015** | Mettre à jour le fichier statistique <br> selon les déclarations données | 11 |
<br>


Le fichier d’entrée JSON n'a pas 17h total accumulées pour les activités dans la catégorie groupe de discussion
Le fichier d’entrée JSON n'a pas 17h total accumulé pour les activités dans la catégorie groupe de discussion

#### 3. Cas de tests
| Identifiants des <br> suites de tests | Identifiants des <br> cas de tests | Description des des cas de test | Préconditions | Sortie attendue |Priorité|
| ------------------------------------- | ---------------------------------- | ------------------------------- | ------------- |-----------------|--------|
| **ST - 001** | **CT - 001** | Ajouter une déclaration avec le champ sexe valide | Le fichier d’entrée JSON comporte un champ sexe <br> avec une valeur entre 0 et 2 | Le champ sexe est valide | Basse |
|| **CT - 002** | Ajouter une déclaration avec le champ sexe <br> avec une valeur ∉ {0,1,2} | Le fichier d’entrée JSON comporte un champ sexe <br> avec une valeur ∉ {0,1,2} | **Erreur** : "Le sexe n'a pas une <br> valeur acceptée (0, 1 ou 2)" | Moyenne |
|| **CT - 003** | Ajouter une déclaration avec une description <br> de 20 caractères ou plus | Le fichier d’entrée JSON comporte une description <br> de 20 caractères ou plus | Le champ description est valide | Moyenne |
|| **CT - 004** | Ajouter une déclaration avec une description <br> de **moins** de 20 caractères | Le fichier d’entrée JSON comporte une description <br> de **moins** de 20 caractères  | **Erreur** : Le programme arrête l'exécution | Haute |
|| **CT - 005** | Ajouter une déclaration avec la **bonne** structure | Le fichier d’entrée JSON <br> est **bien** structuré | La déclaration est valide | Basse |
|| **CT - 006** | Ajouter une déclaration avec une **structure** <br> **autre** que celle demandée | Le fichier d’entrée JSON <br> **n'est pas bien** structuré correctement  | **Erreur** : Le programme arrête l'exécution | Haute |
|| **CT - 007** | Ajouter une déclaration avec le champ **nom** <br> manquant ou vide | Le fichier d’entrée JSON n'a pas de champ nom <br> ou celui-ci est vide | ??? | Basse |
|| **CT - 008** | Ajouter une déclaration avec le champ **prénom** <br> manquant ou vide | Le fichier d’entrée JSON n'a pas de champ prénom <br> ou celui-ci est vide | ??? | Basse |
|| **CT - 009** | Ajouter une déclaration avec le champ "heures_transferees_du_cycle_precedent" <br> avec une valeur **inférieure ou égale à 7 et positive** | "heures_transferees_du_cycle_precedent" <br> a une valeur **inférieure ou égale à 7 et positive** | Le champ "heures_transferees_du_cycle_precedent" est valide  | Haute |
|| **CT - 010** | Ajouter une déclaration avec le champ "heures_transferees_du_cycle_precedent" <br> avec une valeur **supérieure à 7 ou négative** | "heures_transferees_du_cycle_precedent" <br> a une valeur **supérieure à 7 ou négative** | **Erreur** : "Le nombre d'heures transferees <br> depasse la limite permise, seulement 7h seront transferees" | Moyenne |
|| **CT - 011** | Ajouter une déclaration avec une activité <br> **qui n'est pas** dans une catégorie valide |  Une activité avec une catégorie ∉ {catégories valides} | **Erreur** : "La catégorie n'existe pas dans la banque de catégories" | Moyenne |
|| **CT - 012** | Ajouter une déclaration avec une activité <br> dans une catégorie valide | Une activité avec une catégorie valides | L'activité est valide | Basse |
| **ST - 002** | **CT - 013** | Ajouter une déclaration **d'architecte** avec le numéro de permis <br> commençant par A ou T suivi de 4 chiffres | Le numéro de permis commence <br> par A ou T suivi de 4 chiffres| La déclaration est valide | Basse |
|| **CT - 014** | Ajouter une déclaration **d'architecte** avec le numéro de permis <br> **ne commençant pas** par A ou T suivi de 4 chiffres | Le numéro de permis **ne commence pas** <br> par A ou T suivi de 4 chiffres| **Erreur** : Le programme arrête l'exécution  | Haute |
| **ST - 003** | **CT - 015** | Ajouter une déclaration **de psychologue** avec le numéro de permis <br> commençant par 5 chiffres suivis d’un ‘-’ et de 2 chiffres | Le numéro de permis commence <br> par 5 chiffres suivis d’un ‘-’ et de 2 chiffres| La déclaration est valide | Basse |
|| **CT - 016** | Ajouter une déclaration **de psychologue** avec le numéro de permis <br> **ne commençant pas** par 5 chiffres suivis d’un ‘-’ et de 2 chiffres | Le numéro de permis **ne commence pas** <br> par 5 chiffres suivis d’un ‘-’ et de 2 chiffres| **Erreur** : "Le numero de permis du psychologue n'est pas du bon format <br> (5 Chiffres suivit d'un trait d'union et 2 chiffres de plus)" Le programme arrête l'exécution | Haute |
| **ST - 004** | **CT - 017** | Ajouter une déclaration **de géologues** avec <br> le champ d’heures transférées **présent** | Le champ d’heures transférées est **présent** <br> dans la déclaration | **Erreur** : Le programme arrête l'exécution | Haute |
|| **CT - 018** | Ajouter une déclaration **de géologues** avec <br> le champ d’heures transférées **absent**| Le champ d’heures transférées est **absent** <br> dans la déclaration | La déclaration est valide | Basse |
|| **CT - 019** | Ajouter une déclaration **de géologues** avec <br> le numéro de permis correspondant à <br> 2 lettres (pour Nom,Prénom) et de 4 chiffres | Le numéro de permis contient 2 lettres (pour Nom,Prénom) et 4 chiffres | La déclaration est valide | Basse |
|| **CT - 020** | Ajouter une déclaration **de géologues** avec <br> le numéro de permis **ne correspondant pas à** <br> 2 lettres (pour Nom,Prénom) et 4 chiffres | Le numéro de permis **ne contient pas** 2 lettres (pour Nom,Prénom) et 4 chiffres | **Erreur** : Le programme arrête l'exécution | Haute |
| **ST - 005** | **CT - 021** | Ajouter une déclaration **de podiatres** avec le numéro de permis <br> **ne contenant pas** 5 chiffres | Le numéro de permis **ne contient pas** 5 chiffres | **Erreur** : Le programme arrête l'exécution | Haute |
|| **CT - 022**| Ajouter une déclaration **de podiatres** avec le numéro de permis <br> **contenant** 5 chiffres | Le numéro de permis **contient** 5 chiffres | La déclaration est valide | Basse |
| **ST - 006** | **CT - 023** | Ajouter une déclaration **sans** le minimum de 40 heures dans le cycle | Le cycle **n'accumule pas** 40 heures total | **Erreur** : ???| Moyenne |
|| **CT - 024** | Ajouter une déclaration **n'ayant pas** 17h dans les catégories <br> "cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée" | La déclaration **n'a pas** 17h dans les catégories <br> "cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée" | **Erreur** : "Les heures totales de l'ensemble des categories <br> n'est pas pas superieur a 17h"| Moyenne |
|| **CT - 025** | Ajouter une déclaration **n'ayant pas** 23h dans la catégorie présentation | Le fichier d’entrée JSON **n'a pas** 23h total accumulées pour les <br> activités dans la catégorie présentation | **Erreur** : "La catégorie présentation doit avoir au minimum 23h"| Moyenne |
|| **CT - 026** | Ajouter une déclaration **n'ayant pas** 17h dans la catégorie groupe de discussion | Le fichier d’entrée JSON **n'a pas** 17h total accumulées pour les <br> activités dans la catégorie groupe de discussion | **Erreur** : "La catégorie groupe de discussion doit avoir au minimum 17h"| Moyenne |
|| **CT - 027** | Ajouter une déclaration **n'ayant pas** 23h dans la catégorie projet de recherche | Le fichier d’entrée JSON **n'a pas** 23h total accumulées pour les <br> activités dans la catégorie projet de recherche | **Erreur** : "La catégorie projet de recherche doit avoir au minimum 23h"| Moyenne |
|| **CT - 028** | Ajouter une déclaration **n'ayant pas** 17h dans la catégorie rédaction professionnelle | Le fichier d’entrée JSON **n'a pas** 17h total accumulées pour les <br> activités dans la catégorie rédaction professionnelle | **Erreur** : "La catégorie rédaction professionnelle doit avoir au minimum 17h"| Moyenne |
|| **CT - 029** | Ajouter une déclaration avec une activité **ayant plus de** 10h comme durée | Le fichier d’entrée JSON a **plus** de 10h pour une activité | **Erreur** : "Certaines des heures de l'activité n'ont pas été comptabilisé <br> lors du calcul d'heures (limite de 10 heures par jour)" | Moyenne |
|| **CT - 030** | Ajouter une déclaration avec une activité **ayant moins de** 1h comme durée | Le fichier d’entrée JSON a **moins** de 1h pour une activité | **Erreur** : "L'activité n'a pas un nombre valide d'heures" Le programme arrête l'exécution | Haute |
| **ST - 007** | **CT - 031** | Ajouter une déclaration **d'architecte** **sans** le minimum de 42 heures dans le cycle | Le cycle **n'accumule pas** 42 heures total | **Erreur** : ???| Moyenne |
| **ST - 008** | **CT - 032** | Ajouter une déclaration **de psychologue** **sans** le minimum de 90 heures dans le cycle | Le cycle **n'accumule pas** 90 heures total | **Erreur** : ???| Moyenne |
|| **CT - 033** | Ajouter une déclaration **de psychologue** **n'ayant pas** 25h dans la catégorie cours | Le fichier d’entrée JSON **n'a pas** 25h total accumulées pour les <br> activités dans la catégorie cours  | **Erreur** : "La catégorie cours doit avoir au minimum 25h" | Moyenne |
|| **CT - 034** | Ajouter une déclaration **de psychologue** **n'ayant pas** 15h dans la catégorie conférence | Le fichier d’entrée JSON **n'a pas** 15h total accumulées pour les <br> activités dans la catégorie conférence  | **Erreur** : "La catégorie conférence doit avoir au minimum 15h" | Moyenne |
| **ST - 009** | **CT - 035** | Ajouter une déclaration **de géologue** **sans** le minimum de 55 heures dans le cycle | Le cycle **n'accumule pas** 55 heures total | **Erreur** : ???| Moyenne |
|| **CT - 036** | Ajouter une déclaration **de géologue** **n'ayant pas** 22h dans la catégorie cours | Le fichier d’entrée JSON **n'a pas** 22h total accumulées pour les <br> activités dans la catégorie cours  | **Erreur** : "La catégorie cours doit avoir au minimum 22h" | Moyenne |
|| **CT - 037** | Ajouter une déclaration **de géologue** **n'ayant pas** 3h dans la catégorie projet de recherche | Le fichier d’entrée JSON **n'a pas** 3h total accumulées pour les <br> activités dans la catégorie projet de recherche  | **Erreur** : "La catégorie projet de recherche doit avoir au minimum 3h" | Moyenne |
|| **CT - 038** | Ajouter une déclaration **de géologue** **n'ayant pas** 1h dans la catégorie groupe de discussion | Le fichier d’entrée JSON **n'a pas** 1h total accumulées pour les <br> activités dans la catégorie groupe de discussion  | **Erreur** : "La catégorie groupe de discussion doit avoir au minimum 1h" | Moyenne |
| **ST - 010** | **CT - 039** | Ajouter une déclaration **de podiatres** **sans** le minimum de 60 heures dans le cycle | Le cycle **n'accumule pas** 60 heures total | **Erreur** : ??? | Moyenne |
| **ST - 011** | **CT - 040** | Ajouter une déclaration avec un cycle **différent** de 2020-2022  | Le fichier d’entrée JSON **n'a pas** 2020-2022 comme cycle | **Erreur** :"Le cycle de la formation n'est pas valide" | Moyenne |
|| **CT - 041** | Ajouter une déclaration avec une catégorie dont la date <br> **n'est pas** entre le 1er avril 2020 et 1er avril 2022 si le cycle est de 2020-2022 | Le fichier d’entrée JSON a une catégorie **plus tôt** ou **plus tard** que le 1er avril 2020 et 1er avril 2022 | **Erreur** :"La date de la catégorie n'est pas valide" | Moyenne |
| **ST - 012** | **CT - 042** | Ajouter une déclaration **d'architecte** avec un cycle **différent** de 2018-2020  | Le fichier d’entrée JSON **n'a pas** 2018-2020 comme cycle | **Erreur** :"Le cycle de la formation n'est pas valide" | Moyenne |
|| **CT - 043** | Ajouter une déclaration **d'architecte** avec une catégorie dont la date <br> **n'est pas** entre 1er avril 2018 et la 1er avril 2020 si le cycle est 2018-2020 | Le fichier d’entrée JSON a une catégorie **plus tôt** ou **plus tard** que le 1er avril 2018 et le 1er avril 2020 | **Erreur** :"La date de la catégorie n'est pas valide" | Moyenne |
|| **CT - 044** | Ajouter une déclaration **d'architecte** avec un cycle **différent** de 2016-2018  | Le fichier d’entrée JSON **n'a pas** 2016-2018 comme cycle | **Erreur** :"Le cycle de la formation n'est pas valide" | Moyenne |
|| **CT - 045** | Ajouter une déclaration **d'architecte** avec une catégorie dont la date <br> **n'est pas** entre le 1er avril 2016 et la 1er juillet 2018 si le cycle est 2016-2018 | Le fichier d’entrée JSON a une catégorie **plus tôt** ou **plus tard** que le 1er avril 2016 et le 1er juillet 2018 | **Erreur** :"La date de la catégorie n'est pas valide" | Moyenne |
| **ST - 013** | **CT - 046** | Ajouter une déclaration **de psychologue** avec un cycle **différent** de 2018-2023 | Le fichier d’entrée JSON **n'a pas** 2018-2023  comme cycle | **Erreur** :"Le cycle de la formation n'est pas valide" | Moyenne |
|| **CT - 047** | Ajouter une déclaration **de psychologue** avec une catégorie dont la date <br> **n'est pas** entre 1er janvier 2018 et le 1er janvier 2023 si le cycle est 2018-2023 | Le fichier d’entrée JSON a une catégorie **plus tôt** ou **plus tard** que le 1er janvier 2018 et le 1er janvier 2023 | **Erreur** :"La date de la catégorie n'est pas valide" | Moyenne |
| **ST - 014** | **CT - 048** | Ajouter une déclaration **de géologue** avec un cycle **différent** de 2018-2021 | Le fichier d’entrée JSON **n'a pas** 2018-2021 comme cycle | **Erreur** :"Le cycle de la formation n'est pas valide" | Moyenne |
|| **CT - 049** | Ajouter une déclaration **de géologue** avec une catégorie dont la date <br> **n'est pas** entre le 1er juin 2018 et le 1er juin 2021 si le cycle est 2018-2021  | Le fichier d’entrée JSON a une catégorie **plus tôt** ou **plus tard** que le 1er juin 2018 et le 1er juin 2021 | **Erreur** :"La date de la catégorie n'est pas valide" | Moyenne |
| **ST - 015** | **CT - 050** | Mettre à jour le nombre de déclarations traitées | Le fichier d’entrée JSON est une déclaration | Le nombre total de déclarations traitées a été incrémenté de 1 | Moyenne |
|| **CT - 051** | Mettre à jour le nombre de déclarations complètes | Le fichier d’entrée JSON est une déclaration <br> avec tous les champs remplis | Le nombre total de déclarations complètes a été incrémenté de 1 | Moyenne |
|| **CT - 052** | Mettre à jour le nombre de déclarations incomplètes ou invalides | Le fichier d’entrée JSON est une déclaration <br> avec un champ vide ou avec la mauvaise information | Le nombre total de déclarations incomplètes <br> ou invalides a été incrémenté de 1 | Moyenne |
|| **CT - 053** | Mettre à jour le nombre de déclarations faites par des hommes | Le fichier d’entrée JSON est une déclaration <br> avec le champ sexe équivalent à homme | Le nombre total de déclarations faites par des hommes <br> a été incrémenté de 1 | Moyenne |
|| **CT - 054** | Mettre à jour le nombre de déclarations faites par des femmes | Le fichier d’entrée JSON est une déclaration <br> avec le champ sexe équivalent à femme | Le nombre total de déclarations faites par des femmes <br> a été incrémenté de 1 | Moyenne |
|| **CT - 055** | Mettre à jour le nombre de déclarations faites par des individus de sexe inconnu | Le fichier d’entrée JSON est une déclaration <br> avec le champ sexe équivalent à inconnu | Le nombre total de déclarations faites par des <br> individus de sexe inconnu a été incrémenté de 1 | Moyenne |
|| **CT - 056** | Mettre à jour le nombre d'activités total | Le fichier d’entrée JSON est une déclaration <br> avec au moins une activité | Le nombre total d'activités a été incrémenté de 1 | Moyenne |
|| **CT - 057** | Mettre à jour le nombre d'activités pour chaque catégorie | Le fichier d’entrée JSON est une déclaration avec au <br> moins une activité dans une catégorie | Le nombre d'activités par catégorie a été incrémenté de 1 <br> pour la catégorie en question | Moyenne |
|| **CT - 058** |  Mettre à jour le nombre de déclarations valides et complètes <br> pour le bon ordre professionnel | Le fichier d’entrée JSON est une déclaration valide et complète | Le nombre total de déclarations valides et complètes a été incrémenté de 1 <br> pour le bon ordre professionnel | Moyenne |
|| **CT - 059** |  Mettre à jour le nombre de déclarations valides et imcomplètes <br> pour le bon ordre professionnel | Le fichier d’entrée JSON est une déclaration valide et incomplètes | Le nombre total de déclarations valides et imcomplètes a été incrémenté de 1 <br> pour le bon ordre professionnel | Moyenne |
|| **CT - 060** | Mettre à jour le nombre de déclarations avec un mauvais numéro de permis | Le fichier d’entrée JSON est une déclaration <br> avec un numéro de permis invalide | Le nombre de déclarations soumises avec un numéro de permis invalide a été incrémenté de 1 | Moyenne |