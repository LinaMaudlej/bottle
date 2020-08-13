#include <OneWire.h>
#include <DallasTemperature.h>
// Define pins for ultrasonic and buzzer
int const trigPin = D3;
int const echoPin = D2;
int const buzzPin = D1;
int redPin= D7;
int greenPin = D6;
int bluePin = D5;
int buzz_one=0;

//temprature

int ONE_WIRE_BUS = D8;
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
float Celcius=0;
float Fahrenheit=0;

void setup() {
  Serial.begin(9600);

  pinMode(trigPin, OUTPUT); // trig pin will have pulses output
  pinMode(echoPin, INPUT); // echo pin should be input to get pulse width
  pinMode(buzzPin, OUTPUT); // buzz pin is output to control buzzering  // put your setup code here, to run once:

  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  //temprature
    sensors.begin();
}

void loop() {
  // put your main code here, to run repeatedly:
  int duration, distance;
// Output pulse with 1ms width on trigPin
  digitalWrite(trigPin, HIGH); 
  delay(1);
  digitalWrite(trigPin, LOW);
  // Measure the pulse input in echo pin
  duration = pulseIn(echoPin, HIGH);
  // Distance is half the duration devided by 29.1 (from datasheet)
  distance = (duration/2) / 29.1;
      Serial.println(distance);                  // print sensor output 1 - this is also the value you can use to use this in other projects
     if (distance > 17 && buzz_one==0) {
      // Buzz
      digitalWrite(buzzPin, HIGH);
      digitalWrite(buzzPin, HIGH);
      digitalWrite(buzzPin, HIGH);
      delay(50);
      digitalWrite(buzzPin, LOW);
      buzz_one=1;
    } else if(distance < 17){
      // Don't buzz
      digitalWrite(buzzPin, LOW);
      buzz_one=0;
    }
     /**RGB led**/
    setColor(255, 0, 0); // Red Color
    delay(1000);
    setColor(0, 255, 0); // Green Color
    delay(1000);
    setColor(0, 0, 255); // Blue Color
    delay(1000);
    setColor(255, 255, 255); // White Color
    delay(1000);
    setColor(170, 0, 255); // Purple Color
    delay(1000);
    
    // Waiting 60 ms won't hurt any one
    delay(60);

    //temprature
  sensors.requestTemperatures(); 
  Celcius=sensors.getTempCByIndex(0);
  Fahrenheit=sensors.toFahrenheit(Celcius);
  Serial.print(" C  ");
  Serial.print(Celcius);
  Serial.print(" F  ");
  Serial.println(Fahrenheit);
  delay(1000);
}

void setColor(int redValue, int greenValue, int blueValue) {
  analogWrite(redPin, redValue);
  analogWrite(greenPin, greenValue);
  analogWrite(bluePin, blueValue);
}
