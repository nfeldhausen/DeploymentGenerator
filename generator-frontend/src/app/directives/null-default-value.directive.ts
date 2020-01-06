import { Directive, ElementRef, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: 'input[nullValue]'
})
/** 
 * Sets the value of a form field to null, if it's empty
 * 
 * Source: https://stackoverflow.com/questions/38527535/set-null-as-empty-string-value-for-input-field
 */
export class NullDefaultValueDirective {

  constructor(private el: ElementRef, private control: NgControl) {}

  /**
   * Changes the value of the corresponding model to null, if the input field is empty
   * 
   * @param target The HTML Input Field
   */
  @HostListener('input', ['$event.target'])
  onEvent(target: HTMLInputElement){
    this.control.viewToModelUpdate((target.value === '') ? null : target.value);
  }

}
