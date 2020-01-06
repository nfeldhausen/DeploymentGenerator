import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core'
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdvancedMainComponent } from './advanced/advanced-main/advanced-main.component';
import { CustomMinDirective } from './directives/custom-min.directive';
import { CustomMaxDirective } from './directives/custom-max.directive';
import { AdvancedContainerComponent } from './advanced/advanced-container/advanced-container.component';
import { AdvancedDatabaseComponent } from './advanced/advanced-database/advanced-database.component';
import { AdvancedServiceComponent } from './advanced/advanced-service/advanced-service.component';
import { AdvancedLinkComponent } from './advanced/advanced-link/advanced-link.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { TemplateComponent } from './template/template.component';
import { ResultComponent } from './result/result.component';
import { VarDirective } from './directives/var.directive';
import { ErrorModalComponent } from './error-modal/error-modal.component';
import { WizardMainComponent } from './wizard/wizard-main/wizard-main.component';
import { WizardContainerComponent } from './wizard/wizard-container/wizard-container.component';
import { WizardDatabaseComponent } from './wizard/wizard-database/wizard-database.component';
import { WizardLinkComponent } from './wizard/wizard-link/wizard-link.component';
import { WizardResultComponent } from './wizard/wizard-result/wizard-result.component';
import { WizardEditLinkComponent } from './wizard/wizard-edit-link/wizard-edit-link.component';
import { NullDefaultValueDirective } from './directives/null-default-value.directive';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxSpinnerModule } from "ngx-spinner";
import { WizardStepperContainerComponent } from './wizard/wizard-stepper-container/wizard-stepper-container.component';
import { ArchwizardModule } from 'angular-archwizard';
import { WizardStepperMainComponent } from './wizard/wizard-stepper-main/wizard-stepper-main';

@NgModule({
  entryComponents: [
    ErrorModalComponent
  ],
  declarations: [
    AppComponent,
    AdvancedMainComponent,
    CustomMinDirective,
    CustomMaxDirective,
    AdvancedContainerComponent,
    AdvancedDatabaseComponent,
    AdvancedServiceComponent,
    AdvancedLinkComponent,
    WelcomeComponent,
    TemplateComponent,
    ResultComponent,
    VarDirective,
    ErrorModalComponent,
    WizardMainComponent,
    WizardContainerComponent,
    WizardDatabaseComponent,
    WizardLinkComponent,
    WizardResultComponent,
    WizardEditLinkComponent,
    NullDefaultValueDirective,
    WizardStepperContainerComponent,
    WizardStepperMainComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    BrowserAnimationsModule,
    NgxSpinnerModule,
    ArchwizardModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
