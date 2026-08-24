import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ChatDialogComponent } from '../chat-dialog/chat-dialog.component';
import { AuthService } from '../../../services/auth.service';
import { Ruolo } from '../../../enums/ruolo';
import { Router } from '@angular/router';

@Component({
  selector: 'app-chat-button',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatDialogModule],
  templateUrl: './chat-button.component.html',
  styleUrls: ['./chat-button.component.css']
})
export class ChatButtonComponent {
  constructor(
    private dialog: MatDialog,
    private authService: AuthService,
    private router: Router
  ) {}

  ApriChat(): void {
    const ruolo = this.authService.getRuolo();

    if (ruolo === Ruolo.STAFF) {
      this.router.navigateByUrl('/assistenza-ticket');
      return;
    }

    this.dialog.open(ChatDialogComponent, {
      width: '28vw',
      height: '58vh',
      maxWidth: '400px',
      maxHeight: '600px',
      minWidth: '320px',
      hasBackdrop: true,
      autoFocus: false,
    });
  }
}
