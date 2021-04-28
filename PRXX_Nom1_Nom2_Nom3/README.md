# Devoir 2021

Structure de départ pour le devoir sur le jeu "Labyrinthe de la Vallée des Rois".

Si vous le souhaitez, vous pouvez changer l'organisation ou supprimer les fichiers présents initialement dans ce dépôt.
Les seuls fichiers que vous devez impérativement conserver sont les suivants :

- le fichier .gitignore dans la racine du projet
- l'interface IChallenger.java
- la classe MyChallenger.java
- la classe Client.java (utile pour la partie 3)
- la classe Message.java (utile pour la partie 3)

Ligne de commande pour executer le .jar:

java -cp PR1H.jar:commons-cli-1.4.jar iialib.games.contest.Client -p 4536 -s localhost -c games.kingsvalley.MyChallenger
