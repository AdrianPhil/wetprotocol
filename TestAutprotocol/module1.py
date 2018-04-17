'''
Created on Apr 3, 2018

@author: ad
'''
import json
from autoprotocol.protocol import Protocol

#instantiate new Protocol object
p = Protocol()

#append refs (containers) to Protocol object
bacteria = p.ref("bacteria", cont_type="96-pcr", storage="cold_4")
medium = p.ref("medium", cont_type="micro-1.5", storage="cold_4")
reaction_plate = p.ref("reaction_plate", cont_type="96-flat", storage="warm_37")

#distribute medium from 1.5mL tube to reaction wells
p.distribute(medium.well(0).set_volume("1000:microliter"), reaction_plate.wells_from(0,4), "190:microliter")
#transfer bacteria from source wells to reaction wells
p.transfer(bacteria.wells_from(0,4), reaction_plate.wells_from(0,4),
    ["10:microliter", "20:microliter", "30:microliter", "40:microliter"])
#incubate bacteria at 37 degrees for 5 hours
p.incubate(reaction_plate, "warm_37", "5:hour")
#read absorbance of the first four wells on the reaction plate at 600 nanometers
p.absorbance(reaction_plate, reaction_plate.wells_from(0,4).indices(), "600:nanometer",
    "OD600_reading_01092014")
print("Finished")
print( json.dumps(p.as_dict(), indent=2))