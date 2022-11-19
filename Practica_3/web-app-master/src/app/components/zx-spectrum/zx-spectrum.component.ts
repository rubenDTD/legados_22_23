import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable()
@Component({
  selector: 'app-zx-spectrum',
  templateUrl: './zx-spectrum.component.html',
  styleUrls: ['./zx-spectrum.component.css']
})
export class ZxSpectrumComponent implements OnInit {

  total_juegos = 0

  datos_juego: any

  juegos_cinta: any

  nombre_juego: any 

  message = 'Hello, World!';

  constructor(private http:HttpClient) { }

  ngOnInit(): void {  
    // Se obtiene el numero de registros al iniciar la aplicacion
    this.total_juegos = 0;
    /*let resp: string = "";
    this.http.get('http://localhost:8080/', {responseType: "text"}).subscribe(data => {
      resp = data;
      let parser = new DOMParser();
      const doc = parser.parseFromString(resp, 'text/html');     
      let numJuegos = doc.getElementById("numGames")?.textContent;
      console.log(doc);
      console.log(numJuegos);
      if (numJuegos != null) {this.total_juegos = parseInt(numJuegos);}
    })*/
  }

  buscarCinta() {
    // Busca todos los juegos en la cinta introducida por el usuario
    this.juegos_cinta = [

      {"numero": "234", "nombre": "FILECARD", "tipo": "UTILIDAD", "cinta": "6", "registro": "464"},
      {"numero": "217", "nombre": "ESCAPE FROM SINGE'S CASTLE", "tipo": "ARCADE", "cinta": "3", "registro": "423"},
      {"numero": "219", "nombre": "ESPIONAGE ISLAND", "tipo": "ARCADE", "cinta": "F", "registro": "77"},
      {"numero": "231", "nombre": "FEUD", "tipo": "VIDEOAVENTURA", "cinta": "21", "registro": "643"},
      {"numero": "233", "nombre": "FIGHTING WARRIOR", "tipo": "ARCADE", "cinta": "N", "registro": "194"}
    ]

    this.juegos_cinta = this.juegos_cinta.sort((a:any, b:any) => (Number(a.registro) <= Number(b.registro)) ? -1 : 1);


  }

  buscarJuego() {
    let resp: string = "";
    console.log(this.nombre_juego);
    this.http.get('http://localhost:8080/listTitle', {responseType: "text", params: { nombre: this.nombre_juego }}).subscribe(data => {
      resp = data;
      let parser = new DOMParser();
      const doc = parser.parseFromString(resp, 'text/html');     
      let juego = doc.getElementById("datosJuego")?.textContent;
      console.log(doc);
      console.log(juego);
      if(juego != null){      
        const j = juego.split(" ",5)
        const juego_row = [{ numero: j[0], nombre: j[2], tipo: j[3], cinta: j[4] }];
        console.log(juego_row);
        if (juego_row != null) {this.datos_juego = juego_row;}
      }
    })
  }  


}
