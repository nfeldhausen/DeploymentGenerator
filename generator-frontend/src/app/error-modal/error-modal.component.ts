import { Component, Input} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-error-modal',
  templateUrl: './error-modal.component.html',
  styleUrls: ['./error-modal.component.css']
})
/** Error Modal for displaying errors in advanced mode */
export class ErrorModalComponent {
  /** The error message which should be displayed */
  @Input()
  message: string;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {
  }
}
