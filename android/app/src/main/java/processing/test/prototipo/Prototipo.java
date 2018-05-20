package processing.test.prototipo;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.content.Context; 
import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 
import android.os.Bundle; 
import android.Manifest; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Prototipo extends PApplet {








LocationManager locationManager;
MyLocationListener locationListener;

boolean hasLocation = false;

public PImage location;

public Event keyboardEvents[]=new Event[2];

Pagina intro=new Pagina();
Pagina login=new Pagina();
public Pagina choose=new Pagina();
Pagina foundFriends=new Pagina();

public float currentLatitude, currentLongitude;

public void keyPressed(){
  keyboardEvents[0].put(key);
  keyboardEvents[1].put(keyCode);
}

public void setup () {
  
  orientation(PORTRAIT);  
  requestPermission("android.permission.ACCESS_FINE_LOCATION", "initLocation");
  
  location=loadImage("location.png");
  
  keyboardEvents[0]=new Event();
  keyboardEvents[1]=new Event();
  
  defineIntro();
  defineLogin();
  defineChoose();
  defineFoundFriends();
}

public void defineIntro(){
  intro.defineSize(width, height);
  intro.defineBackground("1.png");
  
  intro.defineButton("fazerUmTrajeto", width/2, height-height/10, width, 114, "noForm", 0, "noAction", "", "Gotham", 32);
}

public void defineLogin(){
  login.defineSize(width, height);
  login.defineBackground("2.png");
  
  login.defineButton("facebook", width/4, 3*height/4, 100, 100, "noForm", 0, "noAction", "", "Gothan", 30);
  login.defineButton("google", width/2, 3*height/4, 100, 100, "noForm", 0, "noAction", "", "Gothan", 30);
  login.defineButton("instagram", 20+3*width/4, 3*height/4, 100, 100, "noForm", 0, "noAction", "", "Gothan", 30);
}

public void defineChoose(){
  choose.defineMap("mainMap", 0, 0, width, height);
  
  choose.defineTextBox("destino", width/2, height/4, 5*width/6, 75, "rect+ellipse", 255, "Gothan", 30, "noAction", "Digite seu destino");
  
  choose.defineButton("confirmar", width/2, 5*height/6, width/2, 100, "rect", 0, "noAction", "CONFIRMAR", "Gothan", 30);
}

public void defineFoundFriends(){
  foundFriends.defineSize(width, height);
  foundFriends.defineBackground("mapa.png");
}

int estado=0;
public void draw() {  
  switch(estado){
    case 0:
      if(intro.Pagina()=="fazerUmTrajeto") estado=1;
    break;
    case 1:
      if(login.Pagina()!="-") estado=2;
    break;
    case 2:
      if(choose.Pagina()=="confirmar") estado=3;
    break;
    case 3:
      foundFriends.Pagina();
    break;
  }
  
  keyboardEvents[0].removeOne();
  keyboardEvents[1].removeOne();
}

public void initLocation(boolean granted) {
  if (granted) {    
    Context context = getContext();
    locationListener = new MyLocationListener();
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);    
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    hasLocation = true;
  } else {
    hasLocation = false;
  }
}

class MyLocationListener implements LocationListener {
  public void onLocationChanged(Location location) {
    currentLatitude  = (float)location.getLatitude();
    currentLongitude = (float)location.getLongitude();
  }

  public void onProviderDisabled (String provider) {}

  public void onProviderEnabled (String provider) { }

  public void onStatusChanged (String provider, int status, Bundle extras) {
  }
}
class Button{
  String nameButton="";
  
  float sizeX=0, sizeY=0, curSizeX=0, curSizeY;
  float posX=0, posY=0;
  
  String form="rect";
  PImage imageForm;
  
  int fillColor=255, curFillColor=255;
  
  int clickingButton=0;
  
  String passingAction="noAction";
  
  PImage imageAction;
  
  String wordButton="";
  
  PFont font;
  
  int size=0;
  
  public int Button(){
    verifyButton();
    
    //drawCursor();
    
    drawButton();
    
    return clickingButton;
  }
  
