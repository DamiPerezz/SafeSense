#include <SPI.h>
#include <MFRC522.h>
#include <MySQL_Generic.h>
#include "Credentials.h"
#include "defines.h"
#include <HTTPClient.h>
#include "DHT.h"
#define RST_PIN 22  // Pin de reset para el MFRC522
#define SS_PIN  5  // Pin de slave select para el MFRC522
#define TRIG_PIN 25
#define ECHO_PIN 26
#define DHTTYPE DHT11   // DHT 11
#define DHTPIN 4    // Pin al que está conectado el sensor DHT
#define BUZZER_PIN 27
DHT dht(DHTPIN, DHTTYPE);
HTTPClient http;
IPAddress server(,,,);
uint16_t server_port = ;    //3306;
char default_database[] = "piisafesense"; 
int ID_Zona = 9;
int ID_Sensor = 7;
int hum = 0;
int temp = 0;
MFRC522 mfrc522(SS_PIN, RST_PIN);  // Crear instancia del MFRC522
String tarjeta = "";
String IP_Camara = ""; //Aquí se coje de la base de datos la IP del sensor camara que este en su misma Zona
boolean EnHorario = false;
long distancia;
/////////////////////////////////////
//  ESP     HC-SR04     MFRC522   BUZZER    Tempratura
//  5                   SDA
//  18                  SCK
//  23                  MOSI
//  19                  MISO
//  22                  RST
//  25      TRIG
//  26      ECHO
//  27                            Positivo
//                                            4
/////////////////////////////////////
// Conectar a 3 V el MFRC522 y 5V para el HC-SR04



