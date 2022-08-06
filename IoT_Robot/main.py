#!/usr/bin/python3
# -*- coding: utf-8 -*-

#Imports

from threading import Thread

import os

import smtplib

from googleapiclient.http import MediaFileUpload
from Google import Create_Service

from datetime import date
from datetime import datetime

import sys

import Adafruit_BBIO.GPIO as GPIO
import time

import Adafruit_BBIO.PWM as PWM
import Adafruit_BBIO.ADC as ADC

import board
import digitalio
from Adafruit_IO import MQTTClient

from Adafruit_IO import Client, Feed, RequestError

#Constants

ADAFRUIT_IO_KEY = 'aio_XIJr46lWgKtLgoso4CAw4U6bXAWB'
ADAFRUIT_IO_USERNAME = 'augur'
FEED_ID = 'shortvideo'  
FEED_ID2 = 'longvideo'  

CLIENT_SECRET_FILE = 'client_secret_GoogleCloudDemo.json'
API_NAME = 'drive'
API_VERSION ='v3'
SCOPES = ['https://www.googleapis.com/auth/drive']

#Flags


#sv_startFlag = False
#lv_startFlag = False

sendSoundMailFlag = False
sendGasMailFlag = False

#Pins

soundS = "P9_39"
gasS= "P8_26"

motorLeft1 = "P8_7"  #in1
motorLeft2 = "P8_9" #in2
motorLeftSpeed = "P8_13"

motorRight1 = "P8_8" #in3
motorRight2 = "P8_10" #in4
motorRightSpeed = "P8_19"

triggerLeft = "P9_12"
echoLeft = "P9_15" 

triggerRight = "P9_23"
echoRight = "P8_18"

camServoBot = "P9_14" 
camServoTop = "P9_16"

#Methods


def sendSoundMail():

    gmail_user = 'au.gurselli@gmail.com'
    gmail_password = '*********'

    sent_from = gmail_user
    to = ['heritage.au3@gmail.com']
    subject = 'Sound Level Warning'
    body = 'Unexpected Sound Level Change has been detected !'

    email_text = """\
    From: %s
    To: %s
    Subject: %s

    %s
    """ % (sent_from, ", ".join(to), subject, body)

    try:
        smtp_server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        smtp_server.ehlo()
        smtp_server.login(gmail_user, gmail_password)
        smtp_server.sendmail(sent_from, to, email_text)
        smtp_server.close()
        print ("Email sent successfully!")
    except Exception as ex:
        print ("Something went wrong….",ex)


def sendGasMail():

    gmail_user = 'au.gurselli@gmail.com'
    gmail_password = '********'

    sent_from = gmail_user
    to = ['heritage.au3@gmail.com']
    subject = 'Gas Level Warning'
    body = 'Unexpected Gas Level Change has been detected !'

    email_text = """\
    From: %s
    To: %s
    Subject: %s

    %s
    """ % (sent_from, ", ".join(to), subject, body)

    try:
        smtp_server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        smtp_server.ehlo()
        smtp_server.login(gmail_user, gmail_password)
        smtp_server.sendmail(sent_from, to, email_text)
        smtp_server.close()
        print ("Email sent successfully!")
    except Exception as ex:
        print ("Something went wrong….",ex)


def soundSensorTask():

    while True:

        value = ADC.read(soundS)
        if(value >0.50 or value < 0.40):
            print(value)
            print("Sound Level Change!")
            sendSoundMail()
            print("Mail has been sent")
            sendSoundMailFlag= False
            time.sleep(5)
        time.sleep(0.1)


def GasSensorTask():
    GPIO.setup(gasS, GPIO.IN)

    while True:

        value = GPIO.input(gasS)
        if(value == 0):
            print(value)
            print("High Temp or Flammable Gas Warning")
            print("Mail has been sent")
            sendGasMail()
            sendGasMailFlag= False
            break
         
        time.sleep(0.1)
        