  public void verifyButton(){
    if(touches.length>0){
      if(touches[0].x>=posX-curSizeX/2 && touches[0].x<=posX+curSizeX/2 && touches[0].y>=posY-curSizeY/2 && touches[0].y<=posY+curSizeY/2){
        clickingButton=1;
      }
      else clickingButton=0;
    }
  }
  
  public void defineWordButton(String wordButton, String font, int size){
    this.wordButton=wordButton;
    this.font=createFont(font, size);
  }
  
  public void defineName(String nameButton){
    this.nameButton=nameButton; 
  }
  
  public void defineFillColor(int fillColor){
    this.fillColor=fillColor;
    
    this.curFillColor=fillColor;
  }
  
  public void drawButton(){
    if(form=="-"){
      imageMode(CENTER);
      image(imageForm, posX, posY, curSizeX, curSizeY);
      imageMode(CORNER);
    }
    else{
      switch(form){
        case "rect":
          fill(0xff9B9999, 250);
          rectMode(CENTER);
          rect(posX+3, posY+5, curSizeX, curSizeY);
          rectMode(CORNER);
          
          fill(curFillColor);
          rectMode(CENTER);
          rect(posX, posY, curSizeX, curSizeY);
          rectMode(CORNER);
        break;
        case "ellipse":
          fill(0xff9B9999, 250);
          ellipseMode(CENTER);
          ellipse(posX+3, posY+5, curSizeX, curSizeY);
          ellipseMode(CORNER);
          
          fill(curFillColor);
          ellipseMode(CENTER);
          ellipse(posX, posY, curSizeX, curSizeY);
          ellipseMode(CORNER);
        break;
        case "noForm":
        break;
        default:
          println("ERROR. FORM UNDFINED");
        break;
      } 
    }
    textFont(font);
    fill(255-fillColor);
    text(wordButton, posX-textWidth(wordButton)/2, posY);
  }
  
  public void sizeButton(float sizeX, float sizeY){
    this.sizeX=sizeX;
    this.sizeY=sizeY;
    
    this.curSizeX=sizeX;
    this.curSizeY=sizeY;
  }
  
  public void posButton(float posX, float posY){
   this.posX=posX;
   this.posY=posY;
  }
  
  public void defineForm(String form){
    switch(form){
        case "rect":
        case "ellipse":
        case "noForm":
          this.form=form;   
        break;
        default:
          this.form="-";
          
          imageForm=loadImage(form);
        break;
      }
  }
  
  public void definePassinAction(String passingAction){
    switch(passingAction){
      case "changeColor":
      case "changeSize":
      case "noAction":
        this.passingAction=passingAction;
      break;
      default:
        imageAction=loadImage(passingAction);
      break;
    }
  }
}
class Event{
  private class NODE{
    NODE next;
    
    int val;
  }
  
  private class LinkedList{    
    NODE first;
    NODE last;
    
    int qtdEle;
  }
  
  LinkedList queue= new LinkedList();
  
  public void put(int event){
    if(queue.qtdEle==0){
      queue.first=new NODE();
      queue.first.val=event;
      
      queue.last=queue.first;
    }
    else{
      NODE auxNode= new NODE();
      auxNode.val=event;
      
      queue.last.next=auxNode;
      
      queue.last=auxNode;
    }
    
    queue.qtdEle++;
  }
  
  public int process(){
    if(queue.first!=null) return queue.first.val;
    
    return 0;
  }
  
  public void removeOne(){
    if(queue.first!=null){
      queue.first=queue.first.next;
      
      queue.qtdEle--;
      
      if(queue.qtdEle==0) queue.last=null;
    }
  }
}
class Image{
  String imageName="";
  
  int posX=0, posY=0;
  int sizeX=0, sizeY=0;
  
  PImage image;
  
  public void Image(){
    image(image, posX, posY, sizeX, sizeY);
  }
  
  public void defineName(String imageName){
    this.imageName=imageName;
  }
  
  public void definePosition(int posX, int posY){
    this.posX=posX;
    this.posY=posY;
  }
  
