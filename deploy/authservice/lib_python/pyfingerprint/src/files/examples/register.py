#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tempfile
import sys
from pyfingerprint.pyfingerprint import PyFingerprint

##print("Begin")

##print("argv")
##print(sys.argv[1:])

if len(sys.argv) <= 1 :
	print("invalid arg")
	sys.exit(-1)
## session from server
session=sys.argv[1]
##print("session")
##print(session)


try:
    f = PyFingerprint('/dev/ttyUSB0', 57600, 0xFFFFFFFF, 0x00000000)
    if ( f.verifyPassword() == False ):
        raise ValueError('The given fingerprint sensor password is wrong!')

except Exception as e:
    print('The fingerprint sensor could not be initialized!')
    print('Exception message: ' + str(e))
    exit(1)

try:
##	print('Waiting for finger...' +  session)
	## Wait that finger is read
	while ( f.readImage() == False ):
		pass		
	f.downloadImage(session)

except Exception as e:
	print('Operation failed!')
	print('Exception message: ' + str(e))
	exit(1)

print('OK' + '\t' + session)
exit(1)
