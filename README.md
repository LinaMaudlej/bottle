# bottle
smart bottle application.

Main Activity: 
This is the main activity. It shows for the user the status of the bottler of water:
- temperature : according to the data from the temperature sensor we get from the hardware to azure . when it changes, a trigger is sent to function app and the data is updated in the application accordingly. 
- level: it is the water level inside the bottle. 100% is full. The hardware sends number of litters in the bottle via azure function as it described in temprature, and in the application we calculate the percent of the water in the bottle according to the bottle configurations.
- safe to drink/unsafe: this calculations is happened inside the application from the inputs of the water level and the date which is updated in the application. 
If the level of water does not change/ had been reduced, then the water in the bottle is still the same water (no refill). When 4 days passes the application writes unsafe to drink(red). Otherwise, it is safe(green).
This data is sent to the hardware , via function app in azure, then the led changes its color accordingly. 
-notification: after the user sets its goal (litters per day), the application calculates the when to send the notification according to the hours passed on the day and the amount of water the user has already drank. The day begins at 8:00 and ends at 20:00 (default configuration) and the notification sends every 15 minutes and updated when the user drinks (if he have already drank in the past 15 minutes, the notification does not send to user).
This data is sent to the hardware , via function app in azure, then the buzzer is also notify the user to drink.


User login activity:
it is a per user application. We have a template for creating a log-in activity (by Anwar)


User Configuration: 
user sets here his/here hydration goal (by default is 2 liters). We can add here all the configuration settings , such as active hours to drink, bottle size,switching between number of bottles etc.


