import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, FormControl } from '@angular/forms';

@Directive({
  selector: '[customMax][formControlName],[customMax][formControl],[customMax][ngModel]',
  providers: [{provide: NG_VALIDATORS, useExisting: CustomMaxDirective, multi: true}]
})
/** 
 * Checks a number field for a max value, because max does not work with angular validation
 *
 * Source: https://www.concretepage.com/angular-2/angular-4-min-max-validation 
 */
export class CustomMaxDirective {
  /** The max number */
  @Input()
  customMax: number;
  
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

      return ( v > this.customMax)? {"customMax": true} : null;
  }
}
