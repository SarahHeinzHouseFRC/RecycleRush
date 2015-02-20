#include <Wire.h>
#include "FastSPI_LED2.h"

#define NUM_LEDS 80

// AM-2640 yellow wire is ground
#define DATA_PIN 11 //Green wire from AM-2640's power connector
#define CLOCK_PIN 13 //Blue wire from AM-2640's power connector

#define DEFAULT_STATE 0
#define LOW_BATTERY 1
#define LOW_PRESSURE 2
#define ELEVATOR_STATUS 3

byte dataToSend = 0;
byte data2[6] = {11, 12, 13, 14, 15, 16};

byte dataReceived[6] = {0, 0, 0, 0, 0, 0};
byte prevDataReceived[6] = {0, 0, 0, 0, 0, 0};
boolean stateChanged = true;
CRGB leds[NUM_LEDS];

void setup()
{
  Wire.begin(2);                                            // Start I2C interface (address 2)
  Wire.onReceive(i2cReceive);                               // Receive ISR routine
  Wire.onRequest(i2cRequest);                               // Request (send) ISR routine

  FastLED.addLeds<WS2801, RGB>(leds, NUM_LEDS);
}

int getState()
{
  return dataReceived[0];
}

void loop()
{
  delay(10);

  int curState = getState();

  int percent = 0;

  switch (curState)
  {
    case LOW_BATTERY:
    percent = (dataReceived[1] / 127) * 100;
    coloredPercentFill(CRGB::Green, CRGB::Red, percent);
    break;

    default:
      nColorChase(0xff2500, CRGB::Blue, CRGB::Green, 27, 10);  //SHARP colors
      break;
    }
  }

  void i2cReceive(int count)
  {
  byte i2cCount;                                            // Loop/Array counter
  byte tmp;                                                 // temporary byte holder
  byte i2cDataR[7];                                         // 7 Bytes in length as that is the maximum that the cRIO can handle over I2C

  for (i2cCount = 0; i2cCount < count; i2cCount++)          // Read data into local buffer
  { // looping through the entire message
    tmp = Wire.read();

    if (i2cCount < 7)
    {
      i2cDataR[i2cCount] = tmp; // Copy byte in to received array
    }
  }

  if (i2cDataR[0] == 2)
  {
    dataToSend = 2;

    if (i2cCount > 1)
    {
      memcpy(&dataReceived[0], &i2cDataR[1], i2cCount < 7 ? i2cCount - 1 : 6);
    }

    if (memcmp_P(&dataReceived, &prevDataReceived, 6) != 0)
    {
      stateChanged = true;
      memcpy(&prevDataReceived, &dataReceived, 6);
    }
    else
    {
      stateChanged = false;
    }
  }
}

// This routine is called when the master requests the slave send data.  A full transaction
// typically includes a Receive envent (register to send) followed by a Request event where the
// slave data is actually transmitted to the master.
void i2cRequest()
{
  byte i2cDataW[7];
  byte length;

  switch (dataToSend)                                        // variable set in the i2cReceive event
  {
    // Return the contents of the cRIO read/write array
    case 2:
    i2cDataW[0] = 2;
    memcpy(&i2cDataW[1], &data2[0], 6);
    length = 7;
    break;

    // Unknown or invalid command.  Send an error byte back
    default:
    i2cDataW[0] = 0xFF;
    length = 1;
  }

  Wire.write((uint8_t *) &i2cDataW[0], length);
}

void percentFill(uint32_t color, int percent)
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();

  FastLED.setBrightness(100);

  percent = percent < 0 ? 0 : percent;

  percent = percent > 100 ? 100 : percent;

  int numLEDsToFill = ((percent / 2) / 100) * NUM_LEDS;

  int minHigh = NUM_LEDS - numLEDsToFill;

  fill_solid(leds, numLEDsToFill, color);

  fill_solid(&(leds[minHigh]), numLEDsToFill, color);

  FastLED.show();
}

void coloredPercentFill(uint32_t fillColor, uint32_t indicatorColor, int percent)
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();

  FastLED.setBrightness(100);

  percent = percent < 0 ? 0 : percent;

  percent = percent > 100 ? 100 : percent;

  int numLEDsToFill = (percent / 100) * NUM_LEDS;

  fill_solid(leds, NUM_LEDS, fillColor);

  fill_solid(leds, numLEDsToFill, indicatorColor);

  FastLED.show();
}