def distance_measurement(TRIG,ECHO):
    GPIO.output(TRIG, True)
    time.sleep(0.00001)
    GPIO.output(TRIG, False)
    pulseStart = time.time()
    pulseEnd = time.time()
    counter = 0
    while GPIO.input(ECHO) == 0:
        pulseStart = time.time()
        counter += 1
    while GPIO.input(ECHO) == 1:
        pulseEnd = time.time()

    pulseDuration = pulseEnd - pulseStart
    distance = pulseDuration * 17150
    distance = round(distance, 2)
    return distance


def distance_configuration_Left():
    #Conf
    GPIO.cleanup()
    time.sleep(2)
    print("trigger: [{}]".format(triggerLeft))
    GPIO.setup(triggerLeft, GPIO.OUT) #Trigger
    print("echo: [{}]".format(echoLeft))
    GPIO.setup(echoLeft, GPIO.IN)  #Echo
    GPIO.output(triggerLeft, False)
    print("Left Servo Setup completed!")
    # Security
    GPIO.output(triggerLeft, False)
    time.sleep(0.5)


def distance_configuration_Right():
    #Conf
    GPIO.cleanup()
    time.sleep(2)
    print("trigger: [{}]".format(triggerRight))
    GPIO.setup(triggerRight, GPIO.OUT) #Trigger
    print("echo: [{}]".format(echoRight))
    GPIO.setup(echoRight, GPIO.IN)  #Echo
    GPIO.output(triggerRight, False)
    print("Right Servo Setup completed!")
    # Security
    GPIO.output(triggerRight, False)
    time.sleep(0.5)


def getLeftDist():

    return distance_measurement(triggerLeft,echoLeft)


def getRightDist():
    
    return distance_measurement(triggerRight,echoRight)


def changeDegree(servoPin,degree):
    desiredAngle = degree
    dutyCycle = float(desiredAngle)*0.047+3
    PWM.set_duty_cycle(servoPin,dutyCycle)


def rotateCam():

    global sv_startFlag
    sv_startFlag = False
    while True:
        time.sleep(2)
        while(sv_startFlag == False):
            xyz="a"
        while sv_startFlag==True:

            PWM.start(camServoBot,3,50)
            PWM.start(camServoTop,3,50)

            camServoBotCounter = 3
            camServoTopCounter = 3

            for x in range(30, 150,3):
        
                changeDegree(camServoBot, x)
                time.sleep(0.1)
                changeDegree(camServoTop, x)
                time.sleep(0.1)

            for x in range(150, 30, -3):

                changeDegree(camServoBot, x)
                time.sleep(0.1)
                changeDegree(camServoTop, x)
                time.sleep(0.1)


def driveVehicle():
    
    global lv_startFlag
    lv_startFlag = False

    while True:  
      
        while(lv_startFlag == True):
            
            l=getLeftDist()
            r=getRightDist()
          
            print(l)
            print(r)
            

            if(r< 15 and l<15):

                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2, GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.LOW)

                time.sleep(1.5)

                #drive straight
                PWM.start(motorLeftSpeed,80 , 1000)
                PWM.start(motorRightSpeed, 80 , 1000)

                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2,GPIO.HIGH)
                GPIO.output(motorRight1, GPIO.HIGH)
                GPIO.output(motorRight2, GPIO.LOW)
                time.sleep(1)


            elif(l >100 and r>100): 
            
          
                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2, GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.LOW)

                time.sleep(1.5)

                #drive straight
                PWM.start(motorLeftSpeed,75 , 1000)
                PWM.start(motorRightSpeed, 75 , 1000)
            
                GPIO.output(motorLeft1, GPIO.HIGH)
                GPIO.output(motorLeft2,GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.HIGH)
                time.sleep(0.8)

            elif( r < 15):

          
                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2, GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.LOW)

                time.sleep(1.5)


                #drive left
                PWM.start(motorLeftSpeed, 99 , 1000)
                PWM.start(motorRightSpeed, 99 , 1000)

                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2,GPIO.HIGH)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.HIGH)
                time.sleep(0.2)

            elif(l < 20):

                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2, GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.LOW)

                time.sleep(1.5)


                #drive right
                PWM.start(motorLeftSpeed, 99 , 1000)
                PWM.start(motorRightSpeed, 99 , 1000)

                GPIO.output(motorLeft1, GPIO.HIGH)
                GPIO.output(motorLeft2,GPIO.LOW)
                GPIO.output(motorRight1, GPIO.HIGH)
                GPIO.output(motorRight2, GPIO.LOW)
                time.sleep(0.2)
            else:
                GPIO.output(motorLeft1, GPIO.LOW)
                GPIO.output(motorLeft2, GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.LOW)

                time.sleep(1.5)

                #drive straight
                PWM.start(motorLeftSpeed, 75 , 1000)
                PWM.start(motorRightSpeed, 75 , 1000)

                GPIO.output(motorLeft1, GPIO.HIGH)
                GPIO.output(motorLeft2,GPIO.LOW)
                GPIO.output(motorRight1, GPIO.LOW)
                GPIO.output(motorRight2, GPIO.HIGH)
                time.sleep(1)

