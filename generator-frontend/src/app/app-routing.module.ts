import { WizardResultComponent } from './wizard/wizard-result/wizard-result.component';
import { ResultComponent } from './result/result.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdvancedMainComponent } from './advanced/advanced-main/advanced-main.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { TemplateComponent } from './template/template.component';
import { WizardMainComponent } from './wizard/wizard-main/wizard-main.component';
import { WizardStepperContainerComponent } from './wizard/wizard-stepper-container/wizard-stepper-container.component';
import { WizardStepperMainComponent } from './wizard/wizard-stepper-main/wizard-stepper-main';
import { WizardLinkComponent } from './wizard/wizard-link/wizard-link.component';
import { WizardDatabaseComponent } from './wizard/wizard-database/wizard-database.component';

const routes: Routes = [
  {path:'',redirectTo: '/welcome', pathMatch: 'full'},
  {path:'advanced',component: AdvancedMainComponent, runGuardsAndResolvers: 'always'},
  {path:'welcome',component: WelcomeComponent},
  {path:'template',component: TemplateComponent},
  {path:'old_wizard',component: WizardMainComponent},
  {path:'wizard',component: WizardStepperMainComponent},
  {path:'link',component: WizardLinkComponent},
  {path:'database',component: WizardDatabaseComponent},
  {path: 'result', component: WizardResultComponent},
  {path:'**',redirectTo: '/welcome'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    onSameUrlNavigation: 'reload'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
