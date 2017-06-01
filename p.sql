DROP TABLE Categorie CASCADE CONSTRAINT;

DROP TABLE Vehicule CASCADE CONSTRAINT;

DROP TABLE Utilisateur CASCADE CONSTRAINT;

DROP TABLE Forfait CASCADE CONSTRAINT;

DROP TABLE ForfaitDI CASCADE CONSTRAINT;

DROP TABLE ForfaitDL CASCADE CONSTRAINT;

DROP TABLE Station CASCADE CONSTRAINT;

DROP TABLE Deplacement CASCADE CONSTRAINT;



-- Projet BD --
-- Initialisation de la SGBD --
-- Commandes sql pour la création des relations --*/i



CREATE TABLE Station( nom_station       VARCHAR(32)     PRIMARY KEY,
		AddrSt          VARCHAR(32),
		nbPlacesVelo      INT,
		nbPlacesVelE      INT,
		nbPlacesVrem      INT,
        nbPlacesVoiE      INT,
		nbPlacesUtil       INT);


insert into Station (nom_station,AddrSt,nbPlacesVelo,nbPlacesVelE,nbPlacesVrem,nbPlacesUtil,nbPlacesVoiE) VALUES ('Chavant','3 rue chavant',10,10,10,10,10);

insert into Station (nom_station,AddrSt,nbPlacesVelo,nbPlacesVelE,nbPlacesVrem,nbPlacesUtil,nbPlacesVoiE) VALUES ('gieres','3 rue chavant',10,10,10,10,10);

insert into Station (nom_station,AddrSt,nbPlacesVelo,nbPlacesVelE,nbPlacesVrem,nbPlacesUtil,nbPlacesVoiE) VALUES ('stade des alpes','3 rue chavant',10,10,10,10,10);

insert into Station (nom_station,AddrSt,nbPlacesVelo,nbPlacesVelE,nbPlacesVrem,nbPlacesUtil,nbPlacesVoiE) VALUES ('Victor Hugo','3 rue chavant',10,10,10,10,10);



CREATE TABLE Utilisateur(numCB          VARCHAR(32)     PRIMARY KEY,
		nomAbonne       VARCHAR(32),
		prenomAbonne    VARCHAR(32),
		dateNaissance   DATE,
		addrU           VARCHAR(32) );

insert into Utilisateur(numCB, nomAbonne, prenomAbonne,dateNaissance, addrU) VALUES ('Carte1','Bayard','Guillaume',TO_DATE('06-14-1993', 'MM-DD-YYYY'),NULL);

insert into Utilisateur(numCB, nomAbonne, prenomAbonne,dateNaissance, addrU) VALUES ('Carte2', 'Lher', 'Etienne', TO_DATE('08-11-1994', 'MM-DD-YYYY'),NULL);

insert into Utilisateur(numCB, nomAbonne, prenomAbonne,dateNaissance, addrU) VALUES ('Carte3', 'Marchal', 'Emmanuel', TO_DATE('11-08-1993', 'MM-DD-YYYY'),NULL);

insert into Utilisateur(numCB, nomAbonne, prenomAbonne,dateNaissance, addrU) VALUES ('Carte4', 'Duck', 'Donald', TO_DATE('07-15-1958', 'MM-DD-YYYY'),NULL);

insert into Utilisateur(numCB, nomAbonne, prenomAbonne,dateNaissance, addrU) VALUES ('Carte5', 'Piolle', 'Eric', TO_DATE('06-01-1973', 'MM-DD-YYYY'),NULL);

CREATE TABLE Categorie(catV             VARCHAR(32)     PRIMARY KEY,
		dureeMaxUtil    INT,
		prixHoraire     INT,
		caution INT);

insert into Categorie(catV, dureeMaxUtil, prixHoraire, caution) VALUES ('Voiture Electrique', NULL, NULL, NULL);

insert into Categorie(catV, dureeMaxUtil, prixHoraire, caution) VALUES ('Velo', NULL, NULL, NULL);

insert into Categorie(catV, dureeMaxUtil, prixHoraire, caution) VALUES ('Velo Electrique', NULL, NULL, NULL);

insert into Categorie(catV, dureeMaxUtil, prixHoraire, caution) VALUES ('Velo Remorque', NULL, NULL, NULL);

insert into Categorie(catV, dureeMaxUtil, prixHoraire, caution) VALUES ('Utilitaire', NULL, NULL, NULL);

