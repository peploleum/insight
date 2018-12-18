#!/bin/bash
compteur=1
#echo "interations: " $1
tableau_type=( "EMERG" "ALERT" "CRIT" "ERROR" "WARNING" "NOTICE" "INFO" "DEBUG" )
ECHELLE=8
NBR=0

while (($ECHELLE>0 ))
do
        #echo "interaction #" $compteur
        nombre=$RANDOM
        let "nombre %= $ECHELLE"
        #echo "Nombre aleatoire inferieur à $ECHELLE  ---  $nombre"
        #echo "objet observe " ${tableau_type[$nombre]} message$compteur 
        logger -p ${tableau_type[$nombre]} message$compteur
if [ $NBR = 20 ] 
then
  sleep 1s
  NBR=0
fi
       ((compteur+=1))
       ((NBR+=1))
done
exit 0