void nColorSplit(uint32_t color_1, uint32_t color_2, uint32_t color_3, int N, uint8_t wait)
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();

  FastLED.setBrightness(100);

  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS / 2; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    for (int i = 0; i < N; i++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[(41 + led_number + i) % NUM_LEDS] = color_1;
      leds[(41 + led_number + i + N) % NUM_LEDS] = color_2;     //spills to the right
      leds[(41 + led_number + i + 2 * N) % NUM_LEDS] = color_3;

      leds[(40 - led_number - i) % NUM_LEDS] = color_1;
      leds[(40 - led_number - i - N) % NUM_LEDS] = color_2;     //spills to the left
      leds[(40 - led_number - i - 2 * N) % NUM_LEDS] = color_3;
    }

    // Show the leds (only one of which is set to white, from above)
    FastLED.show();

    if (stateChanged)
    {
      return;
    }

    // Wait a little bit
    if (interruptableDelay(wait))
    {
      return;
    }

    // Turn our current led back to black for the next loop around
    leds[led_number] = CRGB::Black;
  }
}

void lightsOff()
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();

  FastLED.setBrightness(100);

  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    // Turn our current led back to black for the next loop around
    leds[led_number] = CRGB::Black;
  }

  // Show the leds (only one of which is set to white, from above)
  FastLED.show();
}

void nColorChase(uint32_t color_1, uint32_t color_2, uint32_t color_3, int N, uint8_t wait)
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();

  FastLED.setBrightness(100);

  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    for (int i = 0; i < N; i++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[(led_number + i) % NUM_LEDS] = color_1;
      leds[(led_number + i + N) % NUM_LEDS] = color_2;
      leds[(led_number + i + 2 * N) % NUM_LEDS] = color_3;
    }

    // Show the leds (only one of which is set to white, from above)
    FastLED.show();

    // Wait a little bit
    if (interruptableDelay(wait))
    {
      return;
    }

    // Turn our current led back to black for the next loop around
    leds[led_number] = CRGB::Black;
  }
}

void shootColorChase(uint32_t color_1, uint32_t color_2, int N, uint8_t wait)
{
  if (stateChanged)
  {
    return;
  }
  FastLED.clear();

  FastLED.setBrightness(100);

  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    for (int i = 0; i < N; i++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[(led_number + i) % NUM_LEDS] = color_1;
      leds[(led_number + i + N) % NUM_LEDS] = color_2;
    }
  }
}

void passColorChase(uint32_t color_1, uint32_t color_2, int N, uint8_t wait)
{
  if (stateChanged)
  {
    return;
  }

  FastLED.clear();
  FastLED.setBrightness(100);
  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    for (int i = 0; i < N; i++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[(led_number + i) % NUM_LEDS] = color_1;
      leds[(led_number + i + N) % NUM_LEDS] = color_2;
    }

    // Show the leds (only one of which is set to white, from above)
    FastLED.show();

    // Wait a little bit
    if (interruptableDelay(wait))
    {
      return;
    }

    // Turn our current led back to black for the next loop around
    leds[led_number] = CRGB::Black;
  }
}

void threeColorChase(uint32_t color_1, uint32_t color_2, uint32_t color_3, uint8_t wait)
{
  FastLED.clear();
  FastLED.setBrightness(100);
  // Move a single led
  for (int led_number = 0; led_number < NUM_LEDS - 2; led_number++)
  {
    if (stateChanged)
    {
      return;
    }

    // Turn our current leds ON, then show the leds
    leds[led_number] = color_1;
    leds[led_number + 1] = color_2;
    leds[led_number + 2] = color_3;

    // Show the leds (only one of which is set to white, from above)
    FastLED.show();

    // Wait a little bit
    if (interruptableDelay(wait))
    {
      return;
    }
    // Turn our current led back to black for the next loop around
    leds[led_number] = CRGB::Black;
  }
}

//Move an "empty" dot down the strip
void missingDotChase(uint32_t color, uint8_t pauseTime)
{
  int led_number;

  for (int led_brightness = 100; led_brightness > 10; led_brightness /= 2)
  {
    if (stateChanged)
    {
      return;
    }

    FastLED.setBrightness(led_brightness);
    // Start by turning all pixels on:
    for (led_number = 0; led_number < NUM_LEDS; led_number++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[led_number] = color;
    }

    // Then display one pixel at a time:
    for (led_number = 0; led_number < NUM_LEDS; led_number++)
    {
      if (stateChanged)
      {
        return;
      }
      leds[led_number] = CRGB::Black; // Set new pixel 'off'

      if ( led_number > 0 && led_number < NUM_LEDS)
      {
        leds[led_number - 1] = color; // Set previous pixel 'on'
      }

      FastLED.show();

      if (interruptableDelay(pauseTime))
      {
        return;
      }
    }
  }
}

