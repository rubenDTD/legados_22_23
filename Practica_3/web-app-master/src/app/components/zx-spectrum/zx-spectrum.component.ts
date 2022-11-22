import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { levenshtein } from '../../levenshtein';

@Component({
  selector: 'app-zx-spectrum',
  templateUrl: './zx-spectrum.component.html',
  styleUrls: ['./zx-spectrum.component.css']
})
export class ZxSpectrumComponent implements OnInit {

  total_juegos: string = ""

  datos_juego: any = null

  juegos_cinta: any = null

  juego = ""
  cinta = ""

  constructor() { }

  async ngOnInit(): Promise<void> {  
    // Se obtiene el numero de registros al iniciar la aplicacion
    let result = await axios.get("http://localhost:8080")
    this.total_juegos = result.data
  }

  async buscarCinta() {
    // Busca todos los juegos en la cinta introducida por el usuario
    let result = await axios.get("http://localhost:8080/list/" + this.cinta)
    this.juegos_cinta = result.data
    this.juegos_cinta = this.juegos_cinta.sort((a:any, b:any) => 
                                          (Number(a.registro) <= Number(b.registro)) ? -1 : 1);
  }

  async buscarJuego() {
    let result = await axios.get("http://localhost:8080/getTitle/" + this.juego)
    this.datos_juego = this.procesarCadena(result.data)
  }

  procesarCadena(cadena: string): any {
    let cadena_split = cadena.split(" ")
    // Registro siempre esta en 0
    let registro = cadena_split[0]
    // La cinta esta al final, ignoramos lo que hay previo al substring 'CINTA:'
    let cinta = cadena_split[cadena_split.length-1].substring(6)
    // Buscar en que posicion empieza el tipo del juego
    let [tipo,posicion] = this.procesarTipo(cadena_split)
    // El nombre esta despues del registro y antes del tipo
    let nombre = cadena_split.slice(2,posicion).join(" ")
    return {"nombre": nombre, "tipo": tipo, "cinta": cinta, "registro": registro}
  }

  // Proceso el tipo de juego en caso de tener mas de una palabra
  procesarTipo(cadena_split: string[]): [string,number] {
    let tipo = cadena_split[cadena_split.length-2]
    let principio = cadena_split.length-2
    if(levenshtein(tipo,"DEPORTIVO") <= 4) {
      tipo = cadena_split.slice(cadena_split.length-3,cadena_split.length-1).join(" ")
      principio = cadena_split.length-3
    } else if(levenshtein(tipo,"MESA") <= 2) {
      tipo = cadena_split.slice(cadena_split.length-4,cadena_split.length-1).join(" ")
      principio = cadena_split.length-4
    }
    return [tipo,principio]
  }
}
