SELECT v.catV
FROM Deplacement d,Utilisateur u, Vehicule v
WHERE d.numCB=u.numCB AND v.idV=d.idV --AND u.dateNaissance BETWEEN(2001--)
GROUP BY v.catV
HAVING sum(d.DateDepart-d.DateArrivee)=
(SELECT max(sum(d.DateDepart-d.DateArrivee))
FROM Deplacement d,Utilisateur u, Vehicule v
WHERE d.numCB=u.numCB AND v.idV=d.idV 
GROUP BY v.catV
);