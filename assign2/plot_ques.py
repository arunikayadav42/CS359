import pandas as pd
import matplotlib.pyplot as plt
import os.path
from os import path

# uncomment the below code for question a

for i in range(0, 4):
    file_name = 'problem1_part_a_source'+str(i)+'.csv'
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
        plt.savefig('./problem1_part_a_source'+str(i)+'.png')
        plt.close()

# uncomment the below code for question b

for i in range(0, 4):
    file_name = 'problem1_part_b_source'+str(i)+'.csv'
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
        plt.savefig('./problem1_part_b_source'+str(i)+'.png')
        plt.close()