  public void defineSize(int sizeX, int sizeY){
    this.sizeX=sizeX;
    this.sizeY=sizeY;
  }
  
  public void defineImage(String imageString){
     this.image=loadImage(imageString);
  }
}
class Map{
 int lat=0, lon=0; 
 float zoom=15.0f;
 int sizeX=0, sizeY=0;
 int posX=0, posY=0;
 float coordMa=0, preCoord=0;
  
 PImage curMap, preMap;
 
 String mapName;
 
 int t1=0;
 
 boolean inicio=true;
 
 String opc="coordinates", locationString, center;
  
 public void Map(){
   if(inicio && currentLatitude!=0 && currentLongitude!=0){
     if(opc=="coordinates") locationString=str(currentLatitude)+","+str(currentLongitude)+"&zoom="+str((int)zoom)+"&size="+str(sizeX)+"x"+str(sizeY);
     else{
       String vetor[]=split(center, ' ');
       
       locationString="";
       for(int c1=0; c1<vetor.length; c1++){
         locationString+=vetor[c1];
       }
     }
     
     curMap=loadImage("http://maps.google.com/maps/api/staticmap?center="+locationString+"&zoom="+str((int)zoom)+"&size="+str(sizeX)+"x"+str(sizeY)+"&sensor=true&markers%22%20target=%22_blank%22%20rel=%22nofollow%22%3Ehttp://maps.google.com/maps/api/staticmap?center=45.464160,9.191614&zoom=3&size=400x400&sensor=true&markers&key=AIzaSyDT8gV4kSuLPW0zanz_c7TeAk5k3Qnj8mM");
   }
     
   
   if(touches.length==2){
     if(touches[0].y>touches[1].y) coordMa=touches[0].y;
     else coordMa=touches[1].y;
     
     if(preCoord>coordMa) zoom-=0.3f;
     else if(preCoord<coordMa) zoom+=0.3f;
      
     if(millis()-t1>=500){
       if(opc=="coordinates") locationString=str(currentLatitude)+","+str(currentLongitude)+"&zoom="+str((int)zoom)+"&size="+str(sizeX)+"x"+str(sizeY);
       else locationString=center;
       curMap=loadImage("http://maps.google.com/maps/api/staticmap?center="+locationString+"&zoom="+str((int)zoom)+"&size="+str(sizeX)+"x"+str(sizeY)+"&sensor=true&markers%22%20target=%22_blank%22%20rel=%22nofollow%22%3Ehttp://maps.google.com/maps/api/staticmap?center=45.464160,9.191614&zoom=3&size=400x400&sensor=true&markers&key=AIzaSyDT8gV4kSuLPW0zanz_c7TeAk5k3Qnj8mM");
       t1=millis();
     }
     
     preCoord=coordMa;
   }
   
   if(curMap!=null){
     image(curMap, posX, posY, (float)sizeX*zoom/15, (float)sizeY*zoom/15);
     
     inicio=false;
   }
   
   imageMode(CENTER);
   image(location, width/2, height/2, 50, 50);
   imageMode(CORNER);
 }
 
 public void defineMetod(String opc){
   inicio=true;
   this.opc=opc;
 }
 
   public void defineCenter(String center){
     this.center=center;
   }
 
 public void defineZoom(int zoom){
   this.zoom=zoom;
 }
 
 public void defineSize(int sizeX, int sizeY){
   this.sizeX=sizeX;
   this.sizeY=sizeY;
 }
 
 public void defineName(String mapName){
   this.mapName=mapName;
 }
 
 public void definePosition(int posX, int posY){
   this.posX=posX;
   this.posY=posY;
 }
}
class Pagina{
  public Map maps[]=new Map[1];
  int qtdMaps=0;
  
  Button botoes[]=new Button[1];
  int qtdButtons=0;
  
  TextBox textBoxs[]=new TextBox[1];
  int qtdTextBoxs=0;
  
  Image images[]=new Image[1];
  int qtdImages=0;
  
  Text texts[]=new Text[1];
  int qtdTexts=0;
  
  int backgroundColor=0;
  PImage backgroundImage;
  
