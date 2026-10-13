import { Pipe, PipeTransform } from '@angular/core';

// Data e ora del backend → solo l'orario.  "2026-10-13T20:30:00" → "20:30"
@Pipe({
  name: 'orario',
  standalone: true
})
export class OrarioPipe implements PipeTransform {
  transform(dataOra: string | null | undefined): string {
    if (!dataOra) return '';
    const parteOra = dataOra.includes('T') ? dataOra.split('T')[1]
                   : dataOra.includes(' ') ? dataOra.split(' ')[1]
                   : dataOra;
    return parteOra.slice(0, 5);
  }
}
