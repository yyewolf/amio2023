# AMIO 2023 : TAPO

## Authors
- [Aurélien TRONCY](https://github.com/Nounoursdestavernes)
- [Tristan Smagghe](https://github.com/yyewolf)

## Description
L'objectif de ce mini-projet est de finaliser une application Android exploitant des données provenant d'un réseau de capteurs et exposées à travers un service web (IoTLab de TELECOM Nancy). L'objectif principal est de détecter les lumières laissées allumées dans les bureaux en soirée.

## Langages utilisés
- Kotlin


## Fonctionnalités
<img src="/png/home.png" alt="home page" style="max-width: 100%;">
- Journalisation de l'ensemble des actions de l'application (utilisation de Log.d).
- Mise en place d'un service asynchrone périodique de récupération des données du web service (récupération toutes les 30 secondes).
- Switch permettant de lancer le service depuis l'interface utilisateur
- Intégration d'une checkbox permettant à l'utilisateur de choisir de lancer le service au démarrage de l'application.
- Ajout d'un bouton permettant d'effectuer une requête manuelle à l'API. Lorsque l'utilisateur clique sur le bouton, un toast s'affiche pour indiquer que la requête est en cours.
- Affichage des données récupérées sur l'interface utilisateur.
<img src="/yyewolf/amio2023/raw/main/png/home_data.png" alt="data" style="max-width: 50%;">
- Affichage des changements de statut des lumières sur l'interface utilisateur.
<img src="/yyewolf/amio2023/raw/main/png/change_status.png" alt="status" style="max-width: 50%;">
- Envoi d'une notification à l'utilisateur lorsqu'une lumière est détectée allumée (uniquement le week-end ou entre 18h et 23h en semaine).
<img src="/yyewolf/amio2023/raw/main/png/notif.png" alt="oui" style="max-width: 50%;">
- Envoi d'un mail à l'utilisateur lorsqu'une lumière est détectée allumée (uniquement le week-end ou entre 18h et 23h en semaine).
<img src="/yyewolf/amio2023/raw/main/png/mail.png" alt="oui" style="max-width: 50%;">
- Mise en place d'un menu de préférences permettant à l'utilisateur de renseigner son adresse mail et son nom d'utilisateur pour l'envoi de mails.
<img src="/yyewolf/amio2023/raw/main/png/change_mail.png" alt="oui" style="max-width: 50%;">
<img src="/yyewolf/amio2023/raw/main/png/change_name.png" alt="oui" style="max-width: 50%;">
- Intégration d'un bouton permettant de déclencher une vibration sur le téléphone de l'utilisateur. 