  int sizeX=0, sizeY=0;
  
  String clickingObject="-";
  
  int opcMap=0;
  
  public String Pagina(){
     drawBackground();
     executeMaps();
     
     executeTextBoxs();
     executeImages();
     executeButtons();
     executeTexts();
           
     return clickingObject;
  }
  
  public String returnText(String nameTextBox){
    for(int c1=0; c1<qtdTextBoxs; c1++){
      if(textBoxs[c1].nameTextBox==nameTextBox) return textBoxs[c1].text; 
    }
    
    return "-";
  }
  
  public void executeMaps(){
    for(int c1=0; c1<qtdMaps; c1++) maps[c1].Map();
  }
  
  public void executeTexts(){
    for(int c1=0; c1<qtdTexts; c1++) texts[c1].Text();
  }
  
  public void executeImages(){
    for(int c1=0; c1<qtdImages; c1++) images[c1].Image();
  }
  
  public void executeButtons(){
    clickingObject="-";
    
    for(int c1=0; c1<qtdButtons; c1++){
      int buttonAnswer=botoes[c1].Button();
      switch(buttonAnswer){
        case 1:
          clickingObject=botoes[c1].nameButton;
        break;
      }
    }
  }
  
  public void executeTextBoxs(){
    for(int c1=0; c1<qtdTextBoxs; c1++){
      int textBoxAnswer=textBoxs[c1].TextBox();
      
      switch(textBoxAnswer){
        case 1:
          clickingObject=textBoxs[c1].nameTextBox;
        break;
        case 11:
          clickingObject=textBoxs[c1].nameTextBox;
        break;
      }
    }
  }
  
  public void drawBackground(){
    if(backgroundColor==-1) image(backgroundImage, 0, 0, sizeX, sizeY);
    else background(backgroundColor);
  }
  
  public void defineBackground(int backgroundColor){
    this.backgroundColor=backgroundColor;
  }
  
  public void defineBackground(String backgroundImage){
    this.backgroundImage=loadImage(backgroundImage);
    backgroundColor=-1;
  }
  
  public void defineSize(int sizeX, int sizeY){
    this.sizeX=sizeX;
    this.sizeY=sizeY;
  }
  
  public void defineButton(String nameButton, int posX, int posY, int sizeX, int sizeY, String form, int fillColor, String passingAction, String wordButton, String font, int size){
    botoes=(Button[]) expand(botoes);
    botoes[qtdButtons]= new Button();
    
    botoes[qtdButtons].defineName(nameButton);
    
    botoes[qtdButtons].sizeButton(sizeX, sizeY);
    botoes[qtdButtons].posButton(posX, posY);
    
    botoes[qtdButtons].defineForm(form);
    
    botoes[qtdButtons].defineFillColor(fillColor);
    
    botoes[qtdButtons].definePassinAction(passingAction);
    
    botoes[qtdButtons].defineWordButton(wordButton, font, size);
    
    qtdButtons++;
  }
  
  public void defineTextBox(String nameTextBox, int posX, int posY, int sizeX, int sizeY, String form, int fillColor, String textFont, int textSize, String passingAction, String message){
    textBoxs=(TextBox[]) expand(textBoxs);
    textBoxs[qtdTextBoxs]= new TextBox();
    
    textBoxs[qtdTextBoxs].defineName(nameTextBox);
    
    textBoxs[qtdTextBoxs].sizeTextBox(sizeX, sizeY);
    textBoxs[qtdTextBoxs].posTextBox(posX, posY);
    
    textBoxs[qtdTextBoxs].defineForm(form);
    
    textBoxs[qtdTextBoxs].defineFillColor(fillColor);
    
    textBoxs[qtdTextBoxs].defineTextFont(textFont, textSize);
    
    textBoxs[qtdTextBoxs].definePassinAction(passingAction);
    
    textBoxs[qtdTextBoxs].defineMessage(message);
        
    qtdTextBoxs++;
  }
  
