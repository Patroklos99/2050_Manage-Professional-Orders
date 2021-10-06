# Projet TP1 2050

## _README_
Description des technologies présentes et du fonctionnement(compilation et execution) ----

## Description du fonctionnement (en ordre d'exécution)
- Lit les deux premiers arguments lors du lancement du programme et les associe aux fichier d'entrée et sortie.
- Formatte le fichier d'entrée de type .json pour créer un objet JSON 
  recherchable.
- Créer un objet de la classe FormationContinue pour stocker les informations de l'objet JSON.
- Créer un objet de la classe Verification avec l'objet de la classe FormationContinue et le fichier de sortie.
- À sa création, l'objet verificateur effectura lui-même les vérifications selon les exigences demandées.
- À chaque vérification, si celle-ci échoue, un message sera ajouté à la liste de messages d'erreurs.
- À la fin de toutes les vérifications ou si la vérification de cycle échoue,
  tout les messages d'erreurs seront enregistré dans le fichier de sortie en 
  format JSON.


## Particularités 
- Notre code respecte la convention d'écriture camel case.
- La langue choisie pour l'écriture du projet est le français.
- Chacune des méthodes ne fait pas plus de 10 lignes.
- Aucune ligne dans le projet ne dépasse 80 caractères.
- Peu de commentaires car les noms de variables/méthodes/attributs donnent assez d'information par eux-même.

