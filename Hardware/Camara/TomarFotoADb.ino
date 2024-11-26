#include <WebServer.h>
#include <fstream>
#include <esp_camera.h>
#include <Arduino.h>
#include <Base64.h>
#define PWDN_GPIO_NUM     32
#define RESET_GPIO_NUM    -1
#define XCLK_GPIO_NUM      0
#define SIOD_GPIO_NUM     26
#define SIOC_GPIO_NUM     27
#define Y9_GPIO_NUM       35
#define Y8_GPIO_NUM       34
#define Y7_GPIO_NUM       39
#define Y6_GPIO_NUM       36
#define Y5_GPIO_NUM       21
#define Y4_GPIO_NUM       19
#define Y3_GPIO_NUM       18
#define Y2_GPIO_NUM        5
#define VSYNC_GPIO_NUM    25
#define HREF_GPIO_NUM     23
#define PCLK_GPIO_NUM     22
camera_config_t config;
#include "defines.h"
#include "Credentials.h"
#include <MySQL_Generic.h>
WebServer Webserver(80);
#define USING_HOST_NAME     false
#if USING_HOST_NAME
  // Optional using hostname, and Ethernet built-in DNS lookup
  char server[] = "your_account.ddns.net"; // change to your server's hostname/URL
#else
  IPAddress server(,,,);
#endif

uint16_t server_port = 3306;    //3306;

char default_database[] = "piisafesense";       



void initCamera() {
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG; 
  
  if(psramFound()){
    config.frame_size = FRAMESIZE_UXGA;//FRAMESIZE_UXGA; // FRAMESIZE_ + QVGA|CIF|VGA|SVGA|XGA|SXGA|UXGA
    config.jpeg_quality = 10;
    config.fb_count = 2;
  } else {
    config.frame_size = FRAMESIZE_UXGA;
    config.jpeg_quality = 12;
    config.fb_count = 1;
  }  
  // Init Camera
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }  
}

String TomarFoto(){
  String foto_codificada = "error";
  camera_fb_t *fb = NULL;
   fb = esp_camera_fb_get();
    if (!fb) {
        Serial.println("Error al capturar la foto");
        
    }else{
      Serial.println("Foto capturada");
       foto_codificada = base64::encode(fb->buf,fb->len);
  //Serial.println(foto_codificada);
  esp_camera_fb_return(fb);
    }

    return foto_codificada;
}

void HacerInsert(String query,MySQL_Connection &conn){
//Creamos la instancai de query usando la conexion global
MySQL_Query Query  = MySQL_Query(&conn); 

if(conn.connected()){ //Comprobamos conexion
  //Serial.print("Vamos a ejecutar la siguiente Query \n->");
  //Serial.println(query);
  
    if ( !Query.execute(query.c_str())) //La propia llamada del execute ejecuta la query y devuelve el estad, c_str() convierte a String un puntero de caracteres (mucha chapa)
    {
      MYSQL_DISPLAY("Insert error");
    }
    else
    {
      MYSQL_DISPLAY("Data Inserted.");
    }
  }
  else
  {
    MYSQL_DISPLAY("Disconnected from Server. Can't insert.");
  }
  return;
}

void HacerInsertSeguro(String query){
  MySQL_Connection conn((Client *)&client);
  //if (conn.connect(server, server_port, user, password))
  if (conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL)
  {
    delay(500);
    HacerInsert(query,conn);
                         // close the connection
  } 
  else 
  {
    MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
  }
  Serial.print("Cerrando conexion");
  conn.close();
}

void handleTomarFoto(){
  Webserver.send(200,"text/plain","CAM > Entendido, sacando foto y subiendola a la DB");
  String foto = TomarFoto();
  HacerInsertSeguro("INSERT INTO piisafesense.Fotos (Base) VALUES ('" + foto + "');");
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  // put your setup code here, to run once:
Serial.begin(115200);
initCamera();
  Webserver.on("/hacerFotoDB",handleTomarFoto);
 Webserver.begin();    
  MYSQL_DISPLAY1("\nUsando plaquita --> ", BOARD_NAME);
  MYSQL_DISPLAY(MYSQL_MARIADB_GENERIC_VERSION);

  // Remember to initialize your WiFi module
#if ( USING_WIFI_ESP8266_AT  || USING_WIFIESPAT_LIB ) 
  #if ( USING_WIFI_ESP8266_AT )
    MYSQL_DISPLAY("Using ESP8266_AT/ESP8266_AT_WebServer Library");
  #elif ( USING_WIFIESPAT_LIB )
    MYSQL_DISPLAY("Using WiFiEspAT Library");
  #endif
  
  // initialize serial for ESP module
  EspSerial.begin(115200);
  Serial.print("Inciando mudulo serial del esp");
  // initialize ESP module
  WiFi.init(&EspSerial);
  Serial.print("Inciando mudulo wifi del esp");
  
  //MYSQL_DISPLAY(F("WiFi shield init done"));

  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD)
  {
    MYSQL_DISPLAY(F("WiFi shield not present"));
    // don't continue
    while (true);
  }
#endif

  // Begin WiFi section
  MYSQL_DISPLAY1("Conectando a wifiii -->", ssid);

  WiFi.begin(ssid, pass); //Definidos en el Credential.h
  
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    MYSQL_DISPLAY0(".");
  }
  
  // print out info about the connection:
  MYSQL_DISPLAY1("Conectado a la wifi, la IP del cacharoo es -->", WiFi.localIP());
  MYSQL_DISPLAY3("Conectados al servidor SQL --> ", server, ", en el puerto -->", server_port);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void loop() {
  // put your main code here, to run repeatedly:
Webserver.handleClient();
}
