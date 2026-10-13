import { Pipe, PipeTransform } from '@angular/core';

// Lista dei generi → un solo testo.  ["Azione", "Fantascienza"] → "Azione / Fantascienza"
@Pipe({
  name: 'generi',
  standalone: true
})
export class GeneriPipe implements PipeTransform {
  transform(nomiGeneri: string[] | null | undefined): string {
    return nomiGeneri?.length ? nomiGeneri.join(' / ') : 'Genere non specificato';
  }
}
