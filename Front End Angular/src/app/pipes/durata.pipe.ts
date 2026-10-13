import { Pipe, PipeTransform } from '@angular/core';

// Minuti → testo leggibile.  138 → "2h 18m",  45 → "45m"
@Pipe({
  name: 'durata',
  standalone: true
})
export class DurataPipe implements PipeTransform {
  transform(minuti: number | null | undefined): string {
    if (!minuti) return '';
    const ore = Math.floor(minuti / 60);
    const restanti = minuti % 60;
    return ore > 0 ? `${ore}h ${restanti.toString().padStart(2, '0')}m` : `${restanti}m`;
  }
}
