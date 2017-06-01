--Requete 1
--Cas d un forfait DI :
SELECT prix FROM ForfaitDI WHERE idForfait = 'VeloElecGuillaume';
--Cas d un forfait DL :
SELECT (dateFin-dateDebut)*24*prix FROM ForfaitDL WHERE idForfait = 'VeloGuillaume';
-- Le .java est beaucoup plus précis.


--Requete 2
SELECT v.idV, SUM(DateArrivee - DateDepart)/(COUNT (*))
FROM Deplacement d, Vehicule v
where v.idV=d.idV AND DateDepart Between TO_DATE('01-01-1990', 'MM-DD-YYYY') and TO_DATE('01-31-1990','MM-DD-YYYY')
AND DateArrivee Between TO_DATE('01-01-1990', 'MM-DD-YYYY') and TO_DATE('01-31-1990','MM-DD-YYYY')
GROUP BY  v.idV;


--Requete 3
SELECT  v.idV, SUM(DateArrivee - DateDepart)/(COUNT (*))
FROM Deplacement d, Vehicule v
where v.idV=d.idV AND catV='Velo Electrique' AND DateDepart Between TO_DATE('01-01-1990', 'MM-DD-YYYY') and TO_DATE('01-31-1990','MM-DD-YYYY')
AND DateArrivee Between TO_DATE('01-01-1990', 'MM-DD-YYYY') and TO_DATE('01-31-1990','MM-DD-YYYY')
GROUP BY  catV, v.idV;


--requête 4
SELECT dateNaissance
FROM Utilisateur;

SELECT v.catV
FROM Deplacement d,Utilisateur u, Vehicule v
WHERE d.numCB=u.numCB AND v.idV=d.idV 
GROUP BY v.catV
HAVING sum(d.DateDepart-d.DateArrivee)=
(SELECT max(sum(d.DateDepart-d.DateArrivee))
FROM Deplacement d,Utilisateur u, Vehicule v
WHERE d.numCB=u.numCB AND v.idV=d.idV 
GROUP BY v.catV
);

--requêtes 5
SELECT count(MAX(DateArrivee)) AS NB 
FROM Deplacement 
WHERE stationArrivee='Chavant' AND DateArrivee IS NOT NULL AND DateArrivee<'14-NOV-90' AND DateDepart<'14-NOV-90'
GROUP BY idV;





SELECT  nbPlacesVelE
FROM Station 
WHERE nom_station='Chavant';

SELECT  nbPlacesVoiE 
FROM Station 
WHERE nom_station='Chavant';

SELECT  nbPlacesVrem
FROM Station 
WHERE nom_station='Chavant';

SELECT  nbPlacesUtil
FROM Station 
WHERE nom_station='Chavant';

SELECT  nbPlacesVelo
FROM Station 
WHERE nom_station='Chavant';