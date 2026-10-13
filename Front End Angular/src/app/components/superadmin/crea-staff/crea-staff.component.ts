import {Component, OnInit} from '@angular/core';
import {StaffService} from "../../../services/staff.service";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ResponseUtenteDataDTO} from "../../../dto/utente/response/response-utente-data-dto";
import {ConfirmDialogService} from "../../../services/confirm-dialog.service";

@Component({
  selector: 'app-crea-staff',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './crea-staff.component.html',
  styleUrls: ['./crea-staff.component.css']
})
export class CreaStaffComponent implements OnInit {
  listaStaff: ResponseUtenteDataDTO[] = [];
  staffForm: FormGroup;
  messaggioSuccesso: string | null = null;
  messaggioErrore: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private staffService: StaffService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.staffForm = this.formBuilder.group({
      nome: ['', Validators.required],
      cognome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confermaPassword: ['', Validators.required]
    });

  }

  ngOnInit(): void {
    this.caricaListaStaff();
  }
  caricaListaStaff(): void {
    this.staffService.findAll().subscribe({
      next: (data) => {
        this.listaStaff = data;
      },
      error: (err) => console.error("Errore nel recupero staff:", err)
    });
  }

  onSubmit(): void {
    if (this.staffForm.valid) {
      this.staffService.insert(this.staffForm.value).subscribe({
        next: () => {
          this.messaggioSuccesso = 'Staff aggiunto con successo.';
          this.messaggioErrore = null;
          this.staffForm.reset();
          this.caricaListaStaff();
        },
        error: (err) => {
          this.messaggioErrore = err.error?.message || 'Errore durante l\'aggiunta dello staff.';
          this.messaggioSuccesso = null;
        }
      });
    } else {
      this.messaggioErrore = 'Compila correttamente tutti i campi prima di creare il nuovo account.';
      this.messaggioSuccesso = null;
    }
  }

  eliminaStaff(staff: ResponseUtenteDataDTO): void {
    this.confirmDialogService.confirm({
      title: 'Elimina staffer',
      message: `Sei sicuro di voler eliminare ${staff.nome} ${staff.cognome}?`,
      confirmText: 'Elimina',
      variant: 'danger'
    }).subscribe(conferma => {
      if (!conferma) {
        return;
      }

      this.staffService.removeById(staff.id).subscribe({
        next: () => {
          this.listaStaff = this.listaStaff.filter(s => s.id !== staff.id);
          this.messaggioErrore = null;
          this.confirmDialogService.notifySuccess(
            `${staff.nome} ${staff.cognome} è stato eliminato dallo staff.`,
            'Staff eliminato'
          ).subscribe();
        },
        error: (err) => {
          this.messaggioErrore = err?.error?.message || 'Errore durante l\'eliminazione dello staff.';
          this.messaggioSuccesso = null;
          this.confirmDialogService.notifyError(this.messaggioErrore, 'Eliminazione non riuscita').subscribe();
        }
      });
    });
  }
}
