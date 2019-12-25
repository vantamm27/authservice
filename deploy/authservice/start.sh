#! /bin/bash

PRO_NAME=authservice
COUNT=0
TEMP=0

function run() {
 echo "java -cp lib -Xmx300M -Dlog4j.configuration=file:./log4j.properties -jar  dist/authservice.jar"
 java -cp lib -Xmx300M -Dlog4j.configuration=file:./log4j.properties -jar  dist/authservice.jar 
}

echo "PRO_NAME $PRO_NAME"
#echo "java -cp lib -Xmx300M -Dlog4j.configuration=file:./log4j.properties -jar  dist/authservice.jar"

#java -cp lib -Xmx300M -Dlog4j.configuration=file:./log4j.properties -jar  dist/authservice.jar 

run

while [ $COUNT -le 60 ]
do	
	TEMP=$(ps -ef | grep $PRO_NAME | wc -l)

	echo "COUNT $COUNT "	
	echo "TEMP $TEMP"

	if [ $TEMP -lt 2 ] 
	then
		COUNT=$(($COUNT +1))
	else 
		COUNT=0
	fi
	sleep 1
done
echo "reboot"
#reboot



