import { Component, OnInit, Input } from '@angular/core';
import { RequestService } from '../services/request.service';
import { Request } from '../model/Request';

@Component({
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.css']
})
/** Class for displaying templates */
export class TemplateComponent implements OnInit {
  /** Supported templates from the backend */
  templates: string[] = [];

  /** Currently selected template */
  currentSelected: string;

  /** Whether the result should be displayed or not */
  showResult: boolean = false;

  /** Kubernetes YAML Deployment file */
  deployment: string;

  /** The returning request for the template */
  request: Request;

  /** The description for the request */
  description: Object;

  /** Error string in case fetching the templates fails */
  err: string;

  constructor(private requestService: RequestService) { }

  /** Start fetching the templates */
  ngOnInit() {
    this.getTemplates();
  }

  /**
   * Fetches all available templates from the backend
   */
  getTemplates() {
    this.requestService.getTemplates().subscribe(
      t => {
        this.templates = t
        this.err = null;
      },
      err => {
        console.log("Could not fetch templates. Trying again in 5 Seconds");
        this.err = "Could not fetch templates. Trying again in 5 Seconds";
        setTimeout(() => this.getTemplates(), 5000);
      },
      () => {
        if (this.templates.length) {
          this.currentSelected = this.templates[0];
        }
      }
    );
  }

  /**
   * Sends the selected template to the backend and shows the corresponding template file
   */
  send(): void {
    this.requestService.getTemplate(this.currentSelected).subscribe(
      t => {
        this.deployment = t.result; 
        this.request = t.request; 
        this.description = t.description
      }, 
      (err) => console.log(err), 
      () => {
        this.showResult = true;
      });
  }
}