void missingNDotChase(uint32_t color, uint8_t N, uint8_t pauseTime)
{
  int led_number;

  for (int led_brightness = 100; led_brightness > 10; led_brightness /= 2)
  {
    if (stateChanged)
    {
      return;
    }

    FastLED.setBrightness(led_brightness);

    // Start by turning all pixels on:
    for (led_number = 0; led_number < NUM_LEDS; led_number++)
    {
      if (stateChanged)
      {
        return;
      }

      leds[led_number] = color;
    }

    // Then display one pixel at a time:
    for (led_number = 0; led_number < NUM_LEDS; led_number++)
    {
      for (int i = 0; i < N; i++)
      {
        if (stateChanged)
        {
          return;
        }

        leds[(led_number + i) % NUM_LEDS] = CRGB::Black; // Set new pixel 'off'

        if (led_number > 0 && led_number < NUM_LEDS)
        {
          leds[led_number - 1] = color; // Set previous pixel 'on'
        }
      }

      FastLED.show();

      if (interruptableDelay(pauseTime))
      {
        return;
      }
    }
  }
}

void solidColor(uint32_t color)
{
  FastLED.clear();

  FastLED.setBrightness(100);

  for (int curLed = 0; curLed < NUM_LEDS; curLed++)
  {
    if (stateChanged)
    {
      return;
    }

    leds[curLed] = color;
  }

  FastLED.show();
}

void colorSwipe(uint32_t color, uint8_t pauseTime)
{
  FastLED.clear();

  FastLED.setBrightness(100);

  for (int curLed = 0; curLed < NUM_LEDS; curLed++)
  {
    if (stateChanged)
    {
      return;
    }
    leds[curLed] = CRGB(0, 0, 0);

    leds[curLed] = color;

    FastLED.show();

    if (interruptableDelay(pauseTime))
    {
      return;
    }
  }
}

void meteor(uint8_t pauseTime)
{
  uint32_t colors[] =
  {
    CRGB(255, 255, 255),
    CRGB(155, 155, 155),
    CRGB(99, 99, 99),
    CRGB(77, 50, 50),
    CRGB(55, 20, 20),
    CRGB(33, 0, 0)
  };

  int numColors (sizeof(colors) / sizeof(uint32_t));

  for (int i = 0; i < NUM_LEDS + numColors; i++)
  {
    solidColor(CRGB(0, 0, 0));

    for (int c = 0; c < numColors; c++)
    {
      leds[i - c] = colors[c];
    }

    FastLED.show();

    if (interruptableDelay(pauseTime))
    {
      return;
    }
  }
}

void rainbowCycle(uint8_t pauseTime)
{
  FastLED.clear();

  FastLED.setBrightness(100);

  int ihue = 0;

  while (!stateChanged)
  {
    ihue -= 1;

    ihue < -100 ? 0 : ihue;

    fill_rainbow(leds, NUM_LEDS, ihue);

    FastLED.show();

    if (interruptableDelay(pauseTime))
    {
      return;
    }
  }
}

void shootColor(uint32_t color)
{
  FastLED.clear();

  FastLED.setBrightness(100);

  colorSwipe(color, 6);

  if (stateChanged)
  {
    return;
  }
  solidColor(CRGB(0, 0, 0));
  FastLED.show();
  if (interruptableDelay(500))
  {
    return;
  }

  if (stateChanged)
  {
    return;
  }
  solidColor(CRGB(50, 0, 0));
  FastLED.show();
  if (interruptableDelay(500))
  {
    return;
  }

  if (stateChanged)
  {
    return;
  }
  solidColor(CRGB(50, 50, 50));
  FastLED.show();
  delay(500);

  if (stateChanged)
  {
    return;
  }
  solidColor(CRGB(0, 0, 50));
  FastLED.show();
  if (interruptableDelay(500))
  {
    return;
  }

  if (stateChanged)
  {
    return;
  }
  solidColor(CRGB(0, 0, 0));
  FastLED.show();

  if (interruptableDelay(500))
  {
    return;
  }
}

uint32_t wheel(byte wheelPos)
{
  if (wheelPos < 85)
  {
    return CRGB(wheelPos * 3, 255 - (wheelPos * 3), 0);
  }
  else if (wheelPos < 170)
  {
    wheelPos -= 85;

    return CRGB(255 - (wheelPos * 3), 0, wheelPos * 3);
  }
  else
  {
    wheelPos -= 170;

    return CRGB(0, wheelPos * 3, 255 - (wheelPos * 3));
  }
}

boolean interruptableDelay(unsigned long delayTime)
{
  unsigned long timer = millis();

  while (millis() - timer <= delayTime)
  {
    if (stateChanged)
    {
      return true;
    }
  }

  return false;
}
