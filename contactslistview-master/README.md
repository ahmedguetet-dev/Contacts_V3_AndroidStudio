# TP Android - Gestion des contacts V3

Projet Kotlin/XML : fragments, RecyclerView, recherche et Google Maps.

## Ouvrir
1. Décompresser le ZIP.
2. Android Studio > Open > sélectionner le dossier `contactslistview-master`.
3. Attendre Gradle Sync et accepter les téléchargements de dépendances.

## Google Maps (clé non incluse)
1. Créer/configurer un projet Google Cloud et activer Maps SDK for Android.
2. Créer une clé API et la restreindre à Android (package `com.example.contacts` + SHA-1 de debug) et à Maps SDK for Android.
3. Ajouter la clé dans le fichier `gradle.properties` local, à la ligne `MAPS_API_KEY=TA_CLE`.
4. Ne jamais commit/publier cette clé. Le fichier gradle.properties est ignoré par Git.

La carte ne peut afficher les tuiles sans clé valide. Les contacts et changements sont gardés en mémoire durant l'exécution : stockage persistant non demandé par l'énoncé.

## Fonctionnalités
- Liste via RecyclerView, photo, nom/prénom et téléphone
- Recherche live sur nom/prénom/téléphone, insensible à la casse
- Détail/édition et validation des champs obligatoires
- Galerie et caméra (photo caméra temporaire en mémoire)
- Intent Appeler et e-mail
- Coordonnées GPS facultatives, validation de plages
- Fragment Google Maps avec marker; retour via Back
- Données partagées via Activity-scoped ViewModel