  public void defineImage(String imageName, int posX, int posY, int sizeX, int sizeY, String imageString){
    images=(Image[]) expand(images);
    images[qtdImages]= new Image();
    
    images[qtdImages].defineName(imageName);
    
    images[qtdImages].definePosition(posX, posY);
      
    images[qtdImages].defineSize(sizeX, sizeY);
    
    images[qtdImages].defineImage(imageString);
    
    qtdImages++;
  }
  
  public void defineText(String textName, int posX, int posY, String fontString, int sizeText, int colorText, String text){
    texts=(Text[]) expand(texts);
    texts[qtdTexts]= new Text();
    
    texts[qtdTexts].defineName(textName);
    
    texts[qtdTexts].definePosition(posX, posY);
    
    texts[qtdTexts].defineFont(fontString, sizeText);
    
    texts[qtdTexts].defineColor(colorText);
    
    texts[qtdTexts].defineText(text);
    
    qtdTexts++;     
  }
  
  public void defineMap(String mapName, int posX, int posY, int sizeX, int sizeY){
     maps=(Map[]) expand(maps);
     maps[qtdMaps]= new Map();
     
     maps[qtdMaps].defineName(mapName);
     
     maps[qtdMaps].definePosition(posX, posY);
     
     maps[qtdMaps].defineSize(sizeX, sizeY);
     
     qtdMaps++;
  }
}
class Text{
  String textName="";
  
  String text="";
  
  int posX=0, posY=0;
  int textSize=0;
  
  int colorText=0;
  
  PFont font;
  
  public void Text(){
    fill(colorText);
    
    textFont(font);
    
    text(text, posX, posY);
  }
  
  public void defineName(String textName){
    this.textName=textName;
  }
  
  public void definePosition(int posX, int posY){
    this.posX=posX;
    this.posY=posY;
  }
  
  public void defineFont(String textFont, int textSize){
    this.font=createFont(textFont, textSize*displayDensity);
  }
  
  public void defineColor(int textColor){
    this.colorText=textColor;
  }
  
  public void defineText(String text){
    this.text=text; 
  }
}
class TextBox{  
  String text="";
  
  String nameTextBox="";
  
  float sizeX=0, sizeY=0, curSizeX=0, curSizeY=0;
  float posX=0, posY=0;
  
  String form="rect";
  PImage imageForm;
  
  int fillColor=255, curFillColor=255;
  int colorText=0;
    
  String cursor="TEXT";
  
  int writingText=0;
  
  PFont textFont;
  
  String passingAction="noAction";
  
  PImage imageAction;
  
  String message="";
  
  public int TextBox(){
    verifyTextBox();
    
    //drawCursor();
    
    drawTextBox();
    
    if(writingText==1) changeText();
    
    return writingText;
  }
  
  int textCursorCurPosition=0;
  public void changeText(){
    int letter=keyboardEvents[0].process();
    
    if(letter>=32 && letter<=126 && textWidth(text+str((char)letter))<=sizeX){
      String textFirstPart=text.substring(0, textCursorCurPosition), textSecondPart=text.substring(textCursorCurPosition, text.length());
      
      textFirstPart+=str((char)letter);
      
      text=textFirstPart+textSecondPart;
      textCursorCurPosition++;
    }
    else{
      switch(keyboardEvents[1].process()){
        case 67:
          if(textCursorCurPosition>0){
            text=text.substring(0, textCursorCurPosition-1)+text.substring(textCursorCurPosition, text.length());
            textCursorCurPosition--;
          }
        break;
      }
    }
    
    makeTextCursor();
  }
  
  long t1=0;
  float textCursorPositions[]= new float[1000];
  public void makeTextCursor(){
    String textFirstPart=text.substring(0, textCursorCurPosition);
    textCursorPositions[textCursorCurPosition]=posX+textWidth(textFirstPart);
    
    if(millis()-t1<=500){      
      rectMode(CENTER);
     
      rect(textCursorPositions[textCursorCurPosition]-curSizeX/2, posY, 1, textAscent());
      
      rectMode(CORNER);
    }
    else if(millis()-t1>=1000) t1=millis();
  }
  
