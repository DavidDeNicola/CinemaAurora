import {Component, OnInit} from '@angular/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ChatService} from "../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-chat-dialog',
  standalone: true,
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './chat-dialog.component.html',
  styleUrls: ['./chat-dialog.component.css']
})
export class ChatDialogComponent implements OnInit {
  chatForm:FormGroup;
  dialogMessage: string;

  constructor( private formbuilder:FormBuilder, private chatService: ChatService, private dialogRef: MatDialogRef<ChatDialogComponent>, private router: Router ) {
  }

  ngOnInit() {
    this.chatForm = this.formbuilder.group({
      oggetto:['', [Validators.required]],
      messaggio:['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.chatForm.valid) {
      this.chatService.creaChat(this.chatForm.value).subscribe({
        next: (response) => {
          console.log("Prova");
          this.dialogRef.close();
          this.router.navigateByUrl('/assistenza-ticket');
        },
        error: (error) => {
          this.dialogMessage = "Errore nell'apertura del ticket";
        }
      })
    }

  }
}