CREATE TABLE Forfait( idForfait VARCHAR(32)     PRIMARY KEY,
                numCB           VARCHAR(32),
                catV            VARCHAR(32),
                FOREIGN KEY(numCB)              REFERENCES Utilisateur(numCB),
                FOREIGN KEY(catV)               REFERENCES Categorie(catV));

insert into Forfait(idForfait, numCB, catV) values ('VeloGuillaume', 'Carte1', 'Velo');

insert into Forfait(idForfait, numCB, catV) values ('VoitureEleEtienne', 'Carte2', 'Voiture Electrique');

insert into Forfait(idForfait, numCB, catV) values ('VeloEmmanuel', 'Carte3', 'Velo');

insert into Forfait(idForfait, numCB, catV) values ('VeloElecGuillaume', 'Carte1', 'Velo Electrique');

insert into Forfait(idForfait, numCB, catV) values ('VeloRemEtienne', 'Carte2', 'Velo Remorque');

insert into Forfait(idForfait, numCB, catV) values ('UtilEmmanuel', 'Carte3', 'Utilitaire');

insert into Forfait(idForfait, numCB, catV) values ('VoitureEleDonald', 'Carte4', 'Voiture Electrique');

insert into Forfait(idForfait, numCB, catV) values ('VeloDonald', 'Carte4', 'Velo');

insert into Forfait(idForfait, numCB, catV) values ('VeloEleDonald', 'Carte4', 'Velo Electrique');

insert into Forfait(idForfait, numCB, catV) values ('VeloEleEric', 'Carte5', 'Velo Electrique');

insert into Forfait(idForfait, numCB, catV) values ('VeloRemEric', 'Carte5', 'Velo Remorque');

insert into Forfait(idForfait, numCB, catV) values ('UtilEric', 'Carte5', 'Utilitaire');

CREATE TABLE ForfaitDL( dateDebut       DATE,
                dateFin DATE,
                prix            INT,
                idForfait       VARCHAR(32) ,
                FOREIGN KEY(idForfait)  REFERENCES Forfait(idForfait));

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('01-01-1990', 'MM-DD-YYYY'), TO_DATE('01-21-1990', 'MM-DD-YYYY'), 23, 'VeloGuillaume');

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('04-07-1990', 'MM-DD-YYYY'), TO_DATE('05-13-1990', 'MM-DD-YYYY'), 59, 'VoitureEleEtienne');

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('09-23-1990', 'MM-DD-YYYY'), TO_DATE('12-2-1990', 'MM-DD-YYYY'), 32, 'VeloEmmanuel');

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('01-01-1990', 'MM-DD-YYYY'), TO_DATE('07-04-1990', 'MM-DD-YYYY'), 57, 'VoitureEleDonald');

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('05-06-1990', 'MM-DD-YYYY'), TO_DATE('04-10-1990', 'MM-DD-YYYY'), 17, 'VeloDonald');

insert into ForfaitDL(dateDebut, dateFin, prix, idForfait) values (TO_DATE('05-23-1990', 'MM-DD-YYYY'), TO_DATE('06-28-1990', 'MM-DD-YYYY'), 19, 'VeloEleEric');

CREATE TABLE ForfaitDI(prix             INT,
                nbLoc           INT,
                idForfait       VARCHAR(32),
                FOREIGN KEY(idForfait)          REFERENCES Forfait(idForfait));

insert into ForfaitDI(prix, nbLoc, idForfait) values (25, 15, 'VeloElecGuillaume');

insert into ForfaitDI(prix, nbLoc, idForfait) values (121,19, 'VeloRemEtienne');

insert into ForfaitDI(prix, nbLoc, idForfait) values (135, 54, 'UtilEmmanuel');

insert into ForfaitDI(prix, nbLoc, idForfait) values (120,50, 'VeloEleDonald');

insert into ForfaitDI(prix, nbLoc, idForfait) values (37, 22, 'VeloRemEric');

insert into ForfaitDI(prix, nbLoc, idForfait) values (17, 36, 'UtilEric');


CREATE TABLE Vehicule(idV       VARCHAR(32)             PRIMARY KEY,
		nbPlaces        INT,
		catV  VARCHAR(32),
		stationCourante VARCHAR(32),
		FOREIGN KEY(catV) 		REFERENCES Categorie(catV));


insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('Honda', 2, 'Voiture Electrique', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('Velib',20, 'Velo', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('BMW', 4, 'Velo Electrique', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('Nissan',4, 'Velo Remorque', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('Bycle',1, 'Utilitaire', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('Test<1h', 2, 'Voiture Electrique', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('TestDureeTropGrande',20, 'Velo', NULL);

insert into Vehicule(idV, nbPlaces, catV, stationCourante) values ('TestBonneDuree', 12, 'Velo Electrique', NULL);



CREATE TABLE Deplacement(
		DateDepart      DATE,
		DateArrivee     DATE,
		idV             VARCHAR(32),
		numCB           VARCHAR(32),
		stationDepart   VARCHAR(32),
		stationArrivee  VARCHAR(32) ,
		FOREIGN KEY(stationDepart)      REFERENCES Station(nom_station),
		FOREIGN KEY(stationArrivee)     REFERENCES Station(nom_station),
		FOREIGN KEY(numCB)              REFERENCES Utilisateur(numCB),
		FOREIGN KEY(idV)                REFERENCES Vehicule(idV));



	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('01-01-1990','MM-DD-YYYY'),TO_DATE('01-11-1990','MM-DD-YYYY'), 'Velib', 'Carte2', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('01-14-1990','MM-DD-YYYY'), TO_DATE('01-22-1990', 'MM-DD-YYYY'), 'BMW', 'Carte3', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('11-13-1990','MM-DD-YYYY'), TO_DATE('12-02-2990', 'MM-DD-YYYY'), 'Nissan', 'Carte3', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('03-25-1990','MM-DD-YYYY'), TO_DATE('04-16-2100', 'MM-DD-YYYY'), 'Honda', 'Carte1', NULL, NULL);




	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('12-02-1990 09:27:35','MM-DD-YYYY HH:MI:SS'),TO_DATE('12-23-1990 08:45:13','MM-DD-YYYY HH:MI:SS'), 'Velib', 'Carte2', 'Chavant', NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('06-14-1990','MM-DD-YYYY'), TO_DATE('06-22-1990 ', 'MM-DD-YYYY'), 'BMW', 'Carte3', 'stade des alpes', NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('11-13-1990','MM-DD-YYYY'), TO_DATE('12-02-1990 ', 'MM-DD-YYYY'), 'Nissan', 'Carte3', 'gieres', NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('03-25-1990','MM-DD-YYYY'), TO_DATE('04-16-1990 ', 'MM-DD-YYYY'), 'Honda', 'Carte1', 'Victor Hugo', NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('08-25-1990','MM-DD-YYYY'), TO_DATE('09-13-1990', 'MM-DD-YYYY'), 'Honda', 'Carte2', 'gieres', NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('06-21-1990','MM-DD-YYYY'), TO_DATE('09-30-1990 ', 'MM-DD-YYYY'), 'Velib', 'Carte2','Chavant','Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('08-07-1990','MM-DD-YYYY'), TO_DATE('08-21-1990', 'MM-DD-YYYY'), 'BMW', 'Carte1', 'gieres', 'Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('04-04-1990','MM-DD-YYYY'), TO_DATE('04-11-1990', 'MM-DD-YYYY'), 'Nissan', 'Carte3','Chavant', 'Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('12-27-1990','MM-DD-YYYY'), TO_DATE('12-30-1990', 'MM-DD-YYYY'), 'Honda', 'Carte2','Chavant', 'Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('02-11-1990','MM-DD-YYYY'),TO_DATE('02-12-1990','MM-DD-YYYY'), 'Bycle', 'Carte1', NULL , NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('12-02-1990 08:27:35','MM-DD-YYYY HH:MI:SS'),TO_DATE('12-02-1990 08:45:13','MM-DD-YYYY HH:MI:SS'), 'Test<1h', 'Carte2', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('12-02-1990 08:27:35','MM-DD-YYYY HH:MI:SS'),TO_DATE('12-02-1990 09:28:13','MM-DD-YYYY HH:MI:SS'), 'TestBonneDuree', 'Carte2', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('08-07-1990','MM-DD-YYYY'), TO_DATE('08-21-2200 12:00:00', 'MM-DD-YYYY HH:MI:SS'), 'TestDureeTropGrande', 'Carte1', NULL, NULL);

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('12-02-1990 08:27:35','MM-DD-YYYY HH:MI:SS'),NULL, 'Test<1h', 'Carte2', 'Chavant', 'Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values (TO_DATE('12-02-1990 08:27:35','MM-DD-YYYY HH:MI:SS'),NULL, 'TestBonneDuree', 'Carte2', 'Chavant', 'Chavant');

	INSERT into Deplacement( DateDepart, DateArrivee, idV, numCB, stationDepart, stationArrivee) values ( TO_DATE('08-07-1990','MM-DD-YYYY'), NULL, 'TestDureeTropGrande', 'Carte1', 'Victor Hugo', 'Chavant');