void setup() {
  Serial.begin(115200);
  pinMode(TRIG_PIN, OUTPUT);   // Configura el pin TRIG como salida
  pinMode(ECHO_PIN, INPUT);
  pinMode(BUZZER_PIN, OUTPUT);// Iniciar la comunicación serial con la computadora
  SPI.begin();          // Iniciar la librería SPI
  mfrc522.PCD_Init();   // Iniciar el MFRC522
  Serial.println("Acerca la tarjeta RFID...");
  Serial.begin(115200);
  
   
  while (!Serial && millis() < 5000); // wait for serial port to connect

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
  
  //MYSQL_DISPLAY4("Usuario --> ", user, ", Contraseña --> esto no lo muestro jajaja,  DB --> ", default_database);

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

String uidToString(byte *uid, byte uidLength) {
  String uidStr = "";
  for (byte i = 0; i < uidLength; i++) {
    if(uid[i] < 0x10) {
      uidStr += "0";  // Agregar un 0 para la correcta notación hexadecimal si es necesario
    }
    uidStr += String(uid[i], HEX);
    if (i < uidLength - 1) {
      uidStr += ":";  // Agregar ":" entre bytes
    }
  }
  uidStr.toUpperCase();  // Convertir a mayúsculas
  return uidStr;
}

void activateBuzzer() {
  digitalWrite(BUZZER_PIN, HIGH); // Activar el buzzer
  delay(1000);                    // El buzzer suena por 1 segundo
  digitalWrite(BUZZER_PIN, LOW);  // Desactivar el buzzer
}
long MedirDistancia(){
  long duration, distance;
  digitalWrite(TRIG_PIN, LOW);  // Asegura que el trigger esté en bajo
  delayMicroseconds(2);         // Espera para estabilizar el sensor
  digitalWrite(TRIG_PIN, HIGH); // Envía un pulso alto
  delayMicroseconds(10);        // Duración del pulso de 10 microsegundos
  digitalWrite(TRIG_PIN, LOW);  // Vuelve el pulso a bajo

  duration = pulseIn(ECHO_PIN, HIGH); // Lee la duración del eco
  distance = (duration / 2) / 29.1;   // Calcula la distancia en cm
  delay(10);
return distance;
}


void HacerInsert(String query,MySQL_Connection &conn){
//Creamos la instancai de query usando la conexion global
MySQL_Query Query  = MySQL_Query(&conn); 

if(conn.connected()){ //Comprobamos conexion
  Serial.print("Vamos a ejecutar la siguiente Query \n->");
  Serial.println(query);
  
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
  conn.close();
}


int HacerSelectSeguroDeUnCampInt(String query){
MySQL_Connection conn((Client *)&client);
  int i = -1;
if (conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL)
  {
    delay(500);
    i = HacerSelectDeUnCampInt(query,conn);
                       // close the connection
  } 
  else 
  {
    MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
  }
  conn.close();  
return i;
}
int HacerSelectDeUnCampInt(String query,MySQL_Connection &conn){
  row_values *filas = NULL; 
  int res = -1; //Aquí guardamos el resultado
  //Creamos objeto de query
  MySQL_Query Query = MySQL_Query(&conn);
  //Ejecutar query
  if(Query.execute(query.c_str())){
    Query.get_columns(); //Esto es para sacar el numero de columnas que tendra, es pa el programa, no pa nosootross

  do{
    filas = Query.get_next_row();

    if(filas != NULL){
      res = atol(filas->values[0]);
      Serial.println(res);
    }
  }while(filas !=NULL);
  }else{
    Serial.print("Error al ejecutar query");
  }
  Serial.print("Devolviendo de la DB ");
  Serial.println(res);
  return res;
}



boolean DentroDeHorario(){
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    //Serial.println("Failed to obtain time");
    return false;
  }
  if((timeinfo.tm_hour < 9) | (timeinfo.tm_hour > 22)){
    return false;
  }else{
    return true;
  }
}
void CrearRegistroEnDB(int estado,int ID_Usuario, int ID_Sensor){
   Serial.println("Creando registro de usuario con ID");
  Serial.print(ID_Usuario);
  Serial.print(" en el sensor con ID ");
  Serial.print(ID_Sensor);
  Serial.print(" como ");
  Serial.println(estado);
  HacerInsertSeguro("INSERT INTO piisafesense.Historial(estado,ID_Usuario,ID_Sensor) VALUES (" + String(estado) + ", " + String(ID_Usuario) +", " + String(ID_Sensor) +");");
 
}
int SelectIDDeUID(String UID){
  return HacerSelectSeguroDeUnCampInt("SELECT ID_Usuario FROM piisafesense.Usuario WHERE UID = '" + UID +"';");
}
int existeUsuarioConUID(String UID){
  int estado = HacerSelectSeguroDeUnCampInt("SELECT ID_Usuario FROM piisafesense.Usuario WHERE UID = '" + UID +"';");
  if(estado>0){
    return 1;
  }else{
    return 0;
  }
}
void InsertarDatoDeSensor(String contenido,String tipoDato){
  HacerInsertSeguro("INSERT INTO piisafesense.Datos(Contenido,TipoDato,ID_Sensor,ID_Zona) VALUES ('" + contenido +"','" + tipoDato + "'," + ID_Sensor +"," + ID_Zona +"); ");
  Serial.print("Insertando en base de datos tabla datos ");
  Serial.print(contenido);
  Serial.print(" ");
  Serial.print(tipoDato);
}
void DetectarTarjeta(){
  if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
    Serial.print("Tarjeta detectada, UID: ");
    tarjeta = uidToString(mfrc522.uid.uidByte,mfrc522.uid.size);
    Serial.println(tarjeta);
    mfrc522.PICC_HaltA();  // Halt PICC
    mfrc522.PCD_StopCrypto1();  // Stop encryption on PCD
    if(existeUsuarioConUID(tarjeta) == 1){
      if(HacerSelectSeguroDeUnCampInt("SELECT Acceso.ID_Acceso FROM piisafesense.Solicita JOIN piisafesense.Usuario ON Usuario.ID_Usuario = Solicita.ID_Usuario JOIN piisafesense.Acceso ON Acceso.ID_Acceso = Solicita.ID_Acceso WHERE Usuario.UID = '" +tarjeta + "' AND ID_Zona = " + ID_Zona +" AND Acceso.Estado = 'aceptada';") >0){
      Serial.print("Acceso concedido");
      //Poner LED verde
      //Generar RegistrosDeAcceos
      CrearRegistroEnDB(1,SelectIDDeUID(tarjeta),ID_Sensor);
    }else{
      Serial.print("Acceso denegado");
      CrearRegistroEnDB(0,SelectIDDeUID(tarjeta),ID_Sensor);
      activateBuzzer();
    
     //Poner LED rojo
      } 
    }else{
      Serial.print("No existe usuario asociado a esta tarjeta");
      activateBuzzer();
    }
     // Espera un poco antes de volver a leer
}
}
void SacarFoto(){
  http.begin("http:/" + IP_Camara + "/hacerFotoDB");
  int codigoEstado = http.GET();
  if(codigoEstado> 0){
    Serial.println(http.getString());
  }else{
    Serial.println("Error en solicitud");
  }
  http.end();
}
String HacerSelectDeUnCampoSelect(String query,MySQL_Connection &conn){
  Serial.print("Haciendo select --> ");
  Serial.println(query);
  row_values *filas = NULL; 
  String res = ""; //Aquí guardamos el resultado
  //Creamos objeto de query
  MySQL_Query Query = MySQL_Query(&conn);
  //Ejecutar query
  if(Query.execute(query.c_str())){
    Query.get_columns(); //Esto es para sacar el numero de columnas que tendra, es pa el programa, no pa nosootross

  do{
    filas = Query.get_next_row();

    if(filas != NULL){
      res = String(filas->values[0]);
      Serial.println(res);
    }
  }while(filas !=NULL);
  }else{
    Serial.print("Error al ejecutar query");
  }
  return res;
}

