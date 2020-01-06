import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, FormControl } from '@angular/forms';

@Directive({
  selector: '[customMin][formControlName],[customMin][formControl],[customMin][ngModel]',
  providers: [{ provide: NG_VALIDATORS, useExisting: CustomMinDirective, multi: true }]
})
/**
 * Checks a number field for a min value, because min does not work for angular validation
 * 
 * Source: https://www.concretepage.com/angular-2/angular-4-min-max-validation
 */
export class CustomMinDirective implements Validator {
  /** The min number */
  @Input()
  customMin: number;
  
  /**
   * Validates a form control field
   * 
   * @param c The form control field which should be validated
   * @returns null if the field is valid or a JSON Object if the field is invalid
   */
  validate(c: FormControl): {[key: string]: any} {
      let v = c.value;

      if (v === null) {
        return null;
      }

      return ( v < this.customMin)? {"customMin": true} : null;
  }

}
