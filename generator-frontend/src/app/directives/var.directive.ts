import { Directive, Input, ViewContainerRef, TemplateRef } from '@angular/core';

@Directive({
  selector: '[ngVar]'
})
/**
 * Directive for using a custom variable in the HTML template
 * 
 * Source: https://stackoverflow.com/questions/38582293/how-to-declare-a-variable-in-a-template-in-angular
 */
export class VarDirective {
  @Input()
  set ngVar(context: any) {
    this.context.$implicit = this.context.ngVar = context;
    this.updateView();
  }

  context: any = {};

  constructor(private vcRef: ViewContainerRef, private templateRef: TemplateRef<any>) {}

  updateView() {
    this.vcRef.clear();
    this.vcRef.createEmbeddedView(this.templateRef, this.context);
  }

}
