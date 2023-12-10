Aller dans le bashrc pour changer les variables d'environnement : 
```
vim ~/.bashrc
```
Taper i
Rajouter les lignes suivantes à la fin :
```
export PATH=/usr/gide/jdk-1.8.0_261/bin:$PATH
export JAVA_HOME=/usr/gide/jdk-1.8.0_261
export JBOSS_HOME=/home1/id474124/Desktop/Licence1/M2/BDED/TP/TP4/wildfly-26.1.3.Final
export PATH=$JBOSS_HOME/bin:$PATH
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
export JBOSS_HOME=$HOME/Bureau/MASTER_2/BD-ED/wildfly-26.1.3.Final
export PATH=$JBOSS_HOME/bin:$PATH


Taper sur la touche échap puis taper ":wq"

Aller dans wildfly-26.1.3.Final

Dans un terminal lancer le serveur avec la commande suivante : 
```
./bin/standalone.sh --server-config=standalone-full-ha.xml
```

Créer un utilisateur dans un autre terminal avec la commande :
```
add-user.sh. 
```

Le premier utilisateur rentré est le suivant :
    - username : iliassou
    - password : iliassou
    - pas de groupe - yes - yes

Pour lancer le EJB2 aller sur le site Wildfly à l'adresse suivante : http://localhost:9990/console/index.html#home

Pour lancer le EJB3 lancer la commande suivantes dans le dossier exemple du projet:
```
mvn clean package wildfly:deploy
```

aller sur le site Wildfly à l'adresse suivante : http://localhost:9990/console/index.html#home
Aller dans "Deployments"
Puis cliquer sur le lien sur la ligne : "Context Roots"

Changer la connexion si vous voulez tester avec une bd oracle:
- Aller sur le site Wildfly à l'adresse suivante : http://localhost:9990/console/index.html#home
- Aller dans "Deployments"
- Appuyer sur le bouton "+"
- Puis sur "Upload Deployment"
- Et donner le .jar de ojdbc6
- Aller dans l'onglet Configuration 
- Cliquer sur Subsystems --> Datasources & Drivers --> Datasources --> Bouton +
- Sélectionner Oracle
- Cliquer sur Next
    - Name : OracleDS
    - JNDI : java:/OracleDS
- Cliquer sur Next
    - Changer le driver name par ojdbc8.jar
- Cliquer sur Next
    - url = "jdbc:oracle:thin:@eluard:1521:eluard2023";
    - user = "id474124";
    - mdp = "id474124";
- Cliquer sur Next
- Tester la connexion
- Aller dans le dossier à l'adresse suivante : "---> exemple-ejb/src/main/resources/META-INF"
- Changer le fichier "persistence.xml"
- en modifiant la connexion "java:jboss/datasources/exempleDS" par "java:/OracleDS"