String HacerSelectSeguroDeUnCampoSelect(String query){
  MySQL_Connection conn((Client *)&client);
if (conn.connectNonBlocking(server, server_port, user, password) != RESULT_FAIL)
  {
    delay(500);
   return HacerSelectDeUnCampoSelect(query,conn);
                        // close the connection
                        conn.close(); 
  } 
  else 
  {
    MYSQL_DISPLAY("\nConnect failed. Trying again on next iteration.");
  }
return "";
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////

void loop() {

 //Aquí saca primero la camara de la Base de datos que tiene su misma Zona
  String query = "SELECT ip FROM piisafesense.Sensor WHERE ID_Zona = " + String(ID_Zona) +" AND tipo = 'Camara';";
  IP_Camara = HacerSelectSeguroDeUnCampoSelect(query);
  Serial.println("Obteniendo IP de Camara asociada si existe o esta activa");
  Serial.print("La IP es --> ");
  Serial.println(IP_Camara);
  Serial.println(DentroDeHorario());
  EnHorario = DentroDeHorario();
  while(true){
    hum = dht.readHumidity();
    temp = dht.readTemperature();
  if((isnan(hum) > 50) | isnan(temp)> 45){
  activateBuzzer();
    if(hum > 50){
      Serial.println("Humedad inusual detectada");
      //Enviar a DB
      InsertarDatoDeSensor(String(hum),"Humedad");
    }
    if(temp> 45){
      Serial.println("Temperatura superior detectada");
      InsertarDatoDeSensor(String(temp),"Temperatura");
    }
  }else{
    if(Serial.read() == 'C'){
      //Comprobarmos si esta dentro de horario
      Serial.print("Cambiando estado manualmente de ");
      Serial.print(EnHorario);
      Serial.print(" a ");
      Serial.println(!EnHorario);
      EnHorario = !EnHorario;
    }else{
    if(EnHorario == false){
      DetectarTarjeta();
    }else{
      distancia = MedirDistancia();
      if(distancia<20){
        Serial.println(distancia);
      activateBuzzer();
      SacarFoto();
      Serial.println("Sacando foto");
      //Mandar alarma al otro ESP para que saque foto
      }
    }
    }
    
  }

  }

}
