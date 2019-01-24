import pandas as pd
import matplotlib.pyplot as plt
import os.path
from os import path

for i in range(0, 4):
    file_name = 'ques1_source'+str(i)+'.csv'
    filepath = os.path.exists(file_name)
    if filepath:
        df = pd.read_csv(file_name)
        fig = plt.plot(df['Packet Sending Rate'], df['Average Delay'])
        # naming the x axis 
        plt.xlabel('Packet Sending Rate (seconds/packet)') 
        # naming the y axis 
        plt.ylabel('Average Delay(seconds)') 
        # giving a title to my graph 
        plt.title('Source '+str(i))
        plt.savefig('./ques1_source'+str(i)+'.png')
        plt.close()

for i in range(0, 4):
    file_name = 'ques2_source'+str(i)+'.csv'
    filepath = os.path.exists(file_name)
    if filepath:
        df = pd.read_csv(file_name)
        fig = plt.plot(df['Packet Sending Rate'], df['Average Delay'])
        # naming the x axis 
        plt.xlabel('Packet Sending Rate (seconds/packet)') 
        # naming the y axis 
        plt.ylabel('Average Delay(seconds)') 
        # giving a title to my graph 
        plt.title('Source '+str(i))
        plt.savefig('./ques2_source'+str(i)+'.png')
        plt.close()
