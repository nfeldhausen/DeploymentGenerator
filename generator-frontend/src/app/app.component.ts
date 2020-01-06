import { Component } from '@angular/core';
import { Meta } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  current: string = "microservice";

  constructor(private meta: Meta) {
    this.meta.addTag({name: 'viewport', content: 'width=device-width, initial-scale=1.0'});
  }

  currentActive(current: string) {
    this.current = current;
  }
}