def recordVideo(duration):

    service = Create_Service(CLIENT_SECRET_FILE,API_NAME,API_VERSION,SCOPES)
    today = date.today()
    now = datetime.now()
    current_time = now.strftime("%H:%M:%S")
    
    rawVideoName = "record" + str(today) + "_" + str(current_time)
    convertedVideoName = "record" + str(today) + "_" + str(current_time) + ".mp4"
    
    captureCommand = "./capture -F -c " + str(duration)  +" -o > " + rawVideoName + ".raw"
    rawVideoName = rawVideoName + ".raw"

    convertCommand = "ffmpeg  -i "+ rawVideoName+ " " + convertedVideoName    
   
    os.system(captureCommand)
    
    while True:
        output = os.popen('ls').read()
        if rawVideoName in output:
        
            os.system(convertCommand)
            
            while True:
                
                output = os.popen('ls').read()
                if convertedVideoName in output:
                    
                    folder_id = '1VB4rprs0wctQvJi-OToWBWA6n87kv2Bz'
                    file_names = [convertedVideoName]
                    mime_types = ['video/mp4']
                    
                    for file_name,mime_type  in zip( file_names,mime_types):
                        file_metadata = {
                        'name': file_name,
                        'parents': [folder_id]
                        }
    
                    media = MediaFileUpload('./{0}'.format(file_name),mimetype = mime_type,resumable=True)
    
                    service.files().create(
                        body=file_metadata,
                        media_body=media,
                        fields='id'
                    ).execute()
                   
                return


client1 =Client(ADAFRUIT_IO_USERNAME, ADAFRUIT_IO_KEY)

client2 = Client(ADAFRUIT_IO_USERNAME, ADAFRUIT_IO_KEY)

#Main

#Initialize ADC

ADC.setup()

#Initialize motordrive

GPIO.setup(motorLeft1, GPIO.OUT)
GPIO.setup(motorLeft2, GPIO.OUT)
GPIO.setup(motorRight1, GPIO.OUT)
GPIO.setup(motorRight2, GPIO.OUT)

t3= Thread(target = rotateCam)
t4= Thread(target = soundSensorTask)
t5= Thread(target =  GasSensorTask)
t6= Thread(target = rotateCam)
t7= Thread(target = driveVehicle)

t3.start()
t4.start()
t5.start()
t6.start()
t7.start()

distance_configuration_Left()
distance_configuration_Right()


while True:

        time.sleep(0.5)
        print("Main Thread..")

        svfeed = client1.feeds('shortvideo')
        test= client1.receive(svfeed.key)

        lvfeed = client2.feeds('longvideo')
        test2= client2.receive(lvfeed.key)
                 

        if(test.value == "1"):
            sv_startFlag =  True
            print("sv ON")
            time.sleep(1)
            client1.send_data(svfeed.key,"OFF")            
            recordVideo(70)      

        elif(test2.value == "1"):
            lv_startFlag =  True
            sv_startFlag = True
            print("lv ON")
            time.sleep(1)
            client2.send_data(lvfeed.key,"OFF")
            recordVideo(200)
            lv_startFlag=False
            sv_startFlag = False

t3.join()
t4.join()
t5.join()
t6.join()
t7.join()

#End