# TP Développement d'applications réparties – Client/Serveur (Calculatrice)

- **Étudiant(e)** : *Nom Prénom*
- **Classe** : LSI3
- **Matière** : Développement d'applications réparties
- **Répertoire de travail** : `NomPrenom_LSI3_DevAppReparties`

## 1. Objectif

Étendre les applications Client/Serveur de l'Activité 2 du TP N°1, qui échangeaient un seul octet, afin de permettre l'échange d'informations plus complexes liées à une commande. Le projet comporte deux démarches :

1. **Démarche 1** : le client envoie une opération entière (ex. `55 * 25`) sous forme de trois éléments séparés : premier opérande, opérateur, deuxième opérande.
2. **Démarche 2** : le client envoie un **objet sérialisé** (`Operation`) qui contient ces trois éléments.

Dans les deux cas, le serveur calcule le résultat et le renvoie au client, qui l'affiche.

## 2. Structure du projet

```
NomPrenom_LSI3_DevAppReparties/
├── clientPackage/
│   └── client.java        # application cliente
├── serverPackage/
│   └── Server.java        # application serveur
├── commonPackage/
│   └── Operation.java     # classe Serializable (démarche 2 uniquement)
├── captures/              # captures d'écran (code et exécution)
└── README.md
```

## 3. Technologies et prérequis

- Java (JDK 8 ou supérieur) et un IDE (Eclipse)
- Sockets TCP (`java.net.Socket`, `java.net.ServerSocket`)
- Démarche 1 : `DataInputStream` / `DataOutputStream`
- Démarche 2 : `ObjectInputStream` / `ObjectOutputStream` et `java.io.Serializable`

## 4. Fonctionnement

### Démarche 1 : envoi des éléments de l'opération

1. Le client lit l'opération saisie par l'utilisateur (ex. `55 * 25`).
2. Il **valide la syntaxe** avec une expression régulière avant l'envoi : entier, un opérateur parmi `+ - * /`, entier. Les saisies comme `25 ** 55` ou `abc + 3` sont refusées et rien n'est envoyé au serveur.
3. Il envoie, dans l'ordre : `writeBoolean(true)`, `writeInt(x)`, `writeUTF(operateur)`, `writeInt(y)`.
4. Le serveur lit les trois éléments, calcule dans la méthode `calculer()` et renvoie le résultat avec `writeUTF`.
5. Le client lit le résultat avec `readUTF` et l'affiche.

Pour quitter, le client tape `q` : il envoie `writeBoolean(false)` et le serveur répond « Au revoir ! ».

### Démarche 2 : échange d'objets

1. La classe `Operation` implémente `Serializable` et contient `operande1`, `operateur` et `operande2`.
2. Le client crée un objet `Operation` et l'envoie avec `writeObject`.
3. Le serveur reçoit l'objet avec `readObject`, récupère ses données avec les getters et effectue le calcul.
4. Le serveur renvoie le résultat sous forme de texte avec `writeObject`, et le client l'affiche.

Pour quitter, le client envoie le texte `"QUIT"` à la place d'un objet `Operation`.

**Remarque** : `Operation` doit avoir le même package (`commonPackage`) et le même nom côté client et côté serveur. Dans chaque application, le flux de sortie est créé et vidé (`flush`) avant le flux d'entrée, pour éviter un blocage au démarrage.

## 5. Opérations gérées

| Opérateur | Opération | Remarque |
|-----------|-----------|----------|
| `+` | Addition | calcul en `long` pour éviter le dépassement |
| `-` | Soustraction | résultat négatif possible |
| `*` | Multiplication | calcul en `long` |
| `/` | Division | résultat décimal ; la division par zéro renvoie un message d'erreur |

Les opérandes sont des entiers 32 bits.

## 6. Exécution

1. Vérifier que `SERVER_IP` et `PORT` (1234) sont **identiques** dans `client.java` et `Server.java`.
2. Lancer d'abord `Server` (clic droit → *Run As → Java Application*).
3. Lancer ensuite `client`.
4. Saisir une opération, par exemple `55 * 25`.
5. Saisir `q` pour terminer la session.

Si le port est déjà utilisé (`Address already in use`), arrêter les anciens processus avec le carré rouge de la console Eclipse.

## 7. Captures d'écran

### Démarche 1 : envoi des éléments de l'opération

| Capture | Description |
|---------|-------------|
| `code_client1_1` | Code du client (1re partie) |
| `code_client1_2` | Code du client (2e partie) |
| `code_server1_1` | Code du serveur (1re partie) |
| `code_server1_2` | Code du serveur (2e partie) |
| `connexion1` | Connexion entre le client et le serveur |
| `resultat_possible1` | Exemple d'exécution : résultats des opérations |
| `resultats_possible2` | Exemple d'exécution : cas particuliers (division par zéro, syntaxe invalide, fermeture) |

### Démarche 2 : échange d'objets

| Capture | Description |
|---------|-------------|
| `code_client2` | Code du client qui envoie l'objet `Operation` |
| `code_server2` | Code du serveur qui reçoit l'objet et calcule |

![code_client1_1](captures/code_client1_1.png)
![code_client1_2](captures/code_client1_2.png)
![code_server1_1](captures/code_server1_1.png)
![code_server1_2](captures/code_server1_2.png)
![connexion1](captures/connexion1.png)
![resultat_possible1](captures/resultat_possible1.png)
![resultats_possible2](captures/resultats_possible2.png)
![code_client2](captures/code_client2.png)
![code_server2](captures/code_server2.png)

## 8. Exemple d'exécution

```
Opération : 55 * 25
Résultat : 55 * 25 = 1375

Opération : 10 / 4
Résultat : 10 / 4 = 2.5

Opération : 5 / 0
Résultat : Erreur : division par zéro impossible

Opération : 25 ** 55
Syntaxe invalide. Format attendu : <entier> <+|-|*|/> <entier>

Opération : q
Au revoir !
```