  public void verifyTextBox(){
    if(touches.length>0){
      if(touches[0].x>=posX-curSizeX/2 && touches[0].x<=posX+curSizeX/2 && touches[0].y>=posY-curSizeY/2 && touches[0].y<=posY+curSizeY/2){
        if(writingText==0) openKeyboard();
        writingText=1;
      }
      else{
        if(writingText==1) closeKeyboard();
        writingText=0;
      }
    }
    
    if(keyboardEvents[0].process()==10){
      if(writingText==1){
        closeKeyboard();
        
        choose.maps[0].defineMetod("center");
        
        choose.maps[0].defineCenter(text);
      }
      writingText=0;
    }
  }  
  
  public void drawTextBox(){
    noStroke();
    fill(fillColor);
    
    if(form=="-") image(imageForm, posX, posY, curSizeX, curSizeY);
    else{
      switch(form){
        case "rect":
          rectMode(CENTER);
          rect(posX, posY, curSizeX, curSizeY);
          rectMode(CORNER);
        break;
        case "ellipse":
          ellipseMode(CENTER);
          ellipse(posX, posY, curSizeX, curSizeY);
          ellipseMode(CORNER);
        break;
        case "rect+ellipse":
          rectEllipse();
        break;
        case "noForm":
        break;
        default:
          println("ERROR. FORM UNDFINED");
        break;
      } 
    }
    
    writeText();
  }
  
  public void rectEllipse(){
    fill(0xff9B9999, 250);
    
    ellipseMode(CENTER);
    ellipse(posX-curSizeX/2+3, posY+5, curSizeY, curSizeY);
    rectMode(CENTER);
    rect(posX+3, posY+5, curSizeX, curSizeY);   
    rectMode(CORNER);
    ellipse(posX+curSizeX/2+3, posY+5, curSizeY, curSizeY);
    ellipseMode(CORNER);
    
    fill(fillColor);
    ellipseMode(CENTER);
    ellipse(posX-curSizeX/2, posY, curSizeY, curSizeY);
    rectMode(CENTER);
    rect(posX, posY, curSizeX, curSizeY);   
    rectMode(CORNER);
    ellipse(posX+curSizeX/2, posY, curSizeY, curSizeY);
    ellipseMode(CORNER);
  }
  
  public void writeText(){
   textFont(textFont);
   
   textAlign(LEFT, CENTER);
   
   println(text);
   
   if(text==""){
      fill(0xffA5A0A0);
      text(message, posX-curSizeX/2, posY); 
   }
   else{
     fill(colorText);
     text(text, posX-curSizeX/2, posY);
   }
  
  }
  
  public void defineName(String nameTextBox){
    this.nameTextBox=nameTextBox; 
  }
  
  public void sizeTextBox(float sizeX, float sizeY){
    this.sizeX=sizeX;
    this.sizeY=sizeY;
    
    this.curSizeX=sizeX;
    this.curSizeY=sizeY;
  }
  
  public void posTextBox(float posX, float posY){
   this.posX=posX;
   this.posY=posY;
  }
  
  public void defineForm(String form){
    switch(form){
        case "rect":
        case "ellipse":
        case "rect+ellipse":
        case "noForm":
          this.form=form;   
        break;
        default:
          this.form="-";
          
          imageForm=loadImage(form);
        break;
      }
  }
  
  public void defineFillColor(int fillColor){
    this.fillColor=fillColor;
    
    this.curFillColor=fillColor;
  }
  
  public void defineCursor(String cursor){
    this.cursor=cursor; 
  }
  
  public void defineColorText(int colorText){
    this.colorText=colorText;
  }
  
  public void defineTextFont(String textFont, int textSize){
    this.textFont=createFont(textFont, textSize);
  }
  
  public void definePassinAction(String passingAction){
    switch(passingAction){
      case "changeColor":
      case "changeSize":
      case "noAction":
        this.passingAction=passingAction;
      break;
      default:
        imageAction=loadImage(passingAction);
      break;
    }
  }
  
  public void defineMessage(String message){
    this.message=message;
  }
}
  public void settings() {  fullScreen(); }
}
