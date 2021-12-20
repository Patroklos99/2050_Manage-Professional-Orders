# Document de flux de travail
### Notre projet aura les branches suivantes : 
* master
* dev

Les branches individuelles :

* Renzo
* William
* Nicolas
* Lysanne

Chaque fonctionnalité sera développée sur les branches individuelles.
Quand la fonctionnalité est terminée, elle sera "push" sur la branche "dev" 
qui contiendra l'étendu des modifications fonctionnelles. L'équipe pourra
ensuite "pull" à partir de la branche "dev" pour actualiser leur projet si
les modifications sont importantes et impact le développement de futures 
fonctionnalités. 

Une fois toutes les fonctionnalités sur la branche "dev" 
terminées, on effectue un merge request sur master (qui représente la 
branche de production) pour finaliser l'implémentation du tout. 

Le projet aura aussi une branche "fix" qui servira à 
corriger les imperfections que la branche de production pourrais avoir à 
la suite des implémentations.