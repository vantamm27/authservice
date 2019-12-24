#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
PyFingerprint
Copyright (C) 2015 Bastian Raschke <bastian.raschke@posteo.de>
All rights reserved.

"""

import time
import binascii
from pyfingerprint.pyfingerprint import PyFingerprint


## Enrolls new finger
##

## Tries to initialize the sensor
try:
    f = PyFingerprint('/dev/ttyUSB0', 57600, 0xFFFFFFFF, 0x00000000)

    if ( f.verifyPassword() == False ):
        raise ValueError('The given fingerprint sensor password is wrong!')

except Exception as e:
    print('The fingerprint sensor could not be initialized!')
    print('Exception message: ' + str(e))
    exit(1)

## Gets some sensor information
print('Currently used templates: ' + str(f.getTemplateCount()) +'/'+ str(f.getStorageCapacity()))

## Tries to enroll new finger
try:
    print(f.getSystemParameters())
    print('Waiting for finger...')

    ## Wait that finger is read
    while ( f.readImage() == False ):
        pass

    ## Converts read image to characteristics and stores it in charbuffer 1
    f.convertImage(0x01)

    buff1 = f.downloadCharacteristics(0x01)
    print("buff1========================")
    #print(buff1)

    print("================")	
    bytes = bytes(buff1)
   
    
    print(len(buff1))
    print(binascii.hexlify(bytes))
    print("================")	

    ## Checks if finger is already enrolled
    result = f.searchTemplate()
    positionNumber = result[0]

    if ( positionNumber >= 0 ):
        print('Template already exists at position #' + str(positionNumber))
        exit(0)

    print('Remove finger...')
    time.sleep(2)

    print('Waiting for same finger again...')

    ## Wait that finger is read again
    while ( f.readImage() == False ):
        pass

    ## Converts read image to characteristics and stores it in charbuffer 2
    f.convertImage(0x02)
    buff2 = f.downloadCharacteristics(0x02)
    print("buff1========================")
    print(buff2)
   

    ## Compares the charbuffers
    if ( f.compareCharacteristics() == 0 ):
        raise Exception('Fingers do not match')

    ## Creates a template
    f.createTemplate()

    buff3 = f.downloadCharacteristics(0x01)
    print("buff3========================")
    print(buff3)
    buff4 = f.downloadCharacteristics(0x02)
    print("buff4========================")
    print(buff4)
    print("================")	
    print(bytes(buff1))
    print("================")	
    ##print(buff2.decode('utf-8'))
    ##print(buff3.decode('utf-8'))
    ##print(buff4.decode('utf-8'))
    ## Saves template at new position number
    positionNumber = f.storeTemplate()
    print('Finger enrolled successfully!')
    print('New template position #' + str(positionNumber))

except Exception as e:
    print('Operation failed!')
    print('Exception message: ' + str(e))
    exit(1)
