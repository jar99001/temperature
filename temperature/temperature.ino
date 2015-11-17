#include <OneWire.h>
#include <DallasTemperature.h>

#define ONE_WIRE_BUS 2

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

// arrays to hold device address
DeviceAddress insideThermometer;

void setup(void) {
  Serial.begin(9600);

  // locate devices on the bus
  Serial.print("Locating devices...");
  sensors.begin();
  Serial.print("Found ");
  Serial.print(sensors.getDeviceCount(), DEC);
  Serial.println(" devices.");
  
  if (!sensors.getAddress(insideThermometer, 0)) Serial.println("Unable to find address for Device 0"); 
  
  // show the addresses we found on the bus
//  Serial.print("Device 0 Address: ");
//  printAddress(insideThermometer);
//  Serial.println();

  // set the resolution to 9 bit (Each Dallas/Maxim device is capable of several different resolutions)
  sensors.setResolution(insideThermometer, 9);
 
//  Serial.print("Device 0 Resolution: ");
//  Serial.print(sensors.getResolution(insideThermometer), DEC); 
//  Serial.println();
}

// function to print the temperature for a device
void printTemperature(DeviceAddress deviceAddress) {
  // method 1 - slower
  //Serial.print("Temp C: ");
  //Serial.print(sensors.getTempC(deviceAddress));
  //Serial.print(" Temp F: ");
  //Serial.print(sensors.getTempF(deviceAddress)); // Makes a second call to getTempC and then converts to Fahrenheit

  // method 2 - faster
  float tempC = sensors.getTempC(deviceAddress);
  Serial.print("Temp C: ");
  Serial.println(tempC);
//  Serial.print(" Temp F: ");
//  Serial.println(DallasTemperature::toFahrenheit(tempC)); // Converts tempC to Fahrenheit
}

void loop(void)
{ 
//  Serial.print("Requesting temperatures...");
  sensors.requestTemperatures(); // Send the command to get temperatures
//  Serial.println("DONE");
  
  printTemperature(insideThermometer); // Use a simple function to print out the data
  delay(1000);
}

// function to print a device address
void printAddress(DeviceAddress deviceAddress) {
  for (uint8_t i = 0; i < 8; i++) {
    if (deviceAddress[i] < 16) Serial.print("0");
    Serial.print(deviceAddress[i], HEX);
  }